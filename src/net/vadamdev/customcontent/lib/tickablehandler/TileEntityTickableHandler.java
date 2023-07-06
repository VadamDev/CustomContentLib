package net.vadamdev.customcontent.lib.tickablehandler;

import net.vadamdev.customcontent.CustomContentLib;
import net.vadamdev.customcontent.api.tickable.ITickable;
import net.vadamdev.customcontent.api.tickable.TickableHandler;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author VadamDev
 * @since 29/12/2022
 */
public class TileEntityTickableHandler extends TickableHandler {
    private final Map<UUID, BukkitRunnable> runnables;

    public TileEntityTickableHandler() {
        super("CCL-TileEntity");

        runnables = new HashMap<>();
    }

    @Override
    public UUID handle(ITickable tickable) {
        UUID uuid = UUID.randomUUID();

        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                tickable.tick();
            }
        };
        if(tickable.isTickAsync())
            runnable.runTaskTimerAsynchronously(CustomContentLib.instance, 0, tickable.getInterval());
        else
            runnable.runTaskTimer(CustomContentLib.instance, 0, tickable.getInterval());

        runnables.put(uuid, runnable);

        return uuid;
    }

    @Override
    public void cancel(UUID uuid) {
        if(runnables.containsKey(uuid))
            runnables.get(uuid).cancel();
    }
}
