package ru.bastard.culinary.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;
import ru.bastard.culinary.block.entity.ModBlockEntities;
import ru.bastard.culinary.block.entity.PotEntity;

public class Pot extends BaseEntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    private static final VoxelShape INSIDE = box(2.0D, 1.0D, 2.0D, 14.0D, 16.0D, 14.0D);
    private static final VoxelShape SHAPE = Shapes.block();

    public Pot(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (hand == InteractionHand.MAIN_HAND) {
            if (!level.isClientSide()) {
                ItemStack itemInHand = player.getItemInHand(hand);
                if (level.getBlockEntity(pos) instanceof PotEntity potEntity) {
                    if (player.hasPose(Pose.CROUCHING)) {
                        potEntity.processCheck(player);
                        return InteractionResult.SUCCESS;
                    }
                    if (itemInHand.isEmpty()) {
                        ItemStack topItem = potEntity.pop();
                        if (!topItem.isEmpty()) {
                            player.getInventory().add(topItem);
                        }
                        return InteractionResult.SUCCESS;
                    }
                    if (!itemInHand.isEmpty()) {
                        if (itemInHand.getItem() instanceof BucketItem bucket) {
                            if (potEntity.isTankEmpty()) {
                                potEntity.fillTank(new FluidStack(bucket.getFluid(), 1000));
                            }
                            return InteractionResult.SUCCESS;
                        }
                        if (potEntity.tryAdd(itemInHand) == InteractionResult.SUCCESS) {
                            player.getInventory().removeItem(itemInHand);
                            return InteractionResult.SUCCESS;
                        }
                        return InteractionResult.FAIL;
                    }
                }
            }
        }
        return super.use(state, level, pos, player, hand, hitResult);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos pos1, boolean p_60514_) {
        BlockPos underBlock = new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ());
        BlockState blockState = level.getBlockState(underBlock);
        if (level.getBlockEntity(pos) instanceof PotEntity potEntity) {
            potEntity.findTemperature();
        }
        super.neighborChanged(blockState, level, pos, block, pos1, p_60514_);
    }

    @Override
    public VoxelShape getShape(BlockState s, BlockGetter bg, BlockPos p, CollisionContext cc) {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState blockState, Rotation rotation) {
        return blockState.setValue(FACING, rotation.rotate(blockState.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState blockState, Mirror mirror) {
        return blockState.rotate(mirror.getRotation(blockState.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilder) {
        stateDefinitionBuilder.add(FACING);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            if (level.getBlockEntity(pos) instanceof PotEntity potEntity) {
                potEntity.dropContents();
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PotEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.POT.get(), PotEntity::tick);
    }

}
