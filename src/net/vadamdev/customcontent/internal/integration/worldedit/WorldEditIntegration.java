package net.vadamdev.customcontent.internal.integration.worldedit;

import com.boydti.fawe.config.Settings;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.event.extent.EditSessionEvent;
import com.sk89q.worldedit.internal.registry.AbstractFactory;
import com.sk89q.worldedit.internal.registry.InputParser;
import com.sk89q.worldedit.util.eventbus.EventHandler;
import com.sk89q.worldedit.util.eventbus.Subscribe;
import net.vadamdev.customcontent.internal.CustomContentPlugin;
import net.vadamdev.customcontent.internal.handlers.blocks.BlocksHandler;
import net.vadamdev.customcontent.internal.integration.IIntegration;
import net.vadamdev.customcontent.internal.integration.worldedit.delegate.FAWECustomBlockDelegate;
import net.vadamdev.customcontent.internal.integration.worldedit.delegate.WECustomBlockDelegate;
import net.vadamdev.customcontent.internal.registry.BlocksRegistry;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author VadamDev
 * @since 28/11/2023
 */
public class WorldEditIntegration implements IIntegration {
    private BlocksRegistry blocksRegistry;
    private BlocksHandler blocksHandler;

    @Override
    public void load(CustomContentPlugin plugin) {
        blocksRegistry = plugin.getBlocksRegistry();
        blocksHandler = plugin.getBlocksHandler();

        final WorldEdit worldEdit = WorldEdit.getInstance();

        try {
            final Field parsersField = AbstractFactory.class.getDeclaredField("parsers");
            parsersField.setAccessible(true);
            ((List<InputParser<?>>) parsersField.get(worldEdit.getBlockFactory())).add(0, new CustomBlockInputParser(worldEdit, blocksRegistry));

            if(plugin.getServer().getPluginManager().isPluginEnabled("FastAsyncWorldEdit")) {
                Settings.IMP.EXTENT.ALLOWED_PLUGINS.add(FAWECustomBlockDelegate.class.getCanonicalName());
                worldEdit.getEventBus().register(new FAWEListener());

                plugin.getLogger().warning("-> FAWE integration is enabled, we DO NOT recommend using it. You can disable it in the config !");
            }else
                worldEdit.getEventBus().register(new WEListener());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getRequiredPlugin() {
        return "WorldEdit";
    }

    @Override
    public String getConfigEntry() {
        return "worldEdit";
    }

    private class WEListener {
        @Subscribe(priority = EventHandler.Priority.VERY_LATE)
        public void onEditSession(EditSessionEvent event) {
            if(!event.getStage().equals(EditSession.Stage.BEFORE_CHANGE))
                return;

            event.setExtent(new WECustomBlockDelegate(event, blocksRegistry, blocksHandler));
        }
    }

    private class FAWEListener {
        @Subscribe(priority = EventHandler.Priority.VERY_LATE)
        public void onEditSession(EditSessionEvent event) {
            if(!event.getStage().equals(EditSession.Stage.BEFORE_CHANGE))
                return;

            event.setExtent(new FAWECustomBlockDelegate(event, blocksRegistry, blocksHandler));
        }
    }
}
