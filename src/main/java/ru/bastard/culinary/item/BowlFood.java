package ru.bastard.culinary.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class BowlFood extends TastyFood {

    public BowlFood(Item.Properties properties) {
        super(properties);
    }

    public BowlFood(Item.Properties prop, FoodProperties foodProp) {
        super(prop, foodProp);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity livingEntity) {
        ItemStack is = super.finishUsingItem(itemStack, level, livingEntity);
        return new ItemStack(Items.BOWL);
    }

}
