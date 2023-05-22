package ru.bastard.culinary.effect;

import net.minecraft.client.color.item.ItemColors;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.client.model.DynamicFluidContainerModel;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jline.utils.Colors;
import ru.bastard.culinary.Culinary;

public class ModEffects {

    public static final DeferredRegister<MobEffect> EFFECTS
            = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Culinary.MOD_ID);

    public static final RegistryObject<MobEffect> SATURATION
            = EFFECTS.register(
                    "saturation_mod", () -> new SaturationEffect(MobEffectCategory.BENEFICIAL, Colors.roundRgbColor(225, 182, 21, 1))
    );

    public static void register(IEventBus eventBus) {
        EFFECTS.register(eventBus);
    }

}
