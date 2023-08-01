package net.vadamdev.customcontent.internal.handlers.blocks;

import net.vadamdev.customcontent.api.common.tickable.ITickable;
import net.vadamdev.customcontent.internal.CustomContentPlugin;
import net.vadamdev.customcontent.lib.BlockPos;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class TickableExecutor {
    private final Map<BlockPos, ITickable> tickableMap;
    private final BukkitRunnable updater;

    public TickableExecutor(int interval, boolean async) {
        this.tickableMap = new HashMap<>();

        if(async) {
            this.updater = createAsyncUpdater();
            this.updater.runTaskTimerAsynchronously(CustomContentPlugin.instance, 0, interval);
        }else {
            this.updater = createSyncUpdater();
            this.updater.runTaskTimer(CustomContentPlugin.instance, 0, interval);
        }
    }

    public void sumbit(BlockPos blockPos, ITickable tickable) {
        tickableMap.put(blockPos, tickable);
    }

    public boolean remove(BlockPos blockPos) {
        tickableMap.remove(blockPos);
        return tickableMap.isEmpty();
    }

    public boolean contains(BlockPos blockPos) {
        return tickableMap.containsKey(blockPos);
    }

    public void shutdown() {
        updater.cancel();
    }

    private BukkitRunnable createSyncUpdater() {
        return new BukkitRunnable() {
            @Override
            public void run() {
                filterCache().forEach(entry -> entry.getValue().tick());
            }
        };
    }

    private BukkitRunnable createAsyncUpdater() {
        return new BukkitRunnable() {
            @Override
            public void run() {
                CompletableFuture<Stream<Map.Entry<BlockPos, ITickable>>> future = new CompletableFuture<>();
                future.whenComplete((stream, throwable) -> stream.forEach(entry -> entry.getValue().tick()));

                Bukkit.getScheduler().runTask(CustomContentPlugin.instance, () -> future.complete(filterCache()));
            }
        };
    }

    private Stream<Map.Entry<BlockPos, ITickable>> filterCache() {
        return tickableMap.entrySet().stream().filter(entry -> {
            final Chunk chunk = entry.getKey().getChunk();
            return chunk.getWorld().isChunkLoaded(chunk);
        });
    }
}
