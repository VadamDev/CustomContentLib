package net.vadamdev.customcontent.api.blocks;

import net.vadamdev.customcontent.lib.blocks.BlockFlags;

/**
 * A {@link CustomBlock} implementing this interface will be able to have custom abilities (like being unbreakable).
 *
 * @author VadamDev
 * @since 10/12/2022
 */
public interface IBlockFlagHolder {
    /**
     * Return an array of {@link BlockFlags} that will be applied to the {@link CustomBlock}
     *
     * @return An array of {@link BlockFlags}
     */
    BlockFlags[] getFlags();
}
