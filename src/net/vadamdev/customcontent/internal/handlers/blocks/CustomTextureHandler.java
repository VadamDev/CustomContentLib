package net.vadamdev.customcontent.internal.handlers.blocks;

import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.Vector3f;
import net.vadamdev.customcontent.lib.BlockPos;
import net.vadamdev.viapi.tools.builders.ArmorStandBuilder;
import net.vadamdev.viapi.tools.builders.ArmorStandLocker;
import net.vadamdev.viapi.tools.builders.ItemBuilder;
import net.vadamdev.viapi.tools.packet.handler.DynamicPacketEntityHandler;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

/**
 * @author VadamDev
 * @since 05/07/2023
 */
public class CustomTextureHandler {
    private final Map<BlockPos, DynamicPacketEntityHandler> customTextures;

    public CustomTextureHandler() {
        this.customTextures = new HashMap<>();
    }

    public void addCustomTexture(BlockPos blockPos, String textureName, int yaw) {
        final DynamicPacketEntityHandler packetEntityHandler = new DynamicPacketEntityHandler(generateArmorStand(blockPos, textureName, yaw), 50, 20);
        packetEntityHandler.spawn();

        customTextures.put(blockPos, packetEntityHandler);
    }

    public void removeCustomTexture(BlockPos blockPos) {
        if(!customTextures.containsKey(blockPos))
            return;

        customTextures.get(blockPos).delete();
        customTextures.remove(blockPos);
    }

    private EntityArmorStand generateArmorStand(BlockPos blockPos, String textureName, int yaw) {
        return ArmorStandBuilder.nms(blockPos.toLocation().add(0.5, 0, 0.5))
                .setAsMarker()
                .lockSlot(new ArmorStandLocker().lockAll())
                .setVisible(false)
                .setBasePlate(false)
                .setArms(true)
                .setItemInHand(ItemBuilder.item(Material.SLIME_BALL).setName(textureName).build())
                .setRightArmPose(new Vector3f(0, 0, 0))
                .setRotation(yaw, 0)
                .build();
    }
}
