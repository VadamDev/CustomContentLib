package net.vadamdev.customcontent.internal;

import net.vadamdev.customcontent.CustomContentLib;
import net.vadamdev.customcontent.api.IRegistrable;
import net.vadamdev.customcontent.api.items.CustomFood;
import net.vadamdev.customcontent.api.items.CustomItem;
import net.vadamdev.customcontent.api.items.armor.ArmorSet;
import net.vadamdev.customcontent.api.items.armor.CustomArmorPart;
import net.vadamdev.customcontent.internal.deprecated.items.ItemsInteractionManager;
import net.vadamdev.customcontent.internal.handlers.ITickableHandler;
import net.vadamdev.customcontent.internal.utils.FileUtils;
import net.vadamdev.customcontent.lib.exceptions.AlreadyRegisteredException;
import net.vadamdev.customcontent.internal.utils.CustomContentSerializer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author VadamDev
 * @since 22/08/2022
 */
public final class GeneralRegistry {
    private final Logger logger;

    private final FileConfiguration itemsConfig;
    private final FileConfiguration armorsConfig;

    private final Map<String, ItemStack> customItemstacks;
    private final Map<String, IRegistrable> customItems;

    public GeneralRegistry() {
        this.logger = CustomContentLib.instance.getLogger();

        this.itemsConfig = FileUtils.ITEMS.getConfig();
        this.armorsConfig = FileUtils.ARMORS.getConfig();

        this.customItemstacks = new HashMap<>();
        this.customItems = new HashMap<>();
    }

    public void registerCustomItem(CustomItem customItem) {
        String registryName = customItem.getRegistryName();

        if(!canRegister(registryName))
            return;

        logger.info("[CustomContentLib] Registration of " + registryName + " (Custom Item, Configurable: " + customItem.isConfigurable() + "))");

        customItemstacks.put(registryName, getItemStackInConfiguration(customItem, itemsConfig));
        customItems.put(registryName, customItem);

        //TODO: LEGACY STUFF
        ItemsInteractionManager.putInteraction(customItem);
    }

    public void registerCustomFood(CustomFood customFood) {
        String registryName = customFood.getRegistryName();

        if(!canRegister(registryName))
            return;

        logger.info("[CustomContentLib] Registration of " + registryName + " (Custom Food, Configurable: " + customFood.isConfigurable() + "))");

        customItemstacks.put(registryName, getItemStackInConfiguration(customFood, itemsConfig));
        customItems.put(registryName, customFood);

        //Todo: LEGACY STUFF
        if(!customFood.isEdibleEvenWithFullHunger())
            ItemsInteractionManager.putInteraction(customFood);
        else
            ItemsInteractionManager.putInteraction(customFood.getRegistryName(), event -> customFood.getAction().accept(new PlayerItemConsumeEvent(event.getPlayer(), event.getItem())));
    }

    public void registerCustomArmorPart(CustomArmorPart customArmorPart) {
        String registryName = customArmorPart.getRegistryName();

        if(!canRegister(registryName))
            return;

        logger.info("[CustomContentLib] Registration of " + registryName + " (Custom Armor Part, Configurable: " + customArmorPart.isConfigurable() + "))");

        customItemstacks.put(registryName, getItemStackInConfiguration(customArmorPart, armorsConfig));
        customItems.put(registryName, customArmorPart);

        //Todo: LEGACY STUFF
        ItemsInteractionManager.putInteraction(customArmorPart);
    }

    public void registerArmorSet(ArmorSet armorSet) {
        ITickableHandler.registerITickableComponent(armorSet);
    }

    public void registerEmptyItem(ItemStack itemStack, String registryName) {
        if(!canRegister(registryName))
            return;

        logger.info("[CustomContentLib] Registering an empty item (" + registryName + ")");

        customItemstacks.put(registryName, itemStack);
    }

    public Map<String, ItemStack> getCustomItemstacks() {
        return customItemstacks;
    }

    public Collection<IRegistrable> getCustomItems() {
        return customItems.values();
    }

    public boolean isRegistered(String registryName) {
        return customItemstacks.containsKey(registryName);
    }

    /*
       Private methods
     */

    private boolean canRegister(String registryName) {
        if(isRegistered(registryName)) {
            try { throw new AlreadyRegisteredException(registryName); }catch (AlreadyRegisteredException e) { e.printStackTrace(); }
            return false;
        }

        return true;
    }

    private ItemStack getItemStackInConfiguration(IRegistrable registrable, FileConfiguration dataFile) {
        ItemStack itemStack = registrable.getItemStack();

        if(registrable.isConfigurable() && dataFile.isSet(registrable.getRegistryName())) {
            itemStack = CustomContentSerializer.unserializeItemStack(registrable.getItemStack(), registrable.getRegistryName(), dataFile);
        }else if(registrable.isConfigurable()) {
            CustomContentSerializer.serializeItemStack(registrable.getItemStack(), registrable.getRegistryName(), dataFile);
        }

        return itemStack;
    }
}
