package net.vadamdev.customcontent.internal.impl;

import net.vadamdev.customcontent.api.ModelFactory;
import net.vadamdev.customcontent.api.resourcepack.IModelFetcher;
import net.vadamdev.customcontent.api.resourcepack.Model;
import net.vadamdev.customcontent.internal.CustomContentPlugin;
import net.vadamdev.customcontent.internal.resourcepack.ResourcepackFactory;
import net.vadamdev.customcontent.lib.resourcepack.fetchers.BlockModelFetcher;
import net.vadamdev.customcontent.lib.resourcepack.fetchers.ItemModelFetcher;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author VadamDev
 * @since 13/02/2024
 */
public class ModelFactoryImpl implements ModelFactory {
    private final List<Model> models;
    private final List<IModelFetcher> fetchers;

    private final File outDir, blocksDir, itemsDir, overridesDir;

    public ModelFactoryImpl(File dataFolder) {
        this.models = new ArrayList<>();
        this.fetchers = new ArrayList<>();

        this.outDir = new File(dataFolder, "out");
        this.blocksDir = new File(dataFolder, "resources/blocks");
        this.itemsDir = new File(dataFolder, "resources/items");
        this.overridesDir = new File(dataFolder, "resources/overrides");

        initDirectories();

        //Add defaults fetchers
        fetchers.add(new ItemModelFetcher(itemsDir));
        fetchers.add(new BlockModelFetcher(blocksDir));
    }

    @Override
    public void registerModel(Model model) {
        models.add(model);
    }

    @Override
    public void registerModelFetcher(IModelFetcher fetcher) {
        //Add at start to force custom fetcher to be executed first todo: enhance this
        fetchers.add(0, fetcher);
    }

    @Override
    public void bakeModels() {
        final Logger logger = CustomContentPlugin.instance.getLogger();
        logger.info("Starting resourcepack creation...");

        final long before = System.currentTimeMillis();

        if((blocksDir == null || !blocksDir.exists()) && (itemsDir == null || !itemsDir.exists())) {
            logger.warning("Failed to create the resourcepack because resource folder doesn't exists");
            return;
        }

        final ResourcepackFactory factory = new ResourcepackFactory(logger, models);
        factory.fetchModels(fetchers);

        prepareOutDir();

        factory.makeModels(outDir);
        factory.copyOverrides(outDir, overridesDir);
        factory.packResources(outDir);

        logger.info("Resourcepack created ! (Took: " + (System.currentTimeMillis() - before) + "ms)");
    }

    private void prepareOutDir() {
        if(!outDir.exists())
            outDir.mkdir();
        else if(outDir.listFiles() != null) {
            try {
                FileUtils.cleanDirectory(outDir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void initDirectories() {
        prepareOutDir();

        if(!blocksDir.exists())
            blocksDir.mkdirs();

        if(!itemsDir.exists())
            itemsDir.mkdirs();

        if(!overridesDir.exists())
            overridesDir.mkdirs();
    }
}
