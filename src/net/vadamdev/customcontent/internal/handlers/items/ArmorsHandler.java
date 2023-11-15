package net.vadamdev.customcontent.internal.handlers.items;

import net.vadamdev.customcontent.api.items.armor.CustomArmorPart;
import net.vadamdev.customcontent.internal.impl.CustomContentAPIImpl;
import net.vadamdev.customcontent.internal.registry.CommonRegistry;
import net.vadamdev.customcontent.lib.utils.NBTHelper;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * @author VadamDev
 * @since 22/08/2022
 */
public class ArmorsHandler implements Listener {
    public CustomContentAPIImpl customContentAPI;

    private final CommonRegistry commonRegistry;

    public ArmorsHandler(CommonRegistry commonRegistry) {
        this.commonRegistry = commonRegistry;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityDamagedByEntity(EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof Player))
            return;

        final Player player = (Player) event.getEntity();
        for (ItemStack itemStack : player.getEquipment().getArmorContents()) {
            if(itemStack == null || itemStack.getType().equals(Material.AIR))
                continue;

            commonRegistry.getCustomItems().stream()
                    .filter(CustomArmorPart.class::isInstance)
                    .filter(customItem -> customContentAPI.isCustomItem(itemStack, customItem.getRegistryName()))
                    .findFirst().ifPresent(customItem -> {
                        boolean flag = ((CustomArmorPart) customItem).onHolderDamaged(player, event.getDamager(), itemStack);

                        if(flag)
                            event.setCancelled(true);
                    });
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAnvilClick(InventoryClickEvent event) {
        if(!event.getInventory().getType().equals(InventoryType.ANVIL))
            return;

        final Inventory inventory = event.getInventory();

        final ItemStack armorItem = inventory.getItem(0);
        final ItemStack reparationItem = inventory.getItem(1);

        if(armorItem == null || reparationItem == null)
            return;

        commonRegistry.getCustomItems().stream()
                .filter(CustomArmorPart.class::isInstance)
                .filter(registrable -> customContentAPI.isCustomItem(armorItem, registrable.getRegistryName()) && isCustomArmorPart(armorItem))
                .findFirst().ifPresent(registrable -> {
                    final CustomArmorPart customArmorPart = (CustomArmorPart) registrable;
                    final ItemStack repairMaterial = customArmorPart.getRepairMaterial();

                    if(repairMaterial != null && reparationItem.isSimilar(repairMaterial)) {
                        final int maxDurability = customArmorPart.getMaxDurability();
                        final int newDurability = Math.min(customArmorPart.getDurability(armorItem) + maxDurability / 4, maxDurability);

                        final ItemStack newArmorItem = customArmorPart.setDurability(armorItem, newDurability);
                        customArmorPart.updateDurabilityBar(newArmorItem, customArmorPart.getDefaultLore());

                        inventory.setItem(2, newArmorItem);
                    }else
                        inventory.setItem(2, null);
                });
    }

    private boolean isCustomArmorPart(ItemStack itemStack) {
        return NBTHelper.getBooleanInNBTTag(itemStack ,"CustomArmorPart");
    }
}
