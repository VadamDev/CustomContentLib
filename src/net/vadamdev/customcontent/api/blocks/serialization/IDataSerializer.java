package net.vadamdev.customcontent.api.blocks.serialization;

import net.vadamdev.customcontent.lib.BlockPos;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a serializer used for {@link net.vadamdev.customcontent.api.blocks.CustomBlock CustomBlock} and {@link net.vadamdev.customcontent.api.blocks.CustomTileEntity CustomTileEntity} serialization
 * <br>See {@link net.vadamdev.customcontent.lib.dataserializer.FileConfigurationDataSerializer FileConfigurationDataSerializer} for implementation example
 *
 * @author VadamDev
 * @since 16/09/2022
 */
public interface IDataSerializer {
    IDataSerializer EMPTY = new IDataSerializer() {
        @Override
        public void write(BlockPos blockPos, SerializableDataCompound dataCompound) {}

        @Override
        public SerializableDataCompound read(BlockPos blockPos) {
            return new SerializableDataCompound();
        }

        @Override
        public Map<BlockPos, SerializableDataCompound> readAll() {
            return new HashMap<>();
        }

        @Override
        public void remove(BlockPos blockPos) {}

        @Override
        public boolean contains(BlockPos blockPos) {
            return false;
        }
    };

    void write(BlockPos blockPos, SerializableDataCompound dataCompound);

    SerializableDataCompound read(BlockPos blockPos);
    Map<BlockPos, SerializableDataCompound> readAll();

    void remove(BlockPos blockPos);

    boolean contains(BlockPos blockPos);
}
