package net.vadamdev.customcontent.api.items;

import net.vadamdev.customcontent.annotations.ForRemoval;
import net.vadamdev.customcontent.api.IRegistrable;
import net.vadamdev.customcontent.internal.deprecated.events.ItemBreakBlockEvent;
import net.vadamdev.customcontent.internal.deprecated.events.ItemUseEvent;
import net.vadamdev.customcontent.lib.ItemAction;
import net.vadamdev.customcontent.lib.utils.NBTHelper;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author VadamDev
 * @since 22/12/2021
 */
public abstract class CustomItem implements IRegistrable {
    protected ItemStack itemStack;
    protected final List<String> defaultLore;

    public CustomItem(ItemStack itemStack) {
        this.itemStack = NBTHelper.setStringInNBTTag(itemStack, "RegistryName", getRegistryName());
        this.defaultLore = itemStack.getItemMeta().getLore();
    }

    public boolean onClick(Player player, ItemAction action, Block block, BlockFace blockFace, ItemStack item) {
        return false;
    }

    public boolean onEntityClick(Player player, Entity clicked, ItemStack item) {
        return false;
    }

    public boolean hurtEntity(Player player, Entity victim, ItemStack item) {
        return false;
    }

    public boolean mineBlock(Player player, Block block, int exp, ItemStack itemStack) {
        return false;
    }

    @Deprecated
    @ForRemoval(deadLine = "1.0.0")
    public Consumer<ItemUseEvent> getInteractAction() {
        return null;
    }

    @Deprecated
    @ForRemoval(deadLine = "1.0.0")
    public Consumer<ItemBreakBlockEvent> getBlockBreakAction() {
        return null;
    }

    @Override
    public ItemStack getItemStack() {
        return itemStack;
    }
}
