package ru.bastard.culinary.event;

import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import ru.bastard.culinary.Culinary;
import ru.bastard.culinary.block.entity.ModBlockEntities;
import ru.bastard.culinary.block.entity.renderer.CuttingBoardEntityRenderer;

@Mod.EventBusSubscriber(modid = Culinary.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {

    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.CUTTING_BOARD.get(),
                CuttingBoardEntityRenderer::new);
    }

}
