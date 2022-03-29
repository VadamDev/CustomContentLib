package net.vadamdev.customcontent.lib;

import net.vadamdev.customcontent.api.items.CustomItem;
import net.vadamdev.customcontent.api.items.DurabilityProvider;
import net.vadamdev.customcontent.utils.NBTHelper;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * @author VadamDev
 * @since 07/01/2022
 */
public final class DefaultItemTransformer {
    private static final Map<String, CustomItem> transformer = new HashMap<>();

    /**
     * Provide a link between default customitems, running with listeners to CustomContentLib items
     * @param itemName The default item name
     * @param transform The CustomContentLib item
     */
    public static void registerDefaultItemKey(String itemName, CustomItem transform) {
        transformer.put(itemName, transform);
    }

    /**
     * @param itemStack The item that need to be changed
     * @return The transformed item, ready to be use by CustomContentLib
     */
    public static ItemStack transformToCustomItem(ItemStack itemStack) {
        if(itemStack == null || !itemStack.hasItemMeta() || !itemStack.getItemMeta().hasDisplayName()) return null;

        String displayName = itemStack.getItemMeta().getDisplayName();
        if(transformer.containsKey(displayName)) {
            CustomItem customItem = transformer.get(displayName);
            ItemStack newItemStack = NBTHelper.setStringInNBTTag(itemStack, "RegistryName", customItem.getRegistryName());
            if(customItem instanceof DurabilityProvider) newItemStack = ((DurabilityProvider) customItem).setDefaultDurability(newItemStack);
            return newItemStack;
        }else try { throw new NullPointerException(displayName + " is not registered in the transformer !"); }catch(Exception e) { e.printStackTrace(); }

        return null;
    }
}
