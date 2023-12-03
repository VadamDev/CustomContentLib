package net.vadamdev.customcontent.internal.utils;

import net.vadamdev.customcontent.internal.CustomContentPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public enum FileUtils {
    CONFIG("config.yml"),
    DESCRIPTIONS("descriptions.yml");

    private final String filename;
    private final File dataFolder;

    FileUtils(String filename) {
        this.filename = filename;
        this.dataFolder = CustomContentPlugin.instance.getDataFolder();
    }

    public File getFile() {
        return new File(dataFolder,filename);
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

    public String getFilename(){
        return filename;
    }
}
