package net.vadamdev.customcontent.api.blocks;

import net.vadamdev.customcontent.lib.BlockPos;

/**
 * @author VadamDev
 * @since 16/09/2022
 */
public interface ITileEntityProvider {
    CustomTileEntity createTileEntity(BlockPos blockPos);
}
