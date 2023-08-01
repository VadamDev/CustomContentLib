package net.vadamdev.customcontent.api.items;

import net.vadamdev.customcontent.api.common.IRegistrable;
import net.vadamdev.customcontent.lib.utils.NBTHelper;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

/**
 * Represents a CCL's Food
 *
 * @author VadamDev
 * @since 16/01/2022
 */
public abstract class CustomFood implements IRegistrable {
    protected ItemStack itemStack;

    public CustomFood(ItemStack itemStack) {
        this.itemStack = NBTHelper.setStringInNBTTag(itemStack, "RegistryName", getRegistryName());
    }

    /**
     * Called when a {@link Player} consume the {@link CustomFood}
     *
     * @param player {@link Player} who ate the {@link CustomFood}
     * @param item {@link ItemStack}
     * @return True if the {@link org.bukkit.event.player.PlayerItemConsumeEvent PlayerItemConsumeEvent} should be cancelled
     */
    public boolean onEat(Player player, ItemStack item) {
        return false;
    }

    /**
     * Determines if the food is edible even if the player has full hunger
     * <br>If true, the food will be instantly ate, you will also need to do the item consumption / hunger yourself
     *
     * @return True if the food should be edible even if the player has full hunger
     */
    public boolean isEdibleEvenWithFullHunger() {
        return !itemStack.getType().isEdible();
    }

    @Nonnull
    @Override
    public ItemStack getItemStack() {
        return itemStack;
    }
}
