package ru.bastard.culinary.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.bastard.culinary.block.CuttingBoard;
import ru.bastard.culinary.crafting.CuttingBoardRecipe;
import ru.bastard.culinary.networking.ModMessages;
import ru.bastard.culinary.networking.packet.ItemStackSyncS2CPacket;

import java.util.List;
import java.util.Optional;

public class CuttingBoardEntity extends BlockEntity {

    private ItemStackHandler handler = initializeHandler();
    private LazyOptional<IItemHandler> lazyHandler = LazyOptional.empty();

    public CuttingBoardEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CUTTING_BOARD.get(), pos, state);
    }

    /* RENDER STUFF */

    public ItemStack getRenderStack() {
        return handler.getStackInSlot(0);
    }

    public boolean processStoredItemUsingTool(ItemStack tool, @Nullable Player player) {
        if (isEmpty()) return false;

        Optional<CuttingBoardRecipe> match = getMatchingRecipe(tool);
        match.ifPresent(
                recipe -> {
                    List<ItemStack> results = recipe.getResults();
                    System.out.println(results);
                    for (ItemStack is : results) {
                        Direction direction = getBlockState().getValue(CuttingBoard.FACING).getCounterClockWise();
                        ItemEntity entity = new ItemEntity(level,
                                worldPosition.getX() + 0.5 + (direction.getStepX() * 0.2),
                                worldPosition.getY() + 0.2,
                                worldPosition.getZ() + 0.5 + (direction.getStepZ() * 0.2), is);
                        entity.setDeltaMovement(direction.getStepX() * 0.2F, 0F, direction.getStepZ() * 0.2F);
                        level.addFreshEntity(entity);
                    }
                    playSound(match.get());
                    clearBoard();
                }
        );

        if (player != null) {
            tool.hurtAndBreak(1, player, (user) -> user.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        } else {
            if (tool.hurt(1, level.random, null)) {
                tool.setCount(0);
            }
        }

        return match.isPresent();
    }

    private void clearBoard() {
        for (int i = 0; i < handler.getSlots(); i++) {
            handler.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    private void playSound(CuttingBoardRecipe cuttingRecipe) {
        SoundEvent soundEvent =
                ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(cuttingRecipe.getSoundEventID()));

        if (soundEvent != null) {
            level.playSound(
                    null, worldPosition, soundEvent, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }

    private Optional<CuttingBoardRecipe> getMatchingRecipe(ItemStack toolStack) {
        if (level == null) return Optional.empty();

        SimpleContainer inventory = new SimpleContainer(handler.getSlots());
        for (int i = 0; i < handler.getSlots(); i++)
            inventory.setItem(i, handler.getStackInSlot(i));

        List<CuttingBoardRecipe> recipes = level.getRecipeManager()
                .getRecipesFor(CuttingBoardRecipe.Type.INSTANCE, new RecipeWrapper(handler), level);

        if (recipes.size() == 0)
            return Optional.empty();

        return recipes
                .stream()
                .filter(cuttingBoardRecipe -> cuttingBoardRecipe.getTool().test(toolStack)).findFirst();
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
        for (int i = 0; i < handler.getSlots(); i++)
            handler.setStackInSlot(i, ItemStack.EMPTY);
        setChanged();
    }

    public Item checkItem() {
        return handler.getStackInSlot(0).getItem();
    }

    private ItemStackHandler initializeHandler() {
        return new ItemStackHandler(1) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
                if (!level.isClientSide()) {
                    ModMessages.sendToClients(new ItemStackSyncS2CPacket(this, worldPosition));
                }
            }
        };
    }

    /* NETWORKING */

    public void setItemHandler(ItemStackHandler stackHandler) {
        for (int i = 0; i < stackHandler.getSlots(); i++) {
            handler.setStackInSlot(i, stackHandler.getStackInSlot(i));
        }
        setChanged();
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyHandler.cast();
        }
        return super.getCapability(cap, side);
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

    @Override
    public void onLoad() {
        super.onLoad();
        lazyHandler = LazyOptional.of(() -> handler);
    }

    /* NETWORKING */

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
