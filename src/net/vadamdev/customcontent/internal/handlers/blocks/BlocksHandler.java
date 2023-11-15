package net.vadamdev.customcontent.internal.handlers.blocks;

import net.vadamdev.customcontent.api.blocks.*;
import net.vadamdev.customcontent.api.blocks.serialization.SerializableDataCompound;
import net.vadamdev.customcontent.api.common.tickable.ITickable;
import net.vadamdev.customcontent.internal.CustomContentPlugin;
import net.vadamdev.customcontent.internal.handlers.blocks.textures.CustomTextureHandler;
import net.vadamdev.customcontent.internal.impl.CustomContentAPIImpl;
import net.vadamdev.customcontent.internal.registry.BlocksRegistry;
import net.vadamdev.customcontent.lib.BlockFlags;
import net.vadamdev.customcontent.lib.BlockPos;
import net.vadamdev.viapi.tools.enums.EnumDirection;
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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 * @author VadamDev
 * @since 05/09/2022
 */
public class BlocksHandler implements Listener {
    public CustomContentAPIImpl customContentAPI;

    private final BlocksRegistry blocksRegistry;
    private final TileEntityHandler tileEntityHandler;
    private final CustomTextureHandler customTextureHandler;

    private final Map<BlockPos, CustomBlock> customBlocks;

    private boolean loaded;

    public BlocksHandler(BlocksRegistry blocksRegistry, TileEntityHandler tileEntityHandler, CustomTextureHandler customTextureHandler) {
        this.blocksRegistry = blocksRegistry;
        this.tileEntityHandler = tileEntityHandler;
        this.customTextureHandler = customTextureHandler;

        this.customBlocks = new HashMap<>();

        this.loaded = false;
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

        if(!event.blockList().removeAll(toRemove))
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        final Action action = event.getAction();
        if(!action.equals(Action.LEFT_CLICK_BLOCK) && !action.equals(Action.RIGHT_CLICK_BLOCK))
            return;

        final BlockPos blockPos = new BlockPos(event.getClickedBlock());
        findCustomBlock(blockPos).ifPresent(customBlock -> {
            boolean flag;

            if(action.equals(Action.RIGHT_CLICK_BLOCK))
                flag = customBlock.onInteract(event.getClickedBlock(), blockPos, event.getPlayer());
            else
                flag = customBlock.tryBreak(event.getClickedBlock(), blockPos, event.getPlayer());

            if(flag)
                event.setCancelled(true);
        });
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPistonExtend(BlockPistonExtendEvent event) {
        if(event.getBlocks().stream().anyMatch(block -> findCustomBlock(new BlockPos(block)).isPresent()))
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPistonRetract(BlockPistonRetractEvent event) {
        if(event.getBlocks().stream().anyMatch(block -> findCustomBlock(new BlockPos(block)).isPresent()))
            event.setCancelled(true);
    }

    public boolean placeCustomBlock(BlockPos blockPos, CustomBlock customBlock, boolean checkValidity, @Nullable Player player, @Nullable Cancellable event) {
        if(!loaded) {
            CustomContentPlugin.instance.getLogger().warning("Someone tried to place a block before CCL finished loading !");

            if(event != null)
                event.setCancelled(true);

            return false;
        }

        if(checkValidity && !customBlock.canPlace(blockPos, player)) {
            if(event != null)
                event.setCancelled(true);

            return false;
        }

        final SerializableDataCompound compound = new SerializableDataCompound();

        if(customBlock instanceof ITileEntityProvider)
            tileEntityHandler.addTileEntity(blockPos, customBlock, ((ITileEntityProvider) customBlock).createTileEntity(blockPos));

        final Block block = blockPos.getBlock();

        if(customBlock instanceof ICustomTextureHolder) {
            final ICustomTextureHolder textureHolder = (ICustomTextureHolder) customBlock;
            final EnumDirection direction = textureHolder.getBlockRotation(blockPos, player);

            customTextureHandler.addCustomTexture(blockPos, textureHolder.createTextureIcon(textureHolder.getDefaultTexture()), direction);
            compound.putString("direction", direction.name());

            block.setType(Material.BARRIER);
        }else
            block.setType(customBlock.getBlockMaterial());

        customBlock.onPlace(block, blockPos, player);

        customBlock.getDataSerializer().write(blockPos, compound);
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
        if(!loaded) {
            CustomContentPlugin.instance.getLogger().warning("Someone tried to break a block before CCL finished loading !");
            return false;
        }

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

    public void loadAll(BlocksRegistry blocksRegistry, Logger logger) {
        final long before = System.nanoTime();

        final AtomicInteger i = new AtomicInteger();
        final AtomicInteger j = new AtomicInteger();
        for(CustomBlock customBlock : blocksRegistry.getCustomBlocks()) {
            customBlock.getDataSerializer().readAll().forEach((blockPos, compound) -> {
                customBlocks.put(blockPos, customBlock);

                if(customBlock instanceof ICustomTextureHolder) {
                    final ICustomTextureHolder textureHolder = (ICustomTextureHolder) customBlock;

                    final EnumDirection direction = compound.containsKey("direction") ? EnumDirection.valueOf(compound.getString("direction")) : EnumDirection.SOUTH;
                    customTextureHandler.addCustomTexture(blockPos, textureHolder.createTextureIcon(textureHolder.getDefaultTexture()), direction);
                }

                if(customBlock instanceof ITileEntityProvider) {
                    CustomTileEntity tileEntity = ((ITileEntityProvider) customBlock).createTileEntity(blockPos);
                    tileEntity.load(compound);

                    tileEntityHandler.addTileEntity(blockPos, customBlock, tileEntity);
                    j.getAndIncrement();
                }

                i.getAndIncrement();
            });
        }

        logger.info("-> Unserialized " + i.get() + " custom blocks and " + j.get() + " custom tile entities (Took: " + (System.nanoTime() - before) / 1000000D + " ms)");

        loaded = true;
    }
}
