package net.vadamdev.customcontent.api.common.tickable;

import java.util.UUID;

/**
 * Represents a tickable handler that can be used with the {@link net.vadamdev.customcontent.annotations.TickableInfo TickableInfo} annotation
 * See {@link net.vadamdev.customcontent.lib.tickablehandler.BukkitTickableHandler BukkitTickableHandler} for implementation example
 *
 * @author VadamDev
 * @since 20/12/2022
 */
public abstract class AbstractTickableHandler {
    private final String id;

    public AbstractTickableHandler(String id) {
        this.id = id;
    }

    public abstract UUID handle(ITickable tickable, int interval, boolean async);
    public abstract void cancel(UUID uuid);
    public abstract boolean contains(UUID uuid);

    public String getId() {
        return id;
    }
}
