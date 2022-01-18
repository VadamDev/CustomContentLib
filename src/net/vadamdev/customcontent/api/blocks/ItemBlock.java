package net.vadamdev.customcontent.api.blocks;

import net.vadamdev.customcontent.api.items.CustomItem;
import net.vadamdev.customcontent.lib.events.ItemUseEvent;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public abstract class ItemBlock extends CustomItem {
    public ItemBlock(ItemStack itemStack) {
        super(itemStack);
    }

    public Material getPlacedMaterial() {
        return itemStack.getType();
    }

    @Override
    public Consumer<ItemUseEvent> getInteractAction() {
        return event -> {
            if(event.getClickedBlock() != null && event.getBlockFace() != null) event.getClickedBlock().getRelative(event.getBlockFace()).setType(getPlacedMaterial());
        };
    }
}
