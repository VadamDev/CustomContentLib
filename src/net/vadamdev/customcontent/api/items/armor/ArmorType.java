package net.vadamdev.customcontent.api.items.armor;

import org.bukkit.Material;

/**
 * Represents vanilla armor types
 *
 * @author VadamDev
 * @since 31/12/2021
 */
public enum ArmorType {
    DIAMOND,
    IRON,
    CHAINMAIL,
    GOLD,
    LEATHER;

    public Material toBukkitMaterial() {
        switch(this) {
            case DIAMOND:
                return Material.DIAMOND;
            case IRON:
                return Material.IRON_INGOT;
            case GOLD:
                return Material.GOLD_INGOT;
            case LEATHER:
                return Material.LEATHER;
            default:
                return null;
        }
    }

    public Material get(ArmorPart part) {
        return part.get(this);
    }
}
