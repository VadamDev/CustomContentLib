package net.vadamdev.customcontent.api.items.armor;

import net.vadamdev.customcontent.api.common.tickable.ITickable;
import net.vadamdev.customcontent.lib.utils.NBTHelper;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

/**
 * Represents an ArmorSet.
 * <br>It can be used to add effects to the wearer if the armor set is complete
 * <br><br>
 * By default, ArmorSet will have a period of 20 ticks
 * @author VadamDev
 * @since 27/03/2022
 */
public abstract class ArmorSet implements ITickable {
    protected final CustomArmorPart[] pieces;

    public ArmorSet(CustomArmorPart[] pieces) {
        this.pieces = pieces;
    }

    public boolean hasFullSet(Player player) {
        final EntityEquipment equipment = player.getEquipment();

        boolean helmet = pieces[0] == null || (equipment.getHelmet() != null && isSimilar(equipment.getHelmet(), pieces[0]));
        boolean chestplate = pieces[1] == null || (equipment.getChestplate() != null && isSimilar(equipment.getChestplate(), pieces[1]));
        boolean leggings = pieces[2] == null || (equipment.getLeggings() != null && isSimilar(equipment.getLeggings(), pieces[2]));
        boolean boots = pieces[3] == null || (equipment.getBoots() != null && isSimilar(equipment.getBoots(), pieces[3]));

        return helmet && chestplate && leggings && boots;
    }

    protected boolean isSimilar(ItemStack itemStack, CustomArmorPart armorPart) {
        final String registryName = NBTHelper.getStringInNBTTag(itemStack, "RegistryName");
        return registryName != null && registryName.equals(armorPart.getRegistryName());
    }
}
