package ru.bastard.culinary.crafting;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;
import ru.bastard.culinary.Culinary;
import ru.bastard.culinary.util.FluidUtil;
import ru.bastard.culinary.util.comparators.items.ItemStackCountComparator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FermentingRecipe implements Recipe<SimpleContainer> {

    private final ResourceLocation id;
    private final FluidStack fermentingFluid;
    private final ItemStack[] ingredients;
    private final FluidStack resultingFluid;
    //Can be empty
    private final ItemStack[] resultingItems;

    public FermentingRecipe(ResourceLocation id, FluidStack fermentingFluid, ItemStack[] ingredients, FluidStack resultingFluid, ItemStack[] resultingItems) {
        this.id = id;
        this.fermentingFluid = fermentingFluid;
        this.ingredients = ingredients;
        this.resultingFluid = resultingFluid;
        this.resultingItems = resultingItems;
    }

    @Override
    public boolean matches(SimpleContainer sc, Level level) {
        return false;
    }

    public boolean matches(ItemStackHandler items, FluidStack fluidStack) {
        if (items.getSlots() != ingredients.length) return false;
        if (fluidStack.isEmpty()) return false;
        if (!fluidStack.isFluidEqual(fermentingFluid)) return false;

        var ingredientsSorted = Arrays.stream(ingredients).sorted(new ItemStackCountComparator()).toArray();
        var itemsSorted = turnToArrayAndSort(items);

        for (int i = 0; i < ingredientsSorted.length; i++) {
            if (!ingredientsSorted[i].equals(itemsSorted[i])) {
                return false;
            }
        }

        return true;
    }

    private ItemStack[] turnToArrayAndSort(ItemStackHandler items) {
        ItemStack[] itemsCopy = new ItemStack[items.getSlots()];
        for (int i = 0; i < items.getSlots(); i++) {
            itemsCopy[i] = items.getStackInSlot(i);
        }
        itemsCopy = Arrays.stream(itemsCopy).sorted(new ItemStackCountComparator()).toArray(ItemStack[]::new);
        return itemsCopy;
    }

    @Override
    public ItemStack assemble(SimpleContainer c) {
        return null;
    }

    @Override
    public boolean canCraftInDimensions(int p_43999_, int p_44000_) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return Items.AIR.getDefaultInstance();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.FERMENTING.get();
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<FermentingRecipe> {
        private Type () {}
        public static final String ID = "fermenting";
        public static final FermentingRecipe.Type INSTANCE = new FermentingRecipe.Type();
    }

    public static class Serializer implements RecipeSerializer<FermentingRecipe> {

        public static final ResourceLocation ID = new ResourceLocation(Culinary.MOD_ID, "fermenting");
        public static final FermentingRecipe.Serializer INSTANCE = new FermentingRecipe.Serializer();

        @Override
        public FermentingRecipe fromJson(ResourceLocation id, JsonObject json) {
            List<ItemStack> ingredientsBuffer = new ArrayList<>();
            List<ItemStack> resultItemsBuffer = new ArrayList<>();
            if (json.has("ingredients")) {
                GsonHelper.getAsJsonArray(json, "ingredients").forEach(e -> {
                    var i = CraftingHelper.getItemStack(e.getAsJsonObject(), false);
                    resultItemsBuffer.add(i);
                });
            }
            if (json.has("resultItems")) {
                GsonHelper.getAsJsonArray(json, "resultItems").forEach(e -> {
                    var i = CraftingHelper.getItemStack(e.getAsJsonObject(), false);
                    ingredientsBuffer.add(i);
                });
            }
            FluidStack fluidInput =
                    FluidUtil.readFluid(GsonHelper.getAsJsonObject(json, "inputFluid"));
            FluidStack fluidOutput =
                    FluidUtil.readFluid(GsonHelper.getAsJsonObject(json, "outputFluid"));
            return new FermentingRecipe(id, fluidInput, ingredientsBuffer.toArray(new ItemStack[0]), fluidOutput, resultItemsBuffer.toArray(new ItemStack[0]));
        }

        @Override
        public @Nullable FermentingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf fbb) {
            var fluidInput = fbb.readFluidStack();
            var fluidOut = fbb.readFluidStack();
            List<ItemStack> ingredients = fbb.readCollection(ArrayList::new, FriendlyByteBuf::readItem);
            List<ItemStack> resultItems = fbb.readCollection(ArrayList::new, FriendlyByteBuf::readItem);
            return new FermentingRecipe(id, fluidInput, ingredients.toArray(ItemStack[]::new), fluidOut, resultItems.toArray(ItemStack[]::new));
        }

        @Override
        public void toNetwork(FriendlyByteBuf fbb, FermentingRecipe recipe) {
            recipe.fermentingFluid.writeToPacket(fbb);
            recipe.resultingFluid.writeToPacket(fbb);
            fbb.writeCollection(Arrays.stream(recipe.ingredients).toList(), FriendlyByteBuf::writeItem);
            fbb.writeCollection(Arrays.stream(recipe.resultingItems).toList(), FriendlyByteBuf::writeItem);
        }
    }

}
