package net.vadamdev.customcontent.internal.handlers;

import net.vadamdev.customcontent.CustomContentLib;
import net.vadamdev.customcontent.api.items.armor.CustomArmorPart;
import net.vadamdev.customcontent.internal.GeneralRegistry;
import net.vadamdev.customcontent.lib.ItemRegistry;
import net.vadamdev.customcontent.lib.utils.NBTHelper;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * @author VadamDev
 * @since 22/08/2022
 */
public class ArmorsHandler implements Listener {
    private final GeneralRegistry generalRegistry;

    public ArmorsHandler() {
        generalRegistry = CustomContentLib.instance.getGeneralRegistry();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDamagedByEntity(EntityDamageByEntityEvent event) {
        if(event.isCancelled())
            return;

        if(event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            EntityEquipment equipment = player.getEquipment();

            for (ItemStack itemStack : equipment.getArmorContents()) {
                if(itemStack.getType().equals(Material.AIR) || !isCustomArmorPart(itemStack))
                    continue;

                generalRegistry.getCustomItems().stream().filter(customItem -> ItemRegistry.isCustomItem(itemStack, customItem.getRegistryName())).forEach(customItem -> {
                    ((CustomArmorPart) customItem).onHolderDamaged(player, event.getDamager(), itemStack);
                });
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAnvilClick(InventoryClickEvent event) {
        if(event.isCancelled())
            return;

        ItemStack item = event.getCurrentItem();
        Inventory anvilInventory = event.getInventory();

        if(anvilInventory.getType().equals(InventoryType.ANVIL) && event.getRawSlot() == 2 && !item.getType().equals(Material.AIR) && isCustomArmorPart(item)) {
            ItemStack reparationItem = anvilInventory.getItem(1);

            if(reparationItem == null || (!reparationItem.getType().equals(Material.DIAMOND) && !reparationItem.getType().equals(Material.IRON_INGOT) &&
                    !reparationItem.getType().equals(Material.GOLD_INGOT) && !reparationItem.getType().equals(Material.LEATHER))) return;

            int maxDurability = NBTHelper.getIntegerInNBTTag(item, "MaxDurability");
            if(maxDurability != 0) {
                int toAdd = maxDurability / 4;
                int durability = NBTHelper.getIntegerInNBTTag(item, "Durability");

                if((durability + toAdd) > maxDurability)
                    anvilInventory.setItem(2, NBTHelper.setIntegerInNBTTag(item, "Durability", maxDurability));
                else
                    anvilInventory.setItem(2, NBTHelper.setIntegerInNBTTag(item, "Durability", durability + toAdd));
            }
        }
    }

    private boolean isCustomArmorPart(ItemStack itemStack) {
        String registryName = NBTHelper.getStringInNBTTag(itemStack ,"RegistryName");
        if(registryName == null || !NBTHelper.getBooleanInNBTTag(itemStack ,"CustomArmorPart"))
            return false;

        return ItemRegistry.isRegistered(registryName);
    }
}
