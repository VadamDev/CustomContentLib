package net.vadamdev.customcontent.api.common.tickable;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.function.Predicate;

/**
 * A {@link net.vadamdev.customcontent.api.common.IRegistrable IRegistrable} implementing this interface will call inventoryTick() if a player has it in his {@link org.bukkit.inventory.Inventory Inventory}
 * <br><br>
 * By default, IInventoryTickable will have a period of 20 ticks
 *
 * @author VadamDev
 * @since 20/11/2023
 */
public interface IInventoryTickable extends ITickable {
    @Override
    default void tick() {
        final int[] requiredSlots = getRequiredSlots();
        final Predicate<ItemStack> predicate = testItem();

        for(Player player : Bukkit.getOnlinePlayers()) {
            int slot = 0;
            for (ItemStack itemStack : player.getInventory().getContents()) {
                if(itemStack == null || !itemStack.hasItemMeta()) {
                    slot++;
                    continue;
                }

                if(predicate.test(itemStack)) {
                    if(requiredSlots.length == 0)
                        inventoryTick(slot, itemStack, player);
                    else {
                        for(int requiredSlot : requiredSlots) {
                            if(requiredSlot == slot)
                                inventoryTick(slot, itemStack, player);
                        }
                    }
                }

                slot++;
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
     * Return a {@link Predicate} that will define if the provided {@link ItemStack} is similar to the {@link net.vadamdev.customcontent.api.common.IRegistrable IRegistrable} ItemStack
     */
    Predicate<ItemStack> testItem();

    /**
     * Return an array of slots where the item will be checked
     *
     * @return An array of slots
     */
    default int[] getRequiredSlots() {
        return new int[0];
    }
}
