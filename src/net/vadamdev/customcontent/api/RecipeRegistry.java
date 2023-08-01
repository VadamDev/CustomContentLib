package net.vadamdev.customcontent.api;

import net.vadamdev.customcontent.api.recipes.ShapedRecipe;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @author VadamDev
 * @since 08/07/2023
 */
public interface RecipeRegistry {
    void registerShapedRecipe(ShapedRecipe shapedRecipe);
    void removeVanillaRecipe(ItemStack result);

    ShapedRecipe getCraftByResult(ItemStack craftResult);

    List<ShapedRecipe> getCustomRecipes();
}
