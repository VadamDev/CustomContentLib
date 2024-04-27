package net.vadamdev.customcontent.api.blocks.texture;

import net.vadamdev.customcontent.lib.blocks.BlockDirection;
import org.bukkit.inventory.ItemStack;

/**
 * Represents a CCL custom texture placed in a Minecraft world
 * @see AbstractWorldTexture
 *
 * @author VadamDev
 * @since 03/03/2024
 */
public interface WorldTexture {
    default void update(IBlockTextureAccessor accessor, BlockDirection direction) {
        updateTexture(accessor);
        updateDirection(direction);
    }

    void updateTexture(IBlockTextureAccessor accessor);
    ItemStack getTextureIcon();

    void updateDirection(BlockDirection direction);
    BlockDirection getDirection();

    boolean isOccluding();
}
