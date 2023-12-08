package ru.bastard.culinary.fluid;

import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import ru.bastard.culinary.Culinary;
import ru.bastard.culinary.block.ModBlocks;
import ru.bastard.culinary.item.ModItems;

public class ModFluids {

    public static final DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(ForgeRegistries.FLUIDS, Culinary.MOD_ID);

    public static final RegistryObject<FlowingFluid> SOURCE_SUGAR_CANE_JUICE =
            FLUIDS.register("sugar_cane_juice_fluid", () ->
                    new ForgeFlowingFluid.Source(ModFluids.SUGAR_CANE_JUICE_PROPERTIES));

    public static final RegistryObject<FlowingFluid> FLOWING_SUGAR_CANE_JUICE =
            FLUIDS.register("flowing_sugar_cane_juice_fluid", () ->
                    new ForgeFlowingFluid.Flowing(ModFluids.SUGAR_CANE_JUICE_PROPERTIES));

    public static final ForgeFlowingFluid.Properties SUGAR_CANE_JUICE_PROPERTIES =
            new ForgeFlowingFluid.Properties(
                    ModFluidTypes.SUGAR_CANE_JUICE_FLUID_TYPE,
                    SOURCE_SUGAR_CANE_JUICE,
                    FLOWING_SUGAR_CANE_JUICE)
                    .levelDecreasePerBlock(5)
                    .slopeFindDistance(1)
                    .block(ModBlocks.SUGAR_CANE_JUICE_BLOCK).bucket(ModItems.SUGAR_CANE_JUICE_BUCKET);

    public static final RegistryObject<FlowingFluid> SOURCE_MOLASSES =
            FLUIDS.register("molasses_fluid", () ->
                    new ForgeFlowingFluid.Source(ModFluids.MOLASSES_PROPERTIES));

    public static final RegistryObject<FlowingFluid> FLOWING_MOLASSES =
            FLUIDS.register("flowing_molasses_fluid", () ->
                    new ForgeFlowingFluid.Flowing(ModFluids.MOLASSES_PROPERTIES));

    public static final ForgeFlowingFluid.Properties MOLASSES_PROPERTIES =
            new ForgeFlowingFluid.Properties(
                    ModFluidTypes.MOLASSES_FLUID_TYPE,
                    SOURCE_MOLASSES,
                    FLOWING_MOLASSES)
                    .levelDecreasePerBlock(5)
                    .slopeFindDistance(1)
                    .block(ModBlocks.MOLASSES_FLUID_BLOCK).bucket(ModItems.MOLASSES_BUCKET);

    public static final RegistryObject<FlowingFluid> SOURCE_HONEY = FLUIDS.register("honey_fluid",
            () -> new ForgeFlowingFluid.Source(ModFluids.HONEY_PROPERTIES));

    public static final RegistryObject<FlowingFluid> FLOWING_HONEY = FLUIDS.register("flowing_honey_fluid",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.HONEY_PROPERTIES));

    public static final ForgeFlowingFluid.Properties HONEY_PROPERTIES = new ForgeFlowingFluid
            .Properties(ModFluidTypes.HONEY_FLUID_TYPE, SOURCE_HONEY, FLOWING_HONEY)
            .levelDecreasePerBlock(6)
            .slopeFindDistance(1)
            .block(ModBlocks.HONEY_FLUID_BLOCK).bucket(ModItems.HONEY_BUCKET);


    public static void register(IEventBus bus) {
        FLUIDS.register(bus);
    }

}
