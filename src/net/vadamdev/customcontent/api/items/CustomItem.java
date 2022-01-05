package net.vadamdev.customcontent.api.items;

import net.vadamdev.customcontent.lib.events.ItemUseEvent;
import net.vadamdev.customcontent.utils.NBTHelper;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

/**
 * @author VadamDev
 * @since 22.12.2021
 */
public abstract class CustomItem {
    protected ItemStack itemStack;

    public CustomItem(ItemStack itemStack) {
        this.itemStack = NBTHelper.setStringInNBTTag(itemStack, "RegistryName", getRegistryName());
    }

    public abstract String getRegistryName();
    public abstract Consumer<ItemUseEvent> getAction();

    public boolean isConfigurable() {
        return true;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }
}
