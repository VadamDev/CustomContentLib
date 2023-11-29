package net.vadamdev.customcontent.internal.registry;

import net.vadamdev.customcontent.api.items.CustomItem;
import net.vadamdev.customcontent.api.items.EmptyItem;
import net.vadamdev.customcontent.api.items.armor.ArmorSet;
import net.vadamdev.customcontent.api.items.armor.CustomArmorPart;
import net.vadamdev.customcontent.internal.CustomContentPlugin;

/**
 * @author VadamDev
 * @since 22/08/2022
 */
public final class ItemsRegistry {
    private final CommonRegistry commonRegistry;

    public ItemsRegistry(CommonRegistry commonRegistry) {
        this.commonRegistry = commonRegistry;
    }

    public void registerCustomItem(CustomItem customItem) {
        commonRegistry.checkRegistry(customItem.getRegistryName());

        commonRegistry.register(customItem);
    }

    public void registerCustomArmorPart(CustomArmorPart customArmorPart) {
        commonRegistry.checkRegistry(customArmorPart.getRegistryName());

        commonRegistry.register(customArmorPart);
    }

    public void registerArmorSet(ArmorSet armorSet) {
        CustomContentPlugin.instance.getTickableManager().registerITickableComponent(armorSet);
    }

    public void registerEmptyItem(EmptyItem emptyItem) {
        commonRegistry.checkRegistry(emptyItem.getRegistryName());

        commonRegistry.register(emptyItem);
    }
}
