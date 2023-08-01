package net.vadamdev.customcontent.lib.utils;

import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

/**
 * @author VadamDev
 * @since 04/01/2021
 */
public class NBTHelper {
    public static org.bukkit.inventory.ItemStack setInNBTTag(org.bukkit.inventory.ItemStack itemStack, Consumer<NBTTagCompound> consumer) {
        final ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack);

        final NBTTagCompound nbtTag = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();
        consumer.accept(nbtTag);
        nmsItem.setTag(nbtTag);

        return CraftItemStack.asBukkitCopy(nmsItem);
    }

    @Nonnull
    public static NBTTagCompound getNBTTag(org.bukkit.inventory.ItemStack itemStack) {
        final ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack);
        return nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();
    }

    public static org.bukkit.inventory.ItemStack setStringInNBTTag(org.bukkit.inventory.ItemStack itemStack, String key, String value) {
        final ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack);

        final NBTTagCompound nbtTag = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();
        nbtTag.setString(key, value);
        nmsItem.setTag(nbtTag);

        return CraftItemStack.asBukkitCopy(nmsItem);
    }

    @Nullable
    public static String getStringInNBTTag(org.bukkit.inventory.ItemStack itemStack, String key) {
        final ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack);
        return nmsItem.hasTag() ? nmsItem.getTag().getString(key) : null;
    }

    public static org.bukkit.inventory.ItemStack setIntegerInNBTTag(org.bukkit.inventory.ItemStack itemStack, String key, int value) {
        final ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack);

        final NBTTagCompound nbtTag = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();
        nbtTag.setInt(key, value);
        nmsItem.setTag(nbtTag);

        return CraftItemStack.asBukkitCopy(nmsItem);
    }

    public static int getIntegerInNBTTag(org.bukkit.inventory.ItemStack itemStack, String key) {
        ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack);
        return nmsItem.hasTag() ? nmsItem.getTag().getInt(key) : 0;
    }

    public static org.bukkit.inventory.ItemStack setBooleanInNBTTag(org.bukkit.inventory.ItemStack itemStack, String key, boolean value) {
        final ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack);

        final NBTTagCompound nbtTag = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();
        nbtTag.setBoolean(key, value);
        nmsItem.setTag(nbtTag);

        return CraftItemStack.asBukkitCopy(nmsItem);
    }

    public static boolean getBooleanInNBTTag(org.bukkit.inventory.ItemStack itemStack, String key) {
        final ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack);
        return nmsItem.hasTag() && nmsItem.getTag().getBoolean(key);
    }
}
