package net.vadamdev.customcontent.internal.handlers.blocks.textures;

import net.minecraft.server.v1_8_R3.ItemStack;
import net.vadamdev.customcontent.lib.BlockPos;
import net.vadamdev.viapi.internal.VIAPIPlugin;
import net.vadamdev.viapi.tools.packet.IEquipmentHolder;
import net.vadamdev.viapi.tools.packet.IPacketEntity;
import net.vadamdev.viapi.tools.packet.handler.IPacketEntityHandler;
import net.vadamdev.viapi.tools.tuple.MutablePair;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author VadamDev
 * @since 18/09/2023
 */
public class ChunkyPacketEntityHandler implements IPacketEntityHandler {
    private final Map<BlockPos, MutablePair<IPacketEntity, ItemStack>> packetEntities;
    private final Chunk chunk;
    private int viewRadius;
    private final int period;

    private final Set<Player> viewers;

    private Updater updater;

    public ChunkyPacketEntityHandler(Chunk chunk, int viewRadius, int period) {
        this.packetEntities = new HashMap<>();
        this.chunk = chunk;
        this.viewRadius = viewRadius * viewRadius;
        this.period = period;

        this.viewers = new HashSet<>();
    }

    public void addEntity(BlockPos blockPos, IPacketEntity packetEntity, ItemStack itemStack) {
        packetEntity.spawn(viewers);
        packetEntities.put(blockPos, new MutablePair<>(packetEntity, itemStack));
    }

    public boolean removeEntity(BlockPos blockPos) {
        packetEntities.get(blockPos).getLeft().delete(viewers);
        packetEntities.remove(blockPos);

        if(packetEntities.isEmpty()) {
            delete();
            return true;
        }

        return false;
    }

    public ItemStack getItemInHand(BlockPos blockPos) {
        return packetEntities.containsKey(blockPos) ? packetEntities.get(blockPos).getRight() : null;
    }

    public void setItemInHand(BlockPos blockPos, ItemStack itemStack) {
        if(!packetEntities.containsKey(blockPos))
            return;

        final MutablePair<IPacketEntity, ItemStack> pair = packetEntities.get(blockPos);

        if(pair.getLeft() instanceof IEquipmentHolder) {
            final IEquipmentHolder equipmentHolder = (IEquipmentHolder) pair.getLeft();
            equipmentHolder.updateLocalEquipment(0, itemStack);
            equipmentHolder.updateEquipment(0, itemStack, viewers);
        }

        pair.setRight(itemStack);
    }

    public void updateRotation(BlockPos blockPos, float yaw) {
        if(!packetEntities.containsKey(blockPos))
            return;

        final IPacketEntity packetEntity = packetEntities.get(blockPos).getLeft();

        final Location location = packetEntity.getLocalLocation();
        location.setYaw(yaw);

        packetEntity.teleportLocal(location);
        packetEntity.teleport(location, viewers);
    }

    @Override
    public void spawn() {
        updater = new Updater(period);
    }

    @Override
    public void delete() {
        updater.cancel();
        updater = null;

        if(!viewers.isEmpty()) {
            final Set<Player> toRemove = viewers.parallelStream()
                    .filter(Player::isOnline)
                    .collect(Collectors.toSet());

            if(!toRemove.isEmpty())
                packetEntities.forEach((blockPos, packetEntity) -> packetEntity.getLeft().delete(toRemove));

            viewers.clear();
        }
    }

    @Override
    public boolean isViewing(Player player) {
        return viewers.contains(player);
    }

    @Override
    public Set<Player> getViewers() {
        return viewers;
    }

    @Override
    public Location getLocation() {
        return null;
    }

    public void setViewRadius(int viewRadius) {
        this.viewRadius = viewRadius * viewRadius;
    }

    private class Updater extends BukkitRunnable {
        private final Set<Player> toAdd, toRemove;

        public Updater(int period) {
            this.toAdd = new HashSet<>();
            this.toRemove = new HashSet<>();

            runTaskTimerAsynchronously(VIAPIPlugin.instance, 0, period);
        }

        @Override
        public void run() {
            for(final Player player : Bukkit.getOnlinePlayers()) {
                final boolean closeEnough = isCloseEnough(player);

                if(isViewing(player)) {
                    if(!player.isOnline())
                        viewers.remove(player);
                    else if(!closeEnough)
                        toRemove.add(player);
                }else if(closeEnough)
                    toAdd.add(player);
            }

            if(!toRemove.isEmpty()) {
                viewers.removeAll(toRemove);
                packetEntities.forEach((blockPos, packetEntity) -> packetEntity.getLeft().delete(toRemove));
                toRemove.clear();
            }

            if(!toAdd.isEmpty()) {
                viewers.addAll(toAdd);
                packetEntities.forEach((blockPos, packetEntity) -> packetEntity.getLeft().spawn(toAdd));
                toAdd.clear();
            }
        }

        private boolean isCloseEnough(Player player) {
            final Chunk playerChunk = player.getLocation().getChunk();
            final int xDir = chunk.getX() - playerChunk.getX();
            final int zDir = chunk.getZ() - playerChunk.getZ();

            return xDir * xDir + zDir * zDir <= viewRadius;
        }
    }
}

