package net.vadamdev.customcontent.lib;

import net.vadamdev.customcontent.api.items.CustomItem;
import net.vadamdev.customcontent.api.items.armor.CustomArmorPart;
import net.vadamdev.customcontent.lib.events.ItemUseEvent;
import net.vadamdev.customcontent.lib.events.MaterialUseEvent;
import net.vadamdev.customcontent.integration.listeners.items.ItemsInteractionManager;
import net.vadamdev.customcontent.utils.CustomContentSerializer;
import net.vadamdev.customcontent.utils.FileUtils;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

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
     * You can use it for edit default Minecraft items
     * @param material Material that will provide the action
     * @param event Action
     */
    public static void registerMaterialInteraction(Material material, Consumer<MaterialUseEvent> event) {
        ItemsInteractionManager.putInteraction(material, event);
    }

    /**
     * Use it if you want to add interactions without using CustomItem class
     * @param itemStack ItemStack that will provide the action
     * @param event Action
     */
    public static void registerItemStackInteraction(ItemStack itemStack, Consumer<ItemUseEvent> event) {
        ItemsInteractionManager.putInteraction(itemStack, event);
    }

    /**
     * Register a CustomContentLib Item and load it's configuration if exists
     * @param customItem
     */
    public static void registerCustomItem(CustomItem customItem) {
        ItemStack itemStack;
        String registryName = customItem.getRegistryName();

        if(customItem.isConfigurable() && itemsConfig.isSet(registryName)) {
            System.out.println("[CustomContentLib] Loading configuration for " + registryName);
            itemStack = CustomContentSerializer.unserializeItemStack(customItem.getItemStack(), registryName, itemsConfig);
        }else {
            itemStack = customItem.getItemStack();
            System.out.println("[CustomContentLib] Can't load configuration for " + registryName + ", using default ItemStack");
            if(customItem.isConfigurable()) CustomContentSerializer.serializeItemStack(customItem, itemsConfig);
        }

        ItemsInteractionManager.putInteraction(customItem);

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
            if(customItem.isConfigurable()) CustomContentSerializer.serializeItemStack(customItem, armorsConfig);
        }

        customArmorParts.put(registryName, customItem);

        customItems.put(registryName, itemStack);
    }

    public static ItemStack getCustomItemAsItemStack(String registryName) {
        return customItems.get(registryName);
    }

    public static CustomArmorPart getCustomArmorPart(String registryName) {
        return customArmorParts.get(registryName);
    }
}
