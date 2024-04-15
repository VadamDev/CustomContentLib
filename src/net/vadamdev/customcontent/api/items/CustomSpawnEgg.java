package net.vadamdev.customcontent.api.items;

import net.vadamdev.customcontent.api.entities.CustomEntityContainer;
import net.vadamdev.customcontent.lib.ItemAction;
import net.vadamdev.customcontent.lib.utils.NBTHelper;
import net.vadamdev.viapi.tools.builders.ItemBuilder;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;

/**
 * @author VadamDev
 * @since 01/09/2023
 */
public abstract class CustomSpawnEgg extends CustomItem {
    protected final CustomEntityContainer<?> container;

    public CustomSpawnEgg(CustomEntityContainer<?> container) {
        this(ItemBuilder.item(Material.MONSTER_EGG, 1, (short) container.getNMSEntityId()).setName("§rSpawn " + container.getName()).build(), container);
    }

    public CustomSpawnEgg(ItemStack itemStack, CustomEntityContainer<?> container) {
        super(itemStack);
        this.itemStack = NBTHelper.setStringInNBTTag(itemStack, "RegistryName", getRegistryName());

        this.container = container;
    }

    @Override
    public boolean onClick(Player player, ItemAction action, @Nullable Block block, @Nullable BlockFace blockFace, ItemStack item) {
        if(!action.equals(ItemAction.RIGHT_CLICK_BLOCK))
            return false;

        if(!player.getGameMode().equals(GameMode.CREATIVE)) {
            if (player.getItemInHand().getAmount() == 1)
                player.getEquipment().setItemInHand(null);
            else
                player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
        }

        container.spawn(block.getRelative(blockFace).getLocation().clone().add(0.5, 0, 0.5), CreatureSpawnEvent.SpawnReason.SPAWNER_EGG);

        return true;
    }
}
