package net.vadamdev.customcontent.api;

/**
 * @author VadamDev
 * @since 18/01/2022
 */
public interface ITickable {
    void tick();

    /**
     * @return interval in minecraft ticks
     */
    int getInterval();

    /**
     * If you're using that be careful spigot have a lot of issues with async tasks.
     */
    default boolean isTickAsync() {
        return false;
    }
}
