package net.vadamdev.customcontent.internal;

import net.vadamdev.customcontent.annotations.TickableInfo;
import net.vadamdev.customcontent.api.common.tickable.AbstractTickableHandler;
import net.vadamdev.customcontent.api.common.tickable.ITickable;
import net.vadamdev.customcontent.lib.tickablehandler.BukkitTickableHandler;

import java.lang.annotation.Annotation;
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

        for(Annotation annotation : tickable.getClass().getAnnotations()) {
            if(annotation instanceof TickableInfo) {
                final TickableInfo tickableInfo = (TickableInfo) annotation;
                interval = tickableInfo.interval();
                async = tickableInfo.async();
                id = tickableInfo.handlerId();

                break;
            }
        }

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
