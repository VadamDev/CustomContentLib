package net.vadamdev.customcontent.api.items;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Represents a CCL's Food
 *
 * @author VadamDev
 * @since 16/01/2022
 */
public abstract class CustomFood extends CustomItem {
    public CustomFood(ItemStack itemStack) {
        super(itemStack);
    }

    /**
     * Called when a {@link Player} consume the {@link CustomFood}
     *
     * @param player {@link Player} who ate the {@link CustomFood}
     * @param item {@link ItemStack}
     * @return True if the {@link org.bukkit.event.player.PlayerItemConsumeEvent PlayerItemConsumeEvent} should be cancelled
     */
    public abstract boolean onEat(Player player, ItemStack item);

    /**
     * Determines if the food is edible even if the player has full hunger
     * <br>If true, the food will be instantly ate, you will also need to do the item consumption / hunger yourself
     *
     * @return True if the food should be edible even if the player has full hunger
     */
    public boolean isEdibleEvenWithFullHunger() {
        return !itemStack.getType().isEdible();
    }
}
