package net.vadamdev.customcontent.api.INDEV.dataproviders;

import org.bukkit.Location;

import java.util.List;

/**
 * @author VadamDev
 * @since 24/04/2022
 */
public interface IDataProvider {
    void saveCustomBlock(Location location);
    void removeCustomBlock(Location location);
    boolean isCustomBlock(Location location);
    List<Location> listCustomBlocks(Location location);
}
