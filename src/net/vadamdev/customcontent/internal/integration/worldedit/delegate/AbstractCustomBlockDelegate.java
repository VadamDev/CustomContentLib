package net.vadamdev.customcontent.internal.integration.worldedit.delegate;

import com.sk89q.jnbt.CompoundTag;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.event.extent.EditSessionEvent;
import com.sk89q.worldedit.extent.AbstractDelegateExtent;
import net.vadamdev.customcontent.api.blocks.CustomBlock;
import net.vadamdev.customcontent.internal.handlers.blocks.BlocksHandler;
import net.vadamdev.customcontent.internal.registry.BlocksRegistry;
import net.vadamdev.customcontent.lib.BlockPos;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author VadamDev
 * @since 28/11/2023
 */
public abstract class AbstractCustomBlockDelegate extends AbstractDelegateExtent {
    private final BlocksRegistry blocksRegistry;
    private final BlocksHandler blocksHandler;
    private final World world;

    public AbstractCustomBlockDelegate(EditSessionEvent event, BlocksRegistry blocksRegistry, BlocksHandler blocksHandler) {
        super(event.getExtent());

        this.blocksRegistry = blocksRegistry;
        this.blocksHandler = blocksHandler;
        this.world = Bukkit.getWorld(event.getWorld().getName());
    }

    @Override
    public boolean setBlock(Vector location, BaseBlock block) throws WorldEditException {
        try {
            if(processBlockSet(new BlockPos(world, location.getBlockX(), location.getBlockY(), location.getBlockZ()), findCustomBlockInTag(block)).get()) {
                return true;
            }
        }catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return super.setBlock(location, block);
    }

    protected abstract CompletableFuture<Boolean> processBlockSet(BlockPos blockPos, CustomBlock customBlock);

    protected void placeCustomBlock(BlockPos blockPos, CustomBlock customBlock) {
        blocksHandler.placeCustomBlock(blockPos, customBlock, false, null, null);
    }

    protected void breakCustomBlock(BlockPos blockPos) {
        blocksHandler.breakCustomBlock(blockPos, false, false, null);
    }

    private CustomBlock findCustomBlockInTag(BaseBlock block) {
        final CompoundTag data = block.getNbtData();
        if (data != null && data.containsKey("CCL-RegistryName")) {
            final String registryName = data.getString("CCL-RegistryName");

            for(CustomBlock customBlock : blocksRegistry.getCustomBlocks()) {
                if(customBlock.getRegistryName().equals(registryName)) {
                    return customBlock;
                }
            }
        }

        return null;
    }
}
