package ru.bastard.culinary.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class CakeSlice extends Item {

    private int foodStats;
    private float saturationStats;

    //Use builder class to create new instance
    private CakeSlice(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        level.playSound(player, player.getOnPos(), SoundEvents.GENERIC_EAT, SoundSource.PLAYERS);
        ItemStack is = player.getItemInHand(hand);
        if(!level.isClientSide()) {
            if (hand == InteractionHand.MAIN_HAND) {
                player.getFoodData().eat(foodStats, saturationStats);
                is.shrink(1);
            }
        }
        return InteractionResultHolder.sidedSuccess(is, level.isClientSide());
    }

    public static class Builder {
        private int foodStats;
        private float saturationStats;
        private Properties properties;

        public Builder foodStats(int i) {
            this.foodStats = i;
            return this;
        }

        public Builder saturationStats(float d) {
            this.saturationStats = d;
            return this;
        }

        public Builder properties(Properties properties) {
            this.properties = properties;
            return this;
        }

        public CakeSlice build() {
            CakeSlice cakeSlice = new CakeSlice(properties);
            cakeSlice.foodStats = foodStats;
            cakeSlice.saturationStats = saturationStats;
            return cakeSlice;
        }
    }

}
