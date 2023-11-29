package net.vadamdev.customcontent.lib;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;

import java.util.Objects;

/**
 * @author VadamDev
 * @since 21/11/2023
 */
public class ChunkPos {
    private final World world;
    private final int x, z;

    public ChunkPos(World world, int x, int z) {
        this.world = world;
        this.x = x;
        this.z = z;
    }

    public ChunkPos(Chunk chunk) {
        this(chunk.getWorld(), chunk.getX(), chunk.getZ());
    }

    public Chunk getChunk() {
        return world.getChunkAt(x, z);
    }

    public World getWorld() {
        return world;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public String toSerializableString() {
        return world.getName() + ":" + x + ":" + z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChunkPos)) return false;
        ChunkPos chunkPos = (ChunkPos) o;
        return x == chunkPos.x && z == chunkPos.z && Objects.equals(world, chunkPos.world);
    }

    @Override
    public int hashCode() {
        return Objects.hash(world, x, z);
    }

    public static ChunkPos fromSerializableString(String s) {
        String[] split = s.split(":");
        return new ChunkPos(Bukkit.getWorld(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
    }
}
