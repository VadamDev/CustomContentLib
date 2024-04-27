package net.vadamdev.customcontent.lib.blocks;

import org.bukkit.entity.Entity;

/**
 * VIAPI EnumDirection without UP and DOWN
 *
 * @author VadamDev
 * @since 06/04/2024
 */
public enum BlockDirection {
    NORTH(-180),
    EAST(-90),
    SOUTH(0),
    WEST(90),
    NORTH_EAST(-135),
    NORTH_WEST(135),
    SOUTH_EAST(-45),
    SOUTH_WEST(45);

    private final float yaw;

    BlockDirection(float yaw) {
        this.yaw = yaw;
    }

    public BlockDirection getOpposite() {
        switch(this) {
            case SOUTH:
                return NORTH;
            case EAST:
                return WEST;
            case WEST:
                return EAST;
            case NORTH_EAST:
                return SOUTH_WEST;
            case NORTH_WEST:
                return SOUTH_EAST;
            case SOUTH_EAST:
                return NORTH_WEST;
            case SOUTH_WEST:
                return NORTH_EAST;
            default:
                return SOUTH;
        }
    }

    public float getYaw() {
        return yaw;
    }

    public static BlockDirection getCardinalDirection(float yaw) {
        float rot = (yaw - 180) % 360;

        if (rot < 0)
            rot += 360;

        if (0 <= rot && rot < 67.5)
            return BlockDirection.NORTH;
        else if (67.5 <= rot && rot < 157.5)
            return BlockDirection.EAST;
        else if (157.5 <= rot && rot < 247.5)
            return BlockDirection.SOUTH;
        else if (247.5 <= rot && rot < 337.5)
            return BlockDirection.WEST;
        else if (337.5 <= rot && rot < 360.0)
            return BlockDirection.NORTH;

        return null;
    }

    public static BlockDirection getCardinalDirection(Entity entity) {
        return getCardinalDirection(entity.getLocation().getYaw());
    }

    public static BlockDirection getPreciseDirection(float yaw) {
        float rot = yaw - 180;

        if (rot < 0)
            rot += 360;

        if (22.5 <= rot && rot < 67.5)
            return BlockDirection.NORTH_EAST;
        else if (67.5 <= rot && rot < 112.5)
            return BlockDirection.EAST;
        else if (112.5 <= rot && rot < 157.5)
            return BlockDirection.SOUTH_EAST;
        else if (157.5 <= rot && rot < 202.5)
            return BlockDirection.SOUTH;
        else if (202.5 <= rot && rot < 247.5)
            return BlockDirection.SOUTH_WEST;
        else if (247.5 <= rot && rot < 292.5)
            return BlockDirection.WEST;
        else if (292.5 <= rot && rot < 337.5)
            return BlockDirection.NORTH_WEST;
        else
            return BlockDirection.NORTH;
    }

    public static BlockDirection getPreciseDirection(Entity entity) {
        return getPreciseDirection(entity.getLocation().getYaw());
    }
}
