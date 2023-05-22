package ru.bastard.culinary.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.bastard.culinary.item.KnifeItem;
import ru.bastard.culinary.item.ModItems;
import ru.bastard.culinary.util.mixin.ICakeBlockMixin;

import static net.minecraft.world.level.block.CakeBlock.BITES;

@Mixin(CakeBlock.class)
public abstract class CakeBlockMixin implements ICakeBlockMixin {

    private static Item cakeSlice;

    @Override
    public Item getCakeSlice() {
        if (cakeSlice == null) {
            cakeSlice = ModItems.BOWL_RICE.get();
        }
        return cakeSlice;
    }

    @Override
    public void setCakeSlice(Item item) {
        cakeSlice = item;
    }

    @Inject(method = "eat", at=@At("HEAD"), cancellable = true)
    private static void injectEatMethod(LevelAccessor p_51186_, BlockPos p_51187_, BlockState p_51188_, Player p_51189_, CallbackInfoReturnable<InteractionResult> cir) {
        if (cakeSlice == null) {
            cakeSlice = ModItems.BOWL_RICE.get();
        }
        if (p_51189_.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof KnifeItem) {
            int i = p_51188_.getValue(BITES);
            p_51186_.gameEvent(p_51189_, GameEvent.EAT, p_51187_);
            if (i < 6) {
                p_51186_.setBlock(p_51187_, p_51188_.setValue(BITES, Integer.valueOf(i + 1)), 3);
            } else {
                p_51186_.removeBlock(p_51187_, false);
                p_51186_.gameEvent(p_51189_, GameEvent.BLOCK_DESTROY, p_51187_);
            }
            p_51189_.addItem(cakeSlice.getDefaultInstance());
            cir.setReturnValue(InteractionResult.SUCCESS);
        }
    }

}
