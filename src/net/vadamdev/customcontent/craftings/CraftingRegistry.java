package net.vadamdev.customcontent.craftings;

import net.vadamdev.customcontent.CustomContentIntegration;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

/**
 * @author VadamDev
 */
public class CraftingRegistry {
    public static final Set<Recipe> vanillaRecipeSet = new HashSet<>();

    private static final Set<ItemStack> toRemove = new HashSet<>();
    private static final List<Craft> customCraftings = new ArrayList<>();

    public static void removeVanillaRecipe(ItemStack result) {
        toRemove.add(result);
    }

    public static void registerCrafting(Craft craft) {
        customCraftings.add(craft);
    }

    public static void complete(JavaPlugin plugin) {
        processCraftingRemoving(plugin);
        processCraftingAdding(plugin);
    }

    private static void processCraftingRemoving(JavaPlugin plugin) {
        if(toRemove.isEmpty()) return;

        Iterator<Recipe> recipeIterator = plugin.getServer().recipeIterator();
        while(recipeIterator.hasNext()) {
            ItemStack recipeOutput = recipeIterator.next().getResult();

            if(toRemove.stream().anyMatch(result -> result.getType().equals(recipeOutput.getType()) && result.getData().getData() == recipeOutput.getData().getData()))
                recipeIterator.remove();
        }

        CustomContentIntegration.instance.getLogger().info("Removed " + toRemove.size() + " vanilla recipes !");
    }

    private static void processCraftingAdding(JavaPlugin plugin) {
        //Save default recipes
        Iterator<Recipe> recipeIterator = Bukkit.recipeIterator();

        while(recipeIterator.hasNext())
            vanillaRecipeSet.add(recipeIterator.next());

        //Register custom recipes
        customCraftings.forEach(craft -> {
            ShapedRecipe recipe = new ShapedRecipe(craft.getResult());
            recipe.shape(craft.getShape());
            craft.getIngredients().forEach((c, k) -> recipe.setIngredient(c.toCharArray()[0], k.getData()));

            plugin.getServer().addRecipe(recipe);
        });

        CustomContentIntegration.instance.getLogger().info("Added " + customCraftings.size() + " custom recipes !");
    }

    public static List<Craft> getCustomCraftings() {
        return customCraftings;
    }
}
