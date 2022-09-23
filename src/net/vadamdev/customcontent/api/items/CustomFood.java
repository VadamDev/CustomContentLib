package net.vadamdev.customcontent.api.items;

import net.vadamdev.customcontent.api.IRegistrable;
import net.vadamdev.customcontent.lib.utils.NBTHelper;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author VadamDev
 * @since 16/01/2022
 */
public abstract class CustomFood implements IRegistrable {
    protected ItemStack itemStack;

    public CustomFood(ItemStack itemStack) {
        this.itemStack = NBTHelper.setStringInNBTTag(itemStack, "RegistryName", getRegistryName());
    }

    public boolean onEat(Player player, ItemStack item) {
        return false;
    }

    /**
     * It will make the food instantly ate
     * You will also need to do the item consumption / hunger yourself
     */
    public boolean isEdibleEvenWithFullHunger() {
        return false;
    }

    @Override
    public ItemStack getItemStack() {
        return itemStack;
    }
}
