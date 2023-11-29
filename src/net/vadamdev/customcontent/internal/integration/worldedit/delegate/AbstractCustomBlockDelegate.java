package net.vadamdev.customcontent.internal.integration.worldedit.delegate;

import com.sk89q.jnbt.CompoundTag;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.event.extent.EditSessionEvent;
import com.sk89q.worldedit.extent.AbstractDelegateExtent;
import net.vadamdev.customcontent.api.blocks.CustomBlock;
import net.vadamdev.customcontent.internal.registry.BlocksRegistry;
import net.vadamdev.customcontent.lib.BlockPos;
import org.bukkit.Bukkit;
import org.bukkit.World;

/**
 * @author VadamDev
 * @since 28/11/2023
 */
public abstract class AbstractCustomBlockDelegate extends AbstractDelegateExtent {
    private final BlocksRegistry blocksRegistry;
    private final World world;

    protected AbstractCustomBlockDelegate(EditSessionEvent event, BlocksRegistry blocksRegistry) {
        super(event.getExtent());

        this.blocksRegistry = blocksRegistry;
        this.world = Bukkit.getWorld(event.getWorld().getName());
    }

    @Override
    public boolean setBlock(Vector location, BaseBlock block) throws WorldEditException {
        final BlockPos blockPos = new BlockPos(world, location.getBlockX(), location.getBlockY(), location.getBlockZ());
        breakCustomBlock(blockPos);

        final CompoundTag data = block.getNbtData();
        if (data != null && data.containsKey("CCL-RegistryName")) {
            final String registryName = data.getString("CCL-RegistryName");

            for(CustomBlock customBlock : blocksRegistry.getCustomBlocks()) {
                if(customBlock.getRegistryName().equals(registryName)) {
                    placeCustomBlock(blockPos, customBlock);
                    return true;
                }
            }
        }

        return super.setBlock(location, block);
    }

    protected abstract void placeCustomBlock(BlockPos blockPos, CustomBlock customBlock);
    protected abstract void breakCustomBlock(BlockPos blockPos);
}
