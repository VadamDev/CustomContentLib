package net.vadamdev.customcontent.internal;

import net.vadamdev.customcontent.annotations.TickableInfo;
import net.vadamdev.customcontent.api.common.tickable.AbstractTickableHandler;
import net.vadamdev.customcontent.api.common.tickable.IInventoryTickable;
import net.vadamdev.customcontent.api.common.tickable.ITickable;
import net.vadamdev.customcontent.api.items.armor.ArmorSet;
import net.vadamdev.customcontent.lib.tickablehandler.BukkitTickableHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author VadamDev
 * @since 28/03/2022
 */
public final class ITickableManager {
    private final Map<String, AbstractTickableHandler> handlers;

    public ITickableManager() {
        this.handlers = new HashMap<>();
        registerTickableHandler(new BukkitTickableHandler());
    }

    public UUID registerITickableComponent(ITickable tickable) {
        int interval = 1;
        boolean async = false;
        String id = "Bukkit";

        final TickableInfo info = tickable.getClass().getAnnotation(TickableInfo.class);
        if(info != null) {
            interval = info.interval();
            async = info.async();
            id = info.handlerId();
        }else if(tickable instanceof ArmorSet || tickable instanceof IInventoryTickable)
            interval = 20;

        if(!handlers.containsKey(id))
            throw new NullPointerException("Specified handler (" + id + ") does not exist !");

        return handlers.get(id).handle(tickable, interval, async);
    }

    public void cancel(UUID uuid) {
        handlers.values().stream()
                .filter(handler -> handler.contains(uuid))
                .forEach(handler -> handler.cancel(uuid));
    }

    public void registerTickableHandler(AbstractTickableHandler tickableHandler) {
        handlers.put(tickableHandler.getId(), tickableHandler);
    }
}
