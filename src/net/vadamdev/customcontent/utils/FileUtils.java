package net.vadamdev.customcontent.utils;

import net.vadamdev.customcontent.CustomContentIntegration;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public enum FileUtils {
    ITEMS("items.yml"),
    ARMORS("armors.yml"),
    BLOCKS("blocks.yml");

    private final String filename;
    private final File dataForlder;

    FileUtils(String filename) {
        this.filename = filename;
        this.dataForlder = CustomContentIntegration.instance.getDataFolder();
    }

    public File getFile() {
        return new File(dataForlder,filename);
    }

    public FileConfiguration getConfig(){
        return YamlConfiguration.loadConfiguration(getFile());
    }

    public void save(FileConfiguration configuration){
        try {
            configuration.save(getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reload(FileConfiguration configuration) throws IOException, InvalidConfigurationException {
        configuration.load(configuration.getName());
    }

    public String getFilename(){
        return filename;
    }
}
