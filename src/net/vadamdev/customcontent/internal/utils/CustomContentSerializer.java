package net.vadamdev.customcontent.internal.utils;

import net.vadamdev.viaapi.tools.builders.ItemBuilder;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

/**
 * @author VadamDev
 * @since 22/12/2021
 */
public final class CustomContentSerializer {
    public static void serializeItemStack(ItemStack itemStack, String registryName, FileUtils configFile) {
        FileConfiguration config = configFile.getConfig();
        ConfigurationSection section = config.createSection(registryName);

        section.set("name",  itemStack.getItemMeta().getDisplayName());
        section.set("lore", itemStack.getItemMeta().getLore());

        configFile.save(config);
    }

    public static ItemStack unserializeItemStack(ItemStack defaultItemStack, String registryName, FileConfiguration config) {
        ConfigurationSection section = config.getConfigurationSection(registryName);
        return new ItemBuilder(defaultItemStack).setName(section.getString("name")).setLore(section.getStringList("lore")).toItemStack();
    }
}
