package net.vadamdev.customcontent.lib;

import org.bukkit.event.block.Action;

import javax.annotation.Nullable;

/**
 * Spigot Action enum without PHYSICAL.
 *
 * @author VadamDev
 * @since 22/12/2021
 */
public enum ItemAction {
    RIGHT_CLICK_BLOCK,
    RIGHT_CLICK_AIR,
    LEFT_CLICK_BLOCK,
    LEFT_CLICK_AIR;

    /**
     * Convert bukkit {@link Action Action} to an {@link ItemAction ItemAction}.
     *
     * @param action {@link Action Action}
     * @return {@link ItemAction ItemAction}
     */
    @Nullable
    public static ItemAction of(Action action) {
        switch(action) {
            case RIGHT_CLICK_BLOCK:
                return RIGHT_CLICK_BLOCK;
            case RIGHT_CLICK_AIR:
                return RIGHT_CLICK_AIR;
            case LEFT_CLICK_BLOCK:
                return LEFT_CLICK_BLOCK;
            case LEFT_CLICK_AIR:
                return LEFT_CLICK_AIR;
            default:
                return null;
        }
    }
}
