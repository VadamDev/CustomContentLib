package net.vadamdev.customcontent.internal.deprecated.events;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author VadamDev
 */
@Deprecated
public class ItemBreakBlockEvent extends PlayerEvent implements Cancellable {
    private final ItemStack item;
    private final Block block;
    private boolean cancelled;

    @Deprecated
    public ItemBreakBlockEvent(Player who, ItemStack item, Block block) {
        super(who);

        this.item = item;
        this.block = block;
    }

    @Deprecated
    public ItemStack getItem() {
        return item;
    }

    @Deprecated
    public Block getBlock() {
        return block;
    }

    @Deprecated
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Deprecated
    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }

    public static final HandlerList handlers = new HandlerList();
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
