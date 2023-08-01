package net.vadamdev.customcontent.api.blocks.serialization;

import net.vadamdev.customcontent.lib.BlockPos;

import java.util.Map;

/**
 * Represents a serializer used for {@link net.vadamdev.customcontent.api.blocks.CustomBlock CustomBlock} and {@link net.vadamdev.customcontent.api.blocks.CustomTileEntity CustomTileEntity} serialization
 * <br>See {@link net.vadamdev.customcontent.lib.dataserializer.FileConfigurationDataSerializer FileConfigurationDataSerializer} for implementation example
 *
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
