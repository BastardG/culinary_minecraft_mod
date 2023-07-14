package ru.bastard.culinary.util;

import com.google.gson.JsonArray;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.crafting.Ingredient;

public class RecipeUtil {

    public static NonNullList<Ingredient> readIngredients(JsonArray ingredientArray) {
        NonNullList<Ingredient> nonnulllist = NonNullList.create();
        for (int i = 0; i < ingredientArray.size(); ++i) {
            Ingredient ingredient = Ingredient.fromJson(ingredientArray.get(i));
            if (!ingredient.isEmpty()) {
                nonnulllist.add(ingredient);
            }
        }
        return nonnulllist;
    }

}
