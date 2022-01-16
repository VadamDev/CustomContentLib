package net.vadamdev.customcontent.integration.listeners.items;

import net.vadamdev.customcontent.api.items.CustomFood;
import net.vadamdev.customcontent.api.items.CustomItem;
import net.vadamdev.customcontent.lib.events.ItemAction;
import net.vadamdev.customcontent.lib.events.ItemUseEvent;
import net.vadamdev.customcontent.utils.NBTHelper;
import org.bukkit.Bukkit;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author VadamDev
 * @since 22.12.2021
 */
public class ItemsInteractionManager {
    private static final Map<String, Consumer<ItemUseEvent>> customItemActions = new HashMap<>();
    private static final Map<String, Consumer<PlayerItemConsumeEvent>> customFoodsActions = new HashMap<>();

    public static void putInteraction(CustomItem customItem) {
        customItemActions.put(customItem.getRegistryName(), customItem.getAction());
    }

    public static void putInteraction(String registryName, Consumer<ItemUseEvent> action) {
        customItemActions.put(registryName, action);
    }

    public static void putInteraction(CustomFood customFood) {
        customFoodsActions.put(customFood.getRegistryName(), customFood.getAction());
    }

    protected static void triggerInteractAction(PlayerInteractEvent event) {
        ItemStack itemStack = event.getItem().clone();

        if(isCustomItem(itemStack)) {
            ItemUseEvent iue = new ItemUseEvent(event.getPlayer(), ItemAction.of(event.getAction()), itemStack, event.getClickedBlock(), event.getBlockFace());
            Bukkit.getPluginManager().callEvent(iue);
            if(!iue.isCancelled()) getCustomItemAction(itemStack).accept(iue);
        }else return;

        event.setCancelled(true);
    }

    protected static void triggerConsumeAction(PlayerItemConsumeEvent event) {
        ItemStack itemStack = event.getItem().clone();
        if(isCustomFood(itemStack) && !event.isCancelled()) getCustomFoodAction(itemStack).accept(event);
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
       Custom Food
     */
    private static boolean isCustomFood(ItemStack itemStack) {
        String registryName = NBTHelper.getStringInNBTTag(itemStack, "RegistryName");
        if(registryName == null) return false;
        return customFoodsActions.containsKey(registryName);
    }

    private static Consumer<PlayerItemConsumeEvent> getCustomFoodAction(ItemStack itemStack) {
        return isCustomFood(itemStack) ? customFoodsActions.get(NBTHelper.getStringInNBTTag(itemStack, "RegistryName")) : null;
    }
}
