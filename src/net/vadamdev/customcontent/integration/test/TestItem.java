package net.vadamdev.customcontent.integration.test;

import net.vadamdev.customcontent.api.items.CustomItem;
import net.vadamdev.customcontent.api.items.DurabilityProvider;
import net.vadamdev.customcontent.api.items.IDurabilityBar;
import net.vadamdev.customcontent.api.items.durabilitybar.NumberDurabilityBar;
import net.vadamdev.customcontent.lib.events.ItemUseEvent;
import net.vadamdev.viaapi.tools.builders.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class TestItem extends CustomItem implements DurabilityProvider {
    public TestItem() {
        super(new ItemBuilder(Material.BARRIER).setName("Test Item").setLore("This is a test item", "%bar%").toItemStack());
        itemStack = setDefaultDurability(itemStack);
    }

    @Override
    public String getRegistryName() {
        return "test_item";
    }

    @Override
    public Consumer<ItemUseEvent> getInteractAction() {
        return event -> {
            Player player = event.getPlayer();
            ItemStack item = event.getItem();

            player.setItemInHand(setDurability(item, getDurability(item) - 1));
            updateDurabilityBar(player.getItemInHand(), defaultLore);
            checkDurability(player, item);
        };
    }

    /*@Override
    public Consumer<ItemBreakBlockEvent> getBlockBreakAction() {
        return event -> {
            event.getBlock().setType(Material.OBSIDIAN);
        };
    }*/

    @Override
    public int getMaxDurability() {
        return 10;
    }

    @Override
    public IDurabilityBar getDurabilityBar() {
        return new NumberDurabilityBar("Durability : ", "%bar%");
    }

    @Override
    public boolean isConfigurable() {
        return false;
    }
}
