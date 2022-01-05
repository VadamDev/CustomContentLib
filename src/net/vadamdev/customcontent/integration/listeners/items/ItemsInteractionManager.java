package net.vadamdev.customcontent.integration.listeners.items;

import net.vadamdev.customcontent.api.items.CustomItem;
import net.vadamdev.customcontent.lib.events.ItemAction;
import net.vadamdev.customcontent.lib.events.ItemUseEvent;
import net.vadamdev.customcontent.lib.events.MaterialUseEvent;
import net.vadamdev.customcontent.utils.NBTHelper;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author VadamDev
 * @since 22.12.2021
 */
public class ItemsInteractionManager {
    /*
       Items and Materials
     */

    private static final Map<Material, Consumer<MaterialUseEvent>> materialActions = new HashMap<>();
    private static final Map<ItemStack, Consumer<ItemUseEvent>> itemStacksActions = new HashMap<>();
    private static final Map<String, Consumer<ItemUseEvent>> customItemActions = new HashMap<>();

    public static void putInteraction(Material material, Consumer<MaterialUseEvent> action) {
        materialActions.put(material, action);
    }

    public static void putInteraction(ItemStack itemStack, Consumer<ItemUseEvent> action) {
        itemStacksActions.put(itemStack, action);
    }

    public static void putInteraction(CustomItem customItem) {
        customItemActions.put(customItem.getRegistryName(), customItem.getAction());
    }

    protected static void checkAndTrigger(PlayerInteractEvent event) {
        ItemStack itemStack = event.getItem().clone();
        Player player = event.getPlayer();

        if(isCustomItem(itemStack)) {
            ItemUseEvent iue = new ItemUseEvent(player, ItemAction.of(event.getAction()), itemStack, event.getClickedBlock());
            Bukkit.getPluginManager().callEvent(iue);
            if(!iue.isCancelled()) getCustomItemAction(itemStack).accept(iue);
        }else if(isSimilar(itemStack)) {
            ItemUseEvent iue = new ItemUseEvent(player, ItemAction.of(event.getAction()), itemStack, event.getClickedBlock());
            Bukkit.getPluginManager().callEvent(iue);
            if(!iue.isCancelled()) getItemStackAction(itemStack).accept(iue);
        }else if(materialActions.containsKey(itemStack.getType())) {
            MaterialUseEvent mue = new MaterialUseEvent(player, ItemAction.of(event.getAction()), itemStack.getType(), event.getClickedBlock());
            Bukkit.getPluginManager().callEvent(mue);
            if(!mue.isCancelled()) materialActions.get(itemStack.getType()).accept(mue);
        }else return;

        event.setCancelled(true);
    }

    /*
        CustomItems
     */

    private static boolean isCustomItem(ItemStack itemStack) {
        String registryName = NBTHelper.getStringInNBTTag(itemStack, "RegistryName");
        if(registryName == null) return false;
        return customItemActions.containsKey(registryName);
    }

    private static Consumer<ItemUseEvent> getCustomItemAction(ItemStack itemStack) {
        return isCustomItem(itemStack) ? customItemActions.get(NBTHelper.getStringInNBTTag(itemStack, "RegistryName")) : null;
    }

    /*
        ItemStacks
     */

    private static Consumer<ItemUseEvent> getItemStackAction(ItemStack i) {
        return itemStacksActions.keySet().stream().filter(itemMap -> isSimilar(itemMap, i)).findFirst().map(itemStacksActions::get).orElse(null);
    }

    private static boolean isSimilar(ItemStack itemStack) {
        return itemStacksActions.keySet().stream().anyMatch(mapItem -> isSimilar(mapItem, itemStack));
    }

    /*
       Utils
     */

    private static boolean isSimilar(ItemStack i1, ItemStack i2) {
        ItemMeta iMeta1 = i1.getItemMeta();
        ItemMeta iMeta2 = i2.getItemMeta();

        return iMeta1.hasDisplayName() == iMeta2.hasDisplayName() && iMeta1.getDisplayName().equals(iMeta2.getDisplayName()) && iMeta1.getItemFlags().containsAll(iMeta2.getItemFlags())
                && i1.getType().equals(i2.getType()) && i1.getData().getData() == i2.getData().getData();
    }
}
