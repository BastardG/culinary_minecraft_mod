package ru.bastard.culinary.block.entity;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

//TODO: Придумать как сделать из двух интерфейсов абстрактные классы
public interface EntityInventory {

    void clear();
    void dropContents();
    boolean isEmpty();
    int size();
    void setItem(int slot, ItemStack is);
    ItemStack getItem(int slot);
    void setItemHandler(ItemStackHandler isHandler);
    void shrink(int slot, int count);

}
