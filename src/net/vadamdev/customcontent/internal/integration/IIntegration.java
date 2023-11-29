package net.vadamdev.customcontent.internal.integration;

import net.vadamdev.customcontent.internal.CustomContentPlugin;

/**
 * @author VadamDev
 * @since 28/11/2023
 */
public interface IIntegration {
    void load(CustomContentPlugin plugin);

    String getRequiredPlugin();
    String getConfigEntry();
}
