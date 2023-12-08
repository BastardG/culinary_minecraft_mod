package ru.bastard.culinary.datagen;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import ru.bastard.culinary.item.ModItems;
import ru.bastard.culinary.tags.ForgeTags;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        knifeRecipe(ModItems.WOODEN_KNIFE.get(), ForgeTags.PLANKS, consumer);
        knifeRecipe(ModItems.STONE_KNIFE.get(), Items.COBBLESTONE, consumer);
        knifeRecipe(ModItems.GOLDEN_KNIFE.get(), Items.GOLD_INGOT, consumer);
        knifeRecipe(ModItems.IRON_KNIFE.get(), Items.IRON_INGOT, consumer);
        knifeRecipe(ModItems.DIAMOND_KNIFE.get(), Items.DIAMOND, consumer);
        netheriteSmithing(consumer, ModItems.DIAMOND_KNIFE.get(), RecipeCategory.TOOLS, ModItems.NETHERITE_KNIFE.get());
    }

    private static void knifeRecipe(ItemLike result, TagKey<Item> tag, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder
                .shaped(RecipeCategory.TOOLS, result)
                .define('W', tag)
                .define('S', Items.STICK)
                .pattern(" W")
                .pattern("S ")
                .unlockedBy(getHasName(result), has(result))
                .group("knifes")
                .save(consumer);
    }

    private static void knifeRecipe(ItemLike result, ItemLike recipeItem, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder
                .shaped(RecipeCategory.TOOLS, result)
                .define('W', recipeItem)
                .define('S', Items.STICK)
                .pattern(" W")
                .pattern("S ")
                .unlockedBy(getHasName(result), has(result))
                .group("knifes")
                .save(consumer);
    }

}
