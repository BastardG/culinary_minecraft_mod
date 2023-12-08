package ru.bastard.culinary.util.comparators.items;

import net.minecraft.world.item.ItemStack;

import java.util.Comparator;

public class ItemStackCountComparator implements Comparator<ItemStack> {

    @Override
    public int compare(ItemStack o1, ItemStack o2) {
        if (o1.equals(o2, true)) return 0;
        return o1.getCount() > o2.getCount()? 1 : -1;
    }

}
