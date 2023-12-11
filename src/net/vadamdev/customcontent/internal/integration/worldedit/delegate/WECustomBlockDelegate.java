package net.vadamdev.customcontent.internal.integration.worldedit.delegate;

import com.sk89q.worldedit.event.extent.EditSessionEvent;
import net.vadamdev.customcontent.api.blocks.CustomBlock;
import net.vadamdev.customcontent.internal.handlers.blocks.BlocksHandler;
import net.vadamdev.customcontent.internal.registry.BlocksRegistry;
import net.vadamdev.customcontent.lib.BlockPos;

import java.util.concurrent.CompletableFuture;

/**
 * @author VadamDev
 * @since 28/11/2023
 */
public class WECustomBlockDelegate extends AbstractCustomBlockDelegate {
    public WECustomBlockDelegate(EditSessionEvent event, BlocksRegistry blocksRegistry, BlocksHandler blocksHandler) {
        super(event, blocksRegistry, blocksHandler);
    }

    @Override
    protected CompletableFuture<Boolean> processBlockSet(BlockPos blockPos, CustomBlock customBlock) {
        breakCustomBlock(blockPos);

        boolean result = false;
        if(customBlock != null) {
            placeCustomBlock(blockPos, customBlock);
            result = true;
        }

        return CompletableFuture.completedFuture(result);
    }
}
