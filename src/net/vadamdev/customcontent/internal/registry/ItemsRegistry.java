package net.vadamdev.customcontent.internal.registry;

import net.vadamdev.customcontent.api.items.CustomFood;
import net.vadamdev.customcontent.api.items.CustomItem;
import net.vadamdev.customcontent.api.items.EmptyItem;
import net.vadamdev.customcontent.api.items.armor.ArmorSet;
import net.vadamdev.customcontent.api.items.armor.CustomArmorPart;
import net.vadamdev.customcontent.internal.CustomContentPlugin;

import java.util.logging.Logger;

/**
 * @author VadamDev
 * @since 22/08/2022
 */
public final class ItemsRegistry {
    private final Logger logger;

    private final CommonRegistry commonRegistry;

    public ItemsRegistry(CommonRegistry commonRegistry) {
        this.logger = CustomContentPlugin.instance.getLogger();

        this.commonRegistry = commonRegistry;
    }

    public void registerCustomItem(CustomItem customItem) {
        final String registryName = customItem.getRegistryName();
        commonRegistry.checkRegistry(registryName);

        logger.info("Registration of " + registryName + " (Custom Item, Configurable: " + customItem.isConfigurable() + "))");

        commonRegistry.register(customItem);
    }

    public void registerCustomFood(CustomFood customFood) {
        final String registryName = customFood.getRegistryName();
        commonRegistry.checkRegistry(registryName);

        logger.info("Registration of " + registryName + " (Custom Food, Configurable: " + customFood.isConfigurable() + "))");

        commonRegistry.register(customFood);
    }

    public void registerCustomArmorPart(CustomArmorPart customArmorPart) {
        final String registryName = customArmorPart.getRegistryName();
        commonRegistry.checkRegistry(registryName);

        logger.info("Registration of " + registryName + " (Custom Armor Part, Configurable: " + customArmorPart.isConfigurable() + "))");

        commonRegistry.register(customArmorPart);
    }

    public void registerArmorSet(ArmorSet armorSet) {
        CustomContentPlugin.instance.getTickableManager().registerITickableComponent(armorSet);
    }

    public void registerEmptyItem(EmptyItem emptyItem) {
        final String registryName = emptyItem.getRegistryName();
        commonRegistry.checkRegistry(registryName);

        logger.info("Registering an empty item (" + registryName + ")");

        commonRegistry.register(emptyItem);
    }
}
