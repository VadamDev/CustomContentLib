package net.vadamdev.customcontent;

import net.vadamdev.customcontent.craftings.CraftListener;
import net.vadamdev.customcontent.internal.GeneralRegistry;
import net.vadamdev.customcontent.internal.deprecated.items.ItemsListener;
import net.vadamdev.customcontent.internal.handlers.ArmorsHandler;
import net.vadamdev.customcontent.internal.handlers.ItemsHandler;
import net.vadamdev.customcontent.internal.utils.FileUtils;
import net.vadamdev.viaapi.VIPlugin;
import net.vadamdev.viaapi.startup.APIVersion;

import java.util.Arrays;

/**
 * CustomContentLib is an VIAPI extension for create new items in vanilla minecraft.
 * @author VadamDev
 */
public class CustomContentLib extends VIPlugin {
    public static CustomContentLib instance;

    private GeneralRegistry generalRegistry;

    private ItemsHandler itemsHandler;
    private ArmorsHandler armorsHandler;

    @Override
    public void onEnable() {
        super.onEnable();

        instance = this;

        for(FileUtils value : FileUtils.values())
            saveResource(value.getFilename());

        generalRegistry = new GeneralRegistry();

        itemsHandler = new ItemsHandler();
        armorsHandler = new ArmorsHandler();

        registerListeners();
        registerCommands();
    }

    private void registerListeners(){
        Arrays.asList(
                itemsHandler,
                armorsHandler,

                new CraftListener(),
                new ItemsListener()
        ).forEach(e -> this.getServer().getPluginManager().registerEvents(e, this));
    }

    private void registerCommands() {
        Arrays.asList(
                new CustomContentCommand()
        ).forEach(this::registerCommand);
    }

    public GeneralRegistry getGeneralRegistry() {
        return generalRegistry;
    }

    @Override
    public APIVersion getAPIVersion() {
        return APIVersion.V2_4_17;
    }
}
