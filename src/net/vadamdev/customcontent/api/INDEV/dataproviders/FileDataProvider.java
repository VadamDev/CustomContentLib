package net.vadamdev.customcontent.api.INDEV.dataproviders;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

/**
 * @author VadamDev
 * @since 24/04/2022
 */
public class FileDataProvider implements IDataProvider {
    protected final FileConfiguration file;

    public FileDataProvider(FileConfiguration file) {
        this.file = file;
    }

    @Override
    public void saveCustomBlock(Location location) {

    }

    @Override
    public void removeCustomBlock(Location location) {

    }

    @Override
    public boolean isCustomBlock(Location location) {
        return false;
    }

    @Override
    public List<Location> listCustomBlocks(Location location) {
        return null;
    }
}
