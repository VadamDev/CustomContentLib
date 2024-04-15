package net.vadamdev.customcontent.lib.blocks;

import net.minecraft.server.v1_8_R3.*;
import net.vadamdev.customcontent.api.blocks.texture.AbstractWorldTexture;
import net.vadamdev.customcontent.api.blocks.texture.IBlockTextureAccessor;
import net.vadamdev.customcontent.lib.BlockPos;
import net.vadamdev.viapi.tools.builders.ArmorStandBuilder;
import net.vadamdev.viapi.tools.builders.ArmorStandLocker;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Default implementation of {@link net.vadamdev.customcontent.api.blocks.texture.WorldTexture WorldTexture}
 *
 * @author VadamDev
 * @since 05/03/2024
 */
public class DefaultWorldTexture extends AbstractWorldTexture {
    private final EntityArmorStand armorStand;
    private final int entityId;

    private final PacketPlayOutEntityDestroy destroyPacket;

    public DefaultWorldTexture(BlockPos blockPos, BlockDirection direction, IBlockTextureAccessor accessor, boolean occluding) {
        super(occluding, direction);

        this.armorStand = ArmorStandBuilder.nms(blockPos.toLocation().add(0.5, 0, 0.5))
                .setAsMarker()
                .setArms(true)
                .setVisible(false)
                .setBasePlate(false)
                .setRightArmPose(new Vector3f(0, 0, 0))
                .lockSlot(ArmorStandLocker.lockAll())
                .setRotation(direction.getYaw(), 0)
                .setItemInHand(accessor.getTexture())
                .build();
        this.entityId = armorStand.getId();

        this.destroyPacket = new PacketPlayOutEntityDestroy(entityId);
    }

    @Override
    public void spawn(Collection<Player> players) {
        final List<Packet<PacketListenerPlayOut>> packets = new ArrayList<>();

        packets.add(new PacketPlayOutSpawnEntityLiving(armorStand));

        if(armorStand.getEquipment(0) != null)
            packets.add(new PacketPlayOutEntityEquipment(entityId, 0, armorStand.getEquipment(0)));

        for(Player player : players) {
            final PlayerConnection playerConnection = getPlayerConnection(player);
            packets.forEach(playerConnection::sendPacket);
        }
    }

    @Override
    public void delete(Collection<Player> players) {
        for(Player player : players)
            getPlayerConnection(player).sendPacket(destroyPacket);
    }

    @Override
    public void applyTextureChanges(Collection<Player> players, boolean texture, boolean direction) {
        final List<Packet<PacketListenerPlayOut>> packets = new ArrayList<>();

        if(texture)
            packets.add(new PacketPlayOutEntityEquipment(entityId, 0, armorStand.getEquipment(0)));

        if(direction)
            packets.add(new PacketPlayOutEntityTeleport(
                    entityId,
                    MathHelper.floor(armorStand.locX * 32D), MathHelper.floor(armorStand.locY * 32D), MathHelper.floor(armorStand.locZ * 32D),
                    (byte) ((int) (armorStand.yaw * 256.0F / 360.0F)), (byte) 0,
                    false
            ));

        if(packets.isEmpty())
            return;

        for(Player player : players) {
            final PlayerConnection playerConnection = getPlayerConnection(player);
            packets.forEach(playerConnection::sendPacket);
        }
    }

    @Override
    public void updateTexture(IBlockTextureAccessor accessor) {
        armorStand.setEquipment(0, CraftItemStack.asNMSCopy(accessor.getTexture()));
    }

    @Override
    public ItemStack getTextureIcon() {
        return CraftItemStack.asBukkitCopy(armorStand.getEquipment(0));
    }

    @Override
    public void updateDirection(BlockDirection direction) {
        armorStand.yaw = direction.getYaw();
        this.direction = direction;
    }
}
