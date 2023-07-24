package ru.bastard.culinary.event;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import ru.bastard.culinary.Culinary;
import ru.bastard.culinary.block.entity.ModBlockEntities;
import ru.bastard.culinary.block.entity.renderer.CuttingBoardEntityRenderer;
import ru.bastard.culinary.block.entity.renderer.FootTubEntityRenderer;
import ru.bastard.culinary.block.entity.renderer.PotEntityRenderer;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Culinary.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {

    private ModEventBusEvents() {
        /* Prevent default public constructor */
    }

    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.CUTTING_BOARD.get(),
                CuttingBoardEntityRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.POT.get(), PotEntityRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.FOOT_TUB.get(), FootTubEntityRenderer::new);
    }

}
