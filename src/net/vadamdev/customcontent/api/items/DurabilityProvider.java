package net.vadamdev.customcontent.api.items;

import net.vadamdev.customcontent.utils.NBTHelper;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.BiConsumer;

/**
 * @author VadamDev
 * @since 22.12.2021
 */
public interface DurabilityProvider {
    /**
     * Use it to apply the durability effect on your item super(setDefaultDurability) or itemStack = setDefaultDurability
     * @param itemStack Initial Item
     * @return The modified ItemStack (use it in your constructor)
     */
    default ItemStack setDefaultDurability(ItemStack itemStack) {
        itemStack = NBTHelper.setIntegerInNBTTag(itemStack, "Durability", getDefaultDurability());
        if(getDurabilityBar() != null) itemStack = updateDurabilityBar(itemStack, itemStack.getItemMeta().getLore());
        return itemStack;
    }

    /**
     * Store a new durability value in the ItemStack
     * @param itemStack The item
     * @param durability The new durability value
     * @return The modified ItemStack
     */
    default ItemStack setDurability(ItemStack itemStack, int durability) {
        return NBTHelper.setIntegerInNBTTag(itemStack, "Durability", durability);
    }

    /**
     * Get the durability value in the ItemStack
     * @param itemStack The item
     * @return The durability
     */
    default int getDurability(ItemStack itemStack) {
        return NBTHelper.getIntegerInNBTTag(itemStack, "Durability");
    }

    /**
     * Check if the durability is < 0, if true break the item
     * Check if the durability is > getMaxDurability, if true durability = getMaxDurability
     * @param player Item holder
     * @param itemStack The item
     */
    default void checkDurability(Player player, ItemStack itemStack) {
        if(getDurability(itemStack) <= 0) getBreakAction().accept(player, itemStack);
    }

    /**
     * Update the bar in the CustomItem's lore
     * @param itemStack
     * @param lore Default lore
     * @return The modified itemstack, it's actually only used for the CustomItem constructors
     */
    default ItemStack updateDurabilityBar(ItemStack itemStack, List<String> lore) {
        if(getDurabilityBar() != null) getDurabilityBar().applyDurabilityBar(itemStack, lore, getDurability(itemStack), getMaxDurability());
        return itemStack;
    }

    int getMaxDurability();

    default IDurabilityBar getDurabilityBar() {
        return null;
    }

    default Sound getBreakSound() {
        return Sound.ITEM_BREAK;
    }

    default BiConsumer<Player, ItemStack> getBreakAction() {
        return (player, itemStack) -> {
            player.setItemInHand(null);
            player.playSound(player.getLocation(), getBreakSound(), 1, 1);
        };
    }

    default int getDefaultDurability() {
        return getMaxDurability();
    }
}
