package net.vadamdev.customcontent.api;

import org.bukkit.inventory.ItemStack;

/**
 * @author VadamDev
 * @since 07/03/2022
 */
public interface IRegistrable {
    ItemStack getItemStack();
    String getRegistryName();

    default boolean isConfigurable() {
        return true;
    }
}
