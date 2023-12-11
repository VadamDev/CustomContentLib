package net.vadamdev.customcontent.api;

import net.minecraft.server.v1_8_R3.Entity;
import net.vadamdev.customcontent.annotations.ForRemoval;
import net.vadamdev.customcontent.api.blocks.CustomBlock;
import net.vadamdev.customcontent.api.common.IRegistrable;
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
    /**
     * Register any {@link IRegistrable} into CCL.
     *
     * @param registrable {@link IRegistrable}
     */
    void register(IRegistrable registrable);

    /**
     * Register an {@link ArmorSet} into CCL
     *
     * @param armorSet {@link ArmorSet}
     */
    void registerArmorSet(ArmorSet armorSet);

    /**
     * Register a {@link CustomEntityContainer} into CCL
     * <br>It's currently only used for basic auto NMS Custom Entities handling. It will later be used for custom entity models
     *
     * @param customEntity {@link CustomEntityContainer}
     */
    void registerCustomEntity(CustomEntityContainer<?> customEntity);

    /**
     * Return the entity id of the specified class
     *
     * @param entityClass The class of the NMS entity
     * @return The entity id or -1 if entity doesn't exist
     */
    int getEntityId(Class<? extends Entity> entityClass);

    /**
     * Register a {@link AbstractTickableHandler} wich can be used with {@link net.vadamdev.customcontent.annotations.TickableInfo TickableInfo}
     *
     * @see net.vadamdev.customcontent.annotations.TickableInfo TickableInfo
     * @param tickableHandler
     */
    void registerTickableHandler(AbstractTickableHandler tickableHandler);

    /**
     * Check if the provided registryName is registered in the CommonRegistry
     *
     * @param registryName Registry name of the {@link IRegistrable}
     * @return True if the item is registered
     */
    boolean isRegistered(String registryName);

    /*
      Deprecated, going be removed soon!
     */

    @Deprecated
    @ForRemoval(deadLine = "1.4.0", reason = "added register(IRegistrable) method", replacement = "register(emptyItem)")
    default void registerEmptyItem(EmptyItem emptyItem) {
        register(emptyItem);
    }

    @Deprecated
    @ForRemoval(deadLine = "1.4.0", reason = "added register(IRegistrable) method", replacement = "register(customItem)")
    default void registerCustomItem(CustomItem customItem) {
        register(customItem);
    }

    @Deprecated
    @ForRemoval(deadLine = "1.4.0", reason = "added register(IRegistrable) method", replacement = "register(customFood)")
    default void registerCustomFood(CustomFood customFood) {
        register(customFood);
    }

    @Deprecated
    @ForRemoval(deadLine = "1.4.0", reason = "added register(IRegistrable) method", replacement = "register(customArmorPart)")
    default void registerCustomArmorPart(CustomArmorPart customArmorPart) {
        register(customArmorPart);
    }

    @Deprecated
    @ForRemoval(deadLine = "1.4.0", reason = "added register(IRegistrable) method", replacement = "register(customBlock)")
    default void registerCustomBlock(CustomBlock customBlock) {
        register(customBlock);
    }
}
