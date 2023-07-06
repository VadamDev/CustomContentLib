package net.vadamdev.customcontent.internal.handlers;

import net.vadamdev.customcontent.CustomContentLib;
import net.vadamdev.customcontent.api.blocks.*;
import net.vadamdev.customcontent.internal.BlocksRegistry;
import net.vadamdev.customcontent.internal.CommonRegistry;
import net.vadamdev.customcontent.lib.BlockFlags;
import net.vadamdev.customcontent.lib.BlockPos;
import net.vadamdev.customcontent.lib.CustomContentRegistry;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author VadamDev
 * @since 05/09/2022
 */
public class BlocksHandler implements Listener {
    private final CommonRegistry commonRegistry;
    private final BlocksRegistry blocksRegistry;
    private final CustomTextureHandler customTextureHandler;

    private final Map<BlockPos, CustomBlock> customBlocks;

    public BlocksHandler() {
        this.commonRegistry = CustomContentLib.instance.getCommonRegistry();
        this.blocksRegistry = CustomContentLib.instance.getBlocksRegistry();
        this.customTextureHandler = CustomContentLib.instance.getCustomTextureHandler();

        this.customBlocks = new HashMap<>();
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
                    BlockPos blockPos = new BlockPos(event.getBlock().getLocation());

                    if(!customBlock.canPlace(blockPos)) {
                        event.setCancelled(true);
                        return;
                    }

                    customBlock.getDataSerializer().write(blockPos);

                    if(customBlock instanceof ITileEntityProvider)
                        CustomContentLib.instance.getTileEntityHandler().addTileEntity(blockPos, customBlock, ((ITileEntityProvider) customBlock).createTileEntity(blockPos));

                    if(customBlock instanceof ICustomTextureHolder) {
                        ICustomTextureHolder textureHolder = (ICustomTextureHolder) customBlock;
                        customTextureHandler.addCustomTexture(blockPos, textureHolder.getTextureName(), textureHolder.getBlockRotation(event.getPlayer()));
                    }

                    flag.set(customBlock.onPlace(event.getBlock(), blockPos, event.getPlayer()));

                    customBlocks.put(blockPos, customBlock);
                });

        if(flag.get())
            event.setCancelled(true);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if(event.isCancelled())
            return;

        Block block = event.getBlock();
        BlockPos blockPos = new BlockPos(block);

        findCustomBlock(blockPos).ifPresent(customBlock -> {
            if(customBlock instanceof IBlockFlagHolder && hasBlockFlag((IBlockFlagHolder) customBlock, BlockFlags.UNBREAKABLE)) {
                event.setCancelled(true);
                return;
            }

            CustomTileEntity tileEntity = blocksRegistry.getTileEntityAt(blockPos).orElse(null);

            boolean b = customBlock.onBreak(blockPos, tileEntity, event.getPlayer().getItemInHand());
            if(b) {
                event.setCancelled(true);
                return;
            }

            if(tileEntity != null)
                CustomContentLib.instance.getTileEntityHandler().removeTileEntity(blockPos);

            if(customBlock instanceof ICustomTextureHolder)
                customTextureHandler.removeCustomTexture(blockPos);

            if(customBlock.getDataSerializer().contains(blockPos))
                customBlock.getDataSerializer().remove(blockPos);

            customBlocks.remove(blockPos);

            event.setCancelled(true);
            block.setType(Material.AIR);

            if(!event.getPlayer().getGameMode().equals(GameMode.CREATIVE))
                block.getWorld().dropItemNaturally(block.getLocation().clone().add(0.5f, 0.25f, 0.5f), customBlock.getItemStack());
        });
    }

    @EventHandler
    public void onBlockExplode(EntityExplodeEvent event) {
        if(event.isCancelled())
            return;

        Set<Block> toRemove = new HashSet<>();
        for (Block block : event.blockList()) {
            BlockPos blockPos = new BlockPos(block);
            Optional<CustomBlock> optionalCustomBlock = findCustomBlock(blockPos);

            if(optionalCustomBlock.isPresent()) {
                CustomBlock customBlock = optionalCustomBlock.get();

                if(customBlock instanceof IBlockFlagHolder && hasBlockFlag((IBlockFlagHolder) customBlock, BlockFlags.TNT_RESISTANT, BlockFlags.UNBREAKABLE)) {
                    toRemove.add(block);
                    return;
                }

                CustomTileEntity tileEntity = blocksRegistry.getTileEntityAt(blockPos).orElse(null);

                if(customBlock.onBreak(blockPos, tileEntity, null)) {
                    toRemove.add(block);
                    return;
                }

                if(tileEntity != null)
                    CustomContentLib.instance.getTileEntityHandler().removeTileEntity(blockPos);

                if(customBlock instanceof ICustomTextureHolder)
                    customTextureHandler.removeCustomTexture(blockPos);

                if(customBlock.getDataSerializer().contains(blockPos))
                    customBlock.getDataSerializer().remove(blockPos);

                customBlocks.remove(blockPos);

                block.setType(Material.AIR);
                block.getWorld().dropItemNaturally(block.getLocation().clone().add(0.5f, 0.25f, 0.5f), customBlock.getItemStack());
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

    @EventHandler
    public void onPistonExtend(BlockPistonExtendEvent event) {
        if(event.isCancelled())
            return;

        event.getBlocks().forEach(block -> findCustomBlock(new BlockPos(block)).ifPresent(customBlock -> {
            if(customBlock instanceof IBlockFlagHolder && hasBlockFlag((IBlockFlagHolder) customBlock, BlockFlags.PISTON_INVULNERABLE)) {
                event.setCancelled(true);
            }
        }));
    }

    @EventHandler
    public void onPistonRetract(BlockPistonRetractEvent event) {
        if(event.isCancelled())
            return;

        event.getBlocks().forEach(block -> findCustomBlock(new BlockPos(block)).ifPresent(customBlock -> {
            if(customBlock instanceof IBlockFlagHolder && hasBlockFlag((IBlockFlagHolder) customBlock, BlockFlags.PISTON_INVULNERABLE)) {
                event.setCancelled(true);
            }
        }));
    }

    private Optional<CustomBlock> findCustomBlock(BlockPos blockPos) {
        return Optional.ofNullable(customBlocks.get(blockPos));
    }

    private static boolean hasBlockFlag(IBlockFlagHolder blockFlagOwner, BlockFlags... flags) {
        for (BlockFlags flag1 : blockFlagOwner.getFlags()) for (BlockFlags flag2 : flags) {
            if(flag1.equals(flag2))
                return true;
        }

        return false;
    }

    public void loadAll(BlocksRegistry blocksRegistry) {
        blocksRegistry.getCustomBlocks().forEach(customBlock ->
                customBlock.getDataSerializer().readAll().keySet().forEach(blockPos -> {
                    customBlocks.put(blockPos, customBlock);

                    if(customBlock instanceof ICustomTextureHolder) {
                        ICustomTextureHolder textureHolder = (ICustomTextureHolder) customBlock;
                        customTextureHandler.addCustomTexture(blockPos, textureHolder.getTextureName(), 0); //TODO: ROTATION SERIALIZATION
                    }
                })
        );
    }
}
