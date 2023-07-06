package net.vadamdev.customcontent.internal;

import net.vadamdev.customcontent.CustomContentLib;
import net.vadamdev.customcontent.api.items.CustomFood;
import net.vadamdev.customcontent.api.items.CustomItem;
import net.vadamdev.customcontent.api.items.EmptyItem;
import net.vadamdev.customcontent.api.items.armor.ArmorSet;
import net.vadamdev.customcontent.api.items.armor.CustomArmorPart;
import net.vadamdev.customcontent.internal.utils.FileUtils;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.logging.Logger;

/**
 * @author VadamDev
 * @since 22/08/2022
 */
public final class ItemsRegistry {
    private final Logger logger;

    private final CommonRegistry commonRegistry;

    public ItemsRegistry() {
        this.logger = CustomContentLib.instance.getLogger();

        this.commonRegistry = CustomContentLib.instance.getCommonRegistry();
    }

    public void registerCustomItem(CustomItem customItem) {
        String registryName = customItem.getRegistryName();

        if(!commonRegistry.canRegister(registryName))
            return;

        logger.info("Registration of " + registryName + " (Custom Item, Configurable: " + customItem.isConfigurable() + "))");

        commonRegistry.register(customItem, FileUtils.ITEMS);
    }

    public void registerCustomFood(CustomFood customFood) {
        String registryName = customFood.getRegistryName();

        if(!commonRegistry.canRegister(registryName))
            return;

        logger.info("Registration of " + registryName + " (Custom Food, Configurable: " + customFood.isConfigurable() + "))");

        commonRegistry.register(customFood, FileUtils.ITEMS);
    }

    public void registerCustomArmorPart(CustomArmorPart customArmorPart) {
        String registryName = customArmorPart.getRegistryName();

        if(!commonRegistry.canRegister(registryName))
            return;

        logger.info("Registration of " + registryName + " (Custom Armor Part, Configurable: " + customArmorPart.isConfigurable() + "))");

        commonRegistry.register(customArmorPart, FileUtils.ARMORS);
    }

    public void registerArmorSet(ArmorSet armorSet) {
        CustomContentLib.instance.getTickableManager().registerITickableComponent(armorSet);
    }

    public void registerEmptyItem(EmptyItem emptyItem) {
        String registryName = emptyItem.getRegistryName();

        if(!commonRegistry.canRegister(registryName))
            return;

        logger.info("Registering an empty item (" + registryName + ")");

        commonRegistry.register(emptyItem, FileUtils.ITEMS);
    }
}
