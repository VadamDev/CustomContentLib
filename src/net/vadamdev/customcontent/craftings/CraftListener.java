package net.vadamdev.customcontent.craftings;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

public class CraftListener implements Listener {
	@EventHandler
	public void onCraft(PrepareItemCraftEvent event) {
		CraftingInventory inventory = event.getInventory();
		ItemStack result = inventory.getResult();

		if (result != null && !result.getType().equals(Material.AIR)) {
			Craft craft = getByResult(result);

			if(craft != null && !equals(inventory.getContents(), craft.toArray()))
				inventory.setResult(new ItemStack(Material.AIR));
		}

		CraftingRegistry.vanillaRecipeSet.stream().filter(recipe -> recipe.getResult().equals(result)).forEach(recipe -> {
			CraftingRegistry.getCustomCraftings().forEach(craft -> {
				if(contains(inventory.getContents(), craft.toArray()))
					inventory.setResult(new ItemStack(Material.AIR));
			});
		});
	}

	private boolean equals(ItemStack[] inventoryMatrix, ItemStack[] craftingMatrix) {
		for (int i = 0; i < 9; i++) {
			ItemStack item = inventoryMatrix[i];
			ItemStack otherItem = craftingMatrix[i];

			if(item == null || otherItem == null || item.getType().equals(Material.AIR) && otherItem.getType().equals(Material.AIR))
				continue;

			if(!item.isSimilar(otherItem))
				return false;
		}

		return true;
	}

	private boolean contains(ItemStack[] inventoryMatrix, ItemStack[] craftingMatrix) {
		for (ItemStack i1 : inventoryMatrix) {
			for (ItemStack i2 : craftingMatrix) {
				if(!i1.hasItemMeta())
					continue;

				if(i1.isSimilar(i2))
					return true;
			}
		}

		return false;
	}

	private Craft getByResult(ItemStack result) {
		return CraftingRegistry.getCustomCraftings().parallelStream().filter(craft -> craft.getResult().isSimilar(result)).findFirst().orElse(null);
	}
}
