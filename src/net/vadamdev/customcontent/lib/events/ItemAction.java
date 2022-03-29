package net.vadamdev.customcontent.lib.events;

import org.bukkit.event.block.Action;

/**
 * @author VadamDev
 * @since 22/12/2021
 */
public enum ItemAction {
    RIGHT_CLICK_BLOCK,
    RIGHT_CLICK_AIR,
    OTHER;

    public static ItemAction of(Action action) {
        return action == Action.RIGHT_CLICK_BLOCK ? RIGHT_CLICK_BLOCK : action == Action.RIGHT_CLICK_AIR ? RIGHT_CLICK_AIR : OTHER;
    }
}
