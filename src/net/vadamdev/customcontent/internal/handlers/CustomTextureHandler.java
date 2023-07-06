package net.vadamdev.customcontent.internal.handlers;

import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.Vector3f;
import net.vadamdev.customcontent.lib.BlockPos;
import net.vadamdev.viaapi.tools.builders.ItemBuilder;
import net.vadamdev.viaapi.tools.builders.NMSArmorStandBuilder;
import net.vadamdev.viaapi.tools.enums.LockType;
import net.vadamdev.viaapi.tools.packet.entityhandler.RangePacketEntityHandler;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

/**
 * @author VadamDev
 * @since 05/07/2023
 */
public class CustomTextureHandler {
    private final Map<BlockPos, RangePacketEntityHandler> customTextures;

    public CustomTextureHandler() {
        this.customTextures = new HashMap<>();
    }

    public void addCustomTexture(BlockPos blockPos, String textureName, int yaw) {
        RangePacketEntityHandler packetEntityHandler = new RangePacketEntityHandler(generateArmorStand(blockPos, textureName, yaw), 50, 20);
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
        return new NMSArmorStandBuilder(new Location(blockPos.getWorld(), blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5))
                .lockSlot(LockType.ALL)
                .setVisible(false)
                .setBasePlate(false)
                .setArms(true)
                .setItemInHand(new ItemBuilder(Material.SLIME_BALL).setName(textureName).toItemStack())
                .setRightArmPose(new Vector3f(0, 0, 0))
                .setRotation(yaw, 0)
                .toArmorStandEntity();
    }
}
