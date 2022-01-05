package net.vadamdev.customcontent.lib.events;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class MaterialUseEvent extends PlayerEvent implements Cancellable {
    private ItemAction action;
    private Material material;
    private Block clickedBlock;
    private boolean cancelled;

    public MaterialUseEvent(Player who, ItemAction action, Material material, Block clickedBlock) {
        super(who);

        this.player = who;
        this.action = action;
        this.material = material;
        this.clickedBlock = clickedBlock;
    }

    public ItemAction getAction() {
        return action;
    }

    public Material getMaterial() {
        return material;
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
