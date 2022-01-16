package net.vadamdev.customcontent.api.items;

import net.vadamdev.customcontent.utils.NBTHelper;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

/**
 * @author VadamDev
 * @since 16.01.2022
 */
public abstract class CustomFood {
    protected ItemStack itemStack;

    public CustomFood(ItemStack itemStack) {
        this.itemStack = NBTHelper.setStringInNBTTag(itemStack, "RegistryName", getRegistryName());
    }

    public abstract String getRegistryName();
    public abstract Consumer<PlayerItemConsumeEvent> getAction();

    /**
     * It will make the food instantly ate
     * You also need to do the item consumption yourself
     */
    public boolean isEdibleEvenWithFullHunger() {
        return false;
    }

    public boolean isConfigurable() {
        return true;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }
}
