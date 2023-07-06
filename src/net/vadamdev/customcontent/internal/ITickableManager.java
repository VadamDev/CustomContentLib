package net.vadamdev.customcontent.internal;

import net.vadamdev.customcontent.api.tickable.ITickable;
import net.vadamdev.customcontent.api.tickable.TickableHandler;
import net.vadamdev.customcontent.lib.tickablehandler.BukkitTickableHandler;
import net.vadamdev.customcontent.lib.tickablehandler.TileEntityTickableHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author VadamDev
 * @since 28/03/2022
 */
public final class ITickableManager {
    private final Map<String, TickableHandler> handlers;

    public ITickableManager() {
        this.handlers = new HashMap<>();

        registerTickableHandler(new BukkitTickableHandler());
        registerTickableHandler(new TileEntityTickableHandler());
    }

    public UUID registerITickableComponent(ITickable tickable) {
        String id = tickable.getHandlerId();

        if(!handlers.containsKey(id))
            throw new NullPointerException("Specified handler (" + id + ") does not exist !");

        return handlers.get(id).handle(tickable);
    }

    public void registerTickableHandler(TickableHandler tickableHandler) {
        handlers.put(tickableHandler.getId(), tickableHandler);
    }
}
