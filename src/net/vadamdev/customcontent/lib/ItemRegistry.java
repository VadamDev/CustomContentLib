package net.vadamdev.customcontent.lib;

import net.vadamdev.customcontent.api.items.CustomFood;
import net.vadamdev.customcontent.api.items.CustomItem;
import net.vadamdev.customcontent.api.items.armor.CustomArmorPart;
import net.vadamdev.customcontent.integration.listeners.items.ItemsInteractionManager;
import net.vadamdev.customcontent.lib.exceptions.AlreadyRegisteredException;
import net.vadamdev.customcontent.utils.CustomContentSerializer;
import net.vadamdev.customcontent.utils.FileUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * @author VadamDev
 * @since 22.12.2021
 */
public class ItemRegistry {
    private static final FileConfiguration itemsConfig = FileUtils.ITEMS.getConfig();
    private static final FileConfiguration armorsConfig = FileUtils.ARMORS.getConfig();

    //List of all registred items This list is used for the give command
    private static final Map<String, ItemStack> customItems = new HashMap<>();
    private static final Map<String, CustomArmorPart> customArmorParts = new HashMap<>();

    /**
     * Register a CustomContentLib Item and load it's configuration if exists
     * @param customItem
     */
    public static void registerCustomItem(CustomItem customItem) {
        String registryName = customItem.getRegistryName();

        if(isRegistered(registryName)) {
            try { throw new AlreadyRegisteredException(registryName); }catch (AlreadyRegisteredException e) { e.printStackTrace(); }
            return;
        }

        ItemStack itemStack;

        if(customItem.isConfigurable() && itemsConfig.isSet(registryName)) {
            System.out.println("[CustomContentLib] Loading configuration for " + registryName);
            itemStack = CustomContentSerializer.unserializeItemStack(customItem.getItemStack(), registryName, itemsConfig);
        }else {
            itemStack = customItem.getItemStack();
            System.out.println("[CustomContentLib] Can't load configuration for " + registryName + ", using default ItemStack");
            if(customItem.isConfigurable()) CustomContentSerializer.serializeItemStack(customItem.getItemStack(), customItem.getRegistryName(), itemsConfig);
        }

        ItemsInteractionManager.putInteraction(customItem);

        customItems.put(registryName, itemStack);
    }

    /**
     * Register a CustomContentLib Food and load it's configuration if exists
     * @param customFood
     */
    public static void registerCustomFood(CustomFood customFood) {
        String registryName = customFood.getRegistryName();

        if(isRegistered(registryName)) {
            try { throw new AlreadyRegisteredException(registryName); }catch (AlreadyRegisteredException e) { e.printStackTrace(); }
            return;
        }

        ItemStack itemStack;
        if(customFood.isConfigurable() && itemsConfig.isSet(registryName)) {
            System.out.println("[CustomContentLib] Loading configuration for " + registryName);
            itemStack = CustomContentSerializer.unserializeItemStack(customFood.getItemStack(), registryName, itemsConfig);
        }else {
            itemStack = customFood.getItemStack();
            System.out.println("[CustomContentLib] Can't load configuration for " + registryName + ", using default ItemStack");
            if(customFood.isConfigurable()) CustomContentSerializer.serializeItemStack(customFood.getItemStack(), customFood.getRegistryName(), itemsConfig);
        }

        if(!customFood.isEdibleEvenWithFullHunger()) ItemsInteractionManager.putInteraction(customFood);
        else ItemsInteractionManager.putInteraction(customFood.getRegistryName(), event -> customFood.getAction().accept(new PlayerItemConsumeEvent(event.getPlayer(), event.getItem())));

        customItems.put(registryName, itemStack);
    }

    /**
     * Register a CustomContentLib Armor and load it's configuration if exists
     * @param customItem
     */
    @Deprecated
    public static void registerCustomArmorPart(CustomArmorPart customItem) {
        ItemStack itemStack;
        String registryName = customItem.getRegistryName();

        if(customItem.isConfigurable() && armorsConfig.isSet(registryName)) {
            System.out.println("[CustomContentLib] Loading configuration for " + registryName);
            itemStack = CustomContentSerializer.unserializeItemStack(customItem.getItemStack(), registryName, armorsConfig);
        }else {
            itemStack = customItem.getItemStack();
            System.out.println("[CustomContentLib] Can't load configuration for " + registryName + ", using default ItemStack");
            if(customItem.isConfigurable()) CustomContentSerializer.serializeItemStack(customItem.getItemStack(), customItem.getRegistryName(), armorsConfig);
        }

        customArmorParts.put(registryName, customItem);

        customItems.put(registryName, itemStack);
    }

    public static ItemStack getCustomItemAsItemStack(String registryName) {
        return customItems.get(registryName);
    }

    public static boolean isRegistered(String registryName) {
        return customItems.containsKey(registryName);
    }

    public static CustomArmorPart getCustomArmorPart(String registryName) {
        return customArmorParts.get(registryName);
    }
}
