package net.vadamdev.customcontent.craftings;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

public class CraftListener implements Listener {
	@EventHandler
	public void onCraft(PrepareItemCraftEvent event) {
		if (event.getInventory().getResult() != null) {
			Craft craft = getByResult(event.getInventory().getResult());

			if (craft != null && !equals(event.getInventory().getContents(), craft.toArray()))
				event.getInventory().setResult(new ItemStack(Material.AIR, 0));
		}
	}

	private boolean equals(ItemStack[] items, ItemStack[] otherItems) {
		for (int a = 1; a <= 9; a++) {
			ItemStack item = items[a];
			ItemStack otherItem = otherItems[a];

			if(item.getType().equals(Material.AIR) && otherItem.getType().equals(Material.AIR)) continue;
			if(!item.isSimilar(otherItem)) return false;
		}

		return true;
	}

	private Craft getByResult(ItemStack result) {
		return CraftingRegistry.getCraftings().parallelStream().filter(craft -> craft.getResult().isSimilar(result)).findAny().orElse(null);
	}
}
