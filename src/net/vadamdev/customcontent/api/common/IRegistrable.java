package net.vadamdev.customcontent.api.common;

import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Represents a CCL's registrable entity.
 *
 * @author VadamDev
 * @since 07/03/2022
 */
public interface IRegistrable {
    @Nonnull
    ItemStack getItemStack();

    @Nonnull
    String getRegistryName();

    @Nullable
    default List<String> getDefaultLore() {
        return null;
    }

    default boolean isConfigurable() {
        return true;
    }
}
