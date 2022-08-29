package net.vadamdev.customcontent.lib;

import net.vadamdev.customcontent.CustomContentLib;
import net.vadamdev.customcontent.api.items.armor.ArmorSet;
import net.vadamdev.customcontent.api.items.armor.CustomArmorPart;
import net.vadamdev.customcontent.internal.GeneralRegistry;

/**
 * @author VadamDev
 * @since 28/03/2022
 */
public final class ArmorRegistry {
    private static final GeneralRegistry REGISTRY = CustomContentLib.instance.getGeneralRegistry();

    /**
     * Register a CustomContentLib Armor and load it's configuration if exists
     * @param customArmorPart
     */
    public static void registerCustomArmorPart(CustomArmorPart customArmorPart) {
        REGISTRY.registerCustomArmorPart(customArmorPart);
    }

    /**
     * Register a CustomContentLib ArmorSet
     * @param armorSet
     */
    public static void registerArmorSet(ArmorSet armorSet) {
        REGISTRY.registerArmorSet(armorSet);
    }
}
