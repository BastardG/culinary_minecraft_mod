package ru.bastard.culinary.crafting;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import ru.bastard.culinary.Culinary;
import ru.bastard.culinary.crafting.cutting.CuttingBoardRecipe;

public class ModRecipeSerializers {

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
        DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Culinary.MOD_ID);

    public static final RegistryObject<RecipeSerializer<?>> CUTTING =
            RECIPE_SERIALIZERS.register("cutting", CuttingBoardRecipe.Serializer::new);

}
