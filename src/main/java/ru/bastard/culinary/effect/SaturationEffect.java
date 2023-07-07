package ru.bastard.culinary.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class SaturationEffect extends MobEffect {

    public SaturationEffect(MobEffectCategory mobEffectCategory, int color) {
        super(mobEffectCategory, color);
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if(livingEntity instanceof Player) {
            ((Player)livingEntity).getFoodData().setExhaustion(0);
        }
        super.applyEffectTick(livingEntity, amplifier);
    }



    @Override
    public boolean isDurationEffectTick(int p_19455_, int p_19456_) {
        return true;
    }
}
