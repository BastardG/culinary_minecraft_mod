package ru.bastard.culinary.crafting;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.Nullable;
import ru.bastard.culinary.Culinary;
import ru.bastard.culinary.util.FluidUtil;

public class FootTubRecipe implements Recipe<SimpleContainer> {

    private final ResourceLocation id;
    private final ItemStack inputItem;
    private final FluidStack outputFluid;

    public FootTubRecipe(ResourceLocation id, ItemStack inputItem, FluidStack outputFluid) {
        this.id = id;
        this.inputItem = inputItem;
        this.outputFluid = outputFluid;
    }

    @Override
    public boolean matches(SimpleContainer simpleContainer, Level level) {
        return false;
    }

    public boolean matches(ItemStack stack) {
        return inputItem.is(stack.getItem());
    }

    public boolean matches(ItemStack stack, FluidStack fStack) {
        return matches(stack) && fStack.isFluidEqual(outputFluid);
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
        return outputFluid.getFluid().getBucket().getDefaultInstance().copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.SQUEEZING.get();
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public FluidStack getResultingFluid() {
        return outputFluid;
    }

    public ItemStack getInputItem() {
        return inputItem;
    }

    public static class Type implements RecipeType<FootTubRecipe> {
        private Type () {}
        public static final String ID = "squeezing";
        public static final Type INSTANCE = new Type();
    }

    public static class Serializer implements RecipeSerializer<FootTubRecipe> {
        public static final ResourceLocation ID = new ResourceLocation(Culinary.MOD_ID, "squeezing");
        public static final Serializer INSTANCE = new Serializer();

        @Override
        public FootTubRecipe fromJson(ResourceLocation res, JsonObject json) {
            ItemStack in = CraftingHelper.getItemStack(
                    GsonHelper.getAsJsonObject(json, "inputItem"), false);
            FluidStack out = FluidUtil.readFluid(GsonHelper.getAsJsonObject(json, "outputFluid"));
            return new FootTubRecipe(res, in, out);
        }

        @Override
        public @Nullable FootTubRecipe fromNetwork(ResourceLocation res, FriendlyByteBuf fbb) {
            try {
                ItemStack in = fbb.readItem();
                FluidStack out = fbb.readFluidStack();
                return new FootTubRecipe(res, in, out);
            } catch (Exception e) {
                Culinary.LOG.error("\nUnnable to read recipe with id "+res+" from network", e);
                throw e;
            }
        }

        @Override
        public void toNetwork(FriendlyByteBuf fbb, FootTubRecipe rec) {
            fbb.writeItem(rec.getInputItem());
            rec.getResultingFluid().writeToPacket(fbb);
        }

    }

}
