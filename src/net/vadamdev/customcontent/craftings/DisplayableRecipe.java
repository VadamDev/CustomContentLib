package net.vadamdev.customcontent.craftings;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author VadamDev
 */
public class DisplayableRecipe {
    private final ShapedRecipe recipe;

    private final ItemStack output;
    private final Map<Character, ItemStack> ingredientMap;

    public DisplayableRecipe(Craft craft) {
        this.output = craft.getResult();
        this.recipe = new ShapedRecipe(output);

        shape(craft.getShape()[0], craft.getShape()[1], craft.getShape()[2]);
        this.ingredientMap = craft.getIngredientsInChar();
    }

    public void shape(String l1, String l2, String l3) {
        recipe.shape(l1, l2, l3);
    }

    public ItemStack[] getMaterialMatrix() {
        String[] shape = recipe.getShape();
        List<Character> chars = new ArrayList<>();

        int i = 0;
        int j = 0;
        for(int k = 0; k < 9; k++) {
            if(i > 2) {
                i = 0;
                j++;
            }

            chars.add(shape[j].charAt(i));
            i++;
        }

        ItemStack[] stacks = new ItemStack[9];
        int v = 0;
        for (Character aChar : chars) {
            if(ingredientMap.containsKey(aChar) && ingredientMap.get(aChar) != null) stacks[v] = ingredientMap.get(aChar);
            v++;
        }

        return stacks;
    }

    public ItemStack getOutput() {
        return output;
    }
}
