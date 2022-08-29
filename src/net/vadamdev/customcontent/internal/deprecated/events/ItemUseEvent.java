package net.vadamdev.customcontent.internal.deprecated.events;

import net.vadamdev.customcontent.lib.ItemAction;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author VadamDev
 */
@Deprecated
public class ItemUseEvent extends PlayerEvent implements Cancellable {
    private final ItemAction action;
    private final ItemStack item;
    private final Block clickedBlock;
    private final BlockFace blockFace;
    private boolean cancelled;

    public ItemUseEvent(Player who, ItemAction action, ItemStack item, Block clickedBlock, BlockFace clickedFace) {
        super(who);

        this.action = action;
        this.item = item;
        this.clickedBlock = clickedBlock;
        this.blockFace = clickedFace;
    }

    @Deprecated
    public ItemAction getAction() {
        return action;
    }

    @Deprecated
    public ItemStack getItem() {
        return item;
    }

    @Deprecated
    public Block getClickedBlock() {
        return clickedBlock;
    }

    @Deprecated
    public BlockFace getBlockFace() {
        return blockFace;
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
