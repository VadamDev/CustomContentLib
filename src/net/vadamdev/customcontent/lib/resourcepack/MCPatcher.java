package net.vadamdev.customcontent.lib.resourcepack;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author VadamDev
 * @since 12/03/2024
 */
public final class MCPatcher {
    public static ItemPatcher item(Material material) {
        return new ItemPatcher(material);
    }

    public static ItemPatcher item(ItemStack itemStack) {
        return new ItemPatcher(itemStack.getType());
    }

    public static ArmorPatcher armor(Material material) {
        return new ArmorPatcher(material);
    }

    public static ArmorPatcher armor(ItemStack itemStack) {
        return new ArmorPatcher(itemStack.getType());
    }

    public static class AbstractPatcher<T> {
        protected final Map<String, String> data;

        protected AbstractPatcher(Material material) {
            this.data = new HashMap<>();
            put("items", String.valueOf(material.getId()));
        }

        public T nbt(String key, String value) {
            put("nbt." + key, value);
            return (T) this;
        }

        public T put(String key, String value) {
            data.put(key, value);
            return (T) this;
        }

        public File createFile(File file) throws IOException {
            final File parent = file.getParentFile();
            if(!parent.exists())
                parent.mkdirs();

            if(!file.exists())
                file.createNewFile();

            writeToFile(file);

            return file;
        }

        public void writeToFile(File file) throws IOException {
            if(file.isDirectory())
                throw new IOException("File cannot be a directory !");

            if(data.isEmpty())
                return;

            final FileWriter writer = new FileWriter(file);
            for(Map.Entry<String, String> entry : data.entrySet())
                writer.write(entry.getKey() + "=" + entry.getValue() + "\n");

            writer.close();
        }
    }

    public static final class ItemPatcher extends AbstractPatcher<ItemPatcher> {
        private ItemPatcher(Material material) {
            super(material);

            data.put("type", "item");
        }

        public ItemPatcher texture(String texture) {
            return put("texture", texture);
        }

        public ItemPatcher model(String model) {
            return put("model", model);
        }

        public ItemPatcher registryName(String registryName) {
            return nbt("RegistryName", registryName);
        }
    }

    public static final class ArmorPatcher extends AbstractPatcher<ArmorPatcher> {
        private ArmorPatcher(Material material) {
            super(material);

            data.put("type", "armor");
        }

        public ArmorPatcher texture(String vanillaTexture, String texture) {
            data.put("texture." + vanillaTexture, texture);
            return this;
        }

        public ArmorPatcher registryName(String registryName) {
            return nbt("RegistryName", registryName);
        }
    }
}
