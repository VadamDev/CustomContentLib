package net.vadamdev.customcontent.lib.utils;

import net.vadamdev.customcontent.internal.CustomContentPlugin;
import org.bukkit.inventory.ItemStack;

/**
 * @author VadamDev
 * @since 24/02/2022
 */
public final class DurabilityUtils {
    private DurabilityUtils() {}

    /**
     * Calculate the percentage of chance that the armor will decrease its durability when damaged.
     * @see <a href="https://minecraft.fandom.com/wiki/Unbreaking">https://minecraft.fandom.com/wiki/Unbreaking</a>
     *
     * @param level Unbreaking level
     * @return The percentage of chance that the armor will decrease its durability when damaged
     */
    public static int calculateArmorDurabilityWithdrawChance(int level) {
        return 60 + 40 / (level + 1);
    }

    /**
     * Calculate the percentage of chance that the item will decrease its durability when used.
     * @see <a href="https://minecraft.fandom.com/wiki/Unbreaking">https://minecraft.fandom.com/wiki/Unbreaking</a>
     *
     * @param level Unbreaking level
     * @return The percentage of chance that the item will decrease its durability when used
     */
    public static int calculateItemDurabilityWithdrawChance(int level) {
        return 100 / (level + 1);
    }

    /**
     * Check if the provided itemstack has custom durability provided by the {@link net.vadamdev.customcontent.api.items.DurabilityProvider DurabilityProvider} interface.
     *
     * @param itemStack Item to check for
     * @return True if the item has custom durability
     */
    public static boolean hasDurabilityProvider(ItemStack itemStack) {
        if(NBTHelper.getIntegerInNBTTag(itemStack ,"Durability") == 0 || NBTHelper.getIntegerInNBTTag(itemStack ,"MaxDurability") == 0)
            return false;

        return CustomContentPlugin.instance.getCommonRegistry().isRegistered(NBTHelper.getStringInNBTTag(itemStack, "RegistryName"));
    }
}
