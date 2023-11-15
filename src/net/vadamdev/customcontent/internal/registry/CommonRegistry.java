package net.vadamdev.customcontent.internal.registry;

import net.vadamdev.customcontent.api.common.IRegistrable;
import net.vadamdev.customcontent.internal.utils.CustomContentSerializer;
import net.vadamdev.customcontent.internal.utils.FileUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * @author VadamDev
 * @since 05/09/2022
 */
public final class CommonRegistry {
    private final List<IRegistrable> customItems;
    private final Map<String, ItemStack> customItemstacks;

    public CommonRegistry() {
        this.customItems = new ArrayList<>();
        this.customItemstacks = new HashMap<>();
    }

    public void register(IRegistrable registrable) {
        Objects.requireNonNull(registrable.getItemStack(), "Itemstack cannot be null");
        Objects.requireNonNull(registrable.getRegistryName(), "RegistryName cannot be null !");

        customItems.add(registrable);
        customItemstacks.put(registrable.getRegistryName(), getItemStackInConfiguration(registrable));
    }

    public ItemStack getCustomItemAsItemStack(String registryName) {
        if(!customItemstacks.containsKey(registryName))
            return null;

        return customItemstacks.get(registryName).clone();
    }

    public boolean isRegistered(String registryName) {
        return customItemstacks.containsKey(registryName);
    }

    public List<IRegistrable> getCustomItems() {
        return customItems;
    }

    public Map<String, ItemStack> getCustomItemstacks() {
        return customItemstacks;
    }

    void checkRegistry(String registryName) {
        if(!isRegistered(registryName))
            return;

        throw new UnsupportedOperationException(registryName + " is already registered !");
    }

    private ItemStack getItemStackInConfiguration(IRegistrable registrable) {
        final FileConfiguration config = FileUtils.DESCRIPTIONS.getConfig();

        ItemStack itemStack = registrable.getItemStack();
        if(registrable.isConfigurable() && config.isSet(registrable.getRegistryName()))
            itemStack = CustomContentSerializer.unserializeItemStack(registrable.getItemStack(), registrable.getRegistryName(), config);
        else if(registrable.isConfigurable())
            CustomContentSerializer.serializeItemStack(itemStack, registrable.getRegistryName(), FileUtils.DESCRIPTIONS);

        return itemStack;
    }
}
