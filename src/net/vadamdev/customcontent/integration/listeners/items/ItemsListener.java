package net.vadamdev.customcontent.integration.listeners.items;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class ItemsListener implements Listener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(event.getItem() != null) ItemsInteractionManager.checkAndTrigger(event);
    }

    /*@EventHandler
    public void onPlayerTakeDamage(EntityDamageEvent event) {
        if(event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            EntityEquipment equipment = player.getEquipment();

            ItemStack[] newEquipment = new ItemStack[4];

            int i = 0;
            for (ItemStack itemStack : equipment.getArmorContents()) {
                if(itemStack == null || !checkCustomArmorAuthenticity(itemStack)) continue;

                itemStack.setDurability(itemStack.getType().getMaxDurability());

                newEquipment[i] = ItemRegistry.getCustomArmorPart(NBTHelper.getStringInNBTTag(itemStack, "RegistryName")).decreaseDurability(player, itemStack);

                i++;
            }

            equipment.setArmorContents(newEquipment);
        }
    }

    private boolean checkCustomArmorAuthenticity(ItemStack itemStack) {
        return NBTHelper.getStringInNBTTag(itemStack, "RegistryName") != null && ItemRegistry.getCustomArmorPart(NBTHelper.getStringInNBTTag(itemStack, "RegistryName")) != null;
    }*/
}
