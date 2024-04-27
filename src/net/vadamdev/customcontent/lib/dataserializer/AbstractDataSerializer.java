package net.vadamdev.customcontent.lib.dataserializer;

import net.vadamdev.customcontent.api.blocks.serialization.IDataSerializer;
import net.vadamdev.customcontent.internal.CustomContentPlugin;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Abstract implementation of a {@link IDataSerializer}. It only provides a cooldown between each save
 * <p>This cooldown is mandatory because otherwise CCL will write in your datafile every time a block is placed / broken
 *
 * @author VadamDev
 * @since 08/12/2023
 */
public abstract class AbstractDataSerializer implements IDataSerializer {
    private final long delay;

    private final ScheduledExecutorService executorService;
    private ScheduledFuture<?> saveTask;

    public AbstractDataSerializer(long delay) {
        this.delay = delay;

        this.executorService = Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    public void save(boolean force) {
        if(force) {
            if(saveTask == null || saveTask.isDone() || saveTask.cancel(false)) {
                save();
            }else
                CustomContentPlugin.instance.getLogger().severe("The data was unable to be saved");
        }else if(saveTask == null)
            saveTask = executorService.schedule(() -> save(), delay, TimeUnit.MILLISECONDS);
    }

    protected abstract void save();
}
