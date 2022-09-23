package net.vadamdev.customcontent.internal;

import net.vadamdev.customcontent.CustomContentLib;
import net.vadamdev.customcontent.api.items.CustomFood;
import net.vadamdev.customcontent.api.items.CustomItem;
import net.vadamdev.customcontent.api.items.EmptyItem;
import net.vadamdev.customcontent.api.items.IInventoryTickable;
import net.vadamdev.customcontent.api.items.armor.ArmorSet;
import net.vadamdev.customcontent.api.items.armor.CustomArmorPart;
import net.vadamdev.customcontent.internal.handlers.ITickableHandler;
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

    private final FileConfiguration itemsConfig;
    private final FileConfiguration armorsConfig;

    public ItemsRegistry() {
        this.logger = CustomContentLib.instance.getLogger();

        this.commonRegistry = CustomContentLib.instance.getCommonRegistry();

        this.itemsConfig = FileUtils.ITEMS.getConfig();
        this.armorsConfig = FileUtils.ARMORS.getConfig();
    }

    public void registerCustomItem(CustomItem customItem) {
        String registryName = customItem.getRegistryName();

        if(!commonRegistry.canRegister(registryName))
            return;

        logger.info("Registration of " + registryName + " (Custom Item, Configurable: " + customItem.isConfigurable() + "))");

        commonRegistry.register(customItem, itemsConfig);

        if(customItem instanceof IInventoryTickable)
            ITickableHandler.registerIInventoryTickableComponent((IInventoryTickable) customItem);
    }

    public void registerCustomFood(CustomFood customFood) {
        String registryName = customFood.getRegistryName();

        if(!commonRegistry.canRegister(registryName))
            return;

        logger.info("Registration of " + registryName + " (Custom Food, Configurable: " + customFood.isConfigurable() + "))");

        commonRegistry.register(customFood, itemsConfig);

        if(customFood instanceof IInventoryTickable)
            ITickableHandler.registerIInventoryTickableComponent((IInventoryTickable) customFood);
    }

    public void registerCustomArmorPart(CustomArmorPart customArmorPart) {
        String registryName = customArmorPart.getRegistryName();

        if(!commonRegistry.canRegister(registryName))
            return;

        logger.info("Registration of " + registryName + " (Custom Armor Part, Configurable: " + customArmorPart.isConfigurable() + "))");

        commonRegistry.register(customArmorPart, armorsConfig);
    }

    public void registerArmorSet(ArmorSet armorSet) {
        ITickableHandler.registerITickableComponent(armorSet);
    }

    public void registerEmptyItem(EmptyItem emptyItem) {
        String registryName = emptyItem.getRegistryName();

        if(!commonRegistry.canRegister(registryName))
            return;

        logger.info("Registering an empty item (" + registryName + ")");

        commonRegistry.register(emptyItem, itemsConfig);
    }
}
