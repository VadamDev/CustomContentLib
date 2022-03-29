package net.vadamdev.customcontent.lib;

import net.vadamdev.customcontent.CustomContentIntegration;
import net.vadamdev.customcontent.api.IRegistrable;
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
import java.util.logging.Logger;

/**
 * @author VadamDev
 * @since 22/12/2021
 */
public final class ItemRegistry {
    private static final Logger logger = CustomContentIntegration.instance.getLogger();

    private static final FileConfiguration itemsConfig = FileUtils.ITEMS.getConfig();

    //List of all registred items This list is used for the give command
    protected static final Map<String, ItemStack> customItems = new HashMap<>();

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

        ItemsInteractionManager.putInteraction(customItem);

        customItems.put(registryName, getItemStackInConfiguration(customItem, itemsConfig));
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

        if(!customFood.isEdibleEvenWithFullHunger()) ItemsInteractionManager.putInteraction(customFood);
        else ItemsInteractionManager.putInteraction(customFood.getRegistryName(), event -> customFood.getAction().accept(new PlayerItemConsumeEvent(event.getPlayer(), event.getItem())));

        customItems.put(registryName, getItemStackInConfiguration(customFood, itemsConfig));
    }

    /**
     * THIS METHOD WAS DEPLACED IN THE ArmorRegistry CLASS.
     * @param customArmorPart
     */
    @Deprecated
    public static void registerCustomArmorPart(CustomArmorPart customArmorPart) {
        ArmorRegistry.registerCustomArmorPart(customArmorPart);
    }

    /**
     * Register a ItemStack as a custom item
     * @param itemStack
     * @param registryName
     */
    public static void registerEmptyItem(ItemStack itemStack, String registryName) {
        if(isRegistered(registryName)) {
            try { throw new AlreadyRegisteredException(registryName); }catch (AlreadyRegisteredException e) { e.printStackTrace(); }
            return;
        }

        logger.info("[CustomContentLib] Loading empty item (" + registryName + ")");

        customItems.put(registryName, itemStack);
    }

    public static ItemStack getCustomItemAsItemStack(String registryName) {
        return customItems.get(registryName);
    }

    public static boolean isRegistered(String registryName) {
        return customItems.containsKey(registryName);
    }

    /*
       Private Methods
     */
    protected static ItemStack getItemStackInConfiguration(IRegistrable registrable, FileConfiguration dataFile) {
        ItemStack itemStack = registrable.getItemStack();

        if(registrable.isConfigurable() && dataFile.isSet(registrable.getRegistryName())) {
            logger.info("[CustomContentLib] Loading configuration for " + registrable.getRegistryName());
            itemStack = CustomContentSerializer.unserializeItemStack(registrable.getItemStack(), registrable.getRegistryName(), dataFile);
        }else if(registrable.isConfigurable()) {
            logger.warning("[CustomContentLib] Can't load configuration for " + registrable.getRegistryName() + ", using default ItemStack");
            CustomContentSerializer.serializeItemStack(registrable.getItemStack(), registrable.getRegistryName(), dataFile);
        }

        return itemStack;
    }
}
