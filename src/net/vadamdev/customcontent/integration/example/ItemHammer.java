package net.vadamdev.customcontent.integration.example;

import net.vadamdev.customcontent.api.items.CustomItem;
import net.vadamdev.customcontent.lib.events.ItemBreakBlockEvent;
import net.vadamdev.viaapi.tools.builders.ItemBuilder;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.function.Consumer;

/**
 * DONT USE THIS CLASS IT'S USED FOR DEMONSTRATION PURPOSES !
 *
 * An Item Exemple
 * @author VadamDev
 * @since 24/02/2022
 */
public class ItemHammer extends CustomItem {
    public ItemHammer() {
        super(new ItemBuilder(Material.DIAMOND_PICKAXE).setName("Hammer").toItemStack());
    }

    @Override
    public Consumer<ItemBreakBlockEvent> getBlockBreakAction() {
        return event -> {
            int x = event.getBlock().getX();
            int y = event.getBlock().getY();
            int z = event.getBlock().getZ();

            for (int ix = -1; ix < 2; ix++) for (int iy = -1; iy < 2; iy++) for (int iz = -1; iz < 2; iz++) {
                new Location(event.getBlock().getWorld(), x + ix, y + iy, z + iz).getBlock().breakNaturally();
            }
        };
    }

    @Override
    public String getRegistryName() {
        return "hammer";
    }
}
