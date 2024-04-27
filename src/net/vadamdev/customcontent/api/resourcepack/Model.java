package net.vadamdev.customcontent.api.resourcepack;

import net.vadamdev.customcontent.annotations.Experimental;

import java.io.File;
import java.io.IOException;

/**
 * Represents a resource pack model
 *
 * @author VadamDev
 * @since 11/02/2024
 */
@Experimental
public interface Model {
    String MCPATCHER_DIR = "assets/minecraft/mcpatcher/cit";
    String ITEMS_DIR = MCPATCHER_DIR + "/items";
    String BLOCKS_DIR = MCPATCHER_DIR + "/blocks";

    /**
     * Called when the model need to be created on the disk
     *
     * @param packRootDir The root directory of the resourcepack, where the pack.mcmeta is stored
     * @throws IOException Threw when any operation in the process fails
     */
    void make(File packRootDir) throws IOException;
}
