package net.vadamdev.customcontent.api;

import net.minecraft.server.v1_8_R3.Entity;
import net.vadamdev.customcontent.api.blocks.CustomBlock;
import net.vadamdev.customcontent.api.common.tickable.AbstractTickableHandler;
import net.vadamdev.customcontent.api.entities.CustomEntityContainer;
import net.vadamdev.customcontent.api.items.CustomFood;
import net.vadamdev.customcontent.api.items.CustomItem;
import net.vadamdev.customcontent.api.items.EmptyItem;
import net.vadamdev.customcontent.api.items.armor.ArmorSet;
import net.vadamdev.customcontent.api.items.armor.CustomArmorPart;

/**
 * @author VadamDev
 * @since 08/07/2023
 */
public interface ContentRegistry {
    void registerEmptyItem(EmptyItem emptyItem);

    void registerCustomItem(CustomItem customItem);

    void registerCustomFood(CustomFood customFood);

    void registerCustomArmorPart(CustomArmorPart customArmorPart);

    void registerArmorSet(ArmorSet armorSet);

    void registerCustomBlock(CustomBlock customBlock);

    void registerTickableHandler(AbstractTickableHandler tickableHandler);

    void registerCustomEntity(CustomEntityContainer<?> customEntity);

    int getEntityId(Class<? extends Entity> entityClass);

    boolean isRegistered(String registryName);
}
