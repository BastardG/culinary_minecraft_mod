package ru.bastard.culinary.block.entity.abs;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.ItemStackHandler;
import ru.bastard.culinary.block.entity.ModBlockEntities;
import ru.bastard.culinary.crafting.FillRecipe;
import ru.bastard.culinary.networking.ModMessages;
import ru.bastard.culinary.networking.packet.FluidSyncS2CPacket;
import ru.bastard.culinary.networking.packet.ItemStackSyncS2CPacket;

import java.util.List;
import java.util.Optional;

//Только обслуживающие методы
public abstract class AbstractFermentingBarrelEntity extends BlockEntity {

    protected boolean isClosed;

    public AbstractFermentingBarrelEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FERMENTING_BARREL.get(), pos, state);
    }

    protected ItemStackHandler itemHandler = new ItemStackHandler(10){
        @Override
        protected void onContentsChanged(int slot) {
            ModMessages.sendToClients(new ItemStackSyncS2CPacket(this, worldPosition));
        }
    };

    protected FluidTank FLUID_TANK = new FluidTank(10000) {
        @Override
        protected void onContentsChanged() {
            ModMessages.sendToClients(new FluidSyncS2CPacket(this.getFluid(), worldPosition));
        }
    };

    protected void open() {
        isClosed = false;
    }

    protected void close() {
        isClosed = true;
    }

    protected InteractionResult fill(FluidStack fluidStack) {
        if (FLUID_TANK.getSpace() >= fluidStack.getAmount()) {
            if (FLUID_TANK.getFluid().isFluidEqual(fluidStack)) {
                FLUID_TANK.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
                setChanged();
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.FAIL;
    }

    public InteractionResult fillBarrel(ItemStack itemStack) {
        Optional<FillRecipe> fillRecipe = findFillRecipe(itemStack);
        if(fillRecipe.isPresent()) {
            var recipeFluid = fillRecipe.get().getResultingFluid();
            if (FLUID_TANK.getFluid().isFluidEqual(recipeFluid)) {
                if (itemStack == fillRecipe.get().getItemBottle()) {
                    return fill(new FluidStack(recipeFluid, 250));
                }
                return fill(new FluidStack(recipeFluid, 1000));
            }
        }
        return InteractionResult.FAIL;
    }

    public ItemStack fillItemStack(ItemStack itemStack) {
        Optional<FillRecipe> fillRecipe = findFillRecipe(FLUID_TANK.getFluid());
        if (fillRecipe.isPresent()) {
            if (itemStack.is(Items.GLASS_BOTTLE)) {
                if (FLUID_TANK.getFluidAmount() >= 250) {
                    FLUID_TANK.drain(250, IFluidHandler.FluidAction.EXECUTE);
                    setChanged();
                    return fillRecipe.get().getItemBottle();
                }
            }
            if (itemStack.is(Items.BUCKET)) {
                if (FLUID_TANK.getFluidAmount() >= 1000) {
                    FLUID_TANK.drain(1000, IFluidHandler.FluidAction.EXECUTE);
                    setChanged();
                    return fillRecipe.get().getItemBucket();
                }
            }
        }
        return ItemStack.EMPTY;
    }

    protected Optional<FillRecipe> findFillRecipe(ItemStack itemStack) {
        if (level == null) return Optional.empty();

        List<FillRecipe> fillRecipeList =
                level.getRecipeManager().getAllRecipesFor(FillRecipe.Type.INSTANCE);

        return fillRecipeList.stream().filter(recipe -> recipe.matches(itemStack)).findFirst();
    }

    protected Optional<FillRecipe> findFillRecipe(FluidStack fluidStack) {
        if (level == null) return Optional.empty();

        List<FillRecipe> fillRecipeList =
                level.getRecipeManager().getAllRecipesFor(FillRecipe.Type.INSTANCE);

        return fillRecipeList.stream().filter(recipe -> recipe.matches(fluidStack)).findFirst();
    }

    private boolean isFluidValid(Fluid fluid, int amount) {
        return FLUID_TANK.getFluid().getFluid().isSame(fluid) && FLUID_TANK.getSpace() >= amount;
    }

    private boolean isFluidValid(FluidStack fluid) {
        return FLUID_TANK.getFluid().isFluidEqual(fluid) && FLUID_TANK.getSpace() >= fluid.getAmount();
    }

    public void setItemHandler(ItemStackHandler itemHandler) {
        for (int i = 0; i < this.itemHandler.getSlots(); i++) {
            this.itemHandler.setStackInSlot(i, itemHandler.getStackInSlot(i));
        }
        setChanged();
    }

    abstract protected void drain(int amount);
    abstract protected boolean canDrain(int amount);

}
