package ru.bastard.culinary.crafting;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.Arrays;

public class MixingRecipe implements Recipe<SimpleContainer> {

    private Ingredient[] ingredients;
    private FluidStack[] fluidsToMix;
    private FluidStack resultingFluid;

    //Use other matches
    @Override
    public boolean matches(SimpleContainer simpleContainer, Level level) {
        return false;
    }

    public boolean matches(FluidStack[] fluidsToMix, SimpleContainer simpleContainer, Level level) {
        FluidStack[] sortedFluidsFromRecipe = Arrays.copyOf(this.fluidsToMix, this.fluidsToMix.length);
        FluidStack[] sortedFluidsFromArgs = Arrays.copyOf(fluidsToMix, fluidsToMix.length);
        ItemStack[] ingredientsFromContainer = containerToArray(simpleContainer);
        Ingredient[] ingredientsSorted = Arrays.copyOf(ingredients, ingredients.length);
        if (!(sortedFluidsFromRecipe.length == sortedFluidsFromArgs.length)) {
            return false;
        }

        if (!(ingredientsFromContainer.length == ingredients.length)) {
            return false;
        }

        Arrays.sort(sortedFluidsFromRecipe);
        Arrays.sort(sortedFluidsFromArgs);
        Arrays.sort(ingredientsSorted);
        Arrays.sort(ingredientsFromContainer);

        for (int i = 0; i < sortedFluidsFromArgs.length; i++) {
            if (!(sortedFluidsFromRecipe[i].isFluidEqual(sortedFluidsFromArgs[i]))) {
                return false;
            }
        }

        for (int i = 0; i < ingredients.length; i++) {
            if (!ingredientsSorted[i].test(ingredientsFromContainer[i])) {
                return false;
            }
        }

        return true;
    }

    private ItemStack[] containerToArray(SimpleContainer simpleContainer) {
        ItemStack[] arr = new ItemStack[simpleContainer.getContainerSize()];
        for (int i = 0; i < simpleContainer.getContainerSize(); i++) {
            arr[i] = simpleContainer.getItem(i);
        }
        return arr;
    }

    @Override
    public ItemStack assemble(SimpleContainer simpleContainer) {
        return null;
    }

    @Override
    public boolean canCraftInDimensions(int p_43999_, int p_44000_) {
        return false;
    }

    @Override
    public ItemStack getResultItem() {
        return null;
    }

    @Override
    public ResourceLocation getId() {
        return null;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return null;
    }

    @Override
    public RecipeType<?> getType() {
        return null;
    }

}