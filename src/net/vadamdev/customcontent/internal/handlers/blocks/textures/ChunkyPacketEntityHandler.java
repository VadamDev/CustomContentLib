package net.vadamdev.customcontent.internal.handlers.blocks.textures;

import net.vadamdev.customcontent.api.blocks.texture.AbstractWorldTexture;
import net.vadamdev.customcontent.api.blocks.texture.WorldTexture;
import net.vadamdev.customcontent.internal.CustomContentPlugin;
import net.vadamdev.customcontent.lib.BlockPos;
import net.vadamdev.customcontent.lib.ChunkPos;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
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
    private static final BlockFace[] faces = new BlockFace[] { BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST, BlockFace.UP, BlockFace.DOWN };

    private final ChunkPos chunkPos;

    private final Map<BlockPos, AbstractWorldTexture> textures;
    private final Set<Player> viewers;

    private Updater updater;

    public ChunkyPacketEntityHandler(ChunkPos chunkPos) {
        this.chunkPos = chunkPos;

        this.textures = new ConcurrentHashMap<>();
        this.viewers = new HashSet<>();

        this.updater = new Updater();
    }

    public void createTexture(BlockPos blockPos, AbstractWorldTexture customTexture) {
        customTexture.spawn(viewers);
        textures.put(blockPos, customTexture);
    }

    public void applyTextureChanges(BlockPos blockPos, boolean texture, boolean direction) {
        if(!textures.containsKey(blockPos))
            return;

        textures.get(blockPos).applyTextureChanges(viewers, texture, direction);
    }

    public boolean removeTexture(BlockPos blockPos) {
        if(!textures.containsKey(blockPos))
            return false;

        textures.get(blockPos).delete(viewers);
        textures.remove(blockPos);

        if(textures.isEmpty()) {
            delete();
            return true;
        }

        return false;
    }

    public AbstractWorldTexture getTexture(BlockPos blockPos) {
        return textures.get(blockPos);
    }

    private void delete() {
        updater.cancel();
        updater = null;

        if(!viewers.isEmpty()) {
            final Set<Player> toRemove = viewers.stream()
                    .filter(Player::isOnline)
                    .collect(Collectors.toSet());

            if(!toRemove.isEmpty())
                textures.forEach((blockPos, packetEntity) -> packetEntity.delete(toRemove));

            viewers.clear();
        }
    }

    private class Updater extends BukkitRunnable {
        private Updater() {
            runTaskTimerAsynchronously(CustomContentPlugin.instance, 0, CustomTextureHandler.UPDATE_PERIOD);
        }

        @Override
        public void run() {
            if(!chunkPos.isLoaded())
                return;

            doPlayerDistanceCheck();
            doOccludingCheck();
        }

        /*
           Player distance
         */

        private void doPlayerDistanceCheck() {
            final Set<Player> viewersToSpawn = new HashSet<>(), viewersToDelete = new HashSet<>();

            for(final Player player : Bukkit.getOnlinePlayers()) {
                final boolean closeEnough = isCloseEnough(player);

                if(viewers.contains(player)) {
                    if(!player.isOnline())
                        viewers.remove(player);
                    else if(!closeEnough) {
                        viewers.remove(player);
                        viewersToDelete.add(player);
                    }
                }else if(closeEnough) {
                    viewers.add(player);
                    viewersToSpawn.add(player);
                }
            }

            if(!viewersToSpawn.isEmpty())
                textures.values().stream().filter(texture -> texture.enabled).forEach(packetEntity -> packetEntity.spawn(viewersToSpawn));

            if(!viewersToDelete.isEmpty())
                textures.values().stream().filter(texture -> texture.enabled).forEach(packetEntity -> packetEntity.delete(viewersToDelete));
        }

        private boolean isCloseEnough(Player player) {
            final Chunk playerChunk = player.getLocation().getChunk();
            final int xDir = chunkPos.getX() - playerChunk.getX();
            final int zDir = chunkPos.getZ() - playerChunk.getZ();

            return xDir * xDir + zDir * zDir <= CustomTextureHandler.VIEW_RADIUS_SQUARED;
        }

        /*
           Occluding
         */

        private void doOccludingCheck() {
            if(CustomTextureHandler.OCCLUDING_CHECK_DISABLED)
                return;

            final Set<AbstractWorldTexture> texturesToDisable = new HashSet<>(), texturesToEnable = new HashSet<>();

            for(Map.Entry<BlockPos, AbstractWorldTexture> entry : textures.entrySet()) {
                final AbstractWorldTexture worldTexture = entry.getValue();
                final BlockPos blockPos = entry.getKey();

                boolean flag = true;
                for(BlockFace face : faces) {
                    if(!isOccluding(new BlockPos(blockPos.getWorld(), blockPos.getX() + face.getModX(), blockPos.getY() + face.getModY(), blockPos.getZ() + face.getModZ()))) {
                        flag = false; //TODO: check if it's broken
                        break;
                    }
                }

                if(flag) {
                    if(worldTexture.enabled) {
                        worldTexture.enabled = false;
                        texturesToDisable.add(worldTexture);
                    }
                }else if(!worldTexture.enabled) {
                    worldTexture.enabled = true;
                    texturesToEnable.add(worldTexture);
                }
            }

            texturesToDisable.forEach(worldTexture -> worldTexture.delete(viewers));
            texturesToEnable.forEach(worldTexture -> worldTexture.spawn(viewers));
        }
    }

    private boolean isOccluding(BlockPos blockPos) {
        if(!blockPos.isChunkLoaded())
            return true;

        final Material material = blockPos.getBlock().getType();

        if(material.equals(Material.BARRIER)) {
            final WorldTexture worldTexture = CustomContentPlugin.instance.getTextureHandler().getCustomTexture(blockPos);

            if(worldTexture != null)
                return worldTexture.isOccluding();
        }

        return material.isOccluding();
    }
}

