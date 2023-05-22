package ru.bastard.culinary.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import ru.bastard.culinary.block.entity.CuttingBoardEntity;

public class CuttingBoard extends BaseEntityBlock {

    public CuttingBoard(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState blockState,
                                 Level level,
                                 BlockPos blockPos,
                                 Player player,
                                 InteractionHand hand,
                                 BlockHitResult hitResult) {
        if (level.isClientSide && hand == InteractionHand.MAIN_HAND) {
            ItemStack itemInHand = player.getItemInHand(hand);
            CuttingBoardEntity blockEntity = (CuttingBoardEntity)level.getBlockEntity(blockPos);
            Inventory inv = player.getInventory();
            if (blockEntity.isEmpty() && !itemInHand.equals(ItemStack.EMPTY)) {
                blockEntity.set(itemInHand);
                inv.removeItem(itemInHand);
            }
            else if (!blockEntity.isEmpty() && itemInHand.equals(ItemStack.EMPTY)) {
                inv.add(blockEntity.pop());
            }
            else if(!blockEntity.isEmpty() && !itemInHand.equals(ItemStack.EMPTY)) {
                ItemStack temp = blockEntity.pop();
                blockEntity.set(itemInHand);
                inv.removeItem(itemInHand);
                player.setItemSlot(EquipmentSlot.MAINHAND, temp);
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new CuttingBoardEntity(blockPos, blockState);
    }
}


