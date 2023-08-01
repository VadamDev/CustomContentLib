package net.vadamdev.customcontent.api.items;

import net.vadamdev.customcontent.api.common.IRegistrable;
import net.vadamdev.customcontent.lib.ItemAction;
import net.vadamdev.customcontent.lib.utils.NBTHelper;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Represents a CCL's Item
 *
 * @author VadamDev
 * @since 22/12/2021
 */
public abstract class CustomItem implements IRegistrable {
    protected ItemStack itemStack;
    protected final List<String> defaultLore;

    public CustomItem(ItemStack itemStack) {
        this.itemStack = NBTHelper.setStringInNBTTag(itemStack, "RegistryName", getRegistryName());
        this.defaultLore = itemStack.getItemMeta().getLore();
    }

    /**
     * Called when a {@link Player} interacts with a block or air with the {@link CustomItem}
     *
     * @param player {@link Player} who clicked
     * @param action {@link ItemAction}
     * @param block Clicked {@link Block} or null if it was air
     * @param blockFace Clicked {@link BlockFace} or null if clicked {@link Block} was air
     * @param item {@link ItemStack}
     * @return True if the {@link org.bukkit.event.player.PlayerInteractEvent PlayerInteractEvent} should be cancelled
     */
    public boolean onClick(Player player, ItemAction action, @Nullable Block block, @Nullable BlockFace blockFace, ItemStack item) {
        return false;
    }

    /**
     * Called when a {@link Player} right click on an {@link Entity} with the {@link CustomItem}
     *
     * @param player {@link Player} who clicked
     * @param clicked Clicked {@link Entity}
     * @param item {@link ItemStack}
     * @return True if the {@link org.bukkit.event.player.PlayerInteractAtEntityEvent PlayerInteractAtEntityEvent} should be cancelled
     */
    public boolean onEntityClick(Player player, Entity clicked, ItemStack item) {
        return false;
    }

    /**
     * Called when a {@link Player} hit an {@link Entity} with the {@link CustomItem}
     *
     * @param player {@link Player} who damaged the {@link Entity}
     * @param victim Damaged {@link Entity}
     * @param item {@link ItemStack}
     * @return True if the {@link org.bukkit.event.entity.EntityDamageByEntityEvent EntityDamageByEntityEvent} should be cancelled
     */
    public boolean hurtEntity(Player player, Entity victim, ItemStack item) {
        return false;
    }

    /**
     * Called when a {@link Player} break a block with the {@link CustomItem}
     *
     * @param player {@link Player} involved in this action
     * @param block {@link Block} involved in this action
     * @param exp The amount of experience that the Block will drop if the {@link org.bukkit.event.block.BlockBreakEvent BlockBreakEvent} is not cancelled
     * @param item {@link ItemStack}
     * @return True if the {@link org.bukkit.event.block.BlockBreakEvent BlockBreakEvent} should be cancelled
     */
    public boolean mineBlock(Player player, Block block, int exp, ItemStack item) {
        return false;
    }

    @Nullable
    @Override
    public List<String> getDefaultLore() {
        return defaultLore;
    }

    @Nonnull
    @Override
    public ItemStack getItemStack() {
        return itemStack;
    }
}
