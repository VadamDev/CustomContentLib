package net.vadamdev.customcontent.lib.resourcepack.models;

import net.vadamdev.customcontent.api.common.IRegistrable;
import net.vadamdev.customcontent.api.resourcepack.RegistrableModel;
import net.vadamdev.customcontent.lib.resourcepack.MCPatcher;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * @author VadamDev
 * @since 13/02/2024
 */
public class ItemModel implements RegistrableModel {
    private final IRegistrable registrable;
    private final String registryName;

    private final File texture, textureMeta, model;
    private File mcPatcher;

    public ItemModel(IRegistrable registrable, File texture, File textureMeta, File model, File mcPatcher) {
        this.registrable = registrable;
        this.registryName = registrable.getRegistryName();

        this.texture = texture;
        this.textureMeta = textureMeta;
        this.model = model;
        this.mcPatcher = mcPatcher;
    }

    @Override
    public void make(File packRootDir) throws IOException {
        if(texture == null)
            throw new IOException("Texture file cannot be null !");

        //texture
        FileUtils.copyFile(texture, new File(packRootDir, createPath(texture)));

        //textureMeta
        if(textureMeta != null)
            FileUtils.copyFile(textureMeta, new File(packRootDir, createPath(textureMeta)));

        if(model != null)
            FileUtils.copyFile(model, new File(packRootDir, createPath(model)));

        //mcpatcher
        if(mcPatcher != null)
            FileUtils.copyFile(mcPatcher, new File(packRootDir, createPath(mcPatcher)));
        else
            mcPatcher = createMCPatcherFile(new File(packRootDir, createPath(texture, registryName + ".properties")));
    }

    protected File createMCPatcherFile(File file) throws IOException {
        return MCPatcher.item(registrable.getItemStack())
                .texture(texture.getName())
                .registryName(registryName)
                .createFile(file);
    }

    protected String createPath(File file, String name) {
        return createPath(file).replace(file.getName(), "") + name;
    }

    protected String createPath(File file) {
        return ITEMS_DIR + File.separator + file.getPath().replace("plugins\\CustomContentLib\\resources\\items", "");
    }

    @Override
    public String getRegistryName() {
        return registryName;
    }
}
