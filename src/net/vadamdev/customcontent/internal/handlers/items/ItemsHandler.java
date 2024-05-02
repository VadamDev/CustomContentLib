package net.vadamdev.customcontent.internal.handlers.items;

import net.vadamdev.customcontent.api.items.CustomFood;
import net.vadamdev.customcontent.api.items.CustomItem;
import net.vadamdev.customcontent.internal.impl.CustomContentAPIImpl;
import net.vadamdev.customcontent.internal.registry.CommonRegistry;
import net.vadamdev.customcontent.internal.utils.FileUtils;
import net.vadamdev.customcontent.lib.ItemAction;
import net.vadamdev.customcontent.lib.utils.NBTHelper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * @author VadamDev
 * @since 22/08/2022
 */
public class ItemsHandler implements Listener {
    public CustomContentAPIImpl customContentAPI;

    private final CommonRegistry commonRegistry;

    private final boolean shouldFixAnvil;

    public ItemsHandler(CommonRegistry commonRegistry) {
        this.commonRegistry = commonRegistry;

        this.shouldFixAnvil = FileUtils.CONFIG.getConfig().getBoolean("general.fixAnvil");
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        final ItemStack itemStack = event.getItem();
        if(itemStack == null || !itemStack.hasItemMeta())
            return;

        commonRegistry.getCustomItems().stream()
                .filter(CustomItem.class::isInstance)
                .filter(customItem -> customContentAPI.isCustomItem(itemStack, customItem.getRegistryName()))
                .findFirst().ifPresent(customItem -> {
                    boolean shouldCancel;

                    if(customItem instanceof CustomFood && ((CustomFood) customItem).isEdibleEvenWithFullHunger())
                        shouldCancel = ((CustomFood) customItem).onEat(event.getPlayer(), itemStack);
                    else
                        shouldCancel = ((CustomItem) customItem).onClick(event.getPlayer(), ItemAction.of(event.getAction()), event.getClickedBlock(), event.getBlockFace(), itemStack);

                    if(shouldCancel)
                        event.setCancelled(true);
                });
    }

    @EventHandler
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
        final Player player = event.getPlayer();
        final ItemStack itemStack = player.getItemInHand();

        if(itemStack == null || !itemStack.hasItemMeta())
            return;

        commonRegistry.getCustomItems().stream()
                .filter(CustomItem.class::isInstance)
                .filter(customItem -> customContentAPI.isCustomItem(itemStack, customItem.getRegistryName()))
                .findFirst().ifPresent(customItem -> {
                    boolean shouldCancel = ((CustomItem) customItem).onEntityClick(player, event.getRightClicked(), itemStack);

                    if(shouldCancel)
                        event.setCancelled(true);
                });
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if(event.getDamager() instanceof Player) {
            final Player damager = (Player) event.getDamager();
            final ItemStack itemStack = damager.getItemInHand();

            if(itemStack == null || !itemStack.hasItemMeta())
                return;

            commonRegistry.getCustomItems().stream()
                    .filter(CustomItem.class::isInstance)
                    .filter(customItem -> customContentAPI.isCustomItem(itemStack, customItem.getRegistryName()))
                    .findFirst().ifPresent(customItem -> {
                        boolean shouldCancel = ((CustomItem) customItem).hurtEntity(damager, event.getEntity(), itemStack);

                        if(shouldCancel)
                            event.setCancelled(true);
                    });
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        final Player player = event.getPlayer();
        final ItemStack itemStack = player.getItemInHand();

        if(itemStack == null || !itemStack.hasItemMeta())
            return;

        commonRegistry.getCustomItems().stream()
                .filter(CustomItem.class::isInstance)
                .filter(customItem -> customContentAPI.isCustomItem(itemStack, customItem.getRegistryName()))
                .findFirst().ifPresent(customItem -> {
                    boolean shouldCancel = ((CustomItem) customItem).mineBlock(player, event.getBlock(), event.getExpToDrop(), itemStack);

                    if(shouldCancel)
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
                    boolean shouldCancel = ((CustomFood) customItem).onEat(event.getPlayer(), itemStack);

                    if(shouldCancel)
                        event.setCancelled(true);
                });
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onAnvilClick(InventoryClickEvent event) {
        if(!shouldFixAnvil)
            return;

        final Inventory inventory = event.getInventory();
        if(!inventory.getType().equals(InventoryType.ANVIL) || event.getRawSlot() != 2)
            return;

        final ItemStack input = inventory.getItem(0);
        final ItemStack output = inventory.getItem(2);

        if(input == null || !input.hasItemMeta() || output == null || !output.hasItemMeta())
            return;

        final String registryName = NBTHelper.getStringInNBTTag(input, "RegistryName");
        if(registryName.isEmpty())
            return;

        final ItemStack newOutput = output.clone();

        final ItemMeta outputMeta = newOutput.getItemMeta();
        outputMeta.setDisplayName(input.getItemMeta().getDisplayName());
        newOutput.setItemMeta(outputMeta);

        event.setCurrentItem(newOutput);
    }
}
