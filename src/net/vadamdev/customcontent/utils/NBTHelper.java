package net.vadamdev.customcontent.utils;

import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;

/**
 * @author VadamDev
 * @since 04/01/2021
 */
public class NBTHelper {
    public static org.bukkit.inventory.ItemStack setStringInNBTTag(org.bukkit.inventory.ItemStack itemStack, String key, String value) {
        ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack);

        NBTTagCompound nbtTag = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();
        nbtTag.setString(key, value);
        nmsItem.setTag(nbtTag);

        return CraftItemStack.asBukkitCopy(nmsItem);
    }

    public static String getStringInNBTTag(org.bukkit.inventory.ItemStack itemStack, String key) {
        ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound nbtTag = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();
        return nbtTag.getString(key);
    }

    public static org.bukkit.inventory.ItemStack setIntegerInNBTTag(org.bukkit.inventory.ItemStack itemStack, String key, int value) {
        ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack);

        NBTTagCompound nbtTag = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();
        nbtTag.setInt(key, value);
        nmsItem.setTag(nbtTag);

        return CraftItemStack.asBukkitCopy(nmsItem);
    }

    public static int getIntegerInNBTTag(org.bukkit.inventory.ItemStack itemStack, String key) {
        ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound nbtTag = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();
        return nbtTag.getInt(key);
    }

    public static org.bukkit.inventory.ItemStack setBooleanInNBTTag(org.bukkit.inventory.ItemStack itemStack, String key, boolean value) {
        ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack);

        NBTTagCompound nbtTag = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();
        nbtTag.setBoolean(key, value);
        nmsItem.setTag(nbtTag);

        return CraftItemStack.asBukkitCopy(nmsItem);
    }

    public static boolean getBooleanInNBTTag(org.bukkit.inventory.ItemStack itemStack, String key) {
        ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound nbtTag = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();
        return nbtTag.getBoolean(key);
    }
}
