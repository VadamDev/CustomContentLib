package net.vadamdev.customcontent.internal.registry;

import net.vadamdev.customcontent.api.blocks.CustomBlock;
import net.vadamdev.customcontent.internal.utils.FileUtils;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author VadamDev
 * @since 05/09/2022
 */
public final class BlocksRegistry {
    private final CommonRegistry commonRegistry;

    private final Set<CustomBlock> customBlocks;

    public BlocksRegistry(CommonRegistry commonRegistry) {
        this.commonRegistry = commonRegistry;

        this.customBlocks = new HashSet<>();
    }

    public void registerCustomBlock(CustomBlock customBlock) {
        Objects.requireNonNull(customBlock.getDataSerializer(), "Data Serializer cannot be null");

        commonRegistry.checkRegistry(customBlock.getRegistryName());

        commonRegistry.register(customBlock);
        customBlocks.add(customBlock);
    }

    public Set<CustomBlock> getCustomBlocks() {
        return customBlocks;
    }
}
