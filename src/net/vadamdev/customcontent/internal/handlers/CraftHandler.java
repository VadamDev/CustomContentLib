package net.vadamdev.customcontent.internal.handlers;

import net.vadamdev.customcontent.api.common.IRegistrable;
import net.vadamdev.customcontent.api.recipes.ShapedRecipe;
import net.vadamdev.customcontent.internal.CustomContentPlugin;
import net.vadamdev.customcontent.internal.impl.CustomContentAPIImpl;
import net.vadamdev.customcontent.internal.impl.RecipeRegistryImpl;
import net.vadamdev.customcontent.internal.registry.CommonRegistry;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

public class CraftHandler implements Listener {
	private final CustomContentAPIImpl customContentAPI;

	private final RecipeRegistryImpl recipeRegistry;
	private final CommonRegistry commonRegistry;

	public CraftHandler() {
		this.customContentAPI = CustomContentPlugin.instance.getCustomContentAPI();

		this.recipeRegistry = CustomContentPlugin.instance.getRecipeRegistry();
		this.commonRegistry = CustomContentPlugin.instance.getCommonRegistry();
	}

	@EventHandler
	public void onCraft(PrepareItemCraftEvent event) {
		final CraftingInventory craftingInventory = event.getInventory();
		final ItemStack currentResult = craftingInventory.getResult();

		if(currentResult == null || currentResult.getType().equals(Material.AIR))
			return;

		final ShapedRecipe shapedRecipe = recipeRegistry.getCraftByResult(currentResult);
		final ItemStack[] craftMatrix = craftingInventory.getContents();

		if(shapedRecipe != null) {
			if(!equals(craftMatrix, shapedRecipe.toArray())) {
				craftingInventory.setResult(new ItemStack(Material.AIR));
			}
		}else if(recipeRegistry.getVanillaRecipes().stream().anyMatch(recipe -> recipe.getResult().isSimilar(currentResult))) {
			for (ItemStack item : craftMatrix) {
				if(item == null || item.getType().equals(Material.AIR))
					continue;

				for (IRegistrable customItem : commonRegistry.getCustomItems()) {
					if(customContentAPI.isCustomItem(item, customItem.getRegistryName())) {
						craftingInventory.setResult(new ItemStack(Material.AIR));
						return;
					}
				}
			}
		}
	}

	private boolean equals(ItemStack[] inventoryMatrix, ItemStack[] craftingMatrix) {
		for (int i = 0; i < 9; i++) {
			final ItemStack item = inventoryMatrix[i];
			final ItemStack otherItem = craftingMatrix[i];

			if(item == null || otherItem == null || item.getType().equals(Material.AIR) || otherItem.getType().equals(Material.AIR))
				continue;

			if(!item.isSimilar(otherItem))
				return false;
		}

		return true;
	}
}
