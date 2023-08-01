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

    public void register(IRegistrable registrable, FileUtils dataFile) {
        Objects.requireNonNull(registrable.getItemStack(), "Itemstack cannot be null");
        Objects.requireNonNull(registrable.getRegistryName(), "RegistryName cannot be null !");

        customItems.add(registrable);
        customItemstacks.put(registrable.getRegistryName(), getItemStackInConfiguration(registrable, dataFile));
    }

    public ItemStack getCustomItemAsItemStack(String registryName) {
        return customItemstacks.get(registryName);
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

    private ItemStack getItemStackInConfiguration(IRegistrable registrable, FileUtils dataFile) {
        final FileConfiguration config = dataFile.getConfig();

        ItemStack itemStack = registrable.getItemStack();
        if(registrable.isConfigurable() && config.isSet(registrable.getRegistryName()))
            itemStack = CustomContentSerializer.unserializeItemStack(registrable.getItemStack(), registrable.getRegistryName(), config);
        else if(registrable.isConfigurable())
            CustomContentSerializer.serializeItemStack(itemStack, registrable.getRegistryName(), dataFile);

        return itemStack;
    }
}
