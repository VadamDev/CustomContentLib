package net.vadamdev.customcontent.lib.dataserializer;

import net.vadamdev.customcontent.api.blocks.serialization.DataType;
import net.vadamdev.customcontent.api.blocks.serialization.IDataSerializer;
import net.vadamdev.customcontent.api.blocks.serialization.ISerializableData;
import net.vadamdev.customcontent.api.blocks.serialization.SerializableDataCompound;
import net.vadamdev.customcontent.lib.BlockPos;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author VadamDev
 * @since 20/09/2022
 */
public class FileConfigurationDataSerializer implements IDataSerializer {
    private final FileConfiguration configFile;
    private final File dataFile;

    public FileConfigurationDataSerializer(FileConfiguration configFile, File dataFile) {
        this.configFile = configFile;
        this.dataFile = dataFile;
    }

    @Override
    public void write(BlockPos blockPos, SerializableDataCompound dataCompound) {
        dataCompound.getMapCopy().forEach((key, data) -> {
            final String strBlockPos = blockPos.toSerializableString();

            configFile.set(strBlockPos + "." + key + "." + "data", data.serialize());
            configFile.set(strBlockPos + "." + key + "." + "type", data.getType().name());
        });

        save();
    }

    @Override
    public void write(BlockPos blockPos) {
        configFile.set(blockPos.toSerializableString(), "");
        save();
    }

    @Override
    public Map<BlockPos, SerializableDataCompound> readAll() {
        final Map<BlockPos, SerializableDataCompound> dataMap = new HashMap<>();

        for (String strBlockPos : configFile.getKeys(false)) {
            final SerializableDataCompound compound = new SerializableDataCompound();

            final ConfigurationSection section = configFile.getConfigurationSection(strBlockPos);
            if(section != null) {
                for (String sectionKey : section.getKeys(false)) {
                    ISerializableData.parseFrom(
                            DataType.valueOf(section.getString(sectionKey + ".type")),
                            section.getString(sectionKey + ".data")
                    ).ifPresent(serializableData -> compound.put(sectionKey, serializableData));
                }
            }

            dataMap.put(BlockPos.fromSerializableString(strBlockPos), compound);
        }

        return dataMap;
    }

    @Override
    public void remove(BlockPos blockPos) {
        if(!contains(blockPos))
            return;

        configFile.set(blockPos.toSerializableString(), null);

        save();
    }

    @Override
    public boolean contains(BlockPos blockPos) {
        return configFile.isSet(blockPos.toSerializableString());
    }

    private void save() {
        try {
            configFile.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
