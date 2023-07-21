package ru.bastard.culinary.block.entity;

import lombok.SneakyThrows;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MilkBucketItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.bastard.culinary.crafting.PotBoilingRecipe;
import ru.bastard.culinary.networking.ModMessages;
import ru.bastard.culinary.networking.packet.FluidSyncS2CPacket;
import ru.bastard.culinary.networking.packet.ItemStackSyncS2CPacket;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class PotEntity extends BlockEntity implements EntityInventory, EntityFluidTank {

    private ItemStackHandler itemHandler = new ItemStackHandler(10) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide()) {
                ModMessages.sendToClients(new ItemStackSyncS2CPacket(this, worldPosition));
            }
        }
    };
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    private final FluidTank FLUID_TANK = new FluidTank(1000) {
        @Override
        protected void onContentsChanged() {
            setChanged();
            if (!level.isClientSide()) {
                ModMessages.sendToClients(new FluidSyncS2CPacket(this.getFluid(), worldPosition));
            }
        }
    };
    private LazyOptional<IFluidHandler> lazyFluidHandler = LazyOptional.empty();

    private int progress;
    private int temperature;

    public PotEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.POT.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, PotEntity entity) {
        if (level.isClientSide()) return;
        if (entity.isTankEmpty()) return;
        if (entity.temperature == 0) return;
        Optional<PotBoilingRecipe> optionalOfRecipe = entity.getMatchingRecipe();
        if (optionalOfRecipe.isPresent()) {
            entity.progress++;
            setChanged(level, pos, state);
            if (entity.progress >= optionalOfRecipe.get().getTicksToResult()) {
                if (entity.temperature >= optionalOfRecipe.get().getTemperature()) {
                    entity.craft(optionalOfRecipe.get());
                }
            }
        } else {
            entity.progress = 0;
            setChanged(level, pos, state);
        }
        ModMessages.sendToClients(new FluidSyncS2CPacket(entity.getFluid(), entity.worldPosition));
    }

    public FluidTank getFluidTank() {
        return FLUID_TANK;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
        setChanged();
    }

    @SneakyThrows
    public boolean isFurnaceLit(FurnaceBlockEntity furnace) {
        Class furnaceClass = furnace.getClass().getSuperclass();
        Method isLit = furnaceClass.getDeclaredMethod("isLit");
        isLit.setAccessible(true);
        return (boolean) isLit.invoke(furnace);
    }

    private Optional<PotBoilingRecipe> getMatchingRecipe() {
        Optional<PotBoilingRecipe> match = Optional.empty();

        if (level == null) return match;

        List<PotBoilingRecipe> potRecipes =
                level.getRecipeManager().getAllRecipesFor(PotBoilingRecipe.Type.INSTANCE);
        SimpleContainer sc = new SimpleContainer(itemHandler.getSlots());

        for (int i = 0; i < itemHandler.getSlots(); i++)
            sc.setItem(i, itemHandler.getStackInSlot(i));

        if (sc.isEmpty()) {
            return potRecipes.stream().filter(r -> r.matches(FLUID_TANK.getFluid())).findFirst();
        }
        return potRecipes.stream().filter(r -> r.matches(FLUID_TANK.getFluid(), sc)).findFirst();
    }

    private void craft(PotBoilingRecipe recipe) {
        ItemStack result = recipe.getResultItem();
        clear();
        emptyTank();
        if (!(result.getItem() instanceof BucketItem || result.getItem() instanceof MilkBucketItem)) {
            setItem(0, result);
            dropContents();
        }
        fillTank(recipe.getOutputFluid());
        this.progress = 0;
        setChanged();
    }

    @Override
    public void emptyTank() {
        setFluid(FluidStack.EMPTY);
        setChanged();
    }

    @Override
    public void fillTank(int amount) {
        if (!FLUID_TANK.isEmpty()) {
            FluidStack currentFluidStack = getFluid().copy();
            currentFluidStack.setAmount(amount);
            FLUID_TANK.fill(currentFluidStack, IFluidHandler.FluidAction.EXECUTE);
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
        if (FLUID_TANK.getFluidAmount() < 1000 && getFluid().isFluidEqual(fluidStack)) {
            FluidStack copy = fluidStack.copy();
            copy.setAmount(1000);
            FLUID_TANK.fill(copy, IFluidHandler.FluidAction.EXECUTE);
            setChanged();
        }
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
    public void drain(int amount) {
        if (getFluid().getAmount() > 0) {
            FLUID_TANK.drain(amount, IFluidHandler.FluidAction.EXECUTE);
            setChanged();
        }
    }

    @Override
    public int getCapacity() {
        return FLUID_TANK.getCapacity();
    }

    @Override
    public void clear() {
        for (int i = 0; i < size(); i++) {
            itemHandler.setStackInSlot(i, ItemStack.EMPTY);
        }
        setChanged();
    }

    @Override
    public void dropContents() {
        SimpleContainer sc = new SimpleContainer(10);
        for (int i = 0; i < size(); i++) {
            sc.setItem(i, getItem(i));
            setItem(i, ItemStack.EMPTY);
        }
        Containers.dropContents(level, worldPosition, sc);
    }

    @Override
    public boolean isTankEmpty() {
        return FLUID_TANK.isEmpty();
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < size(); i++) {
            if (!itemHandler.getStackInSlot(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int size() {
        return itemHandler.getSlots();
    }

    @Override
    public void setItem(int slot, ItemStack is) {
        itemHandler.setStackInSlot(slot, is.copy());
        setChanged();
    }

    private int getLastAvailableSlot() {
        if (isEmpty())
            return 0;
        for (int i = 1; i < size(); i++) {
            if (itemHandler.getStackInSlot(i).isEmpty()) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public ItemStack getItem(int slot) {
        return itemHandler.getStackInSlot(slot).copy();
    }

    @Override
    public void setItemHandler(ItemStackHandler isHandler) {
        for (int i = 0; i < isHandler.getSlots(); i++) {
            setItem(i, isHandler.getStackInSlot(i));
        }
        setChanged();
    }

    @Override
    public void shrink(int slot, int count) {
        itemHandler.getStackInSlot(slot).shrink(count);
        setChanged();
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
        findTemperature();
    }

    public void findTemperature() {
        BlockPos pos = new BlockPos(worldPosition.getX(), worldPosition.getY() - 1, worldPosition.getZ());
        BlockState blockUnder = level.getBlockState(pos);
        if (blockUnder.is(Blocks.AIR)) {
            setTemperature(0);
        }
        else if (blockUnder.is(Blocks.TORCH)) {
            setTemperature(50);
        }
        else if (level.getBlockEntity(worldPosition) instanceof FurnaceBlockEntity furnaceEntity) {
            if (isFurnaceLit(furnaceEntity)) {
                setTemperature(100);
            }
        }
        else if (blockUnder.is(Blocks.FIRE)) {
            setTemperature(125);
        }
        else if (blockUnder.is(Blocks.MAGMA_BLOCK)) {
            setTemperature(150);
        }
        else if (blockUnder.is(Blocks.LAVA) || blockUnder.is(Blocks.LAVA_CAULDRON)) {
            setTemperature(500);
        } else {
            setTemperature(0);
        }
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
        lazyFluidHandler.invalidate();
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
        return super.getCapability(cap, side);
    }

    public InteractionResult tryAdd(ItemStack itemStack) {
        int availableSlot = getLastAvailableSlot();
        if (availableSlot == -1) {
            return InteractionResult.FAIL;
        }
        setItem(availableSlot, itemStack);
        setChanged();
        return InteractionResult.SUCCESS;
    }

    public ItemStack pop() {
        int availableSlot = getLastAvailableSlot();
        ItemStack retirement = ItemStack.EMPTY;
        switch (availableSlot) {
            case 0 -> {
                return retirement;
            }
            case -1 -> {
                retirement = getItem(9);
                setItem(9, ItemStack.EMPTY);
            }
            default -> {
                retirement = getItem(availableSlot - 1);
                setItem(availableSlot - 1, ItemStack.EMPTY);
            }
        }
        return retirement;
    }

    public void processCheck(Player player) {
        player.sendSystemMessage(Component.literal(
                "Fluid: " + FLUID_TANK.getFluid().getDisplayName() + "\nFluid Amount: " + FLUID_TANK.getFluidAmount() +
                        "\nItems: " + Arrays.toString(getItemsToStringArray()) +
                "\nTemperature: " + temperature + "\nProgress: " + progress));

    }

    private String[] getItemsToStringArray() {
        String[] strings = new String[10];
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            strings[i] = itemHandler.getStackInSlot(i).toString();
        }
        return strings;
    }
}
