package ru.bastard.culinary.crafting;

import it.unimi.dsi.fastutil.ints.Int2BooleanAVLTreeMap;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import ru.bastard.culinary.Culinary;

public class ModRecipeSerializers {

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
        DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Culinary.MOD_ID);

    public static final RegistryObject<RecipeSerializer<?>> CUTTING =
            RECIPE_SERIALIZERS.register("cutting", () -> CuttingBoardRecipe.Serializer.INSTANCE);

    public static final RegistryObject<RecipeSerializer<?>> SQUEEZING =
            RECIPE_SERIALIZERS.register("squeezing", () -> FootTubRecipe.Serializer.INSTANCE);

    public static final RegistryObject<RecipeSerializer<?>> POT_BOILING =
    RECIPE_SERIALIZERS.register("pot_boiling", () -> PotBoilingRecipe.Serializer.INSTANCE);

    public static final RegistryObject<RecipeSerializer<?>> FILLING =
            RECIPE_SERIALIZERS.register("filling", () -> FillRecipe.Serializer.INSTANCE);

    public static final RegistryObject<RecipeSerializer<?>> FERMENTING =
            RECIPE_SERIALIZERS.register("fermenting", () -> FermentingRecipe.Serializer.INSTANCE);

    public static void register(IEventBus eventBus) {
        RECIPE_SERIALIZERS.register(eventBus);
    }

}
