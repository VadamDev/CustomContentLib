package net.vadamdev.customcontent.internal.integration;

import net.vadamdev.customcontent.internal.CustomContentPlugin;
import net.vadamdev.customcontent.internal.integration.worldedit.WorldEditIntegration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.PluginManager;

/**
 * @author VadamDev
 * @since 28/11/2023
 */
public enum Integrations {
    WORLD_EDIT(new WorldEditIntegration());

    private final IIntegration integration;

    Integrations(IIntegration integration) {
        this.integration = integration;
    }

    public static void loadAll(CustomContentPlugin plugin, ConfigurationSection section) {
        final PluginManager pluginManager = plugin.getServer().getPluginManager();

        int loaded = 0, skipped = 0;
        for(Integrations integrations : values()) {
            final IIntegration integration = integrations.integration;

            if(!section.getBoolean(integration.getConfigEntry()) || !pluginManager.isPluginEnabled(integration.getRequiredPlugin())) {
                skipped++;
                continue;
            }

            integration.load(plugin);
            loaded++;
        }

        plugin.getLogger().info("-> Loaded " + loaded + " integrations (" + loaded + "/" + (loaded + skipped) + ")");
    }
}
