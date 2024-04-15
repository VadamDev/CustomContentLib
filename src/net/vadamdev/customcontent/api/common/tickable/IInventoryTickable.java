package net.vadamdev.customcontent.api.common.tickable;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * <p> A {@link net.vadamdev.customcontent.api.common.IRegistrable IRegistrable} implementing this interface will call inventoryTick() if a player has it in his {@link org.bukkit.inventory.Inventory Inventory}
 *
 * <p> By default, IInventoryTickable will have a period of 20 ticks
 *
 * @author VadamDev
 * @since 20/11/2023
 */
public interface IInventoryTickable extends ITickable {
    @Override
    default void tick() {
        final int[] requiredSlots = getRequiredSlots();

        for(Player player : Bukkit.getOnlinePlayers()) {
            final ItemStack[] contents = player.getInventory().getContents();

            for(int i = 0; i < contents.length; i++) {
                final ItemStack itemStack = contents[i];
                if(itemStack == null || !itemStack.hasItemMeta())
                    continue;

                if(!testItem(itemStack))
                    continue;

                if(requiredSlots.length == 0)
                    inventoryTick(i, itemStack, player);
                else {
                    for (int requiredSlot : requiredSlots) {
                        if(i == requiredSlot) {
                            inventoryTick(i, itemStack, player);
                        }
                    }
                }
            }
        }
    }

    /**
     * Called every specified interval if the player has the {@link net.vadamdev.customcontent.api.common.IRegistrable IRegistrable} in they {@link Inventory}
     *
     * @param slot The slot where the {@link ItemStack} is
     * @param itemStack {@link ItemStack}
     * @param player The {@link Player} who posses the provided {@link ItemStack}
     */
    void inventoryTick(int slot, ItemStack itemStack, Player player);

    /**
     * Define if the provided {@link ItemStack} is similar to the {@link net.vadamdev.customcontent.api.common.IRegistrable IRegistrable} ItemStack
     */
    boolean testItem(ItemStack itemStack);

    /**
     * Return an array of slots where the item will be checked
     *
     * @return An array of slots
     */
    default int[] getRequiredSlots() {
        return new int[0];
    }
}
