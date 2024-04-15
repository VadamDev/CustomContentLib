package net.vadamdev.customcontent.lib.blocks;

/**
 * Represents special abilities of a {@link net.vadamdev.customcontent.api.blocks.CustomBlock CustomBlock}.
 *
 * @author VadamDev
 * @since 10/12/2022
 */
public enum BlockFlags {
    /**
     * Make the {@link net.vadamdev.customcontent.api.blocks.CustomBlock CustomBlock} unbreakable by explosions
     */
    TNT_RESISTANT,

    /**
     * Make the {@link net.vadamdev.customcontent.api.blocks.CustomBlock CustomBlock} totally unbreakable, even in creative mode
     */
    UNBREAKABLE
}
