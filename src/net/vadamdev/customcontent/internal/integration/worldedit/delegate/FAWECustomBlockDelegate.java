package net.vadamdev.customcontent.internal.integration.worldedit.delegate;

import com.sk89q.worldedit.event.extent.EditSessionEvent;
import net.vadamdev.customcontent.api.blocks.CustomBlock;
import net.vadamdev.customcontent.internal.CustomContentPlugin;
import net.vadamdev.customcontent.internal.handlers.blocks.BlocksHandler;
import net.vadamdev.customcontent.internal.registry.BlocksRegistry;
import net.vadamdev.customcontent.lib.BlockPos;
import org.bukkit.Bukkit;

/**
 * @author VadamDev
 * @since 28/11/2023
 */
public class FAWECustomBlockDelegate extends AbstractCustomBlockDelegate {
    private final BlocksHandler blocksHandler;

    public FAWECustomBlockDelegate(EditSessionEvent event, BlocksRegistry blocksRegistry, BlocksHandler blocksHandler) {
        super(event, blocksRegistry);

        this.blocksHandler = blocksHandler;
    }

    @Override
    protected void placeCustomBlock(BlockPos blockPos, CustomBlock customBlock) {
        Bukkit.getScheduler().runTask(CustomContentPlugin.instance, () ->
                blocksHandler.placeCustomBlock(blockPos, customBlock, false, null, null));
    }

    @Override
    protected void breakCustomBlock(BlockPos blockPos) {
        Bukkit.getScheduler().runTask(CustomContentPlugin.instance, () ->
                blocksHandler.breakCustomBlock(blockPos, false, false, null));
    }
}
