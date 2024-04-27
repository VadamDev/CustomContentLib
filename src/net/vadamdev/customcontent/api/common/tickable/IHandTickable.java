package net.vadamdev.customcontent.api.common.tickable;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.Predicate;

/**
 * A {@link net.vadamdev.customcontent.api.common.IRegistrable IRegistrable} implementing this interface will call handTick() if a player has it in his hand
 * <br><br>
 * By default, IHandTickable will have a period of 20 ticks
 *
 * @author VadamDev
 * @since 22/11/2023
 */
public interface IHandTickable extends ITickable {
    @Override
    default void tick() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            final ItemStack itemStack = player.getItemInHand();
            if(itemStack == null || !itemStack.hasItemMeta())
                continue;

            if(testItem(itemStack))
                handTick(itemStack, player);
        }
    }

    /**
     * Called every specified interval if the player has the {@link net.vadamdev.customcontent.api.common.IRegistrable IRegistrable} in they hand
     *
     * @param itemStack {@link ItemStack}
     * @param player The {@link Player} who posses the provided {@link ItemStack}
     */
    void handTick(ItemStack itemStack, Player player);

    /**
     * Return a {@link Predicate} that will define if the provided {@link ItemStack} is similar to the {@link net.vadamdev.customcontent.api.common.IRegistrable IRegistrable} ItemStack
     */
    boolean testItem(ItemStack itemStack);
}
