package net.vadamdev.customcontent.lib.events;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;

public class ItemBreakBlockEvent extends PlayerEvent implements Cancellable {
    private final ItemStack item;
    private final Block block;
    private boolean cancelled;

    public ItemBreakBlockEvent(Player who, ItemStack item, Block block) {
        super(who);

        this.player = who;
        this.item = item;
        this.block = block;
    }

    public ItemStack getItem() {
        return item;
    }

    public Block getBlock() {
        return block;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

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
