package net.vadamdev.customcontent.internal.handlers;

import net.vadamdev.customcontent.api.ITickable;
import net.vadamdev.customcontent.api.items.IInventoryTickable;
import net.vadamdev.viaapi.VIAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author VadamDev
 * @since 28/03/2022
 */
public final class ITickableHandler {
    public static void registerITickableComponent(ITickable iTickable) {
        if(!iTickable.isTickAsync())
            VIAPI.getScheduler().runTaskTimer(VIAPI.get(), r -> iTickable.tick(), 0, iTickable.getInterval());
        else
            VIAPI.getScheduler().runTaskTimerAsynchronously(VIAPI.get(), r -> iTickable.tick(), 0, iTickable.getInterval());
    }

    public static void registerIInventoryTickableComponent(IInventoryTickable tickable) {
        if(!tickable.isTickAsync()) {
            VIAPI.getScheduler().runTaskTimer(VIAPI.get(), r -> {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    int[] requiredSlots = tickable.getRequiredSlots();

                    if(requiredSlots.length == 0)
                        continue;

                    if(requiredSlots[0] == -1) {
                        for (ItemStack stack : player.getInventory().getContents()) {
                            if(tickable.getTestPredicate().test(stack))
                                tickable.tick(player, stack);
                        }
                    }else {
                        for(int slot : requiredSlots) {
                            ItemStack stack = player.getInventory().getItem(slot);
                            if(tickable.getTestPredicate().test(stack))
                                tickable.tick(player, stack);
                        }
                    }
                }
            }, 0, tickable.getInterval());
        }
    }
}
