package net.vadamdev.customcontent.lib.dataserializer;

import net.vadamdev.customcontent.lib.BlockPos;
import net.vadamdev.customcontent.lib.serialization.DataType;
import net.vadamdev.customcontent.lib.serialization.SerializableDataCompound;
import net.vadamdev.customcontent.lib.serialization.data.ISerializableData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * A {@link net.vadamdev.customcontent.api.blocks.serialization.IDataSerializer IDataSerializer} implementation that save data to a {@link FileConfiguration}
 *
 * @author VadamDev
 * @since 06/12/2023
 */
public class FileConfigurationDataSerializer extends AbstractDataSerializer {
    private final FileConfiguration configFile;
    private final File file;

    public FileConfigurationDataSerializer(FileConfiguration configFile, File file, long delay) {
        super(delay);

        this.configFile = configFile;
        this.file = file;
    }

    public FileConfigurationDataSerializer(FileConfiguration configFile, File file) {
        this(configFile, file, 3000);
    }

    @Override
    public void write(BlockPos blockPos, SerializableDataCompound dataCompound) {
        final String strBlockPos = blockPos.toSerializableString();

        for(Map.Entry<String, ISerializableData> entry : dataCompound.getMapCopy().entrySet()) {
            final String key = entry.getKey();
            final ISerializableData data = entry.getValue();

            configFile.set(strBlockPos + "." + key + "." + "type", data.getType().name());
            configFile.set(strBlockPos + "." + key + "." + "data", data.serialize());
        }
    }

    @Override
    public void remove(BlockPos blockPos) {
        if(!contains(blockPos))
            return;

        configFile.set(blockPos.toSerializableString(), null);
    }

    @Override
    public SerializableDataCompound read(BlockPos blockPos) {
        final SerializableDataCompound compound = new SerializableDataCompound();
        if(!contains(blockPos))
            return compound;

        final ConfigurationSection section = configFile.getConfigurationSection(blockPos.toSerializableString());
        for (String key : section.getKeys(false)) {
            final ISerializableData serializableData = ISerializableData.parseFrom(DataType.valueOf(section.getString(key + ".type")), section.getString(key + ".data"));

            if(serializableData != null)
                compound.put(key, serializableData);
        }

        return compound;
    }

    @Override
    public Map<BlockPos, SerializableDataCompound> readAll() {
        final Map<BlockPos, SerializableDataCompound> dataMap = new HashMap<>();

        for (String strBlockPos : configFile.getKeys(false)) {
            final SerializableDataCompound compound = new SerializableDataCompound();

            final ConfigurationSection section = configFile.getConfigurationSection(strBlockPos);
            if(section != null) {
                for (String key : section.getKeys(false)) {
                    final ISerializableData serializableData = ISerializableData.parseFrom(DataType.valueOf(section.getString(key + ".type")), section.getString(key + ".data"));

                    if(serializableData != null)
                        compound.put(key, serializableData);
                }
            }

            dataMap.put(BlockPos.fromSerializableString(strBlockPos), compound);
        }

        return dataMap;
    }

    @Override
    public boolean contains(BlockPos blockPos) {
        return configFile.isSet(blockPos.toSerializableString());
    }

    @Override
    protected void save() {
        try {
            configFile.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
