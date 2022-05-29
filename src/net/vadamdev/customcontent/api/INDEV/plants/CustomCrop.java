package net.vadamdev.customcontent.api.INDEV.plants;

import net.vadamdev.customcontent.api.items.CustomItem;
import net.vadamdev.customcontent.lib.events.ItemUseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

/**
 * @author VadamDev
 * @since 23/04/2022
 */
public abstract class CustomCrop extends CustomItem {
    private final CustomPlant customPlant;

    public CustomCrop(ItemStack itemStack, CustomPlant customPlant) {
        super(itemStack);
        this.customPlant = customPlant;
    }

    @Override
    public Consumer<ItemUseEvent> getInteractAction() {
        return event -> {

        };
    }
}
