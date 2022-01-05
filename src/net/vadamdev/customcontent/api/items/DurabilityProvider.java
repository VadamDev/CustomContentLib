package net.vadamdev.customcontent.api.items;

import net.vadamdev.customcontent.utils.NBTHelper;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
        return NBTHelper.setIntegerInNBTTag(itemStack, "Durability", getDefaultDurability());
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

    int getMaxDurability();

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
