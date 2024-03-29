package net.vadamdev.customcontent.internal.utils;

import net.vadamdev.viapi.tools.builders.ItemBuilder;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

/**
 * @author VadamDev
 * @since 22/12/2021
 */
public final class CustomContentSerializer {
    public static void serializeItemStack(ItemStack itemStack, String registryName, FileUtils configFile) {
        if(!itemStack.hasItemMeta())
            return;

        final FileConfiguration config = configFile.getConfig();

        config.set(registryName + ".name",  itemStack.getItemMeta().getDisplayName());
        config.set(registryName + ".lore", itemStack.getItemMeta().getLore());

        configFile.save(config);
    }

    public static ItemStack unserializeItemStack(ItemStack defaultItemStack, String registryName, FileConfiguration config) {
        final ConfigurationSection section = config.getConfigurationSection(registryName);
        if(section == null)
            return defaultItemStack;

        final String name = section.getString("name");

        return ItemBuilder.item(defaultItemStack)
                .setName(name != null ? name : "")
                .setLore(section.getStringList("lore")).build();
    }
}
