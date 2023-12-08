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
import org.jetbrains.annotations.Nullable;
import ru.bastard.culinary.Culinary;
import ru.bastard.culinary.util.FluidUtil;

public class FillRecipe implements Recipe<SimpleContainer> {

    private final ResourceLocation id;
    private final FluidStack fluidStack;
    private final ItemStack itemBottle;
    private final ItemStack itemBucket;

    public FillRecipe(ResourceLocation id, FluidStack fluidStack, ItemStack itemBottle, ItemStack itemBucket) {
        this.id = id;
        this.fluidStack = fluidStack;
        this.itemBottle = itemBottle;
        this.itemBucket = itemBucket;
    }

    //Use next matches, with FluidStack or ItemStack
    @Override
    public boolean matches(SimpleContainer container, Level level) {
        return false;
    }

    public boolean matches(ItemStack itemStack) {
        return itemStack.getItem() == itemBottle.getItem() || itemStack.getItem() == itemBucket.getItem();
    }

    public boolean matches(FluidStack inFluid) {
        return inFluid.isFluidEqual(fluidStack);
    }

    @Override
    public ItemStack assemble(SimpleContainer container) {
        if (container.getItem(0).is(Items.GLASS_BOTTLE)) {
            return itemBottle;
        } else if (container.getItem(0).is(Items.BUCKET)) {
            return itemBucket;
        }
        return Items.AIR.getDefaultInstance();
    }

    @Override
    public boolean canCraftInDimensions(int p_43999_, int p_44000_) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return itemBottle;
    }

    public FluidStack getResultingFluid() {
        return fluidStack;
    }

    public ItemStack getItemBottle() {
        return itemBottle;
    }

    public ItemStack getItemBucket() {
        return itemBucket;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return null;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<FillRecipe> {
        private Type () {}
        public static final String ID = "filling";
        public static final FillRecipe.Type INSTANCE = new FillRecipe.Type();
    }

    public static class Serializer implements RecipeSerializer<FillRecipe> {

        public static final ResourceLocation ID = new ResourceLocation(Culinary.MOD_ID, "filling");
        public static final FillRecipe.Serializer INSTANCE = new FillRecipe.Serializer();

        @Override
        public FillRecipe fromJson(ResourceLocation id, JsonObject json) {
            FluidStack fluid = FluidUtil.readFluid(GsonHelper.getAsJsonObject(json, "fluid"));
            ItemStack bottle = CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(json, "bottleItem"), false);
            ItemStack bucket = CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(json, "bucketItem"), false);
            return new FillRecipe(id, fluid, bottle, bucket);
        }

        @Override
        public @Nullable FillRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf fbb) {
            FluidStack fluid = fbb.readFluidStack();
            ItemStack bottle = fbb.readItem();
            ItemStack bucket = fbb.readItem();
            return new FillRecipe(id, fluid, bottle, bucket);
        }

        @Override
        public void toNetwork(FriendlyByteBuf fbb, FillRecipe recipe) {
            recipe.fluidStack.writeToPacket(fbb);
            fbb.writeItem(recipe.itemBottle);
            fbb.writeItem(recipe.itemBucket);
        }
    }

}
