package net.vadamdev.customcontent.api.resourcepack;

import net.vadamdev.customcontent.annotations.Experimental;
import net.vadamdev.customcontent.api.common.IRegistrable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Represents a model fetcher.
 * CustomContentLib uses them to get all models before preparing a resourcepack.
 *
 * @author VadamDev
 * @since 23/02/2024
 */
@Experimental
public interface IModelFetcher {
    /**
     * Called when CustomContentLib want to get all models before preparing a resourcepack
     *
     * @return a collection of models that CCL will add to the resourcepack
     */
    @Nonnull
    Collection<Model> fetch(Collection<IRegistrable> registrables);

    /**
     * Return the content of directory and its subdirectory
     *
     * @param directory The directory to work with, if the provided file is not a directory the method return an immutable empty list
     * @param filter Optional filter
     * @return A list of files contained in the directory or subdirectories
     */
    static List<File> getDirectoryContent(File directory, @Nullable FileFilter filter) {
        if(!directory.isDirectory())
            return Collections.emptyList();

        if(filter == null)
            filter = file -> true;

        final List<File> files = new ArrayList<>();

        for (File file : directory.listFiles()) {
            if(file.isDirectory()) {
                files.addAll(getDirectoryContent(file, filter));
            }else if(filter.accept(file))
                files.add(file);
        }

        return files;
    }
}
