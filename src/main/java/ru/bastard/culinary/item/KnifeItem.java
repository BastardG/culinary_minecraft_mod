package ru.bastard.culinary.item;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

public class KnifeItem extends SwordItem {

    public KnifeItem(Tier tier, int damageMultiplier, float attackSpeedMultiplier, Properties props) {
        super(tier, damageMultiplier, attackSpeedMultiplier, props);
    }

    @Override
    public float getDestroySpeed(ItemStack itemStack, BlockState blockState) {
        Material material = blockState.getMaterial();
        if (blockState.is(Blocks.BAMBOO)) {
            return 15.0F;
        }
        if (material.equals(Material.CAKE) ||
            material.equals(Material.CLOTH_DECORATION) ||
            material.equals(Material.MOSS) ||
            material.equals(Material.WOOL) ||
            material.equals(Material.LEAVES)){
            return 25.0F;
        }
        return super.getDestroySpeed(itemStack, blockState);
    }

    @Override
    public boolean hurtEnemy(ItemStack itemStack, LivingEntity source, LivingEntity enemy) {
        itemStack.hurtAndBreak(1, enemy, (e) -> {
            e.broadcastBreakEvent(EquipmentSlot.MAINHAND);
        });
        return super.hurtEnemy(itemStack, source, enemy);
    }

    @Override
    public boolean mineBlock(ItemStack itemStack, Level level, BlockState blockState, BlockPos blockPos, LivingEntity entity) {
        Material material = blockState.getMaterial();
        if (getDestroySpeed(itemStack, blockState) > 0.0F) {
            itemStack.hurtAndBreak(2, entity, (e) -> {
                e.broadcastBreakEvent(EquipmentSlot.MAINHAND);
            });
        }
        return super.mineBlock(itemStack, level, blockState, blockPos, entity);
    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        if (state.is(Blocks.CAKE))
            return true;

        if (state.is(BlockTags.BAMBOO_BLOCKS))
            return true;

        if (state.is(BlockTags.CROPS))
            return true;

        return super.isCorrectToolForDrops(stack, state);
    }
    
}
