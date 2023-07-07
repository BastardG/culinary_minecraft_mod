package ru.bastard.culinary.item;

import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import ru.bastard.culinary.Culinary;
import ru.bastard.culinary.sound.ModSounds;
import ru.bastard.culinary.util.FluidUtil;

import java.util.Random;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS
            = DeferredRegister.create(ForgeRegistries.ITEMS, Culinary.MOD_ID);

    /*
    public static final RegistryObject<Item> RICE =
            ITEMS.register("rice",
                    () ->
                            new BowlFoodItem(
                                    new BowlFoodItem
                                            .Properties()
                                            .food(new FoodProperties.Builder().fast().))
            );*/

    public static final RegistryObject<Item> BOWL_RICE =
            ITEMS.register("bowl_rice", () -> new BowlFood(
                    new BowlFood.Builder().prop(
                    new Item.Properties()
                            .stacksTo(1)
                            .food(
                                    new FoodProperties.Builder()
                                            .nutrition(2)
                                            .saturationMod(0.1F)
                                            .fast()
                                            .build())
            ).hot().saturation(10)));

    public static final RegistryObject<Item> WOODEN_KNIFE =
            ITEMS.register("wooden_knife", () -> new KnifeItem(
                    Tiers.WOOD,
                    1,
                    5f,
                    new Item.Properties().setNoRepair()));

    public static final RegistryObject<Item> STONE_KNIFE =
            ITEMS.register("stone_knife", () -> new KnifeItem(
                    Tiers.STONE,
                    1,
                    5f,
                    new Item.Properties().setNoRepair()));

    public static final RegistryObject<Item> IRON_KNIFE =
            ITEMS.register("iron_knife", () -> new KnifeItem(
                    Tiers.IRON,
                    1,
                    5f,
                    new Item.Properties().setNoRepair()));

    public static final RegistryObject<Item> GOLDEN_KNIFE =
            ITEMS.register("golden_knife", () -> new KnifeItem(
                    Tiers.GOLD,
                    1,
                    5f,
                    new Item.Properties().setNoRepair()));

    public static final RegistryObject<Item> DIAMOND_KNIFE =
            ITEMS.register("diamond_knife", () -> new KnifeItem(
                    Tiers.DIAMOND,
                    1,
                    5f,
                    new Item.Properties().setNoRepair()));

    public static final RegistryObject<Item> NETHERITE_KNIFE =
            ITEMS.register("netherite_knife", () -> new KnifeItem(
                    Tiers.NETHERITE,
                    2,
                    5f,
                    new Item.Properties().setNoRepair()));

    public static final RegistryObject<Item> DEFAULT_CAKE_SLICE =
            ITEMS.register("cake_slice", () -> new CakeSlice.Builder()
                    .foodStats(2)
                    .saturationStats(0.4f)
                    .properties(new Item.Properties().stacksTo(16).setNoRepair()).build());

    public static final RegistryObject<Item> TELL_ME_WHY_MUSIC_DISC =
            ITEMS.register("tell_me_why_music_disc", () -> new RecordItem(
                    15, ModSounds.TELL_ME_WHY_SONG,
                    new Item.Properties().rarity(ModRarity.UNIC).stacksTo(1).fireResistant(),
                    4120));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    private static RegistryObject<Item> registry(String bus, Item.Properties properties) {
        return ITEMS.register(bus, () -> new Item(properties));
    }

    private static RegistryObject<Item> squeezeOutput(String name, FluidStack fluid) {
        //TODO: Не забыть
        //FluidUtil.fluidToBottlesMap.put(fluid.getFluid(), );
        // return ITEMS.register;
        return null;
    }

}
