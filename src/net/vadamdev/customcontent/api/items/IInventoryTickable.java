package net.vadamdev.customcontent.api.items;

import net.vadamdev.customcontent.annotations.Experimental;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.Predicate;

/**
 * @author VadamDev
 * @since 05/09/2022
 */
@Experimental
public interface IInventoryTickable {
    void tick(Player player, ItemStack itemStack);

    /**
     * @return interval of each check in minecraft ticks
     */
    int getInterval();

    /**
     * @return a predicate that will define if the provided item is the custom itemstack
     */
    Predicate<ItemStack> getTestPredicate();

    /**
     * @return An array of slot where the item should be ticked
     */
    default int[] getRequiredSlots() {
        return new int[] { -1 };
    }

    /**
     * If you're using that be careful spigot have a lot of issues with async tasks.
     */
    default boolean isTickAsync() {
        return false;
    }
}
