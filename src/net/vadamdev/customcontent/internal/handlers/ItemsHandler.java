package net.vadamdev.customcontent.internal.handlers;

import net.vadamdev.customcontent.CustomContentLib;
import net.vadamdev.customcontent.api.items.CustomFood;
import net.vadamdev.customcontent.api.items.CustomItem;
import net.vadamdev.customcontent.internal.CommonRegistry;
import net.vadamdev.customcontent.lib.CustomContentRegistry;
import net.vadamdev.customcontent.lib.ItemAction;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author VadamDev
 * @since 22/08/2022
 */
public class ItemsHandler implements Listener {
    private final CommonRegistry commonRegistry;

    public ItemsHandler() {
        commonRegistry = CustomContentLib.instance.getCommonRegistry();
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = event.getItem();

        if(itemStack == null)
            return;

        AtomicBoolean flag = new AtomicBoolean(false);

        commonRegistry.getCustomItems().stream().filter(customItem -> CustomContentRegistry.isCustomItem(itemStack, customItem.getRegistryName())).forEach(customItem -> {
            if(customItem instanceof CustomItem) {
                flag.set(((CustomItem) customItem).onClick(player, ItemAction.of(event.getAction()), event.getClickedBlock(), event.getBlockFace(), itemStack));
            }else if(customItem instanceof CustomFood) {
                CustomFood customFood = (CustomFood) customItem;

                if(customFood.isEdibleEvenWithFullHunger())
                    flag.set(customFood.onEat(player, event.getItem()));
            }
        });

        if(flag.get())
            event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = player.getItemInHand();

        if(itemStack == null || itemStack.getType().equals(Material.AIR))
            return;

        AtomicBoolean flag = new AtomicBoolean(false);

        commonRegistry.getCustomItems().stream().filter(customItem -> CustomContentRegistry.isCustomItem(itemStack, customItem.getRegistryName())).forEach(customItem -> {
            if (customItem instanceof CustomItem) {
                flag.set(((CustomItem) customItem).onEntityClick(player, event.getRightClicked(), itemStack));
            }
        });

        if(flag.get())
            event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if(event.getDamager() instanceof Player) {
            Player damager = (Player) event.getDamager();

            ItemStack itemStack = damager.getItemInHand();

            if(itemStack == null || itemStack.getType().equals(Material.AIR))
                return;

            AtomicBoolean flag = new AtomicBoolean(false);

            commonRegistry.getCustomItems().stream().filter(customItem -> CustomContentRegistry.isCustomItem(itemStack, customItem.getRegistryName())).forEach(customItem -> {
                if (customItem instanceof CustomItem) {
                    flag.set(((CustomItem) customItem).hurtEntity(damager, event.getEntity(), itemStack));
                }
            });

            if(flag.get())
                event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = player.getItemInHand();

        if(itemStack == null || itemStack.getType().equals(Material.AIR))
            return;

        AtomicBoolean flag = new AtomicBoolean(false);

        commonRegistry.getCustomItems().stream().filter(customItem -> CustomContentRegistry.isCustomItem(itemStack, customItem.getRegistryName())).forEach(customItem -> {
            if (customItem instanceof CustomItem) {
                flag.set(((CustomItem) customItem).mineBlock(player, event.getBlock(), event.getExpToDrop(), itemStack));
            }
        });

        if(flag.get())
            event.setCancelled(true);
    }

    /*
       Custom Food
     */
    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        ItemStack itemStack = event.getItem();

        AtomicBoolean flag = new AtomicBoolean(false);

        commonRegistry.getCustomItems().stream().filter(customItem -> CustomContentRegistry.isCustomItem(itemStack, customItem.getRegistryName())).forEach(customItem -> {
            if (customItem instanceof CustomFood) {
                flag.set(((CustomFood) customItem).onEat(event.getPlayer(), itemStack));
            }
        });

        if(flag.get())
            event.setCancelled(true);
    }
}
