package net.vadamdev.customcontent.internal.handlers.blocks;

import net.vadamdev.customcontent.annotations.TickableInfo;
import net.vadamdev.customcontent.api.blocks.CustomTileEntity;
import net.vadamdev.customcontent.api.common.tickable.ITickable;
import net.vadamdev.customcontent.lib.BlockPos;

import java.util.HashMap;
import java.util.Map;

public class TickableTileEntityHandler {
    private final Map<Class<? extends CustomTileEntity>, TickableExecutor> executorPool;

    public TickableTileEntityHandler() {
        this.executorPool = new HashMap<>();
    }

    public void sumbit(CustomTileEntity tileEntity) {
        findExecutor(tileEntity.getClass())
                .submit(tileEntity.blockPos, (ITickable) tileEntity);
    }

    public void remove(BlockPos blockPos) {
        executorPool.entrySet().parallelStream()
                .filter(entry -> entry.getValue().contains(blockPos))
                .findFirst().ifPresent(entry -> {
                    final TickableExecutor executor = entry.getValue();

                    final boolean shouldDelete = executor.remove(blockPos);
                    if(shouldDelete) {
                        executor.shutdown();
                        executorPool.remove(entry.getKey());
                    }
                });
    }

    private TickableExecutor findExecutor(Class<? extends CustomTileEntity> clazz) {
        return executorPool.computeIfAbsent(clazz, c -> {
            int interval = 1;
            boolean async = false;

            final TickableInfo info = clazz.getAnnotation(TickableInfo.class);
            if(info != null) {
                interval = info.interval();
                async = info.async();
            }

            return new TickableExecutor(interval, async);
        });
    }
}
