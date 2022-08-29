package net.vadamdev.customcontent.internal.deprecated.items;

import net.vadamdev.customcontent.api.items.CustomFood;
import net.vadamdev.customcontent.api.items.CustomItem;
import net.vadamdev.customcontent.lib.ItemAction;
import net.vadamdev.customcontent.api.items.armor.CustomArmorPart;
import net.vadamdev.customcontent.internal.deprecated.events.CustomArmorEvent;
import net.vadamdev.customcontent.internal.deprecated.events.ItemBreakBlockEvent;
import net.vadamdev.customcontent.internal.deprecated.events.ItemUseEvent;
import net.vadamdev.customcontent.lib.ItemRegistry;
import net.vadamdev.customcontent.lib.utils.NBTHelper;
import org.bukkit.Bukkit;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author VadamDev
 * @since 22/12/2021
 */
@Deprecated
public class ItemsInteractionManager {
    private static final Map<String, Consumer<ItemUseEvent>> customItemActions = new HashMap<>();
    private static final Map<String, Consumer<ItemBreakBlockEvent>> customItemBreakActions = new HashMap<>();
    private static final Map<String, Consumer<PlayerItemConsumeEvent>> customFoodsActions = new HashMap<>();
    private static final Map<String, Consumer<CustomArmorEvent>> customArmorsActions = new HashMap<>();

    @Deprecated
    public static void putInteraction(CustomItem customItem) {
        if(customItem.getInteractAction() != null)
            customItemActions.put(customItem.getRegistryName(), customItem.getInteractAction());

        if(customItem.getBlockBreakAction() != null)
            customItemBreakActions.put(customItem.getRegistryName(), customItem.getBlockBreakAction());
    }

    @Deprecated
    public static void putInteraction(String registryName, Consumer<ItemUseEvent> action) {
        customItemActions.put(registryName, action);
    }

    @Deprecated
    public static void putInteraction(CustomFood customFood) {
        customFoodsActions.put(customFood.getRegistryName(), customFood.getAction());
    }

    @Deprecated
    public static void putInteraction(CustomArmorPart customArmorPart) {
        customArmorsActions.put(customArmorPart.getRegistryName(), customArmorPart.getDamageAction());
    }

    /*
       Custom Items
     */

    //Interract
    @Deprecated
    protected static void triggerInteractAction(PlayerInteractEvent event) {
        ItemStack itemStack = event.getItem().clone();

        if(isCustomInterractItem(itemStack)) {
            ItemUseEvent iue = new ItemUseEvent(event.getPlayer(), ItemAction.of(event.getAction()), itemStack, event.getClickedBlock(), event.getBlockFace());
            Bukkit.getPluginManager().callEvent(iue);
            if(!iue.isCancelled()) getCustomItemAction(itemStack).accept(iue);
        }else return;

        event.setCancelled(true);
    }

    //Break
    @Deprecated
    protected static void triggerBreakAction(BlockBreakEvent event) {
        ItemStack itemStack = event.getPlayer().getItemInHand();

        if(isCustomBreakBlockItem(itemStack)) {
            ItemBreakBlockEvent iue = new ItemBreakBlockEvent(event.getPlayer(), itemStack, event.getBlock());
            Bukkit.getPluginManager().callEvent(iue);
            if(!iue.isCancelled()) getCustomItemBreakBlockAction(itemStack).accept(iue);
        }else return;

        event.setCancelled(true);
    }

    /*
       Custom Food
     */
    @Deprecated
    protected static void triggerConsumeAction(PlayerItemConsumeEvent event) {
        ItemStack itemStack = event.getItem().clone();
        if(isCustomFood(itemStack) && !event.isCancelled()) {
            event.setCancelled(true);
            getCustomFoodAction(itemStack).accept(event);
        }
    }

    /*
        Custom Items Getters
     */
    private static boolean isCustomInterractItem(ItemStack itemStack) {
        String registryName = NBTHelper.getStringInNBTTag(itemStack, "RegistryName");
        if(registryName == null) return false;
        return customItemActions.containsKey(registryName);
    }

    private static boolean isCustomBreakBlockItem(ItemStack itemStack) {
        String registryName = NBTHelper.getStringInNBTTag(itemStack, "RegistryName");
        if(registryName == null) return false;
        return customItemBreakActions.containsKey(registryName);
    }

    private static Consumer<ItemUseEvent> getCustomItemAction(ItemStack itemStack) {
        return isCustomInterractItem(itemStack) ? customItemActions.get(NBTHelper.getStringInNBTTag(itemStack, "RegistryName")) : null;
    }

    private static Consumer<ItemBreakBlockEvent> getCustomItemBreakBlockAction(ItemStack itemStack) {
        return isCustomBreakBlockItem(itemStack) ? customItemBreakActions.get(NBTHelper.getStringInNBTTag(itemStack, "RegistryName")) : null;
    }

    /*
       Custom Food Getters
     */
    private static boolean isCustomFood(ItemStack itemStack) {
        String registryName = NBTHelper.getStringInNBTTag(itemStack, "RegistryName");
        if(registryName == null) return false;
        return customFoodsActions.containsKey(registryName);
    }

    private static Consumer<PlayerItemConsumeEvent> getCustomFoodAction(ItemStack itemStack) {
        return isCustomFood(itemStack) ? customFoodsActions.get(NBTHelper.getStringInNBTTag(itemStack, "RegistryName")) : null;
    }

    /*
       Custom Armor Part Getters
     */

    private static boolean isCustomArmorPart(ItemStack itemStack) {
        if(NBTHelper.getStringInNBTTag(itemStack ,"RegistryName") == null || !NBTHelper.getBooleanInNBTTag(itemStack ,"CustomArmorPart")) return false;
        return ItemRegistry.isRegistered(NBTHelper.getStringInNBTTag(itemStack ,"RegistryName"));
    }

    @Deprecated
    public static Consumer<CustomArmorEvent> getCustomArmorPartAction(ItemStack itemStack) {
        return isCustomArmorPart(itemStack) ? customArmorsActions.get(NBTHelper.getStringInNBTTag(itemStack, "RegistryName")) : null;
    }
}
