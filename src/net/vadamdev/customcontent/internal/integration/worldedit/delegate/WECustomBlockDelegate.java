package net.vadamdev.customcontent.internal.integration.worldedit.delegate;

import com.sk89q.worldedit.event.extent.EditSessionEvent;
import net.vadamdev.customcontent.api.blocks.CustomBlock;
import net.vadamdev.customcontent.internal.handlers.blocks.BlocksHandler;
import net.vadamdev.customcontent.internal.registry.BlocksRegistry;
import net.vadamdev.customcontent.lib.BlockPos;

/**
 * @author VadamDev
 * @since 28/11/2023
 */
public class WECustomBlockDelegate extends AbstractCustomBlockDelegate {
    private final BlocksHandler blocksHandler;

    public WECustomBlockDelegate(EditSessionEvent event, BlocksRegistry blocksRegistry, BlocksHandler blocksHandler) {
        super(event, blocksRegistry);

        this.blocksHandler = blocksHandler;
    }

    @Override
    protected void placeCustomBlock(BlockPos blockPos, CustomBlock customBlock) {
        blocksHandler.placeCustomBlock(blockPos, customBlock, false, null, null);
    }

    @Override
    protected void breakCustomBlock(BlockPos blockPos) {
        blocksHandler.breakCustomBlock(blockPos, false, false, null);
    }
}
