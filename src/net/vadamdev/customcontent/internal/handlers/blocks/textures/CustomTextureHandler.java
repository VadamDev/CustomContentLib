package net.vadamdev.customcontent.internal.handlers.blocks.textures;

import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.Vector3f;
import net.vadamdev.customcontent.lib.BlockPos;
import net.vadamdev.viapi.tools.builders.ArmorStandBuilder;
import net.vadamdev.viapi.tools.builders.ArmorStandLocker;
import net.vadamdev.viapi.tools.enums.EnumDirection;
import net.vadamdev.viapi.tools.packet.entities.GenericPacketEntity;
import org.bukkit.Chunk;
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
    private final Map<Chunk, ChunkyPacketEntityHandler> customTextures;

    private final int viewRadius, updatePeriod;

    public CustomTextureHandler(ConfigurationSection section) {
        this.customTextures = new HashMap<>();

        this.viewRadius = section.getInt("viewRadius");
        this.updatePeriod = section.getInt("updatePeriod");
    }

    public void addCustomTexture(BlockPos blockPos, ItemStack itemStack, EnumDirection direction) {
        final Chunk chunk = blockPos.getChunk();

        final ChunkyPacketEntityHandler entityHandler = customTextures.computeIfAbsent(chunk, k -> new ChunkyPacketEntityHandler(chunk, viewRadius, updatePeriod));
        entityHandler.addEntity(blockPos, new GenericPacketEntity(generateArmorStand(blockPos, itemStack, formatDirection(direction))), CraftItemStack.asNMSCopy(itemStack));
        entityHandler.spawn();
    }

    public void updateCustomTexture(BlockPos blockPos, ItemStack itemStack, EnumDirection direction) {
        final Chunk chunk = blockPos.getChunk();

        if(!customTextures.containsKey(chunk))
            addCustomTexture(blockPos, itemStack, direction == null ? EnumDirection.SOUTH : direction);
        else {
            final ChunkyPacketEntityHandler handler = customTextures.get(chunk);

            handler.setItemInHand(blockPos, CraftItemStack.asNMSCopy(itemStack));

            if(direction != null)
                handler.updateRotation(blockPos, formatDirection(direction));
        }
    }

    public ItemStack getCustomTexture(BlockPos blockPos) {
        final Chunk chunk = blockPos.getChunk();

        if(!customTextures.containsKey(chunk))
            return null;

        return CraftItemStack.asBukkitCopy(customTextures.get(chunk).getItemInHand(blockPos));
    }

    public void removeCustomTexture(BlockPos blockPos) {
        final Chunk chunk = blockPos.getChunk();
        if(!customTextures.containsKey(chunk))
            return;

        if(customTextures.get(chunk).removeEntity(blockPos))
            customTextures.remove(chunk);
    }

    private float formatDirection(EnumDirection direction) {
        switch(direction) {
            case NORTH:
                return 180f;
            case EAST:
                return -90f;
            case WEST:
                return 90f;
            default:
                return 0f;
        }
    }

    private EntityArmorStand generateArmorStand(BlockPos blockPos, ItemStack icon, float yaw) {
        return ArmorStandBuilder.nms(blockPos.toLocation().add(0.5, 0, 0.5))
                .setAsMarker()
                .setArms(true)
                .setVisible(false)
                .setBasePlate(false)
                .setRightArmPose(new Vector3f(0, 0, 0))
                .lockSlot(new ArmorStandLocker().lockAll())
                .setRotation(yaw, 0)
                .setItemInHand(icon)
                .build();
    }
}
