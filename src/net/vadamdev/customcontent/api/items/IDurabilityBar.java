package net.vadamdev.customcontent.api.items;

import org.bukkit.craftbukkit.libs.jline.internal.Nullable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * @author VadamDev
 * @since 17/01/2022
 */
public interface IDurabilityBar {
    /**
     * Create a formatted durability bar. The given placeholder will be replaced with the provided string
     *
     * @param durability Current durability of the item
     * @param maxDurability Max durability of the item
     * @return The formatted durability bar
     */
    @Nullable
    String createDurabilityBar(int durability, int maxDurability);

    /**
     * Determines what text should be replaced with the durability bar in the item's lore
     *
     * @return The placeholder that will be replaced by the durability bar
     */
    @Nullable
    String getPlaceholder();

    /**
     * Called by the {@link DurabilityProvider} each time the durability bar should be updated
     *
     * @param itemStack {@link ItemStack}
     * @param lore Default lore
     * @param durability Current durability of the item
     * @param maxDurability Max durability of the item
     */
    default void applyDurabilityBar(ItemStack itemStack, List<String> lore, int durability, int maxDurability) {
        if(itemStack == null || !itemStack.hasItemMeta())
            return;

        final String placeholder = getPlaceholder();
        final String durabilityBar = createDurabilityBar(durability, maxDurability);

        if(placeholder == null || durabilityBar == null)
            return;

        final List<String> loreCopy = new ArrayList<>(lore);
        for (int i = 0; i < loreCopy.size(); i++) {
            final String line = loreCopy.get(i);
            if(!line.contains(placeholder))
                continue;

            loreCopy.set(i, line.replace(placeholder, durabilityBar));
        }

        ItemMeta im = itemStack.getItemMeta();
        im.setLore(loreCopy);
        itemStack.setItemMeta(im);
    }
}
