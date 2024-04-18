package net.vadamdev.customcontent.api;

import net.vadamdev.customcontent.annotations.Experimental;
import net.vadamdev.customcontent.api.resourcepack.IModelFetcher;
import net.vadamdev.customcontent.api.resourcepack.Model;

/**
 * @author VadamDev
 * @since 13/02/2024
 */
@Experimental
public interface ModelFactory {
    void registerModel(Model model);
    void registerModelFetcher(IModelFetcher fetcher);

    void bakeModels();
}
