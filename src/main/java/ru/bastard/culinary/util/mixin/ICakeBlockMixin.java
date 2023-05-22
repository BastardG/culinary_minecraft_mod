package ru.bastard.culinary.util.mixin;

import net.minecraft.world.item.Item;

public interface ICakeBlockMixin {

    Item getCakeSlice();
    void setCakeSlice(Item item);

}
