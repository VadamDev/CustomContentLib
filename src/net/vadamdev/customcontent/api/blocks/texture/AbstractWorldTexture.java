package net.vadamdev.customcontent.api.blocks.texture;

import net.minecraft.server.v1_8_R3.PlayerConnection;
import net.vadamdev.customcontent.lib.blocks.BlockDirection;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Collection;

/**
 * Abstract implementation of {@link WorldTexture}
 *
 * @author VadamDev
 * @since 06/03/2024
 */
public abstract class AbstractWorldTexture implements WorldTexture {
    private final boolean occluding;
    protected BlockDirection direction;

    public boolean enabled;

    public AbstractWorldTexture(boolean occluding, BlockDirection direction) {
        this.occluding = occluding;
        this.direction = direction;

        this.enabled = true;
    }

    public abstract void spawn(Collection<Player> players);
    public abstract void delete(Collection<Player> players);
    public abstract void applyTextureChanges(Collection<Player> players, boolean texture, boolean direction);

    @Override
    public BlockDirection getDirection() {
        return direction;
    }

    @Override
    public boolean isOccluding() {
        return occluding;
    }

    protected PlayerConnection getPlayerConnection(Player player) {
        return ((CraftPlayer) player).getHandle().playerConnection;
    }
}
