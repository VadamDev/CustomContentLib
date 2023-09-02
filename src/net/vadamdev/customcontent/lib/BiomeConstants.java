package net.vadamdev.customcontent.lib;

import net.minecraft.server.v1_8_R3.BiomeBase;

/**
 * @author VadamDev
 * @since 31/08/2023
 */
public final class BiomeConstants {
    private BiomeConstants() {}

    public static final BiomeBase[] NETHER = new BiomeBase[] { BiomeBase.HELL };

    public static final BiomeBase[] OVERWORLD = new BiomeBase[] {
            BiomeBase.OCEAN,
            BiomeBase.PLAINS,
            BiomeBase.DESERT,
            BiomeBase.EXTREME_HILLS,
            BiomeBase.FOREST,
            BiomeBase.TAIGA,
            BiomeBase.SWAMPLAND,
            BiomeBase.RIVER,
            BiomeBase.FROZEN_OCEAN,
            BiomeBase.FROZEN_RIVER,
            BiomeBase.ICE_PLAINS,
            BiomeBase.ICE_MOUNTAINS,
            BiomeBase.MUSHROOM_ISLAND,
            BiomeBase.MUSHROOM_SHORE,
            BiomeBase.BEACH,
            BiomeBase.DESERT_HILLS,
            BiomeBase.FOREST_HILLS,
            BiomeBase.TAIGA_HILLS,
            BiomeBase.SMALL_MOUNTAINS,
            BiomeBase.JUNGLE,
            BiomeBase.JUNGLE_HILLS,
            BiomeBase.JUNGLE_EDGE,
            BiomeBase.DEEP_OCEAN,
            BiomeBase.STONE_BEACH,
            BiomeBase.COLD_BEACH,
            BiomeBase.BIRCH_FOREST,
            BiomeBase.BIRCH_FOREST_HILLS,
            BiomeBase.ROOFED_FOREST,
            BiomeBase.COLD_TAIGA,
            BiomeBase.COLD_TAIGA_HILLS,
            BiomeBase.MEGA_TAIGA,
            BiomeBase.MEGA_TAIGA_HILLS,
            BiomeBase.EXTREME_HILLS_PLUS,
            BiomeBase.SAVANNA,
            BiomeBase.SAVANNA_PLATEAU,
            BiomeBase.MESA,
            BiomeBase.MESA_PLATEAU_F,
            BiomeBase.MESA_PLATEAU
    };

    public static final BiomeBase[] END = new BiomeBase[] { BiomeBase.SKY };
}
