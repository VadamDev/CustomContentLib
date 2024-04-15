package net.vadamdev.customcontent.lib.resourcepack.models;

import net.vadamdev.customcontent.api.items.armor.CustomArmorPart;
import net.vadamdev.customcontent.api.resourcepack.RegistrableModel;
import net.vadamdev.customcontent.lib.resourcepack.MCPatcher;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;

/**
 * @author VadamDev
 * @since 27/02/2024
 */
public class ArmorModel implements RegistrableModel {
    private final CustomArmorPart armorPart;
    private final String registryName;

    private final File layer1, layer2;
    private File mcPatcher;

    public ArmorModel(CustomArmorPart armorPart, File layer1, File layer2, File mcPatcher) {
        this.armorPart = armorPart;
        this.registryName = armorPart.getRegistryName();

        this.layer1 = layer1;
        this.layer2 = layer2;
        this.mcPatcher = mcPatcher;
    }

    @Override
    public void make(File packRootDir) throws IOException {
        if(layer1 == null && layer2 == null)
            throw new IOException("Textures cannot be null");

        //layer1
        if(layer1 != null)
            FileUtils.copyFile(layer1, new File(packRootDir, createPath(layer1)));

        //layer2
        if(layer2 != null)
            FileUtils.copyFile(layer2, new File(packRootDir, createPath(layer2)));

        //mcpatcher
        if(mcPatcher != null)
            FileUtils.copyFile(mcPatcher, new File(packRootDir, createPath(mcPatcher)));
        else {
            final File computedLayer = layer1 == null ? layer2 : layer1;

            mcPatcher = createMCPatcherFile(new File(packRootDir, createPath(computedLayer).replace(computedLayer.getName(), "") + registryName + "_layer.properties"));
        }
    }

    protected File createMCPatcherFile(File file) throws IOException {
        final String[] layers = chooseLayerAndTexture();

        return MCPatcher.armor(armorPart.getItemStack())
                .texture(layers[0], layers[1])
                .registryName(registryName)
                .createFile(file);
    }

    //layer 1 = helmet / chestplate
    //layer 2 = leggings / boots

    //0 = vanilla layer
    //1 = custom layer
    private String[] chooseLayerAndTexture() {
        final String typeName = armorPart.getArmorType().name();

        switch(armorPart.getArmorPart()) {
            case HELMET: case CHESTPLATE:
                return new String[] { typeName.toLowerCase() + "_layer_1", FilenameUtils.removeExtension(layer1.getName()) };
            case LEGGINGS: case BOOTS:
                return new String[] { typeName.toLowerCase() + "_layer_2", FilenameUtils.removeExtension(layer2.getName()) };
            default:
                return new String[0];
        }
    }

    protected String createPath(File file) {
        return ITEMS_DIR + File.separator + file.getPath().replace("plugins\\CustomContentLib\\resources\\items", "");
    }

    @Override
    public String getRegistryName() {
        return registryName;
    }
}
