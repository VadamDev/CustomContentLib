package net.vadamdev.customcontent.craftings;

import net.vadamdev.viaapi.VIAPI;
import org.bukkit.inventory.ShapedRecipe;

import java.util.ArrayList;
import java.util.List;

public class CraftingRegistry {
    private static final List<Craft> craftings = new ArrayList<>();

    public static void registerCraft(Craft craft) {
        craftings.add(craft);
    }

    public static void registerCrafting(Craft craft) {
        craftings.add(craft);
    }

    public static void registerAll() {
        craftings.forEach(craft -> {
            ShapedRecipe recipe = new ShapedRecipe(craft.getResult());
            recipe.shape(craft.getShape());
            craft.getIngredients().forEach((c, k) -> recipe.setIngredient(c.toCharArray()[0], k.getData()));

            VIAPI.get().getServer().addRecipe(recipe);
        });
    }

    public static List<Craft> getRegisteredCrafting() {
        return craftings;
    }
}
