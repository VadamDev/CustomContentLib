package net.vadamdev.customcontent.craftings;

import net.vadamdev.customcontent.CustomContentLib;
import net.vadamdev.customcontent.api.IRegistrable;
import net.vadamdev.customcontent.internal.CommonRegistry;
import net.vadamdev.customcontent.lib.CustomContentRegistry;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

public class CraftListener implements Listener {
	private final CommonRegistry commonRegistry;

	public CraftListener() {
		this.commonRegistry = CustomContentLib.instance.getCommonRegistry();
	}

	@EventHandler
	public void onCraft(PrepareItemCraftEvent event) {
		CraftingInventory craftingInventory = event.getInventory();
		ItemStack currentResult = craftingInventory.getResult();

		if (currentResult != null && !currentResult.getType().equals(Material.AIR)) {
			ItemStack[] craftMatrix = craftingInventory.getContents();
			Craft craft = CraftingRegistry.getCraftByResult(currentResult);

			if(craft != null) {
				if(!equals(craftMatrix, craft.toArray())) {
					craftingInventory.setResult(new ItemStack(Material.AIR));
				}
			}else if(CraftingRegistry.vanillaRecipeSet.parallelStream().anyMatch(recipe -> recipe.getResult().isSimilar(currentResult))) {
				for (ItemStack item : craftMatrix) {
					if(item == null || item.getType().equals(Material.AIR))
						continue;

					for (IRegistrable customItem : commonRegistry.getCustomItems()) {
						if(CustomContentRegistry.isCustomItem(item, customItem.getRegistryName())) {
							craftingInventory.setResult(new ItemStack(Material.AIR));
							return;
						}
					}
				}
			}
		}
	}

	private boolean equals(ItemStack[] inventoryMatrix, ItemStack[] craftingMatrix) {
		for (int i = 0; i < 9; i++) {
			ItemStack item = inventoryMatrix[i];
			ItemStack otherItem = craftingMatrix[i];

			if(item == null || otherItem == null || item.getType().equals(Material.AIR) || otherItem.getType().equals(Material.AIR))
				continue;

			if(!item.isSimilar(otherItem))
				return false;
		}

		return true;
	}
}
