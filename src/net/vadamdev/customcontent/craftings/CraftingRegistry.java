package net.vadamdev.customcontent.craftings;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * @author VadamDev
 */
public class CraftingRegistry {
    private static final List<Craft> craftings = new ArrayList<>();

    public static void registerCrafting(Craft craft) {
        craftings.add(craft);
    }

    public static void complete(JavaPlugin plugin) {
        craftings.parallelStream().forEach(craft -> {
            ShapedRecipe recipe = new ShapedRecipe(craft.getResult());
            recipe.shape(craft.getShape());
            craft.getIngredients().forEach((c, k) -> recipe.setIngredient(c.toCharArray()[0], k.getData()));

            plugin.getServer().addRecipe(recipe);
        });

        Bukkit.getConsoleSender().sendMessage("Loaded " + craftings.size() + " craftings !");
    }

    public static List<Craft> getCraftings() {
        return craftings;
    }
}
