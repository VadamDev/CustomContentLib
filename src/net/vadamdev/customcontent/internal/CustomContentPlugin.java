package net.vadamdev.customcontent.internal;

import net.vadamdev.customcontent.api.CustomContentAPI;
import net.vadamdev.customcontent.internal.handlers.CraftHandler;
import net.vadamdev.customcontent.internal.handlers.blocks.BlocksHandler;
import net.vadamdev.customcontent.internal.handlers.blocks.TileEntityHandler;
import net.vadamdev.customcontent.internal.handlers.blocks.textures.CustomTextureHandler;
import net.vadamdev.customcontent.internal.handlers.items.ArmorsHandler;
import net.vadamdev.customcontent.internal.handlers.items.ItemsHandler;
import net.vadamdev.customcontent.internal.impl.ContentRegistryImpl;
import net.vadamdev.customcontent.internal.impl.CustomContentAPIImpl;
import net.vadamdev.customcontent.internal.impl.RecipeRegistryImpl;
import net.vadamdev.customcontent.internal.integration.Integrations;
import net.vadamdev.customcontent.internal.registry.BlocksRegistry;
import net.vadamdev.customcontent.internal.registry.CommonRegistry;
import net.vadamdev.customcontent.internal.registry.EntitiesRegistry;
import net.vadamdev.customcontent.internal.registry.ItemsRegistry;
import net.vadamdev.customcontent.internal.utils.FileUtils;
import net.vadamdev.viapi.APIVersion;
import net.vadamdev.viapi.VIPlugin;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 * @author VadamDev
 */
public class CustomContentPlugin extends VIPlugin {
    public static CustomContentPlugin instance;

    private CommonRegistry commonRegistry;
    private ItemsRegistry itemsRegistry;
    private BlocksRegistry blocksRegistry;
    private EntitiesRegistry entitiesRegistry;

    private ITickableManager tickableManager;
    private CustomTextureHandler textureHandler;

    private TileEntityHandler tileEntityHandler;

    private ItemsHandler itemsHandler;
    private ArmorsHandler armorsHandler;
    private BlocksHandler blocksHandler;

    private RecipeRegistryImpl recipeRegistry;
    private CustomContentAPIImpl customContentAPI;

    @Override
    public void onEnable() {
        super.onEnable();

        instance = this;

        final Logger logger = getLogger();
        logger.info("Starting CustomContentLib...");

        final long before = System.currentTimeMillis();

        for(FileUtils value : FileUtils.values())
            saveResource(value.getFilename());

        final FileConfiguration config = FileUtils.CONFIG.getConfig();

        commonRegistry = new CommonRegistry();
        itemsRegistry = new ItemsRegistry(commonRegistry);
        blocksRegistry = new BlocksRegistry(commonRegistry);

        logger.info("-> Registries loaded");

        tileEntityHandler = new TileEntityHandler();
        entitiesRegistry = new EntitiesRegistry();

        tickableManager = new ITickableManager();
        textureHandler = new CustomTextureHandler(config.getConfigurationSection("customTextures"));

        logger.info("-> Managers loaded");


        itemsHandler = new ItemsHandler(commonRegistry);
        armorsHandler = new ArmorsHandler(commonRegistry);
        blocksHandler = new BlocksHandler(blocksRegistry, tileEntityHandler, textureHandler);

        logger.info("-> Handlers loaded");

        //API Initialization
        final ContentRegistryImpl contentRegistry = new ContentRegistryImpl(commonRegistry, itemsRegistry, blocksRegistry, entitiesRegistry);
        recipeRegistry = new RecipeRegistryImpl();
        customContentAPI = new CustomContentAPIImpl(contentRegistry, recipeRegistry, blocksHandler, textureHandler, tileEntityHandler);

        itemsHandler.customContentAPI = customContentAPI;
        armorsHandler.customContentAPI = customContentAPI;
        blocksHandler.customContentAPI = customContentAPI;

        CustomContentAPI.Provider.set(customContentAPI);
        logger.info("-> API Loaded");

        registerListeners(config.getConfigurationSection("features"));
        registerCommand(new CustomContentCommand());

        Integrations.loadAll(this, config.getConfigurationSection("integrations"));

        logger.info("Initialization complete ! (Took: " + (System.currentTimeMillis() - before) + " ms)");

        Bukkit.getScheduler().runTaskLaterAsynchronously(this, () -> {
            logger.info("Loading post world features...");

            entitiesRegistry.complete(logger);
            recipeRegistry.complete(getServer());

            blocksHandler.loadAll(blocksRegistry, logger);

            logger.info("Post world features loading completed");
        }, config.getInt("general.postWorldLoadTime") * 20L);
    }

    @Override
    public void onDisable() {
        tileEntityHandler.saveAll();
    }

    private void registerListeners(ConfigurationSection features) {
        final Set<Listener> listeners = new HashSet<>();

        if(features.getBoolean("customItems"))
            listeners.add(itemsHandler);

        if(features.getBoolean("customArmors"))
            listeners.add(armorsHandler);

        if(features.getBoolean("customBlocks"))
            listeners.add(blocksHandler);

        if(features.getBoolean("customRecipes"))
            listeners.add(new CraftHandler());

        listeners.forEach(this::registerListener);
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

    public ITickableManager getTickableManager() {
        return tickableManager;
    }

    public RecipeRegistryImpl getRecipeRegistry() {
        return recipeRegistry;
    }

    public BlocksHandler getBlocksHandler() {
        return blocksHandler;
    }

    public CustomContentAPIImpl getCustomContentAPI() {
        return customContentAPI;
    }

    @Override
    public APIVersion getAPIVersion() {
        return APIVersion.V3_0_1;
    }
}
