package net.vadamdev.customcontent.internal.impl;

import net.vadamdev.customcontent.api.RecipeRegistry;
import net.vadamdev.customcontent.api.recipes.ShapedRecipe;
import net.vadamdev.customcontent.internal.CustomContentPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import java.util.*;

/**
 * @author VadamDev
 * @since 08/07/2023
 */
public class RecipeRegistryImpl implements RecipeRegistry {
    private final Set<ItemStack> toRemove;
    private final List<ShapedRecipe> customCraftings;

    private final Set<Recipe> vanillaRecipes;

    public RecipeRegistryImpl() {
        this.toRemove = new HashSet<>();
        this.customCraftings = new ArrayList<>();

        this.vanillaRecipes = new HashSet<>();
    }

    @Override
    public void registerShapedRecipe(ShapedRecipe shapedRecipe) {
        customCraftings.add(shapedRecipe);
    }

    @Override
    public void removeVanillaRecipe(ItemStack result) {
        toRemove.add(result);
    }

    public void complete(Server server) {
        processCraftingRemoving(server);
        processCraftingAdding(server);
    }

    @Override
    public ShapedRecipe getCraftByResult(ItemStack craftResult) {
        return customCraftings.stream().filter(craft -> craft.getResult().isSimilar(craftResult)).findFirst().orElse(null);
    }

    @Override
    public List<ShapedRecipe> getCustomRecipes() {
        return customCraftings;
    }

    public Set<Recipe> getVanillaRecipes() {
        return vanillaRecipes;
    }

    private void processCraftingRemoving(Server server) {
        if(toRemove.isEmpty())
            return;

        final Iterator<Recipe> recipeIterator = server.recipeIterator();
        while(recipeIterator.hasNext()) {
            final ItemStack recipeOutput = recipeIterator.next().getResult();

            if(toRemove.stream().anyMatch(result -> result.getType().equals(recipeOutput.getType()) && result.getData().getData() == recipeOutput.getData().getData()))
                recipeIterator.remove();
        }

        if(!toRemove.isEmpty())
            CustomContentPlugin.instance.getLogger().info("-> Removed " + toRemove.size() + " recipes !");

        toRemove.clear();
    }

    private void processCraftingAdding(Server server) {
        //Save default recipes
        final Iterator<Recipe> recipeIterator = Bukkit.recipeIterator();
        while(recipeIterator.hasNext())
            vanillaRecipes.add(recipeIterator.next());

        //Register custom recipes
        customCraftings.forEach(craft -> {
            final org.bukkit.inventory.ShapedRecipe recipe = new org.bukkit.inventory.ShapedRecipe(craft.getResult());
            recipe.shape(craft.getShape());
            craft.getIngredients().forEach((c, k) -> recipe.setIngredient(c.toCharArray()[0], k.getData()));

            server.addRecipe(recipe);
        });

        if(!customCraftings.isEmpty())
            CustomContentPlugin.instance.getLogger().info("-> Added " + customCraftings.size() + " custom recipes !");
    }
}
