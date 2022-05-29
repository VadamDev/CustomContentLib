package net.vadamdev.customcontent.api.INDEV.plants;

import net.vadamdev.customcontent.api.ITickable;

/**
 * @author VadamDev
 * @since 23/04/2022
 */
public class CustomPlant implements ITickable {


    @Override
    public void tick() {

    }

    @Override
    public int getInterval() {
        return 20;
    }

    @Override
    public boolean isTickAsync() {
        return true;
    }
}
