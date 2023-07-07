package ru.bastard.culinary.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;
import ru.bastard.culinary.block.entity.FootTubEntity;
import ru.bastard.culinary.block.entity.ModBlockEntities;
import ru.bastard.culinary.tags.ForgeTags;
import ru.bastard.culinary.util.FluidUtil;

public class FootTub extends BaseEntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 16, 16);

    public FootTub(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext colContext) {
        return SHAPE;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        BlockEntity entity = level.getBlockEntity(pos);
        ItemStack itemInHand = player.getItemInHand(hand);

        if (entity != null) {
            if (!level.isClientSide()) {
                if (entity instanceof FootTubEntity fte) {
                    if (hand == InteractionHand.MAIN_HAND) {
                        if (!itemInHand.isEmpty()) {
                            if (itemInHand.is(Items.GLASS_BOTTLE)) {
                                FluidStack fs = fte.getFluid();
                                InteractionResult res = fte.processStoredFluidUseBottle();
                                if(res == InteractionResult.SUCCESS) {
                                    itemInHand.shrink(1);
                                    player.addItem(FluidUtil.fluidToBottlesMap.get(fs.getFluid()).getDefaultInstance());
                                }
                                return InteractionResult.SUCCESS;
                            } else {
                                int toShrink = fte.processStoredItemFromUseItem(itemInHand);
                                if (toShrink > 0)
                                    itemInHand.shrink(toShrink);
                                return InteractionResult.SUCCESS;
                            }
                        } else if (player.hasPose(Pose.CROUCHING)) {
                            fte.processStoredFluidToCheck(player);
                            return InteractionResult.SUCCESS;
                        } else {
                            fte.dropContents();
                            return InteractionResult.SUCCESS;
                        }
                    }
                }
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float height) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        //todo: remove all souts
        System.out.println("JUMPED ON FOOT TUB");
        if (!(entity instanceof Player)) {
            System.out.println("ENTITY ISN'T PLAYER");
            return;
        }
        if (blockEntity instanceof FootTubEntity tubEntity) {
            System.out.println("BLOCK ENTITY IS FOOT TUB, GOES TO PROCESSING");
            tubEntity.processStoredItemFromJump();
        }
        super.fallOn(level, state, pos, entity, height);
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

    /* ENTITY STUFF */

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FootTubEntity(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof FootTubEntity) {
                ((FootTubEntity)blockEntity).dropContents();
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
                                                                  BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.FOOT_TUB.get(), FootTubEntity::tick);
    }

}
