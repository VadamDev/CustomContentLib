package net.vadamdev.customcontent.internal.registry;

import net.vadamdev.customcontent.api.blocks.CustomBlock;
import net.vadamdev.customcontent.internal.CustomContentPlugin;
import net.vadamdev.customcontent.internal.utils.FileUtils;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;

/**
 * @author VadamDev
 * @since 05/09/2022
 */
public final class BlocksRegistry {
    private final Logger logger;

    private final CommonRegistry commonRegistry;

    private final Set<CustomBlock> customBlocks;

    public BlocksRegistry() {
        this.logger = CustomContentPlugin.instance.getLogger();

        this.commonRegistry = CustomContentPlugin.instance.getCommonRegistry();

        this.customBlocks = new HashSet<>();
    }

    public void registerCustomBlock(CustomBlock customBlock) {
        Objects.requireNonNull(customBlock.getDataSerializer(), "Data Serializer cannot be null");

        String registryName = customBlock.getRegistryName();

        commonRegistry.checkRegistry(registryName);

        logger.info("Registration of " + registryName + " (Custom Block, Configurable: " + customBlock.isConfigurable() + "))");

        commonRegistry.register(customBlock, FileUtils.BLOCKS);
        customBlocks.add(customBlock);
    }

    public Set<CustomBlock> getCustomBlocks() {
        return customBlocks;
    }
}
