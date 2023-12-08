package ru.bastard.culinary.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class FluidHolder extends Item {

    private FluidStack fluid;

    public FluidHolder(FluidStack fluid, Properties properties) {
        super(properties);
        this.fluid = fluid;
    }

    public FluidStack getFluid() {
        return fluid;
    }
}
