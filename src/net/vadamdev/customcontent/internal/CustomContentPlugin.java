package net.vadamdev.customcontent.internal;

import net.vadamdev.customcontent.api.CustomContentAPI;
import net.vadamdev.customcontent.internal.handlers.CraftHandler;
import net.vadamdev.customcontent.internal.handlers.blocks.BlocksHandler;
import net.vadamdev.customcontent.internal.handlers.blocks.CustomTextureHandler;
import net.vadamdev.customcontent.internal.handlers.blocks.TileEntityHandler;
import net.vadamdev.customcontent.internal.handlers.items.ArmorsHandler;
import net.vadamdev.customcontent.internal.handlers.items.ItemsHandler;
import net.vadamdev.customcontent.internal.impl.ContentRegistryImpl;
import net.vadamdev.customcontent.internal.impl.CustomContentAPIImpl;
import net.vadamdev.customcontent.internal.impl.RecipeRegistryImpl;
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
    private CustomTextureHandler customTextureHandler;

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

        for(FileUtils value : FileUtils.values())
            saveResource(value.getFilename());

        commonRegistry = new CommonRegistry();
        itemsRegistry = new ItemsRegistry();
        tileEntityHandler = new TileEntityHandler();
        blocksRegistry = new BlocksRegistry();
        entitiesRegistry = new EntitiesRegistry();

        tickableManager = new ITickableManager();
        customTextureHandler = new CustomTextureHandler();

        //API Initialization
        final ContentRegistryImpl contentRegistry = new ContentRegistryImpl(commonRegistry, itemsRegistry, blocksRegistry, entitiesRegistry);
        recipeRegistry = new RecipeRegistryImpl();
        customContentAPI = new CustomContentAPIImpl(contentRegistry, recipeRegistry);

        itemsHandler = new ItemsHandler();
        armorsHandler = new ArmorsHandler();
        blocksHandler = new BlocksHandler();

        final FileConfiguration config = FileUtils.CONFIG.getConfig();
        final ConfigurationSection features = config.getConfigurationSection("features");

        registerListeners(features);
        registerCommand(new CustomContentCommand());

        CustomContentAPI.Provider.set(customContentAPI);

        Bukkit.getScheduler().runTaskLaterAsynchronously(this, () -> {
            entitiesRegistry.complete();
            recipeRegistry.complete(getServer());

            blocksHandler.loadAll(blocksRegistry);
        }, config.getInt("postWorldLoadTime") * 20L);
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

        if(features.getBoolean("customCraftings"))
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

    public CustomTextureHandler getCustomTextureHandler() {
        return customTextureHandler;
    }

    public TileEntityHandler getTileEntityHandler() {
        return tileEntityHandler;
    }

    public BlocksHandler getBlocksHandler() {
        return blocksHandler;
    }

    public RecipeRegistryImpl getRecipeRegistry() {
        return recipeRegistry;
    }

    public CustomContentAPIImpl getCustomContentAPI() {
        return customContentAPI;
    }

    @Override
    public APIVersion getAPIVersion() {
        return APIVersion.V3_0_0;
    }
}
