package net.vadamdev.customcontent.internal.deprecated.items;

import net.vadamdev.customcontent.internal.deprecated.events.CustomArmorEvent;
import net.vadamdev.customcontent.lib.ItemRegistry;
import net.vadamdev.customcontent.lib.utils.NBTHelper;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

/**
 * @author VadamDev
 */
@Deprecated
public class ItemsListener implements Listener {
    /*
       Custom Items
     */

    //InteractEvent
    @Deprecated
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(event.getAction().equals(Action.PHYSICAL))
            return;

        if(event.getItem() != null)
            ItemsInteractionManager.triggerInteractAction(event);
    }

    //Break Event
    @Deprecated
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if(event.getPlayer() != null && event.getPlayer().getItemInHand().getType() != Material.AIR && event.getBlock() != null)
            ItemsInteractionManager.triggerBreakAction(event);
    }

    /*
       Custom Food
     */
    @Deprecated
    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        if(event.getItem() != null)
            ItemsInteractionManager.triggerConsumeAction(event);
    }

    /*
       Custom Armors
     */

    //Custom Armors
    @Deprecated
    @EventHandler(priority = EventPriority.LOW)
    public void onEntityDamagedByEntity(EntityDamageByEntityEvent event) {
        if(event.isCancelled())
            return;

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

    private boolean isCustomArmorPart(ItemStack itemStack) {
        if(NBTHelper.getStringInNBTTag(itemStack ,"RegistryName") == null || !NBTHelper.getBooleanInNBTTag(itemStack ,"CustomArmorPart")) return false;
        return ItemRegistry.isRegistered(NBTHelper.getStringInNBTTag(itemStack ,"RegistryName"));
    }
}
