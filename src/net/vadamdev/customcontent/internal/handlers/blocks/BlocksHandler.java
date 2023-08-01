package net.vadamdev.customcontent.internal.handlers.blocks;

import net.vadamdev.customcontent.api.blocks.*;
import net.vadamdev.customcontent.api.common.tickable.ITickable;
import net.vadamdev.customcontent.internal.CustomContentPlugin;
import net.vadamdev.customcontent.internal.impl.CustomContentAPIImpl;
import net.vadamdev.customcontent.internal.registry.BlocksRegistry;
import net.vadamdev.customcontent.lib.BlockFlags;
import net.vadamdev.customcontent.lib.BlockPos;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.libs.jline.internal.Nullable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * @author VadamDev
 * @since 05/09/2022
 */
public class BlocksHandler implements Listener {
    private final CustomContentAPIImpl customContentAPI;

    private final BlocksRegistry blocksRegistry;
    private final TileEntityHandler tileEntityHandler;
    private final CustomTextureHandler customTextureHandler;

    private final Map<BlockPos, CustomBlock> customBlocks;

    public BlocksHandler() {
        this.customContentAPI = CustomContentPlugin.instance.getCustomContentAPI();

        this.blocksRegistry = CustomContentPlugin.instance.getBlocksRegistry();
        this.tileEntityHandler = CustomContentPlugin.instance.getTileEntityHandler();
        this.customTextureHandler = CustomContentPlugin.instance.getCustomTextureHandler();

        this.customBlocks = new HashMap<>();
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        final ItemStack itemStack = event.getItemInHand();
        if(itemStack == null)
            return;

        blocksRegistry.getCustomBlocks().stream()
                .filter(customBlock -> customContentAPI.isCustomItem(itemStack, customBlock.getRegistryName()))
                .findFirst().ifPresent(customBlock -> {
                    placeCustomBlock(new BlockPos(event.getBlock().getLocation()), customBlock, true, event.getPlayer(), event);
                });
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        final BlockPos blockPos = new BlockPos(event.getBlock());

        findCustomBlock(blockPos).ifPresent(customBlock -> {
            event.setCancelled(true);

            if(customBlock instanceof IBlockFlagHolder && hasBlockFlag((IBlockFlagHolder) customBlock, BlockFlags.UNBREAKABLE))
                return;

            breakCustomBlock(blockPos, customBlock, true, !event.getPlayer().getGameMode().equals(GameMode.CREATIVE), event.getPlayer());
        });
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockExplode(EntityExplodeEvent event) {
        Set<Block> toRemove = new HashSet<>();
        for (Block block : event.blockList()) {
            final BlockPos blockPos = new BlockPos(block);

            findCustomBlock(blockPos).ifPresent(customBlock -> {
                if(customBlock instanceof IBlockFlagHolder && hasBlockFlag((IBlockFlagHolder) customBlock, BlockFlags.TNT_RESISTANT, BlockFlags.UNBREAKABLE)) {
                    toRemove.add(block);
                    return;
                }

                breakCustomBlock(blockPos, customBlock, true, true, event.getEntity());
                toRemove.add(block);
            });
        }

        event.blockList().removeAll(toRemove);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(!event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
            return;

        final BlockPos blockPos = new BlockPos(event.getClickedBlock());
        findCustomBlock(blockPos).ifPresent(customBlock -> {
            boolean flag = customBlock.onInteract(event.getClickedBlock(), blockPos, event.getPlayer());

            if(flag)
                event.setCancelled(true);
        });
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPistonExtend(BlockPistonExtendEvent event) {
        if(event.getBlocks().stream().anyMatch(block -> findCustomBlock(new BlockPos(block)).isPresent())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPistonRetract(BlockPistonRetractEvent event) {
        if(event.getBlocks().stream().anyMatch(block -> findCustomBlock(new BlockPos(block)).isPresent())) {
            event.setCancelled(true);
        }
    }

    public boolean placeCustomBlock(BlockPos blockPos, CustomBlock customBlock, boolean checkValidity, @Nullable Player player, @Nullable Cancellable event) {
        if(checkValidity && !customBlock.canPlace(blockPos, player)) {
            if(event != null)
                event.setCancelled(true);

            return false;
        }

        customBlock.getDataSerializer().write(blockPos);

        if(customBlock instanceof ITileEntityProvider)
            tileEntityHandler.addTileEntity(blockPos, customBlock, ((ITileEntityProvider) customBlock).createTileEntity(blockPos));

        if(customBlock instanceof ICustomTextureHolder) {
            ICustomTextureHolder textureHolder = (ICustomTextureHolder) customBlock;
            customTextureHandler.addCustomTexture(blockPos, textureHolder.getTextureName(), textureHolder.getBlockRotation(player));
        }

        final Block block = blockPos.getBlock();
        block.setType(customBlock.getBlockMaterial());

        customBlock.onPlace(block, blockPos, player);

        customBlocks.put(blockPos, customBlock);

        return true;
    }

    public boolean breakCustomBlock(BlockPos blockPos, boolean checkValidity, boolean drop, @Nullable Entity entity) {
        final CustomBlock customBlock = findCustomBlock(blockPos).orElse(null);
        if(customBlock == null)
            return false;

        return breakCustomBlock(blockPos, customBlock, checkValidity, drop, entity);
    }

    private boolean breakCustomBlock(BlockPos blockPos, CustomBlock customBlock, boolean checkValidity, boolean drop, @Nullable Entity entity) {
        if(checkValidity && !customBlock.canBreak(blockPos, entity))
            return false;

        CustomTileEntity tileEntity = tileEntityHandler.getTileEntityAt(blockPos).orElse(null);

        customBlock.onBreak(blockPos, tileEntity, entity);

        if(tileEntity != null)
            tileEntityHandler.removeTileEntity(blockPos, tileEntity instanceof ITickable);

        if(customBlock instanceof ICustomTextureHolder)
            customTextureHandler.removeCustomTexture(blockPos);

        if(customBlock.getDataSerializer().contains(blockPos))
            customBlock.getDataSerializer().remove(blockPos);

        customBlocks.remove(blockPos);

        final Block block = blockPos.getBlock();
        block.setType(Material.AIR);

        if(drop)
            block.getWorld().dropItemNaturally(block.getLocation().clone().add(0.5f, 0.25f, 0.5f), customBlock.getItemStack());

        return true;
    }

    private Optional<CustomBlock> findCustomBlock(BlockPos blockPos) {
        return Optional.ofNullable(customBlocks.get(blockPos));
    }

    private boolean hasBlockFlag(IBlockFlagHolder blockFlagOwner, BlockFlags... flags) {
        for (BlockFlags flag1 : blockFlagOwner.getFlags()) for (BlockFlags flag2 : flags) {
            if(flag1.equals(flag2))
                return true;
        }

        return false;
    }

    public void loadAll(BlocksRegistry blocksRegistry) {
        for(CustomBlock customBlock : blocksRegistry.getCustomBlocks()) {
            customBlock.getDataSerializer().readAll().forEach((blockPos, compound) -> {
                customBlocks.put(blockPos, customBlock);

                if(customBlock instanceof ITileEntityProvider) {
                    CustomTileEntity tileEntity = ((ITileEntityProvider) customBlock).createTileEntity(blockPos);
                    tileEntity.load(compound);

                    tileEntityHandler.addTileEntity(blockPos, customBlock, tileEntity);
                }

                if(customBlock instanceof ICustomTextureHolder) {
                    final int rot = compound.containsKey("rotation") ? compound.getInt("rotation") : 0;
                    customTextureHandler.addCustomTexture(blockPos, ((ICustomTextureHolder) customBlock).getTextureName(), rot);
                }
            });
        }
    }
}
