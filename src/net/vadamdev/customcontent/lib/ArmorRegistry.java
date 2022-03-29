package net.vadamdev.customcontent.lib;

import net.vadamdev.customcontent.api.items.armor.ArmorSet;
import net.vadamdev.customcontent.api.items.armor.CustomArmorPart;
import net.vadamdev.customcontent.integration.listeners.items.ItemsInteractionManager;
import net.vadamdev.customcontent.lib.exceptions.AlreadyRegisteredException;
import net.vadamdev.customcontent.utils.FileUtils;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * @author VadamDev
 * @since 28/03/2022
 */
public final class ArmorRegistry {
    private static final FileConfiguration armorsConfig = FileUtils.ARMORS.getConfig();

    /**
     * Register a CustomContentLib Armor and load it's configuration if exists
     * @param customArmorPart
     */
    public static void registerCustomArmorPart(CustomArmorPart customArmorPart) {
        String registryName = customArmorPart.getRegistryName();

        if(ItemRegistry.isRegistered(registryName)) {
            try { throw new AlreadyRegisteredException(registryName); }catch (AlreadyRegisteredException e) { e.printStackTrace(); }
            return;
        }

        ItemsInteractionManager.putInteraction(customArmorPart);

        ItemRegistry.customItems.put(registryName, ItemRegistry.getItemStackInConfiguration(customArmorPart, armorsConfig));
    }

    /**
     * Register a CustomContentLib ArmorSet
     * @param armorSet
     */
    public static void registerArmorSet(ArmorSet armorSet) {
        ITickableHandler.registerITickableComponent(armorSet);
    }
}
