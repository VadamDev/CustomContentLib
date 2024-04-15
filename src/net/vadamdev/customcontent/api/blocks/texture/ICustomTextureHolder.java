package net.vadamdev.customcontent.api.blocks.texture;

import net.vadamdev.customcontent.api.blocks.CustomBlock;
import net.vadamdev.customcontent.lib.BlockPos;
import net.vadamdev.customcontent.lib.blocks.BlockDirection;
import net.vadamdev.customcontent.lib.blocks.DefaultWorldTexture;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A {@link CustomBlock} implementing this interface will be able to have a custom texture
 *
 * @author VadamDev
 * @since 04/03/2024
 */
public interface ICustomTextureHolder {
    /**
     * Return the default texture accessor. If null, the block will be transparent and the texture can be set with an API call.
     *
     * @see IBlockTextureAccessor
     *
     * @return The texture accessor or null
     */
    @Nullable
    IBlockTextureAccessor getDefaultTexture();

    /**
     * Determines if the texture is a full or a transparent block
     *
     * @return True if the custom texture is a full block
     */
    default boolean isOccluding() {
        return true;
    }

    /**
     * Return the texture rotation when placed by a player
     *
     * @param blockPos Location of the placed {@link CustomBlock}
     * @param player Player who placed the {@link CustomBlock}
     * @return Rotation of the placed {@link CustomBlock}
     */
    @Nullable
    default BlockDirection getBlockDirection(BlockPos blockPos, Player player) {
        return BlockDirection.SOUTH;
    }

    /**
     * Generate a new {@link WorldTexture} to the specified {@link BlockPos}
     * <br>Usually used when you don't want to use the {@link DefaultWorldTexture} implementation
     *
     * @see WorldTexture
     *
     * @param blockPos Location of the texture
     * @param direction Rotation of the texture
     * @param accessor
     * @return A new {@link WorldTexture}
     */
    @Nonnull
    default AbstractWorldTexture createWorldTexture(BlockPos blockPos, BlockDirection direction, IBlockTextureAccessor accessor) {
        return new DefaultWorldTexture(blockPos, direction, accessor, isOccluding());
    }
}
