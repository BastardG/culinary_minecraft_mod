package ru.bastard.culinary.fluid;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.joml.Vector3f;
import ru.bastard.culinary.Culinary;

public class ModFluidTypes {

    private static final ResourceLocation STILL = new ResourceLocation("block/water_still");
    private static final ResourceLocation FLOWING = new ResourceLocation("block/water_flow");
    private static final ResourceLocation OVERLAY = new ResourceLocation(Culinary.MOD_ID, "misc/in_sugar_cane_juice");

    public static final DeferredRegister<FluidType> FLUID_TYPES =
            DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, Culinary.MOD_ID);

    public static final RegistryObject<FluidType> SUGAR_CANE_JUICE_FLUID_TYPE =
            registryFluid("sugar_cane_fluid_type",
                    new Properties().tintColor(0xA1AB8035).fogColor(new Vector3f(171f/255f,128f/255f,53f/255f)),
                    FluidType.Properties.create()
                            .canPushEntity(true)
                            .canSwim(true)
                            .density(100)
                            .viscosity(45)
                            .supportsBoating(false));

    public static final RegistryObject<FluidType> MOLASSES_FLUID_TYPE =
            registryFluid("molasses_fluid_type",
                    new Properties().tintColor(0xA1603907).fogColor(new Vector3f(50f/255f,30f/255f,16f/255f)),
                    FluidType.Properties.create()
                            .canPushEntity(true)
                            .canSwim(false)
                            .density(5000)
                            .viscosity(10000)
                            .supportsBoating(false)
                            .canDrown(true)
                            .pathType(BlockPathTypes.DANGER_OTHER));

    private static RegistryObject<FluidType> registryFluid(String name, Properties properties, FluidType.Properties propertiesFluidType) {
        return FLUID_TYPES.register(name, () ->
                new BaseFluidType(
                        STILL,
                        FLOWING,
                        OVERLAY,
                        properties.tintColor,
                        properties.fogColor,
                        propertiesFluidType));
    }

    public static void register(IEventBus bus) {
        FLUID_TYPES.register(bus);
    }

    static class Properties {
        private int tintColor;
        private Vector3f fogColor;

        public Properties() {}

        public Properties tintColor(int c) {
            this.tintColor = c;
            return this;
        }

        public Properties fogColor(Vector3f fogColor) {
            this.fogColor = fogColor;
            return this;
        }

    }

}
