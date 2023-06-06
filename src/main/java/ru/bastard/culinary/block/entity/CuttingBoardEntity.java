package ru.bastard.culinary.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class CuttingBoardEntity extends BlockEntity {

    private NonNullList<ItemStack> container = NonNullList.withSize(1, ItemStack.EMPTY);

    public CuttingBoardEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.CUTTING_BOARD_ENTITY.get(), blockPos, blockState);
    }

    public NonNullList<ItemStack> getContainer() {
        return container;
    }

    public boolean isEmpty() {
        return container.get(0).equals(ItemStack.EMPTY);
    }

    public void set(ItemStack itemStack) {
        container.set(0, itemStack);
    }

    public ItemStack pop() {
        ItemStack ret = container.get(0);
        container.set(0, ItemStack.EMPTY);
        return ret;
    }

}
