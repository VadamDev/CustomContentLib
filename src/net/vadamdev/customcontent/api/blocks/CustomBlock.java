package net.vadamdev.customcontent.api.blocks;

import net.vadamdev.customcontent.api.blocks.serialization.IDataSerializer;
import net.vadamdev.customcontent.api.common.IRegistrable;
import net.vadamdev.customcontent.lib.BlockPos;
import net.vadamdev.customcontent.lib.utils.NBTHelper;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Represents a CCL's Block
 *
 * @author VadamDev
 * @since 01/09/2022
 */
public abstract class CustomBlock implements IRegistrable {
    protected ItemStack itemStack;

    public CustomBlock(ItemStack itemStack) {
        this.itemStack = NBTHelper.setStringInNBTTag(itemStack, "RegistryName", getRegistryName());
    }

    /**
     * Called when a {@link Player} right click on the {@link CustomBlock}
     *
     * @param block Block involved in this action
     * @param blockPos Position of the block involved in this action
     * @param player Player that interacted with the block
     * @return True if the {@link org.bukkit.event.player.PlayerInteractEvent PlayerInteractEvent} should be canceled
     */
    public boolean onInteract(Block block, BlockPos blockPos, Player player) {
        return false;
    }

    /**
     * Called when a {@link Player} left click on the {@link CustomBlock}
     *
     * @param block Block involved in this action
     * @param blockPos Position of the block involved in this action
     * @param player Player that interacted with the block
     * @return True if the {@link org.bukkit.event.player.PlayerInteractEvent PlayerInteractEvent} should be canceled
     */
    public boolean tryBreak(Block block, BlockPos blockPos, Player player) {
        return false;
    }

    /**
     * Called when the {@link CustomBlock} is placed
     *
     * @param block Block involved in this action
     * @param blockPos Position of the block involved in this action
     * @param player The player that placed the block, null if it wasn't a player
     */
    public void onPlace(Block block, BlockPos blockPos, @Nullable Player player) {}

    /**
     * Called when the block is broken
     *
     * @param blockPos Position of the block involved in this action
     * @param tileEntity Associated TileEntity, null if it doesn't exist
     * @param entity Entity that broken the block, null if it doesn't exist
     */
    public void onBreak(BlockPos blockPos, @Nullable CustomTileEntity tileEntity, @Nullable Entity entity) {}

    /**
     * Return true if the block can be placed
     *
     * @param blockPos Position of the block
     * @param player Player that placed the block, null if it wasn't a player
     * @return True if the block can be placed
     */
    public boolean canPlace(BlockPos blockPos, @Nullable Player player) {
        return true;
    }

    /**
     * Return true if the block can be broken
     *
     * @param blockPos Position of the block
     * @param entity Entity that broken the block, null if it wasn't an entity
     * @return True if the block can be broken
     */
    public boolean canBreak(BlockPos blockPos, @Nullable Entity entity) {
        return true;
    }

    /**
     * Return the {@link IDataSerializer} used to serialize block position and the {@link CustomTileEntity} data if exists
     * <br><br>
     * You NEED to store the DataSerializer in a (static?) field because this will be called for EACH serialization
     *
     * @see IDataSerializer
     * @return A new {@link IDataSerializer} instance
     */
    @Nonnull
    public abstract IDataSerializer getDataSerializer();

    @Nonnull
    public Material getBlockMaterial() {
        return itemStack.getType();
    }

    @Nonnull
    @Override
    public ItemStack getItemStack() {
        return itemStack;
    }
}
