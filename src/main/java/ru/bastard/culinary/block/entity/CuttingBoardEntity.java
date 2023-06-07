package ru.bastard.culinary.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.bastard.culinary.block.CuttingBoard;

public class CuttingBoardEntity extends BlockEntity {

    private ItemStackHandler handler = initializeHandler();
    private LazyOptional<IItemHandler> lazyHandler = LazyOptional.empty();

    public CuttingBoardEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CUTTING_BOARD.get(), pos, state);
    }

    public boolean isEmpty() {
        return handler.getStackInSlot(0).isEmpty();
    }

    public void setItem(ItemStack itemStack) {
        handler.setStackInSlot(0, itemStack);
        setChanged();
    }

    public void getItem() {
        drops();
        handler.setStackInSlot(0, ItemStack.EMPTY);
        setChanged();
    }

    private ItemStackHandler initializeHandler() {
        return new ItemStackHandler(1) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }
        };
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return lazyHandler.cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyHandler = LazyOptional.of(() -> handler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.put("inventory", handler.serializeNBT());
        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        handler.deserializeNBT(nbt.getCompound("inventory"));
        super.load(nbt);
    }

    public void drops() {
        if (level != null) {
            SimpleContainer inventory = new SimpleContainer(handler.getSlots());
            for (int i = 0; i < handler.getSlots(); i++) {
                inventory.setItem(i, handler.getStackInSlot(i));
            }
            Containers.dropContents(level, worldPosition, inventory);
        }
    }


    public static void tick(Level level, BlockPos pos, BlockState state, CuttingBoardEntity entity) {
        if (level.isClientSide()) return;
    }

}
