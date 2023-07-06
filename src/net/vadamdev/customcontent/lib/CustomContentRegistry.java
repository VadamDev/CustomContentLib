package net.vadamdev.customcontent.lib;

import net.vadamdev.customcontent.CustomContentLib;
import net.vadamdev.customcontent.api.blocks.CustomBlock;
import net.vadamdev.customcontent.api.blocks.CustomTileEntity;
import net.vadamdev.customcontent.api.items.CustomFood;
import net.vadamdev.customcontent.api.items.CustomItem;
import net.vadamdev.customcontent.api.items.EmptyItem;
import net.vadamdev.customcontent.api.items.armor.ArmorSet;
import net.vadamdev.customcontent.api.items.armor.CustomArmorPart;
import net.vadamdev.customcontent.api.tickable.TickableHandler;
import net.vadamdev.customcontent.internal.BlocksRegistry;
import net.vadamdev.customcontent.internal.CommonRegistry;
import net.vadamdev.customcontent.internal.ItemsRegistry;
import net.vadamdev.customcontent.lib.utils.NBTHelper;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Optional;

/**
 * @author VadamDev
 * @since 22/12/2021
 */
public final class CustomContentRegistry {
    private static final CommonRegistry COMMON_REGISTRY = CustomContentLib.instance.getCommonRegistry();
    private static final ItemsRegistry ITEM_REGISTRY = CustomContentLib.instance.getItemsRegistry();
    private static final BlocksRegistry BLOCK_REGISTRY = CustomContentLib.instance.getBlocksRegistry();

    /*
       -----------------------------------ITEMS-----------------------------------
     */

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

    /*
       -----------------------------------AMORS-----------------------------------
     */

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
     * Register a ItemStack as a custom item
     * @param emptyItem
     */
    public static void registerEmptyItem(EmptyItem emptyItem) {
        ITEM_REGISTRY.registerEmptyItem(emptyItem);
    }

    /*
       -----------------------------------BLOCKS-----------------------------------
     */

    /**
     * Register a CustomContentLib Block
     * @param customBlock
     */
    public static void registerCustomBlock(CustomBlock customBlock) {
        BLOCK_REGISTRY.registerCustomBlock(customBlock);
    }

    public static boolean isCustomBlock(BlockPos blockPos) {
        return BLOCK_REGISTRY.isCustomBlock(blockPos);
    }

    public static Optional<CustomTileEntity> getTileEntityAt(BlockPos blockPos) {
        return BLOCK_REGISTRY.getTileEntityAt(blockPos);
    }

    /*
       ---------------------------------TICKABLE-----------------------------------
     */

    public static void registerTickableHandler(TickableHandler tickableHandler) {
        CustomContentLib.instance.getTickableManager().registerTickableHandler(tickableHandler);
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
