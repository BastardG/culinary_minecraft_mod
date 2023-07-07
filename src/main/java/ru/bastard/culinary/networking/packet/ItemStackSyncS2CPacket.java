package ru.bastard.culinary.networking.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkEvent;
import ru.bastard.culinary.block.entity.CuttingBoardEntity;
import ru.bastard.culinary.block.entity.FootTubEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public class ItemStackSyncS2CPacket {

    private final ItemStackHandler stackHandler;
    private final BlockPos pos;

    public ItemStackSyncS2CPacket(ItemStackHandler stackHandler, BlockPos pos) {
        this.stackHandler = stackHandler;
        this.pos = pos;
    }

    public ItemStackSyncS2CPacket(FriendlyByteBuf buf) {
        this.stackHandler = readHandler(buf);
        this.pos = buf.readBlockPos();
    }

    private ItemStackHandler readHandler(FriendlyByteBuf buf) {
        List<ItemStack> stacks = buf.readCollection(ArrayList::new, FriendlyByteBuf::readItem);
        var stackHandler = new ItemStackHandler(stacks.size());
        for (int i = 0; i < stackHandler.getSlots(); i++) {
            stackHandler.insertItem(i, stacks.get(i), false);
        }
        return stackHandler;
    }

    public void toBytes(FriendlyByteBuf buf) {
        writeItems(buf);
        buf.writeBlockPos(pos);
    }

    private void writeItems(FriendlyByteBuf buf) {
        Collection<ItemStack> stacks = new ArrayList<>();
        for (int i = 0; i < stackHandler.getSlots(); i++) {
            stacks.add(stackHandler.getStackInSlot(i));
        }
        buf.writeCollection(stacks, FriendlyByteBuf::writeItem);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            if (Minecraft.getInstance().level.getBlockEntity(pos) instanceof CuttingBoardEntity entity) {
                entity.setItemHandler(stackHandler);
            }
            if (Minecraft.getInstance().level.getBlockEntity(pos) instanceof FootTubEntity entity) {
                entity.setItemHandler(stackHandler);
            }
        });
        return true;
    }

}
