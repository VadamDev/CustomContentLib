package net.vadamdev.customcontent.internal.handlers;

import net.vadamdev.customcontent.CustomContentLib;
import net.vadamdev.customcontent.api.blocks.CustomBlock;
import net.vadamdev.customcontent.api.blocks.CustomTileEntity;
import net.vadamdev.customcontent.internal.BlocksRegistry;
import net.vadamdev.customcontent.internal.CommonRegistry;
import net.vadamdev.customcontent.lib.BlockPos;
import net.vadamdev.customcontent.lib.CustomContentRegistry;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author VadamDev
 * @since 05/09/2022
 */
public class BlocksHandler implements Listener {
    private final CommonRegistry commonRegistry;
    private final BlocksRegistry blocksRegistry;

    public BlocksHandler() {
        this.commonRegistry = CustomContentLib.instance.getCommonRegistry();
        this.blocksRegistry = CustomContentLib.instance.getBlocksRegistry();
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if(event.isCancelled())
            return;

        ItemStack itemStack = event.getItemInHand();

        if(itemStack == null || itemStack.getType().equals(Material.AIR))
            return;

        AtomicBoolean flag = new AtomicBoolean(false);

        commonRegistry.getCustomItems().stream()
                .filter(CustomBlock.class::isInstance)
                .filter(customItem -> CustomContentRegistry.isCustomItem(itemStack, customItem.getRegistryName()))
                .forEach(registrable -> {
                    CustomBlock customBlock = (CustomBlock) registrable;
                    flag.set(customBlock.onPlace(event.getBlock(), new BlockPos(event.getBlock().getLocation()), event.getPlayer()));
                });

        if(flag.get())
            event.setCancelled(true);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if(event.isCancelled())
            return;

        BlockPos blockPos = new BlockPos(event.getBlock());
        AtomicBoolean flag = new AtomicBoolean(false);

        Optional<CustomTileEntity> tileEntity = blocksRegistry.getTileEntityAt(blockPos);

        findCustomBlock(blockPos).ifPresent(customBlock -> flag.set(customBlock.onBreak(blockPos, tileEntity.orElse(null), event.getPlayer().getItemInHand())));

        if(flag.get())
            event.setCancelled(true);
    }

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event) {
        if(event.isCancelled())
            return;

        Set<Block> toRemove = new HashSet<>();

        for (Block block : event.blockList()) {
            BlockPos blockPos = new BlockPos(block);
            Optional<CustomBlock> customBlock = findCustomBlock(blockPos);

            if(customBlock.isPresent()) {
                Optional<CustomTileEntity> tileEntity = blocksRegistry.getTileEntityAt(blockPos);

                if(customBlock.get().onBreak(blockPos, tileEntity.orElse(null), null))
                    toRemove.add(block);
            }
        }

        event.blockList().removeAll(toRemove);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(event.isCancelled())
            return;

        if(!event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
            return;

        BlockPos blockpos = new BlockPos(event.getClickedBlock());
        AtomicBoolean flag = new AtomicBoolean(false);

        findCustomBlock(blockpos).ifPresent(customBlock -> flag.set(customBlock.onInteract(event.getClickedBlock(), blockpos, event.getPlayer())));

        if(flag.get())
            event.setCancelled(true);
    }

    private Optional<CustomBlock> findCustomBlock(BlockPos blockPos) {
        return blocksRegistry.getCustomBlocks().stream().filter(customBlock -> customBlock.getDataSerializer().contains(blockPos)).findAny();
    }
}
