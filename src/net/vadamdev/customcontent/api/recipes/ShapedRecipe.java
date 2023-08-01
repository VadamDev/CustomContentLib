package net.vadamdev.customcontent.api.recipes;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class ShapedRecipe {
	private final String[] shape;
	private final ItemStack result;
	private final Map<String, ItemStack> ingredients;

	public ShapedRecipe(String[] shape, ItemStack result) {
		this.shape = shape;
		this.result = result;
		this.ingredients = new HashMap<>();
	}

	public ShapedRecipe addIngredient(String shade, ItemStack item) {
		this.ingredients.put(shade, item);
		return this;
	}

	public ShapedRecipe addIngredient(String shade, Material item) {
		this.ingredients.put(shade, new ItemStack(item));
		return this;
	}

	public String[] getShape() {
		return shape;
	}

	public ItemStack getResult() {
		return result;
	}

	public Map<String, ItemStack> getIngredients() {
		return ingredients;
	}

	public ItemStack[] toArray() {
		final ItemStack[] items = new ItemStack[(shape.length * 3) + 1];

		int index = 1;
		for (String shape : this.shape) {
			String[] tempShapes = shape.split("");
			for (String currentShape : tempShapes)
				items[index++] = ingredients.getOrDefault(currentShape, new ItemStack(Material.AIR));
		}

		return items;
	}

}
