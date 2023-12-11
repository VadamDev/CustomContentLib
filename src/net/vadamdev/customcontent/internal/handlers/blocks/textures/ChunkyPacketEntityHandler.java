package net.vadamdev.customcontent.internal.handlers.blocks.textures;

import io.netty.util.internal.ConcurrentSet;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.vadamdev.customcontent.internal.CustomContentPlugin;
import net.vadamdev.customcontent.lib.BlockPos;
import net.vadamdev.customcontent.lib.ChunkPos;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author VadamDev
 * @since 18/09/2023
 */
public class ChunkyPacketEntityHandler {
    private final Map<BlockPos, PacketCustomTexture> packetEntities;
    private final int chunkX, chunkZ, viewRadius, period;

    private final Set<Player> viewers;

    private Updater updater;

    public ChunkyPacketEntityHandler(ChunkPos chunkPos, int viewRadius, int period) {
        this.packetEntities = new ConcurrentHashMap<>();
        this.chunkX = chunkPos.getX();
        this.chunkZ = chunkPos.getZ();
        this.viewRadius = viewRadius * viewRadius;
        this.period = period;

        this.viewers = new ConcurrentSet<>();
    }

    public void addEntity(BlockPos blockPos, PacketCustomTexture customTexture) {
        customTexture.spawn(viewers);
        packetEntities.put(blockPos, customTexture);
    }

    public boolean removeEntity(BlockPos blockPos) {
        packetEntities.get(blockPos).delete(viewers);
        packetEntities.remove(blockPos);

        if(packetEntities.isEmpty()) {
            delete();
            return true;
        }

        return false;
    }

    public ItemStack getIcon(BlockPos blockPos) {
        return packetEntities.containsKey(blockPos) ? packetEntities.get(blockPos).getIcon() : null;
    }

    public void updateIcon(BlockPos blockPos, ItemStack itemStack) {
        if(!packetEntities.containsKey(blockPos))
            return;

        packetEntities.get(blockPos).updateIcon(itemStack, viewers);
    }

    public void updateRotation(BlockPos blockPos, float yaw) {
        if(!packetEntities.containsKey(blockPos))
            return;

        packetEntities.get(blockPos).updateRotation(yaw, viewers);
    }

    public void spawn() {
        updater = new Updater(period);
    }

    public void delete() {
        updater.cancel();
        updater = null;

        if(!viewers.isEmpty()) {
            final Set<Player> toRemove = viewers.stream()
                    .filter(Player::isOnline)
                    .collect(Collectors.toSet());

            if(!toRemove.isEmpty())
                packetEntities.forEach((blockPos, packetEntity) -> packetEntity.delete(toRemove));

            viewers.clear();
        }
    }

    public boolean isViewing(Player player) {
        return viewers.contains(player);
    }

    private class Updater extends BukkitRunnable {
        private final Set<Player> toSpawn, toDelete;

        private Updater(int period) {
            this.toSpawn = new HashSet<>();
            this.toDelete = new HashSet<>();

            runTaskTimerAsynchronously(CustomContentPlugin.instance, 0, period);
        }

        @Override
        public void run() {
            for(final Player player : Bukkit.getOnlinePlayers()) {
                final boolean closeEnough = isCloseEnough(player);

                if(isViewing(player)) {
                    if(!player.isOnline())
                        viewers.remove(player);
                    else if(!closeEnough) {
                        viewers.remove(player);
                        toDelete.add(player);
                    }
                }else if(closeEnough) {
                    viewers.add(player);
                    toSpawn.add(player);
                }
            }

            if(!toSpawn.isEmpty()) {
                packetEntities.values().forEach(packetEntity -> packetEntity.spawn(toSpawn));
                toSpawn.clear();
            }

            if(!toDelete.isEmpty()) {
                packetEntities.values().forEach(packetEntity -> packetEntity.delete(toDelete));
                toDelete.clear();
            }
        }

        private boolean isCloseEnough(Player player) {
            final Chunk playerChunk = player.getLocation().getChunk();
            final int xDir = chunkX - playerChunk.getX();
            final int zDir = chunkZ - playerChunk.getZ();

            return xDir * xDir + zDir * zDir <= viewRadius;
        }
    }
}

