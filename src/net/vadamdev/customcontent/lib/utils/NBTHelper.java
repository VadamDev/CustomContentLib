package net.vadamdev.customcontent.lib.utils;

import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

/**
 * @author VadamDev
 * @since 04/01/2021
 */
public final class NBTHelper {
    private NBTHelper() {}

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

    @Nonnull
    public static String getStringInNBTTag(org.bukkit.inventory.ItemStack itemStack, String key) {
        return getNBTTag(itemStack).getString(key);
    }

    public static int getIntegerInNBTTag(org.bukkit.inventory.ItemStack itemStack, String key) {
        return getNBTTag(itemStack).getInt(key);
    }

    public static boolean getBooleanInNBTTag(org.bukkit.inventory.ItemStack itemStack, String key) {
        return getNBTTag(itemStack).getBoolean(key);
    }

    public static org.bukkit.inventory.ItemStack setStringInNBTTag(org.bukkit.inventory.ItemStack itemStack, String key, String value) {
        return setInNBTTag(itemStack, tag -> tag.setString(key, value));
    }

    public static org.bukkit.inventory.ItemStack setIntegerInNBTTag(org.bukkit.inventory.ItemStack itemStack, String key, int value) {
        return setInNBTTag(itemStack, tag -> tag.setInt(key, value));
    }

    public static org.bukkit.inventory.ItemStack setBooleanInNBTTag(org.bukkit.inventory.ItemStack itemStack, String key, boolean value) {
        return setInNBTTag(itemStack, tag -> tag.setBoolean(key, value));
    }
}
