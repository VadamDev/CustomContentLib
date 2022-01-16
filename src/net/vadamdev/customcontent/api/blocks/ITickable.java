package net.vadamdev.customcontent.api.blocks;

public interface ITickable {
    void tick();
    int getInterval();

    default boolean shouldTickAtSameTime() {
        return true;
    }
}
