package net.vadamdev.customcontent.api.items;

import net.vadamdev.customcontent.lib.utils.NBTHelper;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * A {@link CustomItem} implementing this interface will have support for custom durability.
 *
 * @author VadamDev
 * @since 22/12/2021
 */
public interface DurabilityProvider {
    /**
     * Return the max durability of the {@link CustomItem}
     *
     * @return The max durability of the {@link CustomItem}
     */
    int getMaxDurability();

    /**
     * Return the default durability of the {@link CustomItem} (default = getMaxDurability)
     * @return The default durability of the {@link CustomItem}
     */
    default int getDefaultDurability() {
        return getMaxDurability();
    }

    /**
     * Return a {@link IDurabilityBar} that can be updated with the updateDurabilityBar() method
     *
     * @return A {@link IDurabilityBar}
     */
    @Nullable
    default IDurabilityBar getDurabilityBar() {
        return null;
    }

    /**
     * Return the {@link Sound} used when the item get out of durability
     *
     * @return The {@link Sound} used when the item get out of durability
     */
    default Sound getBreakSound() {
        return Sound.ITEM_BREAK;
    }

    /**
     * Return a {@link BiConsumer} that will be accepted by the checkDurability method if the item run out of durability
     * @return A {@link BiConsumer} that will be accepted by the checkDurability method
     */
    default BiConsumer<Player, ItemStack> getBreakAction() {
        return (player, itemStack) -> {
            player.setItemInHand(null);
            player.playSound(player.getLocation(), getBreakSound(), 1, 1);
        };
    }

    /**
     * Return a copy of the provided {@link ItemStack} with default durability in it.
     * <br>This can be used like this:
     * <pre>
     *     class ExampleItem extends CustomItem implements DurabilityProvider {
     *         ExampleItem(ItemStack itemStack) {
     *             super(setDefaultDurability(itemStack))
     *         }
     *     }
     * </pre>
     *
     * @param itemStack {@link ItemStack}
     * @return A copy of the provided {@link ItemStack} with default durability in it
     */
    default ItemStack setDefaultDurability(ItemStack itemStack) {
        final ItemStack newItemStack = NBTHelper.setInNBTTag(itemStack, tag -> {
            tag.setInt("Durability", getDefaultDurability());
            tag.setInt("MaxDurability", getMaxDurability());
        });

        updateDurabilityBar(newItemStack, itemStack.getItemMeta().getLore());

        return newItemStack;
    }

    /**
     * Change the durability value in the provided {@link ItemStack}
     *
     * @param itemStack {@link ItemStack}
     * @param durability The new durability value
     * @return The modified {@link ItemStack}
     */
    default ItemStack setDurability(ItemStack itemStack, int durability) {
        return NBTHelper.setIntegerInNBTTag(itemStack, "Durability", durability);
    }

    /**
     * Decrease the durability in the provided {@link ItemStack}
     * <br>It also checks if the item gets out of durability, if true return null and accept the getBreakAction {@link BiConsumer}
     *
     * @param itemStack {@link ItemStack}
     * @param player Holder of the {@link ItemStack}
     * @return The modified {@link ItemStack} or null if the item run out of durability
     */
    @Nullable
    default ItemStack decreaseDurability(ItemStack itemStack, @Nullable Player player) {
        final int newDurability = getDurability(itemStack) - 1;

        if(player != null && newDurability < 1) {
            getBreakAction().accept(player, itemStack);
            return null;
        }

        return setDurability(itemStack, newDurability);
    }

    /**
     * Get the durability in the provided {@link ItemStack}
     *
     * @param itemStack {@link ItemStack}
     * @return The durability in the provided {@link ItemStack}
     */
    default int getDurability(ItemStack itemStack) {
        return NBTHelper.getIntegerInNBTTag(itemStack, "Durability");
    }

    /**
     * Check if the durability is <= 1, if true accept the {@link BiConsumer} provided by the getBreakAction method
     * <br>Check if the durability is > getMaxDurability, if true set the durability to getMaxDurability
     *
     * @param player Current {@link ItemStack} holder
     * @param itemStack {@link ItemStack}
     */
    default void checkDurability(Player player, ItemStack itemStack) {
        final int durability = getDurability(itemStack);

        if(durability < 1)
            getBreakAction().accept(player, itemStack);
        else if(durability > getMaxDurability())
            setDurability(itemStack, getMaxDurability());
    }

    /**
     * Update the {@link IDurabilityBar} in the {@link CustomItem}'s lore
     *
     * @param itemStack {@link ItemStack}
     * @param lore {@link ItemStack}'s default lore
     */
    default void updateDurabilityBar(ItemStack itemStack, List<String> lore) {
        final IDurabilityBar durabilityBar = getDurabilityBar();
        if(durabilityBar == null)
            return;

        durabilityBar.applyDurabilityBar(itemStack, lore, getDurability(itemStack), getMaxDurability());
    }
}
