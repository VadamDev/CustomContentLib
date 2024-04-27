package net.vadamdev.customcontent.lib.resourcepack.models;

import net.vadamdev.customcontent.api.blocks.CustomBlock;
import net.vadamdev.customcontent.api.blocks.texture.IBlockTextureAccessor;
import net.vadamdev.customcontent.api.resourcepack.RegistrableModel;
import net.vadamdev.customcontent.lib.resourcepack.MCPatcher;
import net.vadamdev.customcontent.lib.utils.ModelsUtils;
import net.vadamdev.customcontent.lib.utils.NBTHelper;
import net.vadamdev.viapi.tools.builders.ItemBuilder;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;

/**
 * @author VadamDev
 * @since 13/02/2024
 */
public class BlockModel implements RegistrableModel, IBlockTextureAccessor {
    public static final Material ICON_MATERIAL = Material.SLIME_BALL;
    public static final String TEXTURE_IDENTIFIER = "CCL-TextureIdentifier";

    private final CustomBlock customBlock;
    private final String registryName;

    private final File blockTexture;
    private File blockModel, blockMCPatcher;

    private final File itemBlockTexture;
    private File itemBlockModel, itemBlockMCPatcher;

    private final ItemStack icon;

    public BlockModel(CustomBlock customBlock, File blockTexture, File blockModel, File blockMCPatcher, File itemBlockTexture, File itemBlockModel, File itemBlockMCPatcher) {
        this.customBlock = customBlock;
        this.registryName = customBlock.getRegistryName();

        this.blockTexture = blockTexture;
        this.blockModel = blockModel;
        this.blockMCPatcher = blockMCPatcher;

        this.itemBlockTexture = itemBlockTexture;
        this.itemBlockModel = itemBlockModel;
        this.itemBlockMCPatcher = itemBlockMCPatcher;

        this.icon = NBTHelper.setStringInNBTTag(ItemBuilder.item(ICON_MATERIAL).build(), TEXTURE_IDENTIFIER, registryName);
    }

    @Override
    public void make(File packRootDir) throws IOException {
        if(blockTexture == null && blockModel == null)
            throw new IOException(""); //TODO: fill

        /*
           Block
         */

        if(blockTexture != null)
            FileUtils.copyFile(blockTexture, new File(packRootDir, createPath(blockTexture)));

        if(blockModel != null)
            FileUtils.copyFile(blockModel, new File(packRootDir, createPath(blockModel)));
        else
            blockModel = ModelsUtils.generateBlockModel(registryName, new File(packRootDir, createPath(blockTexture, registryName + ".json")));

        if(blockMCPatcher != null)
            FileUtils.copyFile(blockMCPatcher, new File(packRootDir, createPath(blockMCPatcher)));
        else
            blockMCPatcher = createBlockMCPatcherFile(new File(packRootDir, createPath(blockModel, registryName + ".properties")));

        /*
           Item Block
         */

        if(itemBlockTexture != null)
            FileUtils.copyFile(itemBlockTexture, new File(packRootDir, createPath(itemBlockTexture)));

        if(itemBlockModel != null)
            FileUtils.copyFile(itemBlockModel, new File(packRootDir, createPath(itemBlockModel)));
        else {
            if(itemBlockTexture == null)
                itemBlockModel = ModelsUtils.generateItemBlockModel(registryName, new File(packRootDir, createPath(blockModel, registryName + "_item.json")));
            else
                itemBlockModel = ModelsUtils.generate2DItemBlockModel(registryName, new File(packRootDir, createPath(blockModel, registryName + "_item.json")));
        }

        if(itemBlockMCPatcher != null)
            FileUtils.copyFile(itemBlockMCPatcher, new File(packRootDir, createPath(itemBlockMCPatcher)));
        else
            itemBlockMCPatcher = createItemBlockMCPatcherFile(new File(packRootDir, createPath(blockModel, registryName + "_item.properties")));
    }

    protected File createBlockMCPatcherFile(File file) throws IOException {
        return MCPatcher.item(icon)
                .model(FilenameUtils.removeExtension(blockModel.getName()))
                .nbt(TEXTURE_IDENTIFIER, registryName)
                .createFile(file);
    }

    protected File createItemBlockMCPatcherFile(File file) throws IOException {
        return MCPatcher.item(customBlock.getItemStack())
                .model(FilenameUtils.removeExtension(itemBlockModel.getName()))
                .registryName(registryName)
                .createFile(file);
    }

    protected String createPath(File file, String name) {
        return createPath(file).replace(file.getName(), "") + name;
    }

    protected String createPath(File file) {
        return BLOCKS_DIR + File.separator + file.getPath().replace("plugins\\CustomContentLib\\resources\\blocks", "");
    }

    @Nullable
    @Override
    public ItemStack getTexture() {
        return icon;
    }

    @Override
    public String getRegistryName() {
        return registryName;
    }
}
