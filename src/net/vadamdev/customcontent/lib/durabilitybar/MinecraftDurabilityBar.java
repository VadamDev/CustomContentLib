package net.vadamdev.customcontent.lib.durabilitybar;

import net.vadamdev.customcontent.api.items.IDurabilityBar;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * This durability bar use the default minecraft durability bar as display.
 * It works by calculating the proportional value of the durability with a custom max durability
 * @author VadamDev
 * @since 23/02/22
 */
public class MinecraftDurabilityBar implements IDurabilityBar {
    @Override
    public String createDurabilityBar(int durability, int maxDurability) {
        return null;
    }

    @Override
    public String getPlaceholder() {
        return null;
    }

    @Override
    public void applyDurabilityBar(ItemStack itemStack, List<String> lore, int durability, int maxDurability) {
        int materialMaxDurability = itemStack.getType().getMaxDurability();
        itemStack.setDurability((short)  (materialMaxDurability - durability * materialMaxDurability / maxDurability));
    }
}
