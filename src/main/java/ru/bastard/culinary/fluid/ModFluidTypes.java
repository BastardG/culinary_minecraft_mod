package ru.bastard.culinary.fluid;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.joml.Vector3f;
import ru.bastard.culinary.Culinary;

public class ModFluidTypes {

    public static final ResourceLocation WATER_STILL_RL = new ResourceLocation("block/water_still");
    public static final ResourceLocation WATER_FLOWING_RL = new ResourceLocation("block/water_flow");
    public static final ResourceLocation SUGAR_CANE_JUICE_OVERLAY_RL =
            new ResourceLocation(Culinary.MOD_ID, "misc/in_sugar_cane_juice");

    public static final DeferredRegister<FluidType> FLUID_TYPES =
            DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, Culinary.MOD_ID);

    public static final RegistryObject<FluidType> SUGAR_CANE_JUICE_FLUID_TYPE =
            registryFluid("sugar_cane_fluid_type",
                    FluidType.Properties.create()
                            .canPushEntity(true)
                            .canSwim(true)
                            .density(100)
                            .viscosity(45)
                            .supportsBoating(false));

    private static RegistryObject<FluidType> registryFluid(String name, FluidType.Properties properties) {
        return FLUID_TYPES.register(name, () ->
                new BaseFluidType(
                        WATER_STILL_RL,
                        WATER_FLOWING_RL,
                        SUGAR_CANE_JUICE_OVERLAY_RL,
                        0xA1AB8035,
                        new Vector3f(171f/255f,128f/255f,53f/255f),
                        properties));
    }

    public static void register(IEventBus bus) {
        FLUID_TYPES.register(bus);
    }

}
