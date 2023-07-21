package ru.bastard.culinary.networking.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkEvent;
import ru.bastard.culinary.block.entity.CuttingBoardEntity;
import ru.bastard.culinary.block.entity.EntityFluidTank;
import ru.bastard.culinary.block.entity.FootTubEntity;
import ru.bastard.culinary.block.entity.PotEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public class FluidSyncS2CPacket {

    private final FluidStack fluidStack;
    private final BlockPos pos;

    public FluidSyncS2CPacket(FluidStack fluidStack, BlockPos pos) {
        this.fluidStack = fluidStack;
        this.pos = pos;
    }

    public FluidSyncS2CPacket(FriendlyByteBuf buf) {
        this.fluidStack = buf.readFluidStack();
        this.pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeFluidStack(fluidStack);
        buf.writeBlockPos(pos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            if (Minecraft.getInstance().level.getBlockEntity(pos) instanceof FootTubEntity entity) {
                entity.setFluid(fluidStack);
            }
            if (Minecraft.getInstance().level.getBlockEntity(pos) instanceof PotEntity entity) {
                entity.setFluid(fluidStack);
            }
        });
        return true;
    }

}
