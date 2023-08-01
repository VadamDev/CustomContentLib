package net.vadamdev.customcontent.internal.handlers.items;

import net.vadamdev.customcontent.api.items.CustomFood;
import net.vadamdev.customcontent.api.items.CustomItem;
import net.vadamdev.customcontent.internal.CustomContentPlugin;
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
    private final CustomContentAPIImpl customContentAPI;

    private final CommonRegistry commonRegistry;

    public ItemsHandler() {
        this.customContentAPI = CustomContentPlugin.instance.getCustomContentAPI();

        this.commonRegistry = CustomContentPlugin.instance.getCommonRegistry();
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = event.getItem();

        if(itemStack == null)
            return;

        commonRegistry.getCustomItems().stream()
                .filter(customItem -> customContentAPI.isCustomItem(itemStack, customItem.getRegistryName()))
                .findFirst().ifPresent(registrable -> {
                    boolean flag = false;

                    if(registrable instanceof CustomItem)
                        flag = ((CustomItem) registrable).onClick(player, ItemAction.of(event.getAction()), event.getClickedBlock(), event.getBlockFace(), itemStack);
                    else if(registrable instanceof CustomFood && ((CustomFood) registrable).isEdibleEvenWithFullHunger())
                        flag = ((CustomFood) registrable).onEat(player, event.getItem());

                    if(flag)
                        event.setCancelled(true);
                });
    }

    @EventHandler
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = player.getItemInHand();

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
            Player damager = (Player) event.getDamager();
            ItemStack itemStack = damager.getItemInHand();

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
        Player player = event.getPlayer();
        ItemStack itemStack = player.getItemInHand();

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
        ItemStack itemStack = event.getItem();

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
