package net.vadamdev.customcontent.internal.handlers.items;

import net.vadamdev.customcontent.api.items.CustomFood;
import net.vadamdev.customcontent.api.items.CustomItem;
import net.vadamdev.customcontent.internal.impl.CustomContentAPIImpl;
import net.vadamdev.customcontent.internal.registry.CommonRegistry;
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

/**
 * @author VadamDev
 * @since 22/08/2022
 */
public class ItemsHandler implements Listener {
    public CustomContentAPIImpl customContentAPI;

    private final CommonRegistry commonRegistry;

    public ItemsHandler(CommonRegistry commonRegistry) {
        this.commonRegistry = commonRegistry;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        final ItemStack itemStack = event.getItem();
        if(itemStack == null)
            return;

        commonRegistry.getCustomItems().stream()
                .filter(CustomItem.class::isInstance)
                .filter(customItem -> customContentAPI.isCustomItem(itemStack, customItem.getRegistryName()))
                .findFirst().ifPresent(customItem -> {
                    boolean flag;

                    if(customItem instanceof CustomFood && ((CustomFood) customItem).isEdibleEvenWithFullHunger())
                        flag = ((CustomFood) customItem).onEat(event.getPlayer(), itemStack);
                    else
                        flag = ((CustomItem) customItem).onClick(event.getPlayer(), ItemAction.of(event.getAction()), event.getClickedBlock(), event.getBlockFace(), itemStack);

                    if(flag)
                        event.setCancelled(true);
                });
    }

    @EventHandler
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
        final Player player = event.getPlayer();
        final ItemStack itemStack = player.getItemInHand();

        if(itemStack == null)
            return;

        commonRegistry.getCustomItems().stream()
                .filter(CustomItem.class::isInstance)
                .filter(customItem -> customContentAPI.isCustomItem(itemStack, customItem.getRegistryName()))
                .findFirst().ifPresent(customItem -> {
                    boolean flag = ((CustomItem) customItem).onEntityClick(player, event.getRightClicked(), itemStack);

                    if(flag)
                        event.setCancelled(true);
                });
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if(event.getDamager() instanceof Player) {
            final Player damager = (Player) event.getDamager();
            final ItemStack itemStack = damager.getItemInHand();

            if(itemStack == null)
                return;

            commonRegistry.getCustomItems().stream()
                    .filter(CustomItem.class::isInstance)
                    .filter(customItem -> customContentAPI.isCustomItem(itemStack, customItem.getRegistryName()))
                    .findFirst().ifPresent(customItem -> {
                        boolean flag = ((CustomItem) customItem).hurtEntity(damager, event.getEntity(), itemStack);

                        if(flag)
                            event.setCancelled(true);
                    });
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        final Player player = event.getPlayer();
        final ItemStack itemStack = player.getItemInHand();

        if(itemStack == null || itemStack.getType().equals(Material.AIR))
            return;

        commonRegistry.getCustomItems().stream()
                .filter(CustomItem.class::isInstance)
                .filter(customItem -> customContentAPI.isCustomItem(itemStack, customItem.getRegistryName()))
                .findFirst().ifPresent(customItem -> {
                    boolean flag = ((CustomItem) customItem).mineBlock(player, event.getBlock(), event.getExpToDrop(), itemStack);

                    if(flag)
                        event.setCancelled(true);
                });
    }

    /*
       Custom Food
     */
    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        final ItemStack itemStack = event.getItem();

        commonRegistry.getCustomItems().stream()
                .filter(CustomFood.class::isInstance)
                .filter(customItem -> customContentAPI.isCustomItem(itemStack, customItem.getRegistryName()))
                .findFirst().ifPresent(customItem -> {
                    boolean flag = ((CustomFood) customItem).onEat(event.getPlayer(), itemStack);

                    if(flag)
                        event.setCancelled(true);
                });
    }
}
