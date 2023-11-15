package net.vadamdev.customcontent.lib.tickablehandler;

import net.vadamdev.customcontent.api.common.tickable.AbstractTickableHandler;
import net.vadamdev.customcontent.api.common.tickable.ITickable;
import net.vadamdev.customcontent.internal.CustomContentPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author VadamDev
 * @since 29/12/2022
 */
public final class BukkitTickableHandler extends AbstractTickableHandler {
    private final Map<UUID, BukkitRunnable> runnables;

    public BukkitTickableHandler() {
        super("Bukkit");

        this.runnables = new HashMap<>();
    }

    @Override
    public UUID handle(ITickable tickable, int interval, boolean async) {
        final BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                tickable.tick();
            }
        };

        if(async)
            runnable.runTaskTimerAsynchronously(CustomContentPlugin.instance, 0, interval);
        else
            runnable.runTaskTimer(CustomContentPlugin.instance, 0, interval);

        final UUID uuid = UUID.randomUUID();
        runnables.put(uuid, runnable);

        return uuid;
    }

    @Override
    public void cancel(UUID uuid) {
        if(!runnables.containsKey(uuid))
            return;

        runnables.get(uuid).cancel();
        runnables.remove(uuid);
    }

    @Override
    public boolean contains(UUID uuid) {
        return runnables.containsKey(uuid);
    }
}
