package net.vadamdev.customcontent.lib;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.Objects;

/**
 * Represents the position of a block.
 *
 * @author VadamDev
 * @since 01/09/2022
 */
public class BlockPos {
    private final World world;
    private final int x, y, z;

    public BlockPos(World world, int x, int y, int z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public BlockPos(Location location) {
        this(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public BlockPos(Block block) {
        this(block.getWorld(), block.getX(), block.getY(), block.getZ());
    }

    public Chunk getChunk() {
        return world.getChunkAt(x >> 4, z >> 4);
    }

    public Block getBlock() {
        return world.getBlockAt(x, y, z);
    }

    public World getWorld() {
        return world;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public ChunkPos toChunkPos() {
        return new ChunkPos(world, x >> 4, z >> 4);
    }

    public Location toLocation() {
        return new Location(world, x, y, z);
    }

    public String toSerializableString() {
        return world.getName() + ":" + x + ":" + y + ":" + z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlockPos blockPos = (BlockPos) o;
        return x == blockPos.x && y == blockPos.y && z == blockPos.z && Objects.equals(world, blockPos.world);
    }

    @Override
    public int hashCode() {
        return Objects.hash(world, x, y, z);
    }

    public static BlockPos fromSerializableString(String s) {
        String[] split = s.split(":");
        return new BlockPos(Bukkit.getWorld(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3]));
    }
}
