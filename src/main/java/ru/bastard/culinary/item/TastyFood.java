package ru.bastard.culinary.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BowlFoodItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import ru.bastard.culinary.effect.ModEffects;

public class TastyFood extends Item {

    private boolean isHot;
    private boolean isSpicy;
    private boolean isSoup;
    private int saturationSeconds;
    private int counter = 0;
    private Properties prop;

    public TastyFood(Properties properties) {
        super(properties);
    }

    public TastyFood(TastyFood.Builder builder) {
        super(builder.prop);
        this.isHot = builder.isHot;
        this.isSpicy = builder.isSpicy;
        this.isSoup = builder.isSoup;
        this.saturationSeconds = builder.saturationSeconds;
    }

    public TastyFood(BowlFoodItem.Properties prop, FoodProperties foodProp) {
        super(prop);
        prop.food(foodProp);
    }

    @Override
    public SoundEvent getEatingSound() {
        if (isSoup) {
            return SoundEvents.GENERIC_DRINK;
        } else {
            return SoundEvents.GENERIC_EAT;
        }
    }

    @Override
    public void onCraftedBy(ItemStack itemStack, Level level, Player player) {
        super.onCraftedBy(itemStack, level, player);
        if (isHot) {
            CompoundTag ct = new CompoundTag();
            ct.putBoolean("hot", true);
            ct.putInt("hot_ticks", 10 * 20);
            itemStack.setTag(ct);
        }
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int p_41407_, boolean isComplex) {
        super.inventoryTick(itemStack, level, entity, p_41407_, isComplex);
        CompoundTag ct = itemStack.getTag();
        if (itemStack.getItem() instanceof TastyFood) {
            if (itemStack.hasTag()) {
                if (counter >= itemStack.getTag().getInt("hot_ticks")) {
                    itemStack.removeTagKey("hot");
                    itemStack.removeTagKey("hot_ticks");
                } else {
                    counter++;
                }
            }
        }
    }

    @Override
    public boolean onEntityItemUpdate(ItemStack itemStack, ItemEntity entity) {
        if (itemStack.getItem() instanceof TastyFood) {
            if (itemStack.hasTag()) {
                if (counter >= itemStack.getTag().getInt("hot_ticks")) {
                    itemStack.removeTagKey("hot");
                    itemStack.removeTagKey("hot_ticks");
                } else {
                    counter++;
                }
            }
        }
        return super.onEntityItemUpdate(itemStack, entity);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity livingEntity) {
        ItemStack is = super.finishUsingItem(itemStack, level, livingEntity);
        CompoundTag tag = itemStack.getTag();
        if (tag != null) {
            if (tag.contains("hot")) {
                if (tag.getBoolean("hot"))
                    livingEntity.hurt(DamageSource.HOT_FLOOR, 1);
                livingEntity.addEffect(new MobEffectInstance(ModEffects.SATURATION.get(), saturationSeconds));
            }
        }
        if (isSpicy) {
            livingEntity.hurt(DamageSource.HOT_FLOOR, 2);
        }
        return is;
    }

    public static class Builder {
        private Properties prop;
        private boolean isHot;
        private boolean isSpicy;
        private boolean isSoup;
        private int saturationSeconds = 0;

        public Builder prop(Properties prop) {
            this.prop = prop;
            return this;
        }

        public Builder hot() {
            this.isHot = true;
            return this;
        }

        public Builder spicy() {
            this.isSpicy = true;
            return this;
        }

        public Builder soup() {
            this.isSoup = true;
            return this;
        }

        public Builder saturation(int seconds) {
            if (isHot) {
                this.saturationSeconds = seconds * 20;
            }
            return this;
        }

        public TastyFood build() {
            return new TastyFood(this);
        }
    }

}
