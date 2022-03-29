package net.vadamdev.customcontent.api.items.armor;

import org.bukkit.Material;

/**
 * @author VadamDev
 * @since 31/12/2021
 */
public enum ArmorType {
    DIAMOND,
    IRON,
    CHAINMAIL,
    GOLD,
    LEATHER;

    public Material get(ArmorPart part) {
        return part.get(this);
    }
}
