package net.vadamdev.customcontent.lib.events;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author VadamDev
 * @since 23/02/2022
 */
public class CustomArmorEvent extends PlayerEvent implements Cancellable {
    private final ItemStack item;
    private  final Entity damager;
    private boolean cancelled;

    public CustomArmorEvent(Player who, Entity damager, ItemStack item) {
        super(who);
        this.damager = damager;
        this.item = item;
    }

    public ItemStack getItem() {
        return item;
    }

    public Entity getDamager() {
        return damager;
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
