package ru.bastard.culinary.crafting;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import ru.bastard.culinary.Culinary;
import ru.bastard.culinary.crafting.cutting.CuttingBoardRecipe;

public class ModRecipeTypes {

    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, Culinary.MOD_ID);

    public static final RegistryObject<RecipeType<CuttingBoardRecipe>> CUTTING =
            RECIPE_TYPES.register("cutting", () -> registerRecipeType("cutting"));

    public static <T extends Recipe<?>> RecipeType<T> registerRecipeType(final String identifier) {
        return new RecipeType<>()
        {
            public String toString() {
                return Culinary.MOD_ID + ":" + identifier;
            }
        };
    }

}
