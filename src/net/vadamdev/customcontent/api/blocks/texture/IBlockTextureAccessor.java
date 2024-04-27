package net.vadamdev.customcontent.api.blocks.texture;

import net.vadamdev.customcontent.lib.resourcepack.models.BlockModel;
import net.vadamdev.viapi.tools.builders.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;

/**
 * @author VadamDev
 * @since 04/03/2024
 */
@FunctionalInterface
public interface IBlockTextureAccessor {
    static IBlockTextureAccessor of(Material material, String textureName) {
        return () -> ItemBuilder.item(material).setName(textureName).build();
    }

    static IBlockTextureAccessor of(String textureName) {
        return of(BlockModel.ICON_MATERIAL, textureName);
    }

    @Nullable
    ItemStack getTexture();
}
