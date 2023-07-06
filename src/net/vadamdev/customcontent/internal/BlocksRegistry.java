package net.vadamdev.customcontent.internal;

import net.vadamdev.customcontent.CustomContentLib;
import net.vadamdev.customcontent.api.blocks.CustomBlock;
import net.vadamdev.customcontent.api.blocks.CustomTileEntity;
import net.vadamdev.customcontent.internal.handlers.TileEntityHandler;
import net.vadamdev.customcontent.internal.utils.FileUtils;
import net.vadamdev.customcontent.lib.BlockPos;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

/**
 * @author VadamDev
 * @since 05/09/2022
 */
public final class BlocksRegistry {
    private final Logger logger;

    private final CommonRegistry commonRegistry;
    private final TileEntityHandler tileEntityHandler;

    private final Set<CustomBlock> customBlocks;

    public BlocksRegistry() {
        this.logger = CustomContentLib.instance.getLogger();

        this.commonRegistry = CustomContentLib.instance.getCommonRegistry();
        this.tileEntityHandler = CustomContentLib.instance.getTileEntityHandler();

        this.customBlocks = new HashSet<>();
    }

    public void registerCustomBlock(CustomBlock customBlock) {
        String registryName = customBlock.getRegistryName();

        if(!commonRegistry.canRegister(registryName))
            return;

        logger.info("Registration of " + registryName + " (Custom Block, Configurable: " + customBlock.isConfigurable() + "))");

        commonRegistry.register(customBlock, FileUtils.BLOCKS);
        customBlocks.add(customBlock);
    }

    public boolean isCustomBlock(BlockPos blockPos) {
        return customBlocks.stream().anyMatch(block -> block.getDataSerializer().contains(blockPos));
    }

    public Optional<CustomTileEntity> getTileEntityAt(BlockPos blockPos) {
        return tileEntityHandler.getTileEntityAt(blockPos);
    }

    public Set<CustomBlock> getCustomBlocks() {
        return customBlocks;
    }
}
