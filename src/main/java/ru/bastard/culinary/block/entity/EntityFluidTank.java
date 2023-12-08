package ru.bastard.culinary.block.entity;

import net.minecraftforge.fluids.FluidStack;

public interface EntityFluidTank {

    boolean isTanksEmpty();
    boolean isTankEmpty(int tankId);
    void emptyTanks();
    void emptyTank(int tankId);
    void fillTank(int tankId, int amount);
    void fillTank(int tankId, FluidStack fluidStack);
    void fillTankFull(int tankId, FluidStack fluidStack);
    void setFluid(int tankId, FluidStack fluidStack);
    FluidStack getFluid(int tankId);
    void drain(int tankId, int amount);
    int getCapacity(int tankId);

}
