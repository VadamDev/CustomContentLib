package net.vadamdev.customcontent.api.items;

import net.vadamdev.customcontent.api.common.IRegistrable;
import net.vadamdev.customcontent.lib.utils.NBTHelper;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

/**
 * Represents an item with no interactions
 *
 * @author VadamDev
 * @since 15/09/2022
 */
public class EmptyItem implements IRegistrable {
    protected ItemStack itemStack;
    private final String registryName;

    public EmptyItem(ItemStack itemStack, String registryName) {
        this.itemStack = NBTHelper.setStringInNBTTag(itemStack, "RegistryName", registryName);
        this.registryName = registryName;
    }

    @Nonnull
    @Override
    public ItemStack getItemStack() {
        return itemStack;
    }

    @Nonnull
    @Override
    public String getRegistryName() {
        return registryName;
    }
}
