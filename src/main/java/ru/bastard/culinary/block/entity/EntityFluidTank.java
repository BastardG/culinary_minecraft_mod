package ru.bastard.culinary.block.entity;

import net.minecraftforge.fluids.FluidStack;

public interface EntityFluidTank {

    boolean isEmpty();
    void emptyTank();
    void fillTank(int amount);
    void fillTank(FluidStack fluidStack);
    void fillTankFull(FluidStack fluidStack);
    void setFluid(FluidStack fluidStack);
    FluidStack getFluid();
    void drain(int amount);

}
