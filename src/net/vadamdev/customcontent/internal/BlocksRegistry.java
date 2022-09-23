package net.vadamdev.customcontent.internal;

import net.vadamdev.customcontent.CustomContentLib;
import net.vadamdev.customcontent.api.blocks.CustomBlock;
import net.vadamdev.customcontent.api.blocks.CustomTileEntity;
import net.vadamdev.customcontent.internal.handlers.TileEntityHandler;
import net.vadamdev.customcontent.internal.utils.FileUtils;
import net.vadamdev.customcontent.lib.BlockPos;
import org.bukkit.configuration.file.FileConfiguration;

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
    private final FileConfiguration blocksConfig;

    private final CommonRegistry commonRegistry;
    private final TileEntityHandler tileEntityHandler;

    private final Set<CustomBlock> customBlocks;

    public BlocksRegistry() {
        this.logger = CustomContentLib.instance.getLogger();
        this.blocksConfig = FileUtils.BLOCKS.getConfig();

        this.commonRegistry = CustomContentLib.instance.getCommonRegistry();
        this.tileEntityHandler = CustomContentLib.instance.getTileEntityHandler();

        this.customBlocks = new HashSet<>();
    }

    public void registerCustomBlock(CustomBlock customBlock) {
        String registryName = customBlock.getRegistryName();

        if(!commonRegistry.canRegister(registryName))
            return;

        logger.info("Registration of " + registryName + " (Custom Block, Configurable: " + customBlock.isConfigurable() + "))");

        commonRegistry.register(customBlock, blocksConfig);
        customBlocks.add(customBlock);
    }

    public Optional<CustomTileEntity> getTileEntityAt(BlockPos blockPos) {
        return tileEntityHandler.getTileEntityAt(blockPos);
    }

    public Set<CustomBlock> getCustomBlocks() {
        return customBlocks;
    }
}
