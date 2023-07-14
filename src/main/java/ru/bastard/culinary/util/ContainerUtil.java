package ru.bastard.culinary.util;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

import java.util.HashSet;

public class ContainerUtil {

    public static <C extends Container> boolean isContentEquals(C c1, C c2) {
        if (c1.getContainerSize() == c2.getContainerSize()) {
            HashSet<ItemStack> itemStacksOfContainers = new HashSet<>();
            for (int i = 0; i < c1.getContainerSize(); i++) {
                itemStacksOfContainers.add(c1.getItem(i));
                itemStacksOfContainers.add(c2.getItem(i));
            }
            return itemStacksOfContainers.size() == c1.getContainerSize();
        }
        return false;
    }

}
