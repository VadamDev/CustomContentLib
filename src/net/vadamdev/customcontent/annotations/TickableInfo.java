package net.vadamdev.customcontent.annotations;

import net.vadamdev.customcontent.api.common.tickable.AbstractTickableHandler;

import javax.annotation.Nonnull;
import java.lang.annotation.*;

/**
 * <p> Class which implement of {@link net.vadamdev.customcontent.api.common.tickable.ITickable ITickable} and
 * annotated with {@link TickableInfo} will have the ability to define extra parameters for the execution of the tick() method
 *
 * <p> WARNING: There's not a 100% guarantee that these parameters will be taken in consideration by CCL
 * <br> For example, handlerId will be ignored for {@link net.vadamdev.customcontent.api.blocks.CustomTileEntity CustomTileEntity}
 *
 * @author VadamDev
 * @since 29/07/2023
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface TickableInfo {
    /**
     * Determines the delay between each operation
     *
     * @return The update interval in ticks
     */
    int interval() default 1;

    /**
     * Determines if the operation should be handled synchronously or asynchronously
     *
     * @return True if the operation should be handled asynchronously
     */
    boolean async() default false;

    /**
     * Determine what will handle the ITickable.
     * @see AbstractTickableHandler
     *
     * @return A {@link AbstractTickableHandler} id
     */
    @Nonnull
    String handlerId() default "Bukkit";
}
