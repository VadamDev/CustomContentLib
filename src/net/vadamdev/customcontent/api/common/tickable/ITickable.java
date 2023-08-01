package net.vadamdev.customcontent.api.common.tickable;

import net.vadamdev.customcontent.annotations.TickableInfo;

/**
 * Represents a CCL's tickable entity.
 * @see TickableInfo
 *
 * @author VadamDev
 * @since 18/01/2022
 */
@TickableInfo
public interface ITickable {
    void tick();
}
