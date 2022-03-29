package net.vadamdev.customcontent.integration.listeners.items;

import net.vadamdev.customcontent.lib.ItemRegistry;
import net.vadamdev.customcontent.lib.events.CustomArmorEvent;
import net.vadamdev.customcontent.utils.NBTHelper;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

/**
 * @author VadamDev
 */
public class ItemsListener implements Listener {
    /*
       Custom Items
     */

    //InteractEvent
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(event.getItem() != null)
            ItemsInteractionManager.triggerInteractAction(event);
    }

    //Break Event
    @EventHandler
    public void onPlayerInteract(BlockBreakEvent event) {
        if(event.getPlayer() != null && event.getPlayer().getItemInHand().getType() != Material.AIR && event.getBlock() != null)
            ItemsInteractionManager.triggerBreakAction(event);
    }

    /*
       Custom Food
     */
    @EventHandler
    public void onPlayerInteract(PlayerItemConsumeEvent event) {
        if(event.getItem() != null)
            ItemsInteractionManager.triggerConsumeAction(event);
    }

    /*
       Custom Armors
     */

    //Custom Armors
    @EventHandler(priority = EventPriority.LOW)
    public void onEntityDamagedByEntity(EntityDamageByEntityEvent event) {
        if(event.isCancelled()) return;

        if(event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            EntityEquipment equipment = player.getEquipment();

            if(equipment.getHelmet() != null && isCustomArmorPart(equipment.getHelmet()))
                ItemsInteractionManager.getCustomArmorPartAction(equipment.getHelmet()).accept(new CustomArmorEvent(player, event.getDamager(), equipment.getHelmet()));

            if(equipment.getChestplate() != null && isCustomArmorPart(equipment.getChestplate()))
                ItemsInteractionManager.getCustomArmorPartAction(equipment.getChestplate()).accept(new CustomArmorEvent(player, event.getDamager(), equipment.getChestplate()));

            if(equipment.getLeggings() != null && isCustomArmorPart(equipment.getLeggings()))
                ItemsInteractionManager.getCustomArmorPartAction(equipment.getLeggings()).accept(new CustomArmorEvent(player, event.getDamager(), equipment.getLeggings()));

            if(equipment.getBoots() != null && isCustomArmorPart(equipment.getBoots()))
                ItemsInteractionManager.getCustomArmorPartAction(equipment.getBoots()).accept(new CustomArmorEvent(player, event.getDamager(), equipment.getBoots()));
        }
    }

    /*
       This is WIP
     */
    @EventHandler(priority = EventPriority.LOW)
    public void onAnvilClick(InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();

        if(event.getInventory().getType().equals(InventoryType.ANVIL) && event.getRawSlot() == 2 && isCustomArmorPart(item)) {
            ItemStack reparationItem = event.getInventory().getItem(1);

            if(reparationItem == null || (!reparationItem.getType().equals(Material.DIAMOND) && !reparationItem.getType().equals(Material.IRON_INGOT) &&
                    !reparationItem.getType().equals(Material.GOLD_INGOT) && !reparationItem.getType().equals(Material.LEATHER)))
                return;

            if(NBTHelper.getIntegerInNBTTag(item, "MaxDurability") != 0) {
                int maxDurability = NBTHelper.getIntegerInNBTTag(item, "MaxDurability");
                int toAdd = maxDurability / 4;
                int durability = NBTHelper.getIntegerInNBTTag(item, "Durability");

                if((durability + toAdd) > maxDurability)
                    event.getInventory().setItem(2, NBTHelper.setIntegerInNBTTag(item, "Durability", maxDurability));
                else
                    event.getInventory().setItem(2, NBTHelper.setIntegerInNBTTag(item, "Durability", durability + toAdd));
            }
        }
    }

    private boolean isCustomArmorPart(ItemStack itemStack) {
        if(NBTHelper.getStringInNBTTag(itemStack ,"RegistryName") == null || !NBTHelper.getBooleanInNBTTag(itemStack ,"CustomArmorPart")) return false;
        return ItemRegistry.isRegistered(NBTHelper.getStringInNBTTag(itemStack ,"RegistryName"));
    }
}
