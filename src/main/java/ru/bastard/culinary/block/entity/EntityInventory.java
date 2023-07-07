package ru.bastard.culinary.block.entity;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public interface EntityInventory {

    void clear();
    void dropContents();
    boolean isEmpty();
    int size();
    void setItem(ItemStack is);
    ItemStack getItem();
    ItemStack takeItem();
    ItemStack takeItemWithReplace(ItemStack itemStack);
    void setItemHandler(ItemStackHandler isHandler);
    void shrink(int count);

}
