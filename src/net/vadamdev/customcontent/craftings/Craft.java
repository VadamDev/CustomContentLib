package net.vadamdev.customcontent.craftings;

import net.vadamdev.customcontent.lib.ItemRegistry;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class Craft {
    private final String[] shape;
    private final ItemStack result;
    private final Map<String, ItemStack> ingredients;

    public Craft(String[] shape, ItemStack result) {
        this.shape = shape;
        this.result = result;
        this.ingredients = new HashMap<>();
    }

    public Craft addIngredient(String shade, String registryName) {
        this.ingredients.put(shade, ItemRegistry.getCustomItemAsItemStack(registryName));
        return this;
    }

    public Craft addIngredient(String shade, ItemStack item) {
        this.ingredients.put(shade, item);
        return this;
    }

    public Craft addIngredient(String shade, Material item) {
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
        ItemStack[] items = new ItemStack[(shape.length * 3) + 1];
        int index = 1;
        for (String shape : this.shape) {
            String[] tmpShapes = shape.split("");
            for (String currentShape : tmpShapes) {
                items[index++] = ingredients.getOrDefault(currentShape, new ItemStack(Material.AIR, 0));
            }
        }
        return items;
    }

}