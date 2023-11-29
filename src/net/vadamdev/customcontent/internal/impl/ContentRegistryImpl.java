package net.vadamdev.customcontent.internal.impl;

import net.minecraft.server.v1_8_R3.Entity;
import net.vadamdev.customcontent.api.ContentRegistry;
import net.vadamdev.customcontent.api.blocks.CustomBlock;
import net.vadamdev.customcontent.api.common.IRegistrable;
import net.vadamdev.customcontent.api.common.tickable.AbstractTickableHandler;
import net.vadamdev.customcontent.api.entities.CustomEntityContainer;
import net.vadamdev.customcontent.api.items.CustomItem;
import net.vadamdev.customcontent.api.items.EmptyItem;
import net.vadamdev.customcontent.api.items.armor.ArmorSet;
import net.vadamdev.customcontent.api.items.armor.CustomArmorPart;
import net.vadamdev.customcontent.internal.CustomContentPlugin;
import net.vadamdev.customcontent.internal.registry.BlocksRegistry;
import net.vadamdev.customcontent.internal.registry.CommonRegistry;
import net.vadamdev.customcontent.internal.registry.EntitiesRegistry;
import net.vadamdev.customcontent.internal.registry.ItemsRegistry;

/**
 * @author VadamDev
 * @since 08/07/2023
 */
public final class ContentRegistryImpl implements ContentRegistry {
    private final CommonRegistry commonRegistry;
    private final ItemsRegistry itemsRegistry;
    private final BlocksRegistry blocksRegistry;
    private final EntitiesRegistry entitiesRegistry;

    public ContentRegistryImpl(CommonRegistry commonRegistry, ItemsRegistry itemsRegistry, BlocksRegistry blocksRegistry, EntitiesRegistry entitiesRegistry) {
        this.commonRegistry = commonRegistry;
        this.itemsRegistry = itemsRegistry;
        this.blocksRegistry = blocksRegistry;
        this.entitiesRegistry = entitiesRegistry;
    }

    @Override
    public void register(IRegistrable registrable) {
        if(registrable instanceof CustomItem)
            itemsRegistry.registerCustomItem((CustomItem) registrable);
        else if(registrable instanceof CustomArmorPart)
            itemsRegistry.registerCustomArmorPart((CustomArmorPart) registrable);
        else if(registrable instanceof EmptyItem)
            itemsRegistry.registerEmptyItem((EmptyItem) registrable);
        else if(registrable instanceof CustomBlock)
            blocksRegistry.registerCustomBlock((CustomBlock) registrable);
    }

    @Override
    public void registerArmorSet(ArmorSet armorSet) {
        itemsRegistry.registerArmorSet(armorSet);
    }

    @Override
    public void registerCustomEntity(CustomEntityContainer<?> customEntity) {
        entitiesRegistry.registerCustomEntity(customEntity);
    }

    @Override
    public int getEntityId(Class<? extends Entity> entityClass) {
        return entitiesRegistry.getEntityId(entityClass);
    }

    @Override
    public void registerTickableHandler(AbstractTickableHandler tickableHandler) {
        CustomContentPlugin.instance.getTickableManager().registerTickableHandler(tickableHandler);
    }

    @Override
    public boolean isRegistered(String registryName) {
        return commonRegistry.isRegistered(registryName);
    }

    CommonRegistry getCommonRegistry() {
        return commonRegistry;
    }
}
