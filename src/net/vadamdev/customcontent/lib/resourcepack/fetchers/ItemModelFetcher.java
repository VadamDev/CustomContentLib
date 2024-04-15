package net.vadamdev.customcontent.lib.resourcepack.fetchers;

import net.vadamdev.customcontent.api.common.IRegistrable;
import net.vadamdev.customcontent.api.items.CustomItem;
import net.vadamdev.customcontent.api.items.EmptyItem;
import net.vadamdev.customcontent.api.items.armor.CustomArmorPart;
import net.vadamdev.customcontent.api.resourcepack.IModelFetcher;
import net.vadamdev.customcontent.api.resourcepack.Model;
import net.vadamdev.customcontent.lib.resourcepack.models.ArmorModel;
import net.vadamdev.customcontent.lib.resourcepack.models.ItemModel;
import org.apache.commons.io.FilenameUtils;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author VadamDev
 * @since 23/02/2024
 */
public class ItemModelFetcher implements IModelFetcher {
    private final File itemsDir;

    public ItemModelFetcher(File itemsDir) {
        this.itemsDir = itemsDir;
    }

    @Nonnull
    @Override
    public Collection<Model> fetch(Collection<IRegistrable> registrables) {
        if(!itemsDir.exists())
            return Collections.emptyList();

        final List<File> content = IModelFetcher.getDirectoryContent(itemsDir, file -> {
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
                .filter(registrable -> contentNames.contains(registrable.getRegistryName()))
                .flatMap(registrable -> {
                    if(registrable instanceof CustomItem || registrable instanceof EmptyItem)
                        return Stream.of(collectItemModel(registrable, content));
                    else if(registrable instanceof CustomArmorPart)
                        return collectArmorModel((CustomArmorPart) registrable, content).stream();

                    return Stream.empty();
                }).collect(Collectors.toList());
    }

    private Model collectItemModel(IRegistrable registrable, List<File> content) {
        final String registryName = registrable.getRegistryName();

        File texture = null, textureMeta = null, model = null, mcPatcher = null;
        for(File file : content) {
            final String fileName = file.getName();
            if(!FilenameUtils.removeExtension(fileName).equals(registryName))
                continue;

            switch(FilenameUtils.getExtension(fileName)) {
                case "png":
                    texture = file;
                    break;
                case "mcmeta":
                    textureMeta = file;
                    break;
                case "json":
                    model = file;
                    break;
                case "properties":
                    mcPatcher = file;
                    break;
                default:
                    break;
            }
        }

        return new ItemModel(registrable, texture, textureMeta, model, mcPatcher);
    }

    private List<Model> collectArmorModel(CustomArmorPart armorPart, List<File> content) {
        final String registryName = armorPart.getRegistryName();

        File layer1 = null, layer2 = null, mcPatcher = null;
        for(File file : content) {
            final String fileName = FilenameUtils.removeExtension(file.getName());

            switch(FilenameUtils.getExtension(file.getName())) {
                case "png":
                    final String armorMaterial = armorPart.getArmorMaterial();

                    if(fileName.equals(armorMaterial + "_layer_1"))
                        layer1 = file;
                    else if(fileName.equals(armorMaterial + "_layer_2"))
                        layer2 = file;

                    break;
                case "properties":
                    if(fileName.equals(registryName + "_layer"))
                        mcPatcher = file;

                    break;
                default:
                    break;
            }
        }

        return Arrays.asList(new ArmorModel(armorPart, layer1, layer2, mcPatcher), collectItemModel(armorPart, content));
    }
}
