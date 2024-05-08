package net.vadamdev.customcontent.internal.menu;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;
import net.vadamdev.customcontent.internal.CustomContentPlugin;
import net.vadamdev.customcontent.internal.registry.CommonRegistry;
import net.vadamdev.viapi.tools.builders.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author VadamDev
 * @since 04/05/2024
 */
public class CustomContentMenu implements InventoryProvider {
    private static final SmartInventory inventory = SmartInventory.builder()
            .id("ccl_menu")
            .title("§8▌ CCL Items")
            .size(6, 9)
            .provider(new CustomContentMenu())
            .build();

    public static SmartInventory get() {
        return inventory;
    }

    private final List<ItemStack> customItems;

    public CustomContentMenu() {
        final CommonRegistry commonRegistry = CustomContentPlugin.instance.getCommonRegistry();

        this.customItems = commonRegistry.getCustomItems().stream()
                .map(registrable -> commonRegistry.getCustomItemAsItemStack(registrable.getRegistryName()))
                .peek(itemStack -> itemStack.setAmount(1))
                .collect(Collectors.toList());
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(ClickableItem.empty(ItemBuilder.item(Material.STAINED_GLASS_PANE, 1, (byte) 15).setName(" ").build()));

        final ClickableItem[] items = new ClickableItem[customItems.size()];
        for(int i = 0; i < customItems.size(); i++) {
            final ItemStack customItem = customItems.get(i).clone();
            items[i] = ClickableItem.of(customItem, e -> e.getWhoClicked().getInventory().addItem(customItem));
        }

        final Pagination pagination = contents.pagination();
        pagination.setItems(items);
        pagination.setItemsPerPage(28);
        pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 1, 1).allowOverride(false));

        if(!pagination.isFirst())
            contents.set(5, 3, ClickableItem.of(ItemBuilder.skull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzZlYmFhNDFkMWQ0MDVlYjZiNjA4NDViYjlhYzcyNGFmNzBlODVlYWM4YTk2YTU1NDRiOWUyM2FkNmM5NmM2MiJ9fX0=").setName("§fPrevious").build(),
                    e -> inventory.open(player, pagination.previous().getPage())));
        else
            contents.set(5, 3, ClickableItem.empty(ItemBuilder.item(Material.BARRIER).setName("§c§mPrevious").build()));

        if(!pagination.isLast())
            contents.set(5, 5, ClickableItem.of(ItemBuilder.skull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODM5OWU1ZGE4MmVmNzc2NWZkNWU0NzJmMzE0N2VkMTE4ZDk4MTg4NzczMGVhN2JiODBkN2ExYmVkOThkNWJhIn19fQ==").setName("§fNext").build(),
                    e -> inventory.open(player, pagination.next().getPage())));
        else
            contents.set(5, 5, ClickableItem.empty(ItemBuilder.item(Material.BARRIER).setName("§c§mNext").build()));
    }
}
