package net.vadamdev.customcontent.lib.utils;

import net.vadamdev.customcontent.lib.CustomContentRegistry;
import org.bukkit.inventory.ItemStack;

/**
 * @author VadamDev
 * @since 24/02/2022
 */
public class DurabilityUtils {
    public static int calculateArmorDurabilityWithdrawChance(int unbreakingLevel) {
        return 60 + 40 / (unbreakingLevel + 1);
    }

    public static int calculateItemDurabilityWithdrawChance(int unbreakingLevel) {
        return 100 / (unbreakingLevel + 1);
    }

    public static boolean hasDurabilityProvider(ItemStack itemStack) {
        if(NBTHelper.getStringInNBTTag(itemStack ,"RegistryName") == null) return false;
        if(NBTHelper.getIntegerInNBTTag(itemStack ,"Durability") == 0 && NBTHelper.getIntegerInNBTTag(itemStack ,"MaxDurability") == 0) return false;

        return CustomContentRegistry.isRegistered(NBTHelper.getStringInNBTTag(itemStack ,"RegistryName"));
    }
}
