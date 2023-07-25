package ru.bastard.culinary;

import com.google.gson.*;
import com.mojang.logging.LogUtils;
import lombok.SneakyThrows;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.brewing.BrewingRecipe;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.event.brewing.PotionBrewEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;
import ru.bastard.culinary.block.ModBlocks;
import ru.bastard.culinary.block.Pot;
import ru.bastard.culinary.block.entity.ModBlockEntities;
import ru.bastard.culinary.crafting.BBrewingRecipe;
import ru.bastard.culinary.crafting.ModRecipeSerializers;
import ru.bastard.culinary.effect.ModEffects;
import ru.bastard.culinary.fluid.ModFluidTypes;
import ru.bastard.culinary.fluid.ModFluids;
import ru.bastard.culinary.item.ModItems;
import ru.bastard.culinary.networking.ModMessages;
import ru.bastard.culinary.sound.ModSounds;
import ru.bastard.culinary.tags.ForgeTags;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;

@Mod(ru.bastard.culinary.Culinary.MOD_ID)
public class Culinary {

    public static final String MOD_ID = "culinary";
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    public static final Logger LOG = LogUtils.getLogger();

    public Culinary() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModFluids.register(modEventBus);
        ModFluidTypes.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModEffects.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModRecipeSerializers.register(modEventBus);
        ModSounds.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(ModMessages::register);
        event.enqueueWork(() -> {
            //TODO: Autoregistration these recipes for sugar, don't know how to do right now
            BrewingRecipeRegistry.addRecipe(new BBrewingRecipe(Potions.WATER, ModItems.SUGAR_CANE_SUGAR.get(), Potions.MUNDANE));
            BrewingRecipeRegistry.addRecipe(new BBrewingRecipe(Potions.AWKWARD, ModItems.SUGAR_CANE_SUGAR.get(), Potions.SWIFTNESS));
        });
    }

    private void addCreative(CreativeModeTabEvent.BuildContents event) {
        if(event.getTab() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            event.accept(ModBlocks.CUTTING_BOARD.get());
        }
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_SUGAR_CANE_JUICE.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_SUGAR_CANE_JUICE.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.CUTTING_BOARD.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.POT.get(), RenderType.solid());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.FOOT_TUB.get(), RenderType.solid());
        }
    }

}
