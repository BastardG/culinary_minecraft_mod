package ru.bastard.culinary.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.bastard.culinary.Culinary;
import ru.bastard.culinary.crafting.ingredient.ChanceResult;
import ru.bastard.culinary.util.RecipeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CuttingBoardRecipe implements Recipe<RecipeWrapper> {

    private final ResourceLocation id;
    private final String group;
    private final Ingredient input;
    private final Ingredient tool;
    private final NonNullList<ChanceResult> results;
    private final String soundEvent;

    public CuttingBoardRecipe(ResourceLocation id, String group, Ingredient input, Ingredient tool, NonNullList<ChanceResult> results, String soundEvent) {
        this.id = id;
        this.group = group;
        this.input = input;
        this.tool = tool;
        this.results = results;
        this.soundEvent = soundEvent;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return id;
    }

    @Override
    public @NotNull String getGroup() {
        return group;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.of(input);
    }

    public NonNullList<Ingredient> getIngredientsAndTool() {
        return NonNullList.of(input, tool);
    }

    public Ingredient getTool() {
        return tool;
    }

    @Override
    public ItemStack assemble(RecipeWrapper wrapper) {
        return results.get(0).getStack().copy();
    }

    @Override
    public ItemStack getResultItem() {
        return results.get(0).getStack();
    }

    public List<ItemStack> getResults() {
        return getRollableResults().stream()
                .map(ChanceResult::getStack)
                .collect(Collectors.toList());
    }

    public NonNullList<ChanceResult> getRollableResults() {
        return results;
    }

    public List<ItemStack> rollResults(RandomSource rand, int fortune) {
        List<ItemStack> results = new ArrayList<>();
        NonNullList<ChanceResult> rollableResults = getRollableResults();
        for (ChanceResult out : rollableResults) {
            ItemStack stack = out.rollOutput(rand, fortune);
            if(stack.isEmpty()) {
                results.add(stack);
            }
        }
        return results;
    }

    public String getSoundEventID() {
        return soundEvent;
    }

    protected int getMaxInputCount() {
        return 1;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= getMaxInputCount();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.CUTTING.get();
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    @Override
    public boolean matches(RecipeWrapper wrapper, Level level) {
        if(wrapper.isEmpty())
            return false;
        return input.test(wrapper.getItem(0));
    }

    public static class Type implements RecipeType<CuttingBoardRecipe> {
        private Type() {}
        public static final Type INSTANCE = new Type();
        public static final String ID = "cutting";
    }

    public static class Serializer implements RecipeSerializer<CuttingBoardRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(Culinary.MOD_ID, "cutting");
        @Override
        public CuttingBoardRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            final String groupIn = GsonHelper.getAsString(json, "group", "");
            final NonNullList<Ingredient> inputItemsIn = RecipeUtil.readIngredients(GsonHelper.getAsJsonArray(json, "ingredients"));
            final JsonObject toolObject = GsonHelper.getAsJsonObject(json, "tool");
            final Ingredient toolIn = Ingredient.fromJson(toolObject);
            if (inputItemsIn.isEmpty()) {
                throw new JsonParseException("No ingredients for cutting recipe");
            } else if (toolIn.isEmpty()) {
                throw new JsonParseException("No tool for cutting recipe");
            } else if (inputItemsIn.size() > 1) {
                throw new JsonParseException("Too many ingredients for cutting recipe! Please define only one ingredient");
            } else {
                final NonNullList<ChanceResult> results = readResults(GsonHelper.getAsJsonArray(json, "result"));
                if (results.size() > 4) {
                    throw new JsonParseException("Too many results for cutting recipe! The maximum quantity of unique results is ");
                } else {
                    final String soundID = GsonHelper.getAsString(json, "sound", "");
                    return new CuttingBoardRecipe(recipeId, groupIn, inputItemsIn.get(0), toolIn, results, soundID);
                }
            }
        }

        private static NonNullList<ChanceResult> readResults(JsonArray resultArray) {
            NonNullList<ChanceResult> results = NonNullList.create();
            for (JsonElement result : resultArray) {
                results.add(ChanceResult.deserialize(result));
            }
            return results;
        }

        @Nullable
        @Override
        public CuttingBoardRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            String groupIn = buffer.readUtf(32767);
            Ingredient inputItemIn = Ingredient.fromNetwork(buffer);
            Ingredient toolIn = Ingredient.fromNetwork(buffer);

            int i = buffer.readVarInt();
            NonNullList<ChanceResult> resultsIn = NonNullList.withSize(i, ChanceResult.EMPTY);
            for (int j = 0; j < resultsIn.size(); ++j) {
                resultsIn.set(j, ChanceResult.read(buffer));
            }
            String soundEventIn = buffer.readUtf();

            return new CuttingBoardRecipe(recipeId, groupIn, inputItemIn, toolIn, resultsIn, soundEventIn);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, CuttingBoardRecipe recipe) {
            buffer.writeUtf(recipe.getGroup());
            recipe.getIngredients().get(0).toNetwork(buffer);
            recipe.getTool().toNetwork(buffer);
            buffer.writeVarInt(recipe.getResults().size());
            for (ChanceResult result : recipe.getRollableResults()) {
                result.write(buffer);
            }
            buffer.writeUtf(recipe.getSoundEventID());
        }
    }
}
