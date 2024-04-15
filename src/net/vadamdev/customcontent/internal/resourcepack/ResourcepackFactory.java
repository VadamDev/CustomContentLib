package net.vadamdev.customcontent.internal.resourcepack;

import net.vadamdev.customcontent.api.common.IRegistrable;
import net.vadamdev.customcontent.api.resourcepack.IModelFetcher;
import net.vadamdev.customcontent.api.resourcepack.Model;
import net.vadamdev.customcontent.internal.CustomContentPlugin;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author VadamDev
 * @since 14/02/2024
 */
public class ResourcepackFactory {
    private final Logger logger;
    private final List<Model> models;

    public ResourcepackFactory(Logger logger, List<Model> models) {
        this.logger = logger;
        this.models = new ArrayList<>(models);
    }

    public void fetchModels(List<IModelFetcher> fetchers) {
        final List<IRegistrable> registrables = Collections.unmodifiableList(CustomContentPlugin.instance.getCommonRegistry().getCustomItems());

        final int originalSize = models.size();

        for(IModelFetcher fetcher : fetchers) {
            try {
                models.addAll(fetcher.fetch(registrables));
            }catch(Exception e) {
                logger.severe("An error occurred while fetching " + fetcher.getClass().getSimpleName() + ":");
                e.printStackTrace();
            }
        }

        if(originalSize != models.size())
            logger.info("-> Fetched " + (models.size() - originalSize) + " models (Total: " + models.size() + ")");
        else if(!models.isEmpty())
            logger.info("-> No models where fetched (Total: " + models.size() + ")");
        else
            logger.info("-> No models where found");
    }

    public void makeModels(File outDir) {
        logger.info("-> Baking models...");

        for (Model model : models) {
            try {
                model.make(outDir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void copyOverrides(File outDir, File overrideDir) {
        if(!overrideDir.exists() || !overrideDir.isDirectory() || overrideDir.listFiles().length == 0)
            return;

        try {
            FileUtils.copyDirectory(overrideDir, outDir);
            logger.info("-> Applied overrides");
        }catch (IOException e) {
            logger.severe("Failed to apply overrides:");
            e.printStackTrace();
        }
    }

    public void packResources(File outDir) {
        logger.info("-> Zipping resourcepack...");

        //TODO: ZIP outdir content
    }
}
