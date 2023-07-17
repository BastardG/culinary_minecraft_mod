package ru.bastard.culinary.crafting;

import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;
import ru.bastard.culinary.Culinary;
import ru.bastard.culinary.util.FluidUtil;
import ru.bastard.culinary.util.RecipeUtil;

import java.util.List;

public class PotBoilingRecipe implements Recipe<SimpleContainer> {

    private final ResourceLocation id;
    private final int temperature;
    private final List<Ingredient> ingredients;
    private final FluidStack inputFluid;
    private final FluidStack outputFluid;
    //can be null
    private final ItemStack resultingItem;
    private final int ticksToResult;

    public PotBoilingRecipe(ResourceLocation id, int temperature, List<Ingredient> ingredients, FluidStack inputFluid, FluidStack outputFluid, ItemStack resultingItem, int ticksToResult) {
        this.id = id;
        this.temperature = temperature;
        this.ingredients = ingredients;
        this.inputFluid = inputFluid;
        this.outputFluid = outputFluid;
        this.resultingItem = resultingItem;
        this.ticksToResult = ticksToResult;
    }

    //Use method with FluidStack and SimpleContainer
    @Override
    public boolean matches(SimpleContainer simpleContainer, Level level) {
        return false;
    }

    public boolean matches(FluidStack inputFluid, SimpleContainer simpleContainer) {
        SimpleContainer needs = new SimpleContainer();
        if (inputFluid.isFluidEqual(this.inputFluid) && simpleContainer.getContainerSize() == ingredients.size()) {
            for (int i = 0; i < ingredients.size(); i++) {
                if (!ingredients.get(i).test(simpleContainer.getItem(i))) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean matches(FluidStack inputFluid) {
        return inputFluid.isFluidEqual(this.inputFluid) && inputFluid.getAmount() == this.inputFluid.getAmount();
    }

    @Override
    public ItemStack assemble(SimpleContainer simpleContainer) {
        return getResultItem();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return resultingItem.isEmpty()?
                outputFluid.getFluid().getBucket().getDefaultInstance().copy() : resultingItem;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.POT_BOILING.get();
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<PotBoilingRecipe> {
        private Type(){}
        public static final String ID = "pot_boiling";
        public static final Type INSTANCE = new Type();
    }

    public static class Serializer implements RecipeSerializer<PotBoilingRecipe> {
        public static final ResourceLocation ID = new ResourceLocation(Culinary.MOD_ID, "pot_boiling");
        public static final Serializer INSTANCE = new Serializer();

        @Override
        public PotBoilingRecipe fromJson(ResourceLocation rs, JsonObject jsonObject) {
            final int temp = GsonHelper.getAsInt(jsonObject, "temperature");
            final List<Ingredient> ingrs = RecipeUtil.readIngredients(GsonHelper.getAsJsonArray(jsonObject, "ingredients"));
            final FluidStack inpFl = FluidUtil.readFluid(GsonHelper.getAsJsonObject(jsonObject, "inputFluid"));
            final FluidStack outFl = FluidUtil.readFluid(GsonHelper.getAsJsonObject(jsonObject, "outputFluid"));
            final ItemStack itemRemin = CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(jsonObject, "itemReminder"), false);
            final int timeAm = GsonHelper.getAsInt(jsonObject, "ticksToResult");
            return new PotBoilingRecipe(rs, temp, ingrs, inpFl, outFl, itemRemin, timeAm);
        }

        @Override
        public @Nullable PotBoilingRecipe fromNetwork(ResourceLocation rs, FriendlyByteBuf fbb) {
            final int temp = fbb.readVarInt();
            final int sizeIngrs = fbb.readVarInt();
            List<Ingredient> ingrs = NonNullList.withSize(sizeIngrs, Ingredient.EMPTY);
            for (int i = 0; i < sizeIngrs; i++) {
                ingrs.add(Ingredient.fromNetwork(fbb));
            }
            final FluidStack inpFl = fbb.readFluidStack();
            final FluidStack outFl = fbb.readFluidStack();
            final ItemStack itemRemin = fbb.readItem();
            final int ticksAm = fbb.readVarInt();
            return new PotBoilingRecipe(rs, temp, ingrs, inpFl, outFl, itemRemin, ticksAm);
        }

        @Override
        public void toNetwork(FriendlyByteBuf fbb, PotBoilingRecipe recipe) {
            fbb.writeVarInt(recipe.temperature);
            fbb.writeVarInt(recipe.ingredients.size());
            for (Ingredient i : recipe.ingredients) {
                i.toNetwork(fbb);
            }
            fbb.writeFluidStack(recipe.inputFluid);
            fbb.writeFluidStack(recipe.outputFluid);
            fbb.writeItem(recipe.resultingItem);
            fbb.writeVarInt(recipe.ticksToResult);
        }

    }

    public int getTemperature() {
        return temperature;
    }

    public FluidStack getInputFluid() {
        return inputFluid;
    }

    public FluidStack getOutputFluid() {
        return outputFluid;
    }

    public ItemStack getResultingItem() {
        return resultingItem;
    }

    public int getTicksToResult() {
        return ticksToResult;
    }

    @Override
    public String toString() {
        return "PotBoilingRecipe{" +
                "id=" + id +
                ", temperature=" + temperature +
                ", ingredients=" + ingredients +
                ", inputFluid=" + inputFluid +
                ", outputFluid=" + outputFluid +
                ", resultingItem=" + resultingItem +
                ", ticksToResult=" + ticksToResult +
                '}';
    }
}
