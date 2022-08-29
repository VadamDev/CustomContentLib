package net.vadamdev.customcontent.internal.deprecated.events;

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
@Deprecated
public class CustomArmorEvent extends PlayerEvent implements Cancellable {
    private final ItemStack item;
    private  final Entity damager;
    private boolean cancelled;

    @Deprecated
    public CustomArmorEvent(Player who, Entity damager, ItemStack item) {
        super(who);
        this.damager = damager;
        this.item = item;
    }

    @Deprecated
    public ItemStack getItem() {
        return item;
    }

    @Deprecated
    public Entity getDamager() {
        return damager;
    }

    @Override
    @Deprecated
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    @Deprecated
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }

    public static final HandlerList handlers = new HandlerList();
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
