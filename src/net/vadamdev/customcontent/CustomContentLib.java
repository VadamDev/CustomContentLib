package net.vadamdev.customcontent;

import net.vadamdev.customcontent.craftings.CraftListener;
import net.vadamdev.customcontent.internal.BlocksRegistry;
import net.vadamdev.customcontent.internal.CommonRegistry;
import net.vadamdev.customcontent.internal.ItemsRegistry;
import net.vadamdev.customcontent.internal.handlers.ArmorsHandler;
import net.vadamdev.customcontent.internal.handlers.BlocksHandler;
import net.vadamdev.customcontent.internal.handlers.ItemsHandler;
import net.vadamdev.customcontent.internal.handlers.TileEntityHandler;
import net.vadamdev.customcontent.internal.utils.FileUtils;
import net.vadamdev.viaapi.VIAPI;
import net.vadamdev.viaapi.VIPlugin;
import net.vadamdev.viaapi.startup.APIVersion;

import java.util.Arrays;

/**
 * CustomContentLib is an VIAPI extension for create new items in vanilla minecraft.
 * @author VadamDev
 */
public class CustomContentLib extends VIPlugin {
    public static CustomContentLib instance;

    private CommonRegistry commonRegistry;
    private ItemsRegistry itemsRegistry;
    private BlocksRegistry blocksRegistry;

    private TileEntityHandler tileEntityHandler;

    private ItemsHandler itemsHandler;
    private ArmorsHandler armorsHandler;
    private BlocksHandler blocksHandler;

    @Override
    public void onEnable() {
        super.onEnable();

        instance = this;

        for(FileUtils value : FileUtils.values())
            saveResource(value.getFilename());

        commonRegistry = new CommonRegistry();
        itemsRegistry = new ItemsRegistry();

        tileEntityHandler = new TileEntityHandler();
        blocksRegistry = new BlocksRegistry();

        itemsHandler = new ItemsHandler();
        armorsHandler = new ArmorsHandler();
        blocksHandler = new BlocksHandler();

        registerListeners();
        registerCommands();

        VIAPI.getScheduler().runTaskLater(this, r -> tileEntityHandler.loadAll(blocksRegistry), 100);
    }

    @Override
    public void onDisable() {
        tileEntityHandler.saveAll();
    }

    private void registerListeners(){
        Arrays.asList(
                itemsHandler,
                armorsHandler,
                blocksHandler,

                new CraftListener()
        ).forEach(e -> this.getServer().getPluginManager().registerEvents(e, this));
    }

    private void registerCommands() {
        Arrays.asList(
                new CustomContentCommand()
        ).forEach(this::registerCommand);
    }

    public CommonRegistry getCommonRegistry() {
        return commonRegistry;
    }

    public ItemsRegistry getItemsRegistry() {
        return itemsRegistry;
    }

    public BlocksRegistry getBlocksRegistry() {
        return blocksRegistry;
    }

    public TileEntityHandler getTileEntityHandler() {
        return tileEntityHandler;
    }

    @Override
    public APIVersion getAPIVersion() {
        return APIVersion.V2_4_17;
    }
}
