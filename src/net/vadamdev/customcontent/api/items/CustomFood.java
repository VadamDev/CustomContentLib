package net.vadamdev.customcontent.api.items;

import net.vadamdev.customcontent.annotations.ForRemoval;
import net.vadamdev.customcontent.api.IRegistrable;
import net.vadamdev.customcontent.lib.utils.NBTHelper;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

/**
 * @author VadamDev
 * @since 16/01/2022
 */
public abstract class CustomFood implements IRegistrable {
    protected ItemStack itemStack;

    public CustomFood(ItemStack itemStack) {
        this.itemStack = NBTHelper.setStringInNBTTag(itemStack, "RegistryName", getRegistryName());
    }

    public boolean onEat(Player player, ItemStack item) {
        return false;
    }

    @Deprecated
    @ForRemoval(deadLine = "1.0.0")
    public Consumer<PlayerItemConsumeEvent> getAction() {
        return null;
    }

    /**
     * It will make the food instantly ate
     * You will also need to do the item consumption / hunger yourself
     */
    public boolean isEdibleEvenWithFullHunger() {
        return false;
    }

    @Override
    public ItemStack getItemStack() {
        return itemStack;
    }
}
