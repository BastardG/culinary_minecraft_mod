package ru.bastard.culinary.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
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
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import ru.bastard.culinary.effect.ModEffects;

public class HotFood extends Item {

    private int hotTicks;
    private int saturationTicks;
    private SoundEvent eatingSound;
    private FoodProperties foodProperties;
    private Item reminder;

    public HotFood(HotFood.Properties props) {
        super(props.properties());
        this.hotTicks = props.hotTicks;
        this.saturationTicks = props.saturationTicks;
        this.eatingSound = props.eatingSound;
        this.foodProperties = props.foodProperties;
        this.reminder = props.reminder;
    }


    @Override
    public SoundEvent getEatingSound() {
        return eatingSound;
    }

    @Override
    public void onCraftedBy(ItemStack itemStack, Level level, Player player) {
        super.onCraftedBy(itemStack, level, player);
        CompoundTag ct = new CompoundTag();
        ct.putBoolean("hot", true);
        ct.putInt("hot_ticks", hotTicks);
        ct.putInt("count", 0);
        itemStack.setTag(ct);
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int p_41407_, boolean isComplex) {
        super.inventoryTick(itemStack, level, entity, p_41407_, isComplex);
        if (itemStack.getTag() != null) {
            if (itemStack.getItem() instanceof HotFood) {
                if (itemStack.getTag().getBoolean("hot")) {
                    removeOrUpdate(itemStack);
                }
            }
        }
    }

    @Override
    public boolean onEntityItemUpdate(ItemStack itemStack, ItemEntity entity) {
        if (itemStack.getTag() != null) {
            if (itemStack.getItem() instanceof HotFood) {
                if (itemStack.getTag().getBoolean("hot")) {
                    removeOrUpdate(itemStack);
                }
            }
        }
        return super.onEntityItemUpdate(itemStack, entity);
    }

    private void removeOrUpdate(ItemStack itemStack) {
        CompoundTag tag = itemStack.getTag();
        if (tag.getInt("count") >= tag.getInt("hot_ticks")) {
            tag.putBoolean("hot", false);
            tag.remove("hot_ticks");
            tag.remove("count");
        } else {
            tag.putInt("count", tag.getInt("count" + 1));
        }
        itemStack.setTag(tag);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity livingEntity) {
        ItemStack is = super.finishUsingItem(itemStack, level, livingEntity);
        CompoundTag tag = itemStack.getTag();
        if (tag != null) {
            if (tag.contains("hot")) {
                if (livingEntity instanceof Player player) {
                    player.hurt(DamageSource.ON_FIRE, 0.5F);
                    player.sendSystemMessage(Component.literal("Hot!"));
                }
            }
        }
        return reminder.getDefaultInstance();
    }

    public static class Properties {
        private int hotTicks;
        private int saturationTicks;
        private SoundEvent eatingSound;
        private FoodProperties foodProperties;
        private Item reminder;

        public Properties hotTicks(int ticks) {
            this.hotTicks = ticks;
            return this;
        }

        public Properties saturationTicks(int ticks) {
            this.saturationTicks = ticks;
            return this;
        }

        public Properties eatSound(SoundEvent sound) {
            this.eatingSound = sound;
            return this;
        }

        public Properties food(FoodProperties properties) {
            this.foodProperties = properties;
            return this;
        }

        public Properties reminder(Item item) {
            this.reminder = item;
            return this;
        }

        private Item.Properties properties() {
            Item.Properties itemProperties = new Item.Properties();
            if (reminder != null) {
                itemProperties.craftRemainder(reminder);
            }
            itemProperties.food(foodProperties);
            itemProperties.stacksTo(1);
            return itemProperties;
        }

    }

}
