package net.vadamdev.customcontent.craftings;

import net.vadamdev.customcontent.CustomContentLib;
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
    protected static final Set<Recipe> vanillaRecipeSet = new HashSet<>();

    private static final Set<ItemStack> toRemove = new HashSet<>();
    private static final Set<Craft> customCraftings = new HashSet<>();

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
        if(toRemove.isEmpty())
            return;

        Iterator<Recipe> recipeIterator = plugin.getServer().recipeIterator();
        while(recipeIterator.hasNext()) {
            ItemStack recipeOutput = recipeIterator.next().getResult();

            if(toRemove.stream().anyMatch(result -> result.getType().equals(recipeOutput.getType()) && result.getData().getData() == recipeOutput.getData().getData()))
                recipeIterator.remove();
        }

        CustomContentLib.instance.getLogger().info("Removed " + toRemove.size() + " vanilla recipes !");
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

        CustomContentLib.instance.getLogger().info("Added " + customCraftings.size() + " custom recipes !");
    }

    public static Craft getCraftByResult(ItemStack craftResult) {
        return CraftingRegistry.getCustomCraftings().stream().filter(craft -> craft.getResult().isSimilar(craftResult)).findFirst().orElse(null);
    }

    public static Set<Craft> getCustomCraftings() {
        return customCraftings;
    }
}
