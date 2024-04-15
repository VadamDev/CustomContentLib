package net.vadamdev.customcontent.internal.impl;

import net.vadamdev.customcontent.api.ContentRegistry;
import net.vadamdev.customcontent.api.CustomContentAPI;
import net.vadamdev.customcontent.api.ModelFactory;
import net.vadamdev.customcontent.api.RecipeRegistry;
import net.vadamdev.customcontent.api.blocks.CustomBlock;
import net.vadamdev.customcontent.api.blocks.CustomTileEntity;
import net.vadamdev.customcontent.api.blocks.texture.WorldTexture;
import net.vadamdev.customcontent.internal.handlers.blocks.BlocksHandler;
import net.vadamdev.customcontent.internal.handlers.blocks.textures.CustomTextureHandler;
import net.vadamdev.customcontent.internal.handlers.blocks.tileentity.TileEntityHandler;
import net.vadamdev.customcontent.internal.registry.CommonRegistry;
import net.vadamdev.customcontent.lib.BlockPos;
import net.vadamdev.customcontent.lib.utils.NBTHelper;
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
public class CustomContentAPIImpl implements CustomContentAPI {
    private final CommonRegistry commonRegistry;

    private final ContentRegistryImpl contentRegistry;
    private final RecipeRegistryImpl recipeRegistry;

    private final BlocksHandler blocksHandler;
    private final CustomTextureHandler textureHandler;
    private final TileEntityHandler tileEntityHandler;

    private final ModelFactoryImpl modelFactory;

    public CustomContentAPIImpl(ContentRegistryImpl contentRegistry, RecipeRegistryImpl recipeRegistry, BlocksHandler blocksHandler, CustomTextureHandler textureHandler, TileEntityHandler tileEntityHandler, ModelFactoryImpl modelFactory) {
        this.commonRegistry = contentRegistry.getCommonRegistry();

        this.contentRegistry = contentRegistry;
        this.recipeRegistry = recipeRegistry;

        this.blocksHandler = blocksHandler;
        this.textureHandler = textureHandler;
        this.tileEntityHandler = tileEntityHandler;

        this.modelFactory = modelFactory;
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
    public ModelFactory getModelFactory() {
        return modelFactory;
    }

    @Override
    public boolean placeCustomBlock(BlockPos blockPos, CustomBlock customBlock, boolean checkValidity, @Nullable Player player) {
        return blocksHandler.placeCustomBlock(blockPos, customBlock, checkValidity, player, null);
    }

    @Override
    public boolean breakCustomBlock(BlockPos blockPos, boolean checkValidity, boolean drop, @Nullable Entity entity) {
        return blocksHandler.breakCustomBlock(blockPos, checkValidity, drop, entity);
    }

    @Override
    public Optional<CustomTileEntity> getTileEntityAt(BlockPos blockPos) {
        return tileEntityHandler.getTileEntityAt(blockPos);
    }

    @Override
    public <T extends CustomTileEntity> Optional<T> findTileEntity(BlockPos blockPos, Class<T> clazz) {
        return tileEntityHandler.findTileEntity(blockPos, clazz);
    }

    @Override
    public Optional<WorldTexture> getWorldTexture(BlockPos blockPos) {
        return Optional.ofNullable(textureHandler.getCustomTexture(blockPos));
    }

    @Override
    public void applyTextureChanges(BlockPos blockPos, boolean texture, boolean direction) {
        textureHandler.applyTextureChanges(blockPos, texture, direction);
    }

    @Override
    public ItemStack getCustomItemAsItemStack(String registryName) {
        return commonRegistry.getCustomItemAsItemStack(registryName);
    }

    @Override
    public boolean isCustomItem(ItemStack itemStack, String registryName) {
        return commonRegistry.isRegistered(registryName) && NBTHelper.getStringInNBTTag(itemStack, "RegistryName").equals(registryName);
    }

    @Override
    public Map<String, ItemStack> getCustomItemstacks() {
        return contentRegistry.getCommonRegistry().getCustomItemstacks();
    }
}
