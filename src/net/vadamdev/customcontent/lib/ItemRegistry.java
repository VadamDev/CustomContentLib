package net.vadamdev.customcontent.lib;

import net.vadamdev.customcontent.CustomContentLib;
import net.vadamdev.customcontent.api.items.CustomFood;
import net.vadamdev.customcontent.api.items.CustomItem;
import net.vadamdev.customcontent.internal.GeneralRegistry;
import net.vadamdev.customcontent.lib.utils.NBTHelper;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

/**
 * @author VadamDev
 * @since 22/12/2021
 */
public final class ItemRegistry {
    private static final GeneralRegistry REGISTRY = CustomContentLib.instance.getGeneralRegistry();

    /**
     * Register a CustomContentLib Item and load it's configuration if exists
     * @param customItem
     */
    public static void registerCustomItem(CustomItem customItem) {
        REGISTRY.registerCustomItem(customItem);
    }

    /**
     * Register a CustomContentLib Food and load it's configuration if exists
     * @param customFood
     */
    public static void registerCustomFood(CustomFood customFood) {
        REGISTRY.registerCustomFood(customFood);
    }

    /**
     * Register a ItemStack as a custom item
     * @param itemStack
     * @param registryName
     */
    public static void registerEmptyItem(ItemStack itemStack, String registryName) {
        REGISTRY.registerEmptyItem(itemStack, registryName);
    }

    /*
       Getters
     */

    public static ItemStack getCustomItemAsItemStack(String registryName) {
        return REGISTRY.getCustomItemstacks().get(registryName);
    }

    public static boolean isCustomItem(ItemStack itemStack, String registryName) {
        String theoreticalRegistryName = NBTHelper.getStringInNBTTag(itemStack, "RegistryName");
        return theoreticalRegistryName != null && REGISTRY.isRegistered(registryName) && NBTHelper.getStringInNBTTag(itemStack, "RegistryName").equals(registryName);
    }

    public static boolean isRegistered(String registryName) {
        return REGISTRY.isRegistered(registryName);
    }

    public static Map<String, ItemStack> getCustomItems() {
        return REGISTRY.getCustomItemstacks();
    }
}
