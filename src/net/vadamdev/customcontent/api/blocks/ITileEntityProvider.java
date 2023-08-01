package net.vadamdev.customcontent.api.blocks;

import net.vadamdev.customcontent.lib.BlockPos;

import javax.annotation.Nonnull;

/**
 * A {@link CustomBlock} implementing this interface will own a {@link CustomTileEntity}.
 *
 * @author VadamDev
 * @since 16/09/2022
 */
public interface ITileEntityProvider {
    /**
     * Create a new {@link CustomTileEntity} at the given {@link BlockPos}
     *
     * @param blockPos {@link BlockPos}
     * @return A new {@link CustomTileEntity} at the given {@link BlockPos}
     */
    @Nonnull
    CustomTileEntity createTileEntity(BlockPos blockPos);
}
