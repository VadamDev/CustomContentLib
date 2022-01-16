package net.vadamdev.customcontent.integration;

import net.vadamdev.customcontent.craftings.CraftListener;
import net.vadamdev.customcontent.integration.listeners.items.ItemsListener;
import net.vadamdev.customcontent.integration.test.TestArmorPart;
import net.vadamdev.customcontent.integration.test.TestFood;
import net.vadamdev.customcontent.integration.test.TestItem;
import net.vadamdev.customcontent.lib.ItemRegistry;
import net.vadamdev.customcontent.utils.FileUtils;
import net.vadamdev.viaapi.integration.VIAPIIntegration;

/**
 * CustomContentLib is an VIAPI extension for create new items in vanilla minecraft.
 * @author VadamDev
 */
public class CustomContentIntegration extends VIAPIIntegration {
    public static CustomContentIntegration instance;

    @Override
    public void onEnable() {
        instance = this;

        for(FileUtils value : FileUtils.values()) saveResource(getClass(), value.getFilename(), value.getFilename());

        registerListener(new ItemsListener());
        registerListener(new CraftListener());

        registerCommand(new CustomContentCommand());

        ItemRegistry.registerCustomItem(new TestItem());
        ItemRegistry.registerCustomFood(new TestFood());
        ItemRegistry.registerCustomArmorPart(new TestArmorPart());
    }

    @Override
    public String getName() {
        return "CustomContentLib";
    }

    @Override
    public String getAuthor() {
        return "VadamDev";
    }

    @Override
    public String getVersion() {
        return "0.1.1";
    }
}
