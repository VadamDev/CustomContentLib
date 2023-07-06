package net.vadamdev.customcontent.internal;

import net.vadamdev.customcontent.api.IRegistrable;
import net.vadamdev.customcontent.internal.utils.CustomContentSerializer;
import net.vadamdev.customcontent.internal.exceptions.AlreadyRegisteredException;
import net.vadamdev.customcontent.internal.utils.FileUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    protected boolean canRegister(String registryName) {
        if(isRegistered(registryName)) {
            try {
                throw new AlreadyRegisteredException(registryName);
            } catch (AlreadyRegisteredException e) {
                e.printStackTrace();
            }

            return false;
        }

        return true;
    }

    private static ItemStack getItemStackInConfiguration(IRegistrable registrable, FileUtils dataFile) {
        ItemStack itemStack = registrable.getItemStack();
        FileConfiguration config = dataFile.getConfig();

        if(registrable.isConfigurable() && config.isSet(registrable.getRegistryName())) {
            itemStack = CustomContentSerializer.unserializeItemStack(registrable.getItemStack(), registrable.getRegistryName(), config);
        }else if(registrable.isConfigurable()) {
            CustomContentSerializer.serializeItemStack(registrable.getItemStack(), registrable.getRegistryName(), dataFile);
        }

        return itemStack;
    }
}
