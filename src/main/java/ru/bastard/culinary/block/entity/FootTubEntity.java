package ru.bastard.culinary.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.bastard.culinary.crafting.FootTubRecipe;
import ru.bastard.culinary.networking.ModMessages;
import ru.bastard.culinary.networking.packet.FluidSyncS2CPacket;
import ru.bastard.culinary.networking.packet.ItemStackSyncS2CPacket;
import ru.bastard.culinary.sound.ModSounds;
import ru.bastard.culinary.tags.ForgeTags;

import java.util.List;
import java.util.Optional;

public class FootTubEntity extends BlockEntity implements EntityInventory, EntityFluidTank {

    private ItemStackHandler itemHandler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            ModMessages.sendToClients(new ItemStackSyncS2CPacket(this, worldPosition));
        }
    };
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    private FluidTank FLUID_TANK = new FluidTank(1000) {
        @Override
        protected void onContentsChanged() {
            ModMessages.sendToClients(new FluidSyncS2CPacket(FLUID_TANK.getFluid(), worldPosition));
        }
    };
    private LazyOptional<IFluidHandler> lazyFluidHandler = LazyOptional.empty();

    public FootTubEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FOOT_TUB.get(), pos, state);
    }

    public void setItemHandler(ItemStackHandler ish) {
        itemHandler.setStackInSlot(0, ish.getStackInSlot(0));
        setChanged();
    }

    public void processStoredFluidToCheck(Player player) {
        player.sendSystemMessage(Component.literal("Fluid in foot tub is: "+getFluid().getRawFluid().getBucket()+"\n Amount of fluid is: " + getFluid().getAmount()));
        player.sendSystemMessage(Component.literal("Items in Foot Tub: " + getItem().getItem() + "\nCount: " + getItem().getCount()));
    }

    public InteractionResult processStoredFluidUseBottle() {
        if (FLUID_TANK.getFluidAmount() >= 250) {
            drain(250);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }

    public void processStoredItemFromJump() {
        if (size() > 0 && getFluid().getAmount() < 1000) {
            Optional<FootTubRecipe> match = getMatchingRecipe();
            match.ifPresent((recipe) -> {
                int rand = Math.min((int)(1 + Math.random() * 3), size());
                playSound(rand);
                FluidStack resultingFluid = new FluidStack(recipe.getResultingFluid(), rand * recipe.getResultingFluid().getAmount());
                fillTank(resultingFluid);
                shrink(rand);
            });
        }
    }

    private void playSound(int rand) {
        SoundEvent soundEvent;
        switch (rand) {
            case 1 -> soundEvent = ModSounds.SQUEEZE_FIRST.get();
            case 2 -> soundEvent = ModSounds.SQUEEZE_SECOND.get();
            case 3 -> soundEvent = ModSounds.SQUEEZE_THIRD.get();
            default -> soundEvent = SoundEvents.SLIME_JUMP_SMALL;
        }
        level.playSound(
                null, worldPosition, soundEvent, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    public int processStoredItemFromUseItem(ItemStack itemStack) {
        if (!itemStack.is(ForgeTags.FRUITS_SQUEEZABLE)) {
            return 0;
        }

        int toShrink = size() + itemStack.getCount() > 64?
                64 - size() : itemStack.getCount();

        if (isEmpty()) {
            setItem(itemStack);
        } else if (getItem().is(itemStack.getItem())) {
            add(toShrink);
        }
        return toShrink;
    }

    private Optional<FootTubRecipe> getMatchingRecipe() {
        Optional<FootTubRecipe> match = Optional.empty();
        if (level == null) return match;

        SimpleContainer sc = new SimpleContainer(1);
        sc.setItem(0, getItem());

        List<FootTubRecipe> recipes = level.getRecipeManager()
                .getAllRecipesFor(FootTubRecipe.Type.INSTANCE);

        for (FootTubRecipe recipe : recipes) {
            if (recipe.matches(getItem())) {
                if (recipe.getResultingFluid().isFluidEqual(getFluid()) || getFluid().isEmpty()) {
                    match = Optional.of(recipe);
                }
                return match;
            }
        }

        return match;
    }

    @Override
    public void setFluid(FluidStack fluidStack) {
        FLUID_TANK.setFluid(fluidStack);
        setChanged();
    }

    @Override
    public FluidStack getFluid() {
        return FLUID_TANK.getFluid();
    }

    @Override
    public void emptyTank() {
        FLUID_TANK.drain(FLUID_TANK.getFluidAmount(), IFluidHandler.FluidAction.EXECUTE);
        setChanged();
    }

    @Override
    public void fillTank(int amount) {
        if (FLUID_TANK.getFluidAmount() > 0) {
            FluidStack fluidStack = FLUID_TANK.getFluid().copy();
            fluidStack.setAmount(amount);
            FLUID_TANK.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
            setChanged();
        }
    }

    @Override
    public void fillTank(FluidStack fluidStack) {
        if (FLUID_TANK.getFluidAmount() < 1000) {
            FLUID_TANK.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
            setChanged();
        }
    }

    @Override
    public void fillTankFull(FluidStack fluidStack) {
        if (FLUID_TANK.isEmpty()) {
            FluidStack fluidStackCopy = fluidStack.copy();
            fluidStackCopy.setAmount(1000);
            FLUID_TANK.fill(fluidStackCopy, IFluidHandler.FluidAction.EXECUTE);
            setChanged();
        }
    }

    @Override
    public void drain(int amount) {
        FLUID_TANK.drain(amount, IFluidHandler.FluidAction.EXECUTE);
        setChanged();
    }

    @Override
    public void clear() {
        itemHandler.setStackInSlot(0, ItemStack.EMPTY);
        setChanged();
    }

    @Override
    public void dropContents() {
        if (level != null) {
            SimpleContainer sc = new SimpleContainer(1);
            sc.setItem(0, getItem());
            clear();
            Containers.dropContents(level, worldPosition, sc);
        }
    }

    @Override
    public boolean isEmpty() {
        return getItem().isEmpty();
    }

    @Override
    public int size() {
        return getItem().getCount();
    }

    private void add(int amount) {
        ItemStack copy = getItem();
        copy.setCount(size() + amount);
        setItem(copy);
    }

    @Override
    public void shrink(int count) {
        itemHandler.getStackInSlot(0).shrink(count);
        setChanged();
    }

    @Override
    public void setItem(ItemStack is) {
        itemHandler.setStackInSlot(0, is.copy());
        setChanged();
    }

    @Override
    public ItemStack getItem() {
        return itemHandler.getStackInSlot(0).copy();
    }

    @Override
    public ItemStack takeItem() {
        ItemStack copy = getItem();
        dropContents();
        setChanged();
        return copy;
    }

    @Override
    public ItemStack takeItemWithReplace(ItemStack itemStack) {
        ItemStack copy = getItem();
        dropContents();
        setItem(itemStack);
        return copy;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap,
                                                      @Nullable Direction side) {

        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }
        if (cap == ForgeCapabilities.FLUID_HANDLER) {
            return lazyFluidHandler.cast();
        }
        return super.getCapability(cap, side);//TODO
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
        lazyFluidHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.put("inventory", itemHandler.serializeNBT());
        nbt.put("fluid_tank", FLUID_TANK.writeToNBT(new CompoundTag()));
        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        itemHandler.deserializeNBT(nbt.getCompound("inventory"));
        FLUID_TANK.readFromNBT(nbt.getCompound("fluid_tank"));
        super.load(nbt);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
        lazyFluidHandler = LazyOptional.of(() -> FLUID_TANK);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, FootTubEntity entity) {
        if (level.isClientSide()) return;
    }

}
