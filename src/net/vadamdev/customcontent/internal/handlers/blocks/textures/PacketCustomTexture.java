package net.vadamdev.customcontent.internal.handlers.blocks.textures;

import net.minecraft.server.v1_8_R3.*;
import net.vadamdev.viapi.tools.builders.ArmorStandBuilder;
import net.vadamdev.viapi.tools.builders.ArmorStandLocker;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author VadamDev
 * @since 22/11/2023
 */
public class PacketCustomTexture {
    protected final EntityArmorStand entity;
    protected final int entityId;

    private final PacketPlayOutEntityDestroy destroyPacket;

    protected Location location;

    public PacketCustomTexture(Location location, float yaw, org.bukkit.inventory.ItemStack icon) {
        this.entity = ArmorStandBuilder.nms(location)
                .setAsMarker()
                .setArms(true)
                .setVisible(false)
                .setBasePlate(false)
                .setRightArmPose(new Vector3f(0, 0, 0))
                .lockSlot(new ArmorStandLocker().lockAll())
                .setRotation(yaw, 0)
                .setItemInHand(icon)
                .build();
        this.entityId = entity.getId();

        this.destroyPacket = new PacketPlayOutEntityDestroy(entityId);

        this.location = entity.getBukkitEntity().getLocation();
    }

    public void spawn(Collection<Player> players) {
        final List<Packet<PacketListenerPlayOut>> packets = new ArrayList<>();

        packets.add(new PacketPlayOutSpawnEntityLiving(entity));
        packets.add(new PacketPlayOutEntityHeadRotation(entity, (byte) ((location.getYaw() * 256f) / 360f)));

        if(entity.getEquipment()[0] != null)
            packets.add(new PacketPlayOutEntityEquipment(entityId, 0, entity.getEquipment()[0]));

        for (Player player : players) {
            final PlayerConnection playerConnection = getPlayerConnection(player);
            packets.forEach(playerConnection::sendPacket);
        }
    }

    public void delete(Collection<Player> players) {
        for(Player player : players)
            getPlayerConnection(player).sendPacket(destroyPacket);
    }

    public void updateIcon(ItemStack itemStack, Collection<Player> players) {
        entity.setEquipment(0, itemStack);

        final PacketPlayOutEntityEquipment packet = new PacketPlayOutEntityEquipment(entityId, 0, itemStack);

        for (Player player : players)
            getPlayerConnection(player).sendPacket(packet);
    }

    public void updateRotation(float yaw, Collection<Player> players) {
        location.setYaw(yaw);

        final PacketPlayOutEntityHeadRotation packet = new PacketPlayOutEntityHeadRotation(entity, (byte) ((yaw * 256f) / 360f));

        for (Player player : players)
            getPlayerConnection(player).sendPacket(packet);
    }

    public ItemStack getIcon() {
        return entity.getEquipment(0);
    }

    private PlayerConnection getPlayerConnection(Player player) {
        return ((CraftPlayer) player).getHandle().playerConnection;
    }
}
