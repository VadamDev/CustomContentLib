package net.vadamdev.customcontent.lib;

import net.vadamdev.customcontent.CustomContentLib;
import net.vadamdev.customcontent.annotations.ForRemoval;
import net.vadamdev.customcontent.api.blocks.CustomBlock;
import net.vadamdev.customcontent.api.items.CustomFood;
import net.vadamdev.customcontent.api.items.CustomItem;
import net.vadamdev.customcontent.api.items.EmptyItem;
import net.vadamdev.customcontent.api.items.armor.ArmorSet;
import net.vadamdev.customcontent.api.items.armor.CustomArmorPart;
import net.vadamdev.customcontent.internal.BlocksRegistry;
import net.vadamdev.customcontent.internal.CommonRegistry;
import net.vadamdev.customcontent.internal.ItemsRegistry;
import net.vadamdev.customcontent.lib.utils.NBTHelper;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

/**
 * @author VadamDev
 * @since 22/12/2021
 */
public final class CustomContentRegistry {
    private static final CommonRegistry COMMON_REGISTRY = CustomContentLib.instance.getCommonRegistry();
    private static final ItemsRegistry ITEM_REGISTRY = CustomContentLib.instance.getItemsRegistry();
    private static final BlocksRegistry BLOCK_REGISTRY = CustomContentLib.instance.getBlocksRegistry();

    /**
     * Register a CustomContentLib Item and load it's configuration if exists
     * @param customItem
     */
    public static void registerCustomItem(CustomItem customItem) {
        ITEM_REGISTRY.registerCustomItem(customItem);
    }

    /**
     * Register a CustomContentLib Food and load it's configuration if exists
     * @param customFood
     */
    public static void registerCustomFood(CustomFood customFood) {
        ITEM_REGISTRY.registerCustomFood(customFood);
    }

    /**
     * Register a CustomContentLib Armor and load it's configuration if exists
     * @param customArmorPart
     */
    public static void registerCustomArmorPart(CustomArmorPart customArmorPart) {
        ITEM_REGISTRY.registerCustomArmorPart(customArmorPart);
    }

    /**
     * Register a CustomContentLib ArmorSet
     * @param armorSet
     */
    public static void registerArmorSet(ArmorSet armorSet) {
        ITEM_REGISTRY.registerArmorSet(armorSet);
    }

    /**
     * Register a CustomContentLib Block
     * @param customBlock
     */
    public static void registerCustomBlock(CustomBlock customBlock) {
        BLOCK_REGISTRY.registerCustomBlock(customBlock);
    }

    /**
     * Register a ItemStack as a custom item
     * @param emptyItem
     */
    public static void registerEmptyItem(EmptyItem emptyItem) {
        ITEM_REGISTRY.registerEmptyItem(emptyItem);
    }

    @ForRemoval(deadLine = "1.0.0-pre3", reason = "Better empty item system")
    @Deprecated
    public static void registerEmptyItem(ItemStack itemStack, String registryName) {
        CustomContentLib.instance.getLogger().warning(registryName + " was registered with a deprecated method. Please use the new method !");
        ITEM_REGISTRY.registerEmptyItem(new EmptyItem(itemStack, registryName));
    }

    /*
       Getters
     */

    public static ItemStack getCustomItemAsItemStack(String registryName) {
        return COMMON_REGISTRY.getCustomItemstacks().get(registryName);
    }

    public static boolean isCustomItem(ItemStack itemStack, String registryName) {
        String theoreticalRegistryName = NBTHelper.getStringInNBTTag(itemStack, "RegistryName");

        if(theoreticalRegistryName == null)
            return false;

        return COMMON_REGISTRY.isRegistered(registryName) && theoreticalRegistryName.equals(registryName);
    }

    public static boolean isRegistered(String registryName) {
        return COMMON_REGISTRY.isRegistered(registryName);
    }

    public static Map<String, ItemStack> getCustomItemstacks() {
        return COMMON_REGISTRY.getCustomItemstacks();
    }
}
