package net.vadamdev.customcontent.api.blocks.serialization;

import net.vadamdev.customcontent.lib.BlockPos;
import net.vadamdev.customcontent.lib.serialization.SerializableDataCompound;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a serializer used for {@link net.vadamdev.customcontent.api.blocks.CustomBlock CustomBlock} and {@link net.vadamdev.customcontent.api.blocks.CustomTileEntity CustomTileEntity} serialization
 * <br>See {@link net.vadamdev.customcontent.lib.dataserializer.FileConfigurationDataSerializer FileConfigurationDataSerializer} or {@link net.vadamdev.customcontent.lib.dataserializer.JsonDataSerializer JsonDataSerializer} for implementation example
 *
 * @author VadamDev
 * @since 16/09/2022
 */
public interface IDataSerializer {
    /**
     * An empty {@link IDataSerializer}
     * <br>It can be used as a placeholder or for debugging purposes
     */
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

        @Override
        public void save(boolean force) {}
    };

    void write(BlockPos blockPos, SerializableDataCompound dataCompound);
    void remove(BlockPos blockPos);

    SerializableDataCompound read(BlockPos blockPos);
    Map<BlockPos, SerializableDataCompound> readAll();

    boolean contains(BlockPos blockPos);

    void save(boolean force);
}
