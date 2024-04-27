package net.vadamdev.customcontent.internal.integration.worldedit;

import com.sk89q.jnbt.CompoundTag;
import com.sk89q.jnbt.StringTag;
import com.sk89q.jnbt.Tag;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.extension.input.ParserContext;
import com.sk89q.worldedit.internal.registry.InputParser;
import net.vadamdev.customcontent.api.common.IRegistrable;
import net.vadamdev.customcontent.internal.registry.BlocksRegistry;

import java.util.HashMap;
import java.util.Map;

/**
 * @author VadamDev
 */
public class CustomBlockInputParser extends InputParser<BaseBlock> {
    private final BlocksRegistry blocksRegistry;

    public CustomBlockInputParser(WorldEdit worldEdit, BlocksRegistry blocksRegistry) {
        super(worldEdit);

        this.blocksRegistry = blocksRegistry;
    }

    @Override
    public BaseBlock parseFromInput(String input, ParserContext context) {
        return blocksRegistry.getCustomBlocks().stream()
                .map(IRegistrable::getRegistryName)
                .filter(registryName -> registryName.equalsIgnoreCase(input))
                .findFirst().map(registryName -> {
                    final Map<String, Tag> data = new HashMap<>();
                    data.put("CCL-RegistryName", new StringTag(registryName));

                    return new BaseBlock(1, 0, new CompoundTag(data));
                }).orElse(null);
    }
}
