package net.vadamdev.customcontent.api.blocks.container;

import net.vadamdev.customcontent.annotations.Experimental;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;

/**
 * A {@link net.vadamdev.customcontent.api.blocks.CustomTileEntity CustomTileEntity} implementing this interface will have extra functions to deal with its own inventory.
 *
 * @author VadamDev
 * @since 13/05/2024
 */
@Experimental
public interface Container {
    /**
     * Set the an {@link ItemStack} in the provided slot
     * <p> Use canPlaceItem(slot, itemStack) if you want to check if it's possible
     *
     * @param slot Slot index
     * @param itemStack {@link ItemStack} to work with
     */
    void setItem(int slot, ItemStack itemStack);

    /**
     * Retrieve the {@link ItemStack} contained in the provided slot or null if there isn't
     *
     * @param slot Slot index
     * @return The {@link ItemStack} contained in the provided slot
     */
    @Nullable
    ItemStack getItem(int slot);

    /**
     * Increase the amount of the stack contained in the provided slot
     *
     * @param slot Slot index
     * @param itemStack {@link ItemStack} to work with
     * @return Null, if all items where added or return remaining the {@link ItemStack}
     */
    @Nullable
    ItemStack addItem(int slot, ItemStack itemStack);

    /**
     * Decrease the amount of the stack contained in the provided slot
     *
     * @param slot Slot index
     * @param amount Amount to decrease
     * @return True, if an item was removed
     */
    boolean decrItem(int slot, int amount);

    /**
     * Decrease the amount of the stack contained in the provided slot
     *
     * @param slot Slot index
     * @return True, if an item was removed
     */
    default boolean decrItem(int slot) {
        return decrItem(slot, 1);
    }

    /**
     * Remove every {@link ItemStack} in the container
     */
    void clearContents();

    /**
     * Check if the container is empty
     *
     * @return True, if all slots in the container are empty
     */
    boolean isEmpty();

    /**
     * Check if the provided {@link ItemStack} can be placed in the provided slot
     *
     * @param slot Slot index where the {@link ItemStack} will be placed
     * @param itemStack {@link ItemStack} to work with
     * @return True, if the {@link ItemStack} can be placed in the slot (is slot empty or provided {@link ItemStack} is similar to the one in the slot and total amount doesn't exceed 64)
     */
    default boolean canPlaceItem(int slot, ItemStack itemStack) {
        final ItemStack itemInSlot = getItem(slot);

        if(itemInSlot == null || itemInSlot.getType().equals(Material.AIR))
            return true;

        if(itemInSlot.isSimilar(itemStack))
            return itemInSlot.getAmount() + itemStack.getAmount() <= itemInSlot.getMaxStackSize();

        return false;
    }

    /**
     * Get the container size (Similar to {@link org.bukkit.inventory.Inventory Inventory}.getSize)
     *
     * @return The container size (number of slots)
     */
    int getContainerSize();
}
