package net.vadamdev.customcontent.api.items;

import net.vadamdev.customcontent.api.IRegistrable;
import net.vadamdev.customcontent.lib.utils.NBTHelper;
import org.bukkit.inventory.ItemStack;

/**
 * @author VadamDev
 * @since 15/09/2022
 */
public class EmptyItem implements IRegistrable {
    private final ItemStack itemStack;
    private final String registryName;

    public EmptyItem(ItemStack itemStack, String registryName) {
        this.itemStack = NBTHelper.setStringInNBTTag(itemStack, "RegistryName", registryName);
        this.registryName = registryName;
    }

    @Override
    public ItemStack getItemStack() {
        return itemStack;
    }

    @Override
    public String getRegistryName() {
        return registryName;
    }
}
