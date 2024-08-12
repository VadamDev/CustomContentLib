package net.vadamdev.customcontent.api.blocks.container;

import net.vadamdev.customcontent.annotations.Experimental;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

/**
 * A {@link net.vadamdev.customcontent.api.blocks.CustomTileEntity CustomTileEntity} implementing this interface will be able to automatically receive items from connected hoppers
 *
 * @author VadamDev
 * @since 13/05/2024
 */
@Experimental
public interface WorldyContainer extends Container {
    default boolean canPlaceItemTroughFace(int slot, ItemStack itemStack, BlockFace face) {
        return !face.equals(BlockFace.DOWN);
    }

    default boolean canTakeItemTroughFace(int slot, ItemStack itemStack, BlockFace face) {
        return face.equals(BlockFace.DOWN);
    }

    default int[] getSlotsForFace(BlockFace face) {
        return new int[0];
    }

    default boolean isHopperCompatible() {
        return true;
    }
}
