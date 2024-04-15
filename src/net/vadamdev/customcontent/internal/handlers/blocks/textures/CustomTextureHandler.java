package net.vadamdev.customcontent.internal.handlers.blocks.textures;

import net.vadamdev.customcontent.api.blocks.texture.AbstractWorldTexture;
import net.vadamdev.customcontent.api.blocks.texture.IBlockTextureAccessor;
import net.vadamdev.customcontent.api.blocks.texture.ICustomTextureHolder;
import net.vadamdev.customcontent.lib.BlockPos;
import net.vadamdev.customcontent.lib.ChunkPos;
import net.vadamdev.customcontent.lib.blocks.BlockDirection;
import net.vadamdev.customcontent.lib.serialization.SerializableDataCompound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author VadamDev
 * @since 05/07/2023
 */
public class CustomTextureHandler {
    protected static int VIEW_RADIUS_SQUARED = 0;
    protected static int UPDATE_PERIOD = 0;
    protected static boolean OCCLUDING_CHECK_DISABLED = false;

    private final Map<ChunkPos, ChunkyPacketEntityHandler> customTextures;

    public CustomTextureHandler(ConfigurationSection section) {
        this.customTextures = new ConcurrentHashMap<>();

        final int viewRadius = section.getInt("viewRadius");

        VIEW_RADIUS_SQUARED = viewRadius * viewRadius;
        UPDATE_PERIOD = section.getInt("updatePeriod");
        OCCLUDING_CHECK_DISABLED = !section.getBoolean("occluding");
    }

    public void createCustomTexture(BlockPos blockPos, ICustomTextureHolder textureHolder, BlockDirection direction, @Nullable SerializableDataCompound compound) {
        final AbstractWorldTexture worldTexture = createWorldTexture(blockPos, textureHolder, direction);
        if(worldTexture == null)
            return;

        if(compound != null)
            compound.putString("direction", worldTexture.getDirection().name());

        final ChunkPos chunkPos = blockPos.toChunkPos();
        customTextures.computeIfAbsent(chunkPos, k -> new ChunkyPacketEntityHandler(chunkPos))
                .createTexture(blockPos, worldTexture);
    }

    public void applyTextureChanges(BlockPos blockPos, boolean texture, boolean direction) {
        final ChunkPos chunkPos = blockPos.toChunkPos();
        if(!customTextures.containsKey(chunkPos))
            return;

        customTextures.get(chunkPos).applyTextureChanges(blockPos, texture, direction);
    }

    public void removeCustomTexture(BlockPos blockPos) {
        final ChunkPos chunkPos = blockPos.toChunkPos();
        if(!customTextures.containsKey(chunkPos))
            return;

        if(customTextures.get(chunkPos).removeTexture(blockPos))
            customTextures.remove(chunkPos);
    }

    public AbstractWorldTexture getCustomTexture(BlockPos blockPos) {
        final ChunkPos chunkPos = blockPos.toChunkPos();
        if(!customTextures.containsKey(chunkPos))
            return null;

        return customTextures.get(chunkPos).getTexture(blockPos);
    }

    private AbstractWorldTexture createWorldTexture(BlockPos blockPos, ICustomTextureHolder textureHolder, BlockDirection direction) {
        final IBlockTextureAccessor accessor = textureHolder.getDefaultTexture();
        if(accessor == null)
            return null;

        final ItemStack texture = accessor.getTexture();
        if(texture == null)
            return null;

        return textureHolder.createWorldTexture(blockPos, direction, accessor);
    }
}
