package net.vadamdev.customcontent.lib.resourcepack.fetchers;

import net.vadamdev.customcontent.api.blocks.CustomBlock;
import net.vadamdev.customcontent.api.common.IRegistrable;
import net.vadamdev.customcontent.api.resourcepack.IModelFetcher;
import net.vadamdev.customcontent.api.resourcepack.Model;
import org.apache.commons.io.FilenameUtils;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author VadamDev
 * @since 27/02/2024
 */
public class BlockModelFetcher implements IModelFetcher {
    private final File blocksDir;

    public BlockModelFetcher(File blocksDir) {
        this.blocksDir = blocksDir;
    }

    @Nonnull
    @Override
    public Collection<Model> fetch(Collection<IRegistrable> registrables) {
        if(!blocksDir.exists())
            return Collections.emptyList();

        final List<File> content = IModelFetcher.getDirectoryContent(blocksDir, file -> {
            switch(FilenameUtils.getExtension(file.getName())) {
                case "png": case "mcmeta": case "json": case "properties":
                    return true;
                default:
                    return false;
            }
        });

        final Set<String> contentNames = content.stream()
                .map(file -> FilenameUtils.removeExtension(file.getName()))
                .collect(Collectors.toSet());

        return registrables.stream()
                .filter(CustomBlock.class::isInstance)
                .filter(registrable -> contentNames.contains(registrable.getRegistryName()))
                .map(CustomBlock.class::cast)
                .flatMap(customBlock -> collectBlockModel(customBlock, content).stream())
                .collect(Collectors.toList());
    }

    private List<Model> collectBlockModel(CustomBlock customBlock, List<File> content) {


        return Collections.emptyList();
    }
}
