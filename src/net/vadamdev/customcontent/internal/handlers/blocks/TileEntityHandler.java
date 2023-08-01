package net.vadamdev.customcontent.internal.handlers.blocks;

import net.vadamdev.customcontent.api.blocks.CustomBlock;
import net.vadamdev.customcontent.api.blocks.CustomTileEntity;
import net.vadamdev.customcontent.api.blocks.serialization.SerializableDataCompound;
import net.vadamdev.customcontent.api.common.tickable.ITickable;
import net.vadamdev.customcontent.internal.utils.Tuple;
import net.vadamdev.customcontent.lib.BlockPos;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author VadamDev
 * @since 21/09/2022
 */
public class TileEntityHandler {
    private final Map<BlockPos, Tuple<CustomBlock, CustomTileEntity>> tileEntities;
    private final TickableTileEntityHandler tickableHandler;

    public TileEntityHandler() {
        this.tileEntities = new HashMap<>();
        this.tickableHandler = new TickableTileEntityHandler();
    }

    public void addTileEntity(BlockPos blockPos, CustomBlock customBlock, CustomTileEntity tileEntity) {
        if(tileEntity instanceof ITickable)
            tickableHandler.sumbit(tileEntity);

        tileEntities.put(blockPos, new Tuple<>(customBlock, tileEntity));
    }

    public void removeTileEntity(BlockPos blockPos, boolean tickable) {
        if(tickable)
            tickableHandler.remove(blockPos);

        tileEntities.remove(blockPos);
    }

    public Optional<CustomTileEntity> getTileEntityAt(BlockPos blockPos) {
        return tileEntities.containsKey(blockPos) ? Optional.ofNullable(tileEntities.get(blockPos).getB()) : Optional.empty();
    }

    public <T extends CustomTileEntity> Optional<T> findTileEntity(BlockPos blockPos, Class<T> clazz) {
        if(!tileEntities.containsKey(blockPos))
            return Optional.empty();

        final CustomTileEntity tileEntity = tileEntities.get(blockPos).getB();
        if(!clazz.isInstance(tileEntity))
            return Optional.empty();

        return (Optional<T>) Optional.of(tileEntity);
    }

    public void saveAll() {
        tileEntities.forEach((blockPos, tuple) -> {
            tuple.getA().getDataSerializer().write(blockPos, tuple.getB().save(new SerializableDataCompound()));
        });
    }
}
