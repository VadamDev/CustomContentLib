package net.vadamdev.customcontent.internal.handlers;

import net.vadamdev.customcontent.api.ITickable;
import net.vadamdev.viaapi.VIAPI;

/**
 * @author VadamDev
 * @since 28/03/2022
 */
public final class ITickableHandler {
    public static void registerITickableComponent(ITickable iTickable) {
        if(!iTickable.isTickAsync())
            VIAPI.getScheduler().runTaskTimer(VIAPI.get(), r -> iTickable.tick(), 0, iTickable.getInterval());
        else
            VIAPI.getScheduler().runTaskTimerAsynchronously(VIAPI.get(), r -> iTickable.tick(), 0, iTickable.getInterval());
    }
}
