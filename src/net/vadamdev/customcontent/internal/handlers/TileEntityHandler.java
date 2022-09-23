package net.vadamdev.customcontent.internal.handlers;

import net.vadamdev.customcontent.CustomContentLib;
import net.vadamdev.customcontent.api.ITickable;
import net.vadamdev.customcontent.api.blocks.CustomBlock;
import net.vadamdev.customcontent.api.blocks.CustomTileEntity;
import net.vadamdev.customcontent.api.blocks.ITileEntityProvider;
import net.vadamdev.customcontent.api.blocks.serialization.SerializableDataCompound;
import net.vadamdev.customcontent.internal.BlocksRegistry;
import net.vadamdev.customcontent.internal.utils.Tuple;
import net.vadamdev.customcontent.lib.BlockPos;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author VadamDev
 * @since 21/09/2022
 */
public class TileEntityHandler {
    private final Map<BlockPos, Tuple<CustomBlock, CustomTileEntity>> tileEntities;
    private final Map<BlockPos, BukkitRunnable> tickableTileEntities;

    public TileEntityHandler() {
        this.tileEntities = new HashMap<>();
        this.tickableTileEntities = new HashMap<>();
    }

    public void addTileEntity(BlockPos blockPos, CustomBlock customBlock, CustomTileEntity tileEntity) {
        if(tileEntity instanceof ITickable) {
            ITickable tickable = (ITickable) tileEntity;

            BukkitRunnable runnable = new BukkitRunnable() {
                @Override
                public void run() {
                    tickable.tick();
                }
            };

            if(tickable.isTickAsync())
                runnable.runTaskTimerAsynchronously(CustomContentLib.instance, 0, tickable.getInterval());
            else
                runnable.runTaskTimer(CustomContentLib.instance, 0, tickable.getInterval());

            tickableTileEntities.put(blockPos, runnable);
        }

        tileEntities.put(blockPos, new Tuple<>(customBlock, tileEntity));
    }

    public void removeTileEntity(BlockPos blockPos) {
        if(tickableTileEntities.containsKey(blockPos))
            tickableTileEntities.get(blockPos).cancel();

        tileEntities.remove(blockPos);
    }

    public Optional<CustomTileEntity> getTileEntityAt(BlockPos blockPos) {
        return Optional.ofNullable(tileEntities.get(blockPos).getB());
    }

    public void loadAll(BlocksRegistry blocksRegistry) {
        blocksRegistry.getCustomBlocks().stream().filter(ITileEntityProvider.class::isInstance).forEach(customBlock -> {
            ITileEntityProvider tileEntityProvider = (ITileEntityProvider) customBlock;
            Map<BlockPos, SerializableDataCompound> dataSerializer = customBlock.getDataSerializer().readAll();

            dataSerializer.forEach((blockPos, compound) -> {
                CustomTileEntity tileEntity = tileEntityProvider.createTileEntity(blockPos);
                tileEntity.load(compound);

                addTileEntity(blockPos, customBlock, tileEntity);
            });
        });
    }

    public void saveAll() {
        tileEntities.forEach((blockPos, tuple) -> tuple.getA().getDataSerializer().write(blockPos, tuple.getB().save(new SerializableDataCompound())));
    }
}
