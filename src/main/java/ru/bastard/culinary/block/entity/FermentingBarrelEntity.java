package ru.bastard.culinary.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import ru.bastard.culinary.block.entity.abs.AbstractFermentingBarrelEntity;
import ru.bastard.culinary.crafting.FermentingRecipe;
import ru.bastard.culinary.crafting.FillRecipe;
import ru.bastard.culinary.crafting.MixingRecipe;

import java.util.List;
import java.util.Optional;

/*
*
* TODO: Закончить логику
*
* Используется для ферментации
* жидкостей
*
* */
public class FermentingBarrelEntity extends AbstractFermentingBarrelEntity {

    public FermentingBarrelEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    protected void drain(int amount) {
        FLUID_TANK.drain(1000, IFluidHandler.FluidAction.EXECUTE);
    }

    @Override
    protected boolean canDrain(int amount) {
        return FLUID_TANK.getFluidAmount() >= 1000;
    }

    public InteractionResult processRightClickWithItem(Player player, ItemStack itemInHand) {
        if (itemInHand.is(Items.GLASS_BOTTLE)) {
            Optional<FillRecipe> fillRecipe = findFillRecipe(FLUID_TANK.getFluid());
            if (fillRecipe.isPresent() && canDrain(250)) {
                player.addItem(fillRecipe.get().getItemBottle());
                drain(250);
                setChanged();
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.FAIL;
        }

        if (itemInHand.is(Items.BUCKET)) {
            Optional<FillRecipe> fillRecipe = findFillRecipe(FLUID_TANK.getFluid());
            if (fillRecipe.isPresent() && canDrain(1000)) {
                player.addItem(fillRecipe.get().getItemBottle());
                drain(1000);
                setChanged();
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.FAIL;
        }

        Optional<FillRecipe> fillRecipe = findFillRecipe(itemInHand);
        if (fillRecipe.isPresent()) {
            ItemStack bottle = fillRecipe.get().getItemBottle();
            ItemStack bucket = fillRecipe.get().getItemBucket();
            if (itemInHand.is(bucket.getItem())) {
                fillBarrel(bucket);
            }
            if (itemInHand.is(bottle.getItem())) {
                fillBarrel(bottle);
            }
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.FAIL;
    }

    public void processRightClickWithHand() {

    }

    private Optional<FillRecipe> getMatchingFillRecipe(ItemStack itemStack) {
        if (level == null) return Optional.empty();

        List<FillRecipe> fillRecipeList = level.getRecipeManager()
                .getAllRecipesFor(FillRecipe.Type.INSTANCE);

        return fillRecipeList.stream().filter(recipe -> recipe.matches(itemStack)).findFirst();
    }

    private Optional<FillRecipe> getMatchingFillRecipe() {
        if (level == null) return Optional.empty();

        List<FillRecipe> fillRecipes = level.getRecipeManager()
                .getAllRecipesFor(FillRecipe.Type.INSTANCE);

        return fillRecipes
                .stream()
                .filter(recipe -> recipe.matches(currentFluid()))
                .findFirst();
    }

    private Optional<FermentingRecipe> getMatchingFermentingRecipe() {
        if (level == null) return Optional.empty();

        List<FermentingRecipe> fermentingRecipes = level.getRecipeManager()
                .getAllRecipesFor(FermentingRecipe.Type.INSTANCE);

        return fermentingRecipes
                .stream()
                .filter(recipe -> recipe.matches(itemHandler, FLUID_TANK.getFluid()))
                .findFirst();
    }

    protected FluidStack currentFluid() {
        return FLUID_TANK.getFluid();
    }

    public static <E extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, E e) {

    }

}
