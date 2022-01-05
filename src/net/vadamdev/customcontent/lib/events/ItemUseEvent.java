package net.vadamdev.customcontent.lib.events;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;

public class ItemUseEvent extends PlayerEvent implements Cancellable {
    private ItemAction action;
    private ItemStack item;
    private Block clickedBlock;
    private boolean cancelled;

    public ItemUseEvent(Player who, ItemAction action, ItemStack item, Block clickedBlock) {
        super(who);

        this.player = who;
        this.action = action;
        this.item = item;
        this.clickedBlock = clickedBlock;
    }

    public ItemAction getAction() {
        return action;
    }

    public ItemStack getItem() {
        return item;
    }

    public Block getClickedBlock() {
        return clickedBlock;
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
