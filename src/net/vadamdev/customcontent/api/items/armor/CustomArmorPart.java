package net.vadamdev.customcontent.api.items.armor;

import net.vadamdev.customcontent.api.IRegistrable;
import net.vadamdev.customcontent.api.items.DurabilityProvider;
import net.vadamdev.customcontent.lib.utils.DurabilityUtils;
import net.vadamdev.customcontent.lib.utils.NBTHelper;
import net.vadamdev.viaapi.tools.builders.ItemBuilder;
import net.vadamdev.viaapi.tools.math.MathUtils;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @author VadamDev
 * @since 23/02/2022
 */
public abstract class CustomArmorPart implements IRegistrable, DurabilityProvider {
    private final ArmorPart armorPart;
    protected ItemStack itemStack;

    protected final List<String> defaultLore;

    public CustomArmorPart(ArmorType armorType, ArmorPart armorPart, String name, String... lore) {
        this.armorPart = armorPart;

        this.itemStack = NBTHelper.setStringInNBTTag(new ItemBuilder(armorType.get(armorPart)).setName(name).setLore(lore).toItemStack(), "RegistryName", getRegistryName());
        this.itemStack = NBTHelper.setBooleanInNBTTag(itemStack, "CustomArmorPart", true);
        this.defaultLore = itemStack.getItemMeta().getLore();
        this.itemStack = setDefaultDurability(itemStack);
    }

    /**
     * IF YOU'RE MAKING A CUSTOM DAMAGE ACTION, DONT FORGET TO EXECUTE THE applyDurability METHOD!
     * @return The Action
     */
    public boolean onHolderDamaged(Player player, Entity damager, ItemStack item) {
        applyDurability(player, item);
        return false;
    }

    @Override
    public ItemStack getItemStack() {
        return itemStack;
    }

    protected void applyDurability(Player player, ItemStack item) {
        EntityEquipment equipment = player.getEquipment();

        item.setDurability((short) 0);

        if(!MathUtils.percentageLuck(DurabilityUtils.calculateArmorDurabilityWithdrawChance(item.getEnchantmentLevel(Enchantment.DURABILITY))))
            return;

        switch (armorPart) {
            case HELMET:
                equipment.setHelmet(setDurability(item, getDurability(item) - 1));
                updateDurabilityBar(equipment.getHelmet(), defaultLore);

                if(getDurability(item) <= 0) {
                    equipment.setHelmet(null);
                    player.playSound(player.getLocation(), getBreakSound(), 1, 1);
                }

                break;
            case CHESTPLATE:
                equipment.setChestplate(setDurability(item, getDurability(item) - 1));
                updateDurabilityBar(equipment.getChestplate(), defaultLore);

                if(getDurability(item) <= 0) {
                    equipment.setChestplate(null);
                    player.playSound(player.getLocation(), getBreakSound(), 1, 1);
                }
                break;
            case LEGGINGS:
                equipment.setLeggings(setDurability(item, getDurability(item) - 1));
                updateDurabilityBar(equipment.getLeggings(), defaultLore);

                if(getDurability(item) <= 0) {
                    equipment.setLeggings(null);
                    player.playSound(player.getLocation(), getBreakSound(), 1, 1);
                }

                break;
            case BOOTS:
                equipment.setBoots(setDurability(item, getDurability(item) - 1));
                updateDurabilityBar(equipment.getBoots(), defaultLore);

                if(getDurability(item) <= 0) {
                    equipment.setBoots(null);
                    player.playSound(player.getLocation(), getBreakSound(), 1, 1);
                }

                break;
            default:
                break;
        }
    }
}
