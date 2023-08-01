package net.vadamdev.customcontent.api.items.armor;

import org.bukkit.Material;

/**
 * Represents parts of an {@link ArmorSet}
 *
 * @author VadamDev
 * @since 31/12/2021
 */
public enum ArmorPart {
    HELMET,
    CHESTPLATE,
    LEGGINGS,
    BOOTS;

    public Material get(ArmorType type) {
        switch(type) {
            case DIAMOND:
                switch(this) {
                    case HELMET:
                        return Material.DIAMOND_HELMET;
                    case CHESTPLATE:
                        return Material.DIAMOND_CHESTPLATE;
                    case LEGGINGS:
                        return Material.DIAMOND_LEGGINGS;
                    case BOOTS:
                        return Material.DIAMOND_BOOTS;
                }
                break;
            case IRON:
                switch(this) {
                    case HELMET:
                        return Material.IRON_HELMET;
                    case CHESTPLATE:
                        return Material.IRON_CHESTPLATE;
                    case LEGGINGS:
                        return Material.IRON_LEGGINGS;
                    case BOOTS:
                        return Material.IRON_BOOTS;
                }
                break;
            case CHAINMAIL:
                switch(this) {
                    case HELMET:
                        return Material.CHAINMAIL_HELMET;
                    case CHESTPLATE:
                        return Material.CHAINMAIL_CHESTPLATE;
                    case LEGGINGS:
                        return Material.CHAINMAIL_LEGGINGS;
                    case BOOTS:
                        return Material.CHAINMAIL_BOOTS;
                }
                break;
            case GOLD:
                switch(this) {
                    case HELMET:
                        return Material.GOLD_HELMET;
                    case CHESTPLATE:
                        return Material.GOLD_CHESTPLATE;
                    case LEGGINGS:
                        return Material.GOLD_LEGGINGS;
                    case BOOTS:
                        return Material.GOLD_BOOTS;
                }
                break;
            case LEATHER:
                switch(this) {
                    case HELMET:
                        return Material.LEATHER_HELMET;
                    case CHESTPLATE:
                        return Material.LEATHER_CHESTPLATE;
                    case LEGGINGS:
                        return Material.LEATHER_LEGGINGS;
                    case BOOTS:
                        return Material.LEATHER_BOOTS;
                }
                break;
        }

        return null;
    }
}
