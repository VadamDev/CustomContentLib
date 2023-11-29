package net.vadamdev.customcontent.internal.handlers.blocks;

import net.vadamdev.customcontent.api.blocks.CustomBlock;
import net.vadamdev.customcontent.api.blocks.CustomTileEntity;
import net.vadamdev.customcontent.api.blocks.serialization.IDataSerializer;
import net.vadamdev.customcontent.api.common.tickable.ITickable;
import net.vadamdev.customcontent.lib.BlockPos;
import net.vadamdev.viapi.tools.tuple.ImmutablePair;
import net.vadamdev.viapi.tools.tuple.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author VadamDev
 * @since 21/09/2022
 */
public class TileEntityHandler {
    private final Map<BlockPos, Pair<IDataSerializer, CustomTileEntity>> tileEntities;
    private final TickableTileEntityHandler tickableHandler;

    public TileEntityHandler() {
        this.tileEntities = new HashMap<>();
        this.tickableHandler = new TickableTileEntityHandler();
    }

    public void addTileEntity(BlockPos blockPos, CustomBlock customBlock, CustomTileEntity tileEntity) {
        if(tileEntity instanceof ITickable)
            tickableHandler.sumbit(tileEntity);

        tileEntities.put(blockPos, new ImmutablePair<>(customBlock.getDataSerializer(), tileEntity));
    }

    public void removeTileEntity(BlockPos blockPos, boolean tickable) {
        if(tickable)
            tickableHandler.remove(blockPos);

        tileEntities.remove(blockPos);
    }

    public Optional<CustomTileEntity> getTileEntityAt(BlockPos blockPos) {
        return tileEntities.containsKey(blockPos) ? Optional.ofNullable(tileEntities.get(blockPos).getRight()) : Optional.empty();
    }

    public <T extends CustomTileEntity> Optional<T> findTileEntity(BlockPos blockPos, Class<T> clazz) {
        if(!tileEntities.containsKey(blockPos))
            return Optional.empty();

        final CustomTileEntity tileEntity = tileEntities.get(blockPos).getRight();
        if(!clazz.isInstance(tileEntity))
            return Optional.empty();

        return (Optional<T>) Optional.of(tileEntity);
    }

    public void saveAll() {
        if(tileEntities.isEmpty())
            return;

        tileEntities.forEach((blockPos, tuple) -> {
            final IDataSerializer dataSerializer = tuple.getLeft();
            dataSerializer.write(blockPos, tuple.getRight().save(dataSerializer.read(blockPos)));
        });
    }
}
