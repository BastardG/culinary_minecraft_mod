package ru.bastard.culinary.item;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import ru.bastard.culinary.Culinary;

@Mod.EventBusSubscriber(modid = Culinary.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModCreativeModeTabs {

    public static CreativeModeTab CEREALS;

    @SubscribeEvent
    public static void registerCreativeModeTabs(CreativeModeTabEvent.Register event) {
        CEREALS = event.registerCreativeModeTab(new ResourceLocation(Culinary.MOD_ID, "cereals_tab"),
                builder -> builder.
                        icon(Items.WHEAT::getDefaultInstance)
                        .title(Component.translatable("creativemodetab.cereals_tab")));
    }

}
