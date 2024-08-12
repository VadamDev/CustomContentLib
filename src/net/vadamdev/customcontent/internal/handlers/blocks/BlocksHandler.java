package net.vadamdev.customcontent.internal.handlers.blocks;

import net.vadamdev.customcontent.api.blocks.CustomBlock;
import net.vadamdev.customcontent.api.blocks.CustomTileEntity;
import net.vadamdev.customcontent.api.blocks.IBlockFlagHolder;
import net.vadamdev.customcontent.api.blocks.ITileEntityProvider;
import net.vadamdev.customcontent.api.blocks.serialization.IDataSerializer;
import net.vadamdev.customcontent.api.blocks.texture.ICustomTextureHolder;
import net.vadamdev.customcontent.api.common.tickable.ITickable;
import net.vadamdev.customcontent.internal.CustomContentPlugin;
import net.vadamdev.customcontent.internal.handlers.blocks.textures.CustomTextureHandler;
import net.vadamdev.customcontent.internal.handlers.blocks.tileentity.TileEntityHandler;
import net.vadamdev.customcontent.internal.impl.CustomContentAPIImpl;
import net.vadamdev.customcontent.internal.registry.BlocksRegistry;
import net.vadamdev.customcontent.lib.BlockPos;
import net.vadamdev.customcontent.lib.blocks.BlockDirection;
import net.vadamdev.customcontent.lib.blocks.BlockFlags;
import net.vadamdev.customcontent.lib.serialization.SerializableDataCompound;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.libs.jline.internal.Nullable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
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

        this.customBlocks = new ConcurrentHashMap<>();

        this.loaded = false;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        final ItemStack itemStack = event.getItemInHand();
        if(itemStack == null || !itemStack.hasItemMeta())
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
        final Set<Block> toRemove = new HashSet<>();
        for (Block block : event.blockList()) {
            final BlockPos blockPos = new BlockPos(block);

            findCustomBlock(blockPos).ifPresent(customBlock -> {
                toRemove.add(block);

                if(customBlock instanceof IBlockFlagHolder && hasBlockFlag((IBlockFlagHolder) customBlock, BlockFlags.TNT_RESISTANT, BlockFlags.UNBREAKABLE))
                    return;

                breakCustomBlock(blockPos, customBlock, true, true, event.getEntity());
            });
        }

        if(!toRemove.isEmpty())
            event.blockList().removeAll(toRemove);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        final Action action = event.getAction();
        if(!action.equals(Action.LEFT_CLICK_BLOCK) && !action.equals(Action.RIGHT_CLICK_BLOCK))
            return;

        final BlockPos blockPos = new BlockPos(event.getClickedBlock());
        findCustomBlock(blockPos).ifPresent(customBlock -> {
            final Player player = event.getPlayer();

            boolean shouldCancel;

            if(action.equals(Action.RIGHT_CLICK_BLOCK))
                shouldCancel = customBlock.onInteract(event.getClickedBlock(), blockPos, event.getBlockFace(), player);
            else {
                shouldCancel = customBlock.tryBreak(event.getClickedBlock(), blockPos, event.getBlockFace(), player);

                if(!shouldCancel && player.getGameMode().equals(GameMode.SURVIVAL))
                    breakCustomBlock(blockPos, customBlock, true, true, player);
            }

            if(shouldCancel)
                event.setCancelled(true);
        });
    }

    //TODO: check if it works
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInventoryMoveItem(InventoryMoveItemEvent event) {
        final InventoryHolder holder = event.getDestination().getHolder();
        if(!(holder instanceof BlockState))
            return;

        if(findCustomBlock(new BlockPos(((BlockState) holder).getLocation())).isPresent())
            event.setCancelled(true);
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

    public synchronized boolean placeCustomBlock(BlockPos blockPos, CustomBlock customBlock, boolean checkValidity, @Nullable Player player, @Nullable Cancellable event) {
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
        final Block block = blockPos.getBlock();

        if(customBlock instanceof ICustomTextureHolder) {
            final ICustomTextureHolder textureHolder = (ICustomTextureHolder) customBlock;

            customTextureHandler.createCustomTexture(
                    blockPos,
                    textureHolder,
                    player != null ? textureHolder.getBlockDirection(blockPos, player) : BlockDirection.SOUTH,
                    compound
            );

            block.setType(Material.BARRIER);
        }else
            block.setType(customBlock.getBlockMaterial());

        final IDataSerializer dataSerializer = customBlock.getDataSerializer();
        if(customBlock instanceof ITileEntityProvider)
            tileEntityHandler.addTileEntity(blockPos, dataSerializer, ((ITileEntityProvider) customBlock).createTileEntity(blockPos));

        customBlock.onPlace(block, blockPos, player);

        dataSerializer.write(blockPos, compound);
        dataSerializer.save(false);

        customBlocks.put(blockPos, customBlock);

        return true;
    }

    public synchronized boolean breakCustomBlock(BlockPos blockPos, boolean checkValidity, boolean drop, @Nullable Entity entity) {
        final CustomBlock customBlock = findCustomBlock(blockPos).orElse(null);
        if(customBlock == null)
            return false;

        return breakCustomBlock(blockPos, customBlock, checkValidity, drop, entity);
    }

    private synchronized boolean breakCustomBlock(BlockPos blockPos, CustomBlock customBlock, boolean checkValidity, boolean drop, @Nullable Entity entity) {
        if(!loaded) {
            CustomContentPlugin.instance.getLogger().warning("Someone tried to break a block before CCL finished loading !");
            return false;
        }

        if(checkValidity && !customBlock.canBreak(blockPos, entity))
            return false;

        final CustomTileEntity tileEntity = tileEntityHandler.getTileEntityAt(blockPos).orElse(null);

        customBlock.onBreak(blockPos, tileEntity, entity);

        if(tileEntity != null)
            tileEntityHandler.removeTileEntity(blockPos, tileEntity instanceof ITickable);

        if(customBlock instanceof ICustomTextureHolder)
            customTextureHandler.removeCustomTexture(blockPos);

        final IDataSerializer dataSerializer = customBlock.getDataSerializer();
        dataSerializer.remove(blockPos);
        dataSerializer.save(false);

        customBlocks.remove(blockPos);

        final Block block = blockPos.getBlock();
        block.setType(Material.AIR);

        if(drop)
            block.getWorld().dropItemNaturally(block.getLocation().clone().add(0.5f, 0.25f, 0.5f), customBlock.getItemStack());

        return true;
    }

    public Optional<CustomBlock> findCustomBlock(BlockPos blockPos) {
        return Optional.ofNullable(customBlocks.get(blockPos));
    }

    private boolean hasBlockFlag(IBlockFlagHolder blockFlagOwner, BlockFlags... flags) {
        for (BlockFlags a : blockFlagOwner.getFlags()) for (BlockFlags b : flags) {
            if(a.equals(b))
                return true;
        }

        return false;
    }

    public void loadAll(BlocksRegistry blocksRegistry, Logger logger) {
        final long before = System.currentTimeMillis();

        int i = 0, j = 0;
        for(CustomBlock customBlock : blocksRegistry.getCustomBlocks()) {
            for(Map.Entry<BlockPos, SerializableDataCompound> entry : customBlock.getDataSerializer().readAll().entrySet()) {
                final BlockPos blockPos = entry.getKey();
                final SerializableDataCompound compound = entry.getValue();

                customBlocks.put(blockPos, customBlock);

                if(customBlock instanceof ICustomTextureHolder) {
                    customTextureHandler.createCustomTexture(
                            blockPos,
                            (ICustomTextureHolder) customBlock,
                            compound.containsKey("direction") ? BlockDirection.valueOf(compound.getString("direction")) : BlockDirection.SOUTH,
                            null
                    );
                }

                if(customBlock instanceof ITileEntityProvider) {
                    final CustomTileEntity tileEntity = ((ITileEntityProvider) customBlock).createTileEntity(blockPos);
                    tileEntity.load(compound);

                    tileEntityHandler.addTileEntity(blockPos, customBlock.getDataSerializer(), tileEntity);
                    j++;
                }

                i++;
            }
        }

        logger.info("-> Unserialized " + i + " custom blocks and " + j + " custom tile entities (Took: " + (System.currentTimeMillis() - before) + " ms)");

        loaded = true;
    }
}
