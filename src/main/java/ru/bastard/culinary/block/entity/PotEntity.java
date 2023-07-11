package ru.bastard.culinary.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class PotEntity extends BlockEntity {
    public PotEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.POT.get(), pos, state);
    }
}
