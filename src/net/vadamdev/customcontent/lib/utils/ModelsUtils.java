package net.vadamdev.customcontent.lib.utils;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;

/**
 * @author VadamDev
 * @since 15/04/2024
 */
public final class ModelsUtils {
    private static final JSONObject customBlockModel, customItemBlockModel, customItemBlockModel2D;

    static {
        JSONObject a = null, b = null, c = null;

        try {
            a = JSONUtils.readFile(ModelsUtils.class.getResourceAsStream("/builtin_models/custom_block.json"), JSONObject.class);
            b = JSONUtils.readFile(ModelsUtils.class.getResourceAsStream("/builtin_models/custom_block_item.json"), JSONObject.class);
            c = JSONUtils.readFile(ModelsUtils.class.getResourceAsStream("/builtin_models/custom_block_item_2d.json"), JSONObject.class);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        customBlockModel = a;
        customItemBlockModel = b;
        customItemBlockModel2D = c;
    }

    private ModelsUtils() {}

    public static File generateBlockModel(String registryName, File file) throws IOException {
        if(customBlockModel == null)
            throw new IOException(""); //TODO: fill

        final JSONObject json = (JSONObject) customBlockModel.clone();
        ((JSONObject) json.get("textures")).replace("0", "./" + registryName);

        initializeFile(file);
        JSONUtils.saveJSONAwareToFile(json, file);

        return file;
    }

    public static File generateItemBlockModel(String registryName, File file) throws IOException {
        if(customItemBlockModel == null)
            throw new IOException(""); //TODO: fill

        final JSONObject json = (JSONObject) customItemBlockModel.clone();
        json.replace("parent", "./" + registryName);

        initializeFile(file);
        JSONUtils.saveJSONAwareToFile(json, file);

        return file;
    }

    public static File generate2DItemBlockModel(String registryName, File file) throws IOException {
        if(customItemBlockModel2D == null)
            throw new IOException(""); //TODO: fill

        final JSONObject json = (JSONObject) customItemBlockModel2D.clone();
        ((JSONObject) json.get("textures")).replace("layer0", "./" + registryName);

        initializeFile(file);
        JSONUtils.saveJSONAwareToFile(json, file);

        return file;
    }

    private static void initializeFile(File file) throws IOException {
        final File parent = file.getParentFile();
        if(!parent.exists())
            parent.mkdirs();

        if(!file.exists())
            file.createNewFile();
    }
}
