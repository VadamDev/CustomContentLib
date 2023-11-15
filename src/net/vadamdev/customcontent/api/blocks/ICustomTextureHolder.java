package net.vadamdev.customcontent.api.blocks;

import net.vadamdev.customcontent.lib.BlockPos;
import net.vadamdev.viapi.tools.builders.ItemBuilder;
import net.vadamdev.viapi.tools.enums.EnumDirection;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author VadamDev
 * @since 05/07/2023
 */
public interface ICustomTextureHolder {
    String getDefaultTexture();

    /**
     * Return the block rotation when placed by a player
     *
     * @param blockPos Location of the placed {@link CustomBlock}
     * @param player Player who placed the {@link CustomBlock}
     * @return Rotation of the placed {@link CustomBlock}
     */
    default EnumDirection getBlockRotation(BlockPos blockPos, Player player) {
        return EnumDirection.SOUTH;
    }

    default ItemStack createTextureIcon(String textureName) {
        return ItemBuilder.item(Material.SLIME_BALL).setName(textureName).build();
    }
}
