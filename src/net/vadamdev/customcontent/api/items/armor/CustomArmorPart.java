package net.vadamdev.customcontent.api.items.armor;

import net.vadamdev.customcontent.api.items.CustomItem;
import net.vadamdev.customcontent.api.items.DurabilityProvider;
import net.vadamdev.customcontent.lib.events.ItemUseEvent;
import net.vadamdev.viaapi.tools.builders.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

/**
 * @author VadamDev
 * @since 30.12.2021
 */
public abstract class CustomArmorPart extends CustomItem implements DurabilityProvider {
    public CustomArmorPart(ArmorType armorType, ArmorPart armorPart, String name, String... lore) {
        super(new ItemBuilder(armorType.get(armorPart)).setName(name).setLore(lore).toItemStack());
        itemStack = setDefaultDurability(itemStack);
    }

    public ItemStack decreaseDurability(Player holder, ItemStack itemStack) {
        ItemStack nItem = setDurability(itemStack, getDurability(itemStack) - 1);

        if(getDurability(itemStack) <= 0) return null;

        Bukkit.broadcastMessage(getDurability(itemStack) + "/" + getMaxDurability());

        if(shouldUseDefaultDurabilityBar()) {
            int itemStackMaxDurability = itemStack.getType().getMaxDurability();
            int customMaxDurability = getMaxDurability();
            int durability = getDurability(itemStack);

            short logicDurability = (short) (itemStackMaxDurability * durability / customMaxDurability);

            nItem.setDurability(logicDurability);
        }

        return nItem;
    }

    public boolean shouldUseDefaultDurabilityBar() {
        return true;
    }

    @Override
    public Consumer<ItemUseEvent> getInteractAction() {
        return null;
    }
}
