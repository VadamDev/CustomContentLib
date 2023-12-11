package net.vadamdev.customcontent.internal.handlers.blocks;

import net.vadamdev.customcontent.api.blocks.CustomTileEntity;
import net.vadamdev.customcontent.api.blocks.serialization.IDataSerializer;
import net.vadamdev.customcontent.api.common.tickable.ITickable;
import net.vadamdev.customcontent.lib.BlockPos;
import net.vadamdev.viapi.tools.tuple.ImmutablePair;
import net.vadamdev.viapi.tools.tuple.Pair;

import java.util.*;
import java.util.logging.Logger;

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

    public void addTileEntity(BlockPos blockPos, IDataSerializer dataSerializer, CustomTileEntity tileEntity) {
        if(tileEntity instanceof ITickable)
            tickableHandler.sumbit(tileEntity);

        tileEntities.put(blockPos, new ImmutablePair<>(dataSerializer, tileEntity));
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

    public void saveAll(Logger logger) {
        if(tileEntities.isEmpty())
            return;

        final long before = System.currentTimeMillis();

        Map<IDataSerializer, Set<Pair<BlockPos, CustomTileEntity>>> data = new HashMap<>();
        tileEntities.forEach((blockPos, tuple) ->
                data.computeIfAbsent(tuple.getLeft(), k -> new HashSet<>()).add(new ImmutablePair<>(blockPos, tuple.getRight())));

        int saved = 0;
        for(Map.Entry<IDataSerializer, Set<Pair<BlockPos, CustomTileEntity>>> entry : data.entrySet()) {
            final IDataSerializer dataSerializer = entry.getKey();

            for (Pair<BlockPos, CustomTileEntity> pair : entry.getValue()) {
                dataSerializer.write(pair.getLeft(), pair.getRight().save(dataSerializer.read(pair.getLeft())));
                saved++;
            }

            dataSerializer.save(true);
        }

        logger.info("-> Saved " + saved + " Custom Tile Entities (Took: " + (System.currentTimeMillis() - before) + " ms)");
    }
}
