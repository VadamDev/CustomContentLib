package net.vadamdev.customcontent.lib;

import org.bukkit.event.block.Action;

/**
 * Spigot Action enum without PHYSICAL
 * @author VadamDev
 * @since 22/12/2021
 */
public enum ItemAction {
    RIGHT_CLICK_BLOCK,
    RIGHT_CLICK_AIR,
    LEFT_CLICK_BLOCK,
    LEFT_CLICK_AIR;

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
