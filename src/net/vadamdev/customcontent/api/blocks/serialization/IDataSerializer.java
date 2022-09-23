package net.vadamdev.customcontent.api.blocks.serialization;

import net.vadamdev.customcontent.lib.BlockPos;

import java.util.Map;

/**
 * @author VadamDev
 * @since 16/09/2022
 */
public interface IDataSerializer {
    void write(BlockPos blockPos, SerializableDataCompound dataCompound);
    void write(BlockPos blockPos);

    Map<BlockPos, SerializableDataCompound> readAll();

    void remove(BlockPos blockPos);

    boolean contains(BlockPos blockPos);
}
