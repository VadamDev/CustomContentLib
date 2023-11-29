package net.vadamdev.customcontent.internal.handlers.blocks.textures;

import net.vadamdev.customcontent.lib.BlockPos;
import net.vadamdev.customcontent.lib.ChunkPos;
import net.vadamdev.viapi.tools.enums.EnumDirection;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * @author VadamDev
 * @since 05/07/2023
 */
public class CustomTextureHandler {
    private final Map<ChunkPos, ChunkyPacketEntityHandler> customTextures;

    private final int viewRadius, updatePeriod;

    public CustomTextureHandler(ConfigurationSection section) {
        this.customTextures = new HashMap<>();

        this.viewRadius = section.getInt("viewRadius");
        this.updatePeriod = section.getInt("updatePeriod");
    }

    public void addCustomTexture(BlockPos blockPos, ItemStack icon, EnumDirection direction) {
        final ChunkPos chunkPos = blockPos.toChunkPos();

        final ChunkyPacketEntityHandler entityHandler = customTextures.computeIfAbsent(chunkPos, k -> new ChunkyPacketEntityHandler(chunkPos, viewRadius, updatePeriod));
        entityHandler.addEntity(blockPos, new PacketCustomTexture(blockPos.toLocation().add(0.5, 0, 0.5), formatDirection(direction), icon));
        entityHandler.spawn();
    }

    public void updateCustomTexture(BlockPos blockPos, ItemStack icon, EnumDirection direction) {
        final ChunkPos chunkPos = new ChunkPos(blockPos.getChunk());

        if(!customTextures.containsKey(chunkPos))
            addCustomTexture(blockPos, icon, direction == null ? EnumDirection.SOUTH : direction);
        else {
            final ChunkyPacketEntityHandler handler = customTextures.get(chunkPos);

            handler.updateIcon(blockPos, CraftItemStack.asNMSCopy(icon));

            if(direction != null)
                handler.updateRotation(blockPos, formatDirection(direction));
        }
    }

    public ItemStack getCustomTexture(BlockPos blockPos) {
        final ChunkPos chunkPos = blockPos.toChunkPos();
        if(!customTextures.containsKey(chunkPos))
            return null;

        return CraftItemStack.asBukkitCopy(customTextures.get(chunkPos).getIcon(blockPos));
    }

    public void removeCustomTexture(BlockPos blockPos) {
        final ChunkPos chunkPos = blockPos.toChunkPos();
        if(!customTextures.containsKey(chunkPos))
            return;

        if(customTextures.get(chunkPos).removeEntity(blockPos))
            customTextures.remove(chunkPos);
    }

    private float formatDirection(EnumDirection direction) {
        switch(direction) {
            case NORTH: case EAST: case SOUTH: case WEST:
                return direction.getYaw();
            default:
                return 0f;
        }
    }
}
