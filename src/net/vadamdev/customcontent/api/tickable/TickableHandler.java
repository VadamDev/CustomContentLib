package net.vadamdev.customcontent.api.tickable;

import java.util.UUID;

/**
 * @author VadamDev
 * @since 20/12/2022
 */
public abstract class TickableHandler {
    private final String id;

    public TickableHandler(String id) {
        this.id = id;
    }

    public abstract UUID handle(ITickable tickable);
    public abstract void cancel(UUID uuid);

    public String getId() {
        return id;
    }
}
