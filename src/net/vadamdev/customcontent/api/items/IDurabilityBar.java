package net.vadamdev.customcontent.api.items;

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
     * Create a text with the Durability and MaxDurability value
     * @param durability Acutal durability of the item
     * @param maxDurability Max durability of the item
     * @return The formatted text
     */
    String createDurabilityBar(int durability, int maxDurability);

    /**
     * The text that will be the DurabilityBar
     */
    String getPlaceholder();

    /**
     * Method called by the DurabilityProvider class.
     * You should theoretically not override it.
     * @param itemStack
     * @param lore Default lore
     * @param durability The durability
     * @param maxDurability The max durability
     */
    default void applyDurabilityBar(ItemStack itemStack, List<String> lore, int durability, int maxDurability) {
        if(itemStack == null || !itemStack.hasItemMeta()) return;

        List<String> newLore = new ArrayList<>(lore);

        for(int index = 0; index < newLore.size(); index++) {
            if(newLore.get(index).contains(getPlaceholder())) {
                newLore.set(index, newLore.get(index).replace(getPlaceholder(), createDurabilityBar(durability, maxDurability)));
                break;
            }
        }

        ItemMeta meta = itemStack.getItemMeta();
        meta.setLore(newLore);
        itemStack.setItemMeta(meta);
    }
}
