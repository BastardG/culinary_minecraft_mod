package ru.bastard.culinary.block;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.CakeBlock;
import ru.bastard.culinary.util.mixin.ICakeBlockMixin;

public class ModCakeBlock extends CakeBlock {

    public ModCakeBlock(Item cakeSlice, Properties properties) {
        super(properties);
        ICakeBlockMixin iCakeBlockMixin = (ICakeBlockMixin) this;
        iCakeBlockMixin.setCakeSlice(cakeSlice);
    }

}
