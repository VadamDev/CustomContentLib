package net.vadamdev.customcontent.api.items.armor;

import net.vadamdev.customcontent.api.common.IRegistrable;
import net.vadamdev.customcontent.api.items.DurabilityProvider;
import net.vadamdev.customcontent.api.items.IDurabilityBar;
import net.vadamdev.customcontent.lib.durabilitybar.MinecraftDurabilityBar;
import net.vadamdev.customcontent.lib.utils.DurabilityUtils;
import net.vadamdev.customcontent.lib.utils.NBTHelper;
import net.vadamdev.viaapi.tools.builders.ItemBuilder;
import net.vadamdev.viaapi.tools.math.MathUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Represents a CCL's ArmorPart
 *
 * @author VadamDev
 * @since 23/02/2022
 */
public abstract class CustomArmorPart implements IRegistrable, DurabilityProvider {
    private final ArmorType armorType;
    private final ArmorPart armorPart;
    protected ItemStack itemStack;

    protected final List<String> defaultLore;

    public CustomArmorPart(ArmorType armorType, ArmorPart armorPart, String name, String... lore) {
        this.armorType = armorType;
        this.armorPart = armorPart;

        this.itemStack = new ItemBuilder(armorType.get(armorPart)).setName(name).setLore(lore).toItemStack();
        this.itemStack = NBTHelper.setInNBTTag(itemStack, compound -> {
            compound.setString("RegistryName", getRegistryName());
            compound.setBoolean("CustomArmorPart", true);
        });

        this.defaultLore = itemStack.getItemMeta().getLore();
        this.itemStack = setDefaultDurability(itemStack);
    }

    /**
     * Called when the wearer ({@link Player}) is damaged
     * <br>If you're overriding this method, make sure to call the applyCustomDurability method
     *
     * @param player Wearer of the armor piece
     * @param damager Entity who damaged the {@link Player}
     * @param item {@link ItemStack}
     * @return True if the {@link org.bukkit.event.entity.EntityDamageByEntityEvent EntityDamageByEntityEvent} should be cancelled
     */
    public boolean onHolderDamaged(Player player, Entity damager, ItemStack item) {
        applyCustomDurability(player, item);
        return false;
    }

    protected void applyCustomDurability(Player player, ItemStack item) {
        item.setDurability((short) 0);

        if(!MathUtils.percentageLuck(DurabilityUtils.calculateArmorDurabilityWithdrawChance(item.getEnchantmentLevel(Enchantment.DURABILITY))))
            return;

        final EntityEquipment equipment = player.getEquipment();
        switch(armorPart) {
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

    @Nullable
    public ItemStack getRepairMaterial() {
        final Material repairMaterial = armorType.toBukkitMaterial();
        return repairMaterial != null ? new ItemStack(repairMaterial) : null;
    }

    @Nullable
    @Override
    public IDurabilityBar getDurabilityBar() {
        return new MinecraftDurabilityBar();
    }

    @Nullable
    @Override
    public List<String> getDefaultLore() {
        return defaultLore;
    }

    @Nonnull
    @Override
    public ItemStack getItemStack() {
        return itemStack;
    }
}
