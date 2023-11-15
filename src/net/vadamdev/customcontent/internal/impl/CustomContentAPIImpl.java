package net.vadamdev.customcontent.internal.impl;

import net.vadamdev.customcontent.api.ContentRegistry;
import net.vadamdev.customcontent.api.CustomContentAPI;
import net.vadamdev.customcontent.api.RecipeRegistry;
import net.vadamdev.customcontent.api.blocks.CustomBlock;
import net.vadamdev.customcontent.api.blocks.CustomTileEntity;
import net.vadamdev.customcontent.internal.handlers.blocks.BlocksHandler;
import net.vadamdev.customcontent.internal.handlers.blocks.TileEntityHandler;
import net.vadamdev.customcontent.internal.handlers.blocks.textures.CustomTextureHandler;
import net.vadamdev.customcontent.internal.registry.CommonRegistry;
import net.vadamdev.customcontent.lib.BlockPos;
import net.vadamdev.customcontent.lib.utils.NBTHelper;
import net.vadamdev.viapi.tools.enums.EnumDirection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;

/**
 * @author VadamDev
 * @since 08/07/2023
 */
public final class CustomContentAPIImpl implements CustomContentAPI {
    private final CommonRegistry commonRegistry;

    private final ContentRegistryImpl contentRegistry;
    private final RecipeRegistryImpl recipeRegistry;

    private final BlocksHandler blocksHandler;
    private final CustomTextureHandler textureHandler;
    private final TileEntityHandler tileEntityHandler;

    public CustomContentAPIImpl(ContentRegistryImpl contentRegistry, RecipeRegistryImpl recipeRegistry, BlocksHandler blocksHandler, CustomTextureHandler textureHandler, TileEntityHandler tileEntityHandler) {
        this.commonRegistry = contentRegistry.getCommonRegistry();

        this.contentRegistry = contentRegistry;
        this.recipeRegistry = recipeRegistry;

        this.blocksHandler = blocksHandler;
        this.textureHandler = textureHandler;
        this.tileEntityHandler = tileEntityHandler;
    }

    @Override
    public ContentRegistry getContentRegistry() {
        return contentRegistry;
    }

    @Override
    public RecipeRegistry getRecipeRegistry() {
        return recipeRegistry;
    }

    @Override
    public void placeCustomBlock(BlockPos blockPos, CustomBlock customBlock, boolean checkValidity, @Nullable Player player) {
        blocksHandler.placeCustomBlock(blockPos, customBlock, checkValidity, player, null);
    }

    @Override
    public void breakCustomBlock(BlockPos blockPos, boolean checkValidity, boolean drop, @Nullable Entity entity) {
        blocksHandler.breakCustomBlock(blockPos, checkValidity, drop, entity);
    }

    @Override
    public Optional<CustomTileEntity> getTileEntityAt(BlockPos blockPos) {
        return tileEntityHandler.getTileEntityAt(blockPos);
    }

    @Override
    public <T extends CustomTileEntity> Optional<T> findTileEntity(BlockPos blockPos, Class<T> clazz) {
        return tileEntityHandler.findTileEntity(blockPos, clazz);
    }

    @Nullable
    @Override
    public ItemStack getCustomTexture(BlockPos blockPos) {
        return textureHandler.getCustomTexture(blockPos);
    }

    @Override
    public void updateCustomTexture(BlockPos blockPos, ItemStack itemStack, @Nullable EnumDirection direction) {
        textureHandler.updateCustomTexture(blockPos, itemStack, direction);
    }

    @Override
    public ItemStack getCustomItemAsItemStack(String registryName) {
        return commonRegistry.getCustomItemAsItemStack(registryName);
    }

    @Override
    public boolean isCustomItem(ItemStack itemStack, String registryName) {
        String theoreticalRegistryName = NBTHelper.getStringInNBTTag(itemStack, "RegistryName");
        if(theoreticalRegistryName == null)
            return false;

        return commonRegistry.isRegistered(registryName) && theoreticalRegistryName.equals(registryName);
    }

    @Override
    public Map<String, ItemStack> getCustomItemstacks() {
        return contentRegistry.getCommonRegistry().getCustomItemstacks();
    }
}
