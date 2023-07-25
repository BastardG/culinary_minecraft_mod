package ru.bastard.culinary.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import ru.bastard.culinary.Culinary;
import ru.bastard.culinary.fluid.ModFluids;
import ru.bastard.culinary.sound.ModSounds;

import java.util.ArrayList;

public class ModItems {

    public static final ArrayList<RegistryObject<Item>> CEREALS = new ArrayList<>();

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
            );
    */

    public static final RegistryObject<Item> SUGAR_CANE_SUGAR =
            ITEMS.register("sugar_cane_sugar", () ->
                    new Item(new Item.Properties()
                            .food(new FoodProperties.Builder().fast().nutrition(1).build())
                            .stacksTo(64)));

    public static final RegistryObject<Item> BOWL_RICE =
            ITEMS.register("bowl_rice", () -> new HotFood(
                    new HotFood.Properties()
                            .hotTicks(10 * 20)
                            .saturationTicks(30 * 20)
                            .eatSound(SoundEvents.GENERIC_EAT)
                            .reminder(Items.BOWL)
                            .food(
                                    new FoodProperties
                                            .Builder()
                                            .nutrition(4)
                                            .saturationMod(0.1f)
                                            .fast().build())));

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

    public static final RegistryObject<Item> SUGAR_CANE_JUICE_BUCKET = ITEMS.register("sugar_cane_juice_bucket",
            () -> new BucketItem(ModFluids.SOURCE_SUGAR_CANE_JUICE, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

    public static final RegistryObject<Item> MOLASSES_BUCKET = ITEMS.register("molasses_bucket",
            () -> new BucketItem(ModFluids.SOURCE_MOLASSES, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

    public static final RegistryObject<Item> WHEAT_GRAIN = grain("wheat"); // +
    public static final RegistryObject<Item> OAT_GRAIN = grain("oat"); // +
    public static final RegistryObject<Item> RYE_GRAIN = grain("rye"); // +
    public static final RegistryObject<Item> MAIZE_GRAIN = grain("maize"); // +
    public static final RegistryObject<Item> RICE_GRAIN = grain("rice"); // +
    public static final RegistryObject<Item> SORGHUM_GRAIN = grain("sorghum");
    public static final RegistryObject<Item> BARLEY_GRAIN = grain("barley"); // +
    public static final RegistryObject<Item> MILLET_GRAIN = grain("millet");
    public static final RegistryObject<Item> QUINOA_GRAIN = grain("quinoa");
    public static final RegistryObject<Item> BUCKWHEAT_GRAIN = grain("buckwheat");

    public static final RegistryObject<Item> OAT = registerSimpleCerealItem("oat");
    public static final RegistryObject<Item> RYE = registerSimpleCerealItem("rye");
    public static final RegistryObject<Item> MAIZE = registerSimpleCerealItem("maize");
    public static final RegistryObject<Item> RICE = registerSimpleCerealItem("rice");
    public static final RegistryObject<Item> SORGHUM = registerSimpleCerealItem("sorghum");
    public static final RegistryObject<Item> BARLEY = registerSimpleCerealItem("barley");
    public static final RegistryObject<Item> MILLET = registerSimpleCerealItem("millet");
    public static final RegistryObject<Item> QUINOA = registerSimpleCerealItem("quinoa");
    public static final RegistryObject<Item> BUCKWHEAT = registerSimpleCerealItem("buckwheat");

    public static final RegistryObject<Item> WHEAT_FLOUR = flour("wheat");
    public static final RegistryObject<Item> OAT_FLOUR = flour("oat");
    public static final RegistryObject<Item> RYE_FLOUR= flour("rye");
    public static final RegistryObject<Item> MAIZE_FLOUR = flour("maize");
    public static final RegistryObject<Item> RICE_FLOUR = flour("rice");
    public static final RegistryObject<Item> SORGHUM_FLOUR = flour("sorghum");
    public static final RegistryObject<Item> BARLEY_FLOUR = flour("barley");
    public static final RegistryObject<Item> MILLET_FLOUR = flour("millet");
    public static final RegistryObject<Item> QUINOA_FLOUR = flour("quinoa");
    public static final RegistryObject<Item> BUCKWHEAT_FLOUR = flour("buckwheat");

    public static final RegistryObject<Item> WHEAT_DOUGH = dough("wheat");
    public static final RegistryObject<Item> OAT_DOUGH = dough("oat");
    public static final RegistryObject<Item> RYE_DOUGH = dough("rye");
    public static final RegistryObject<Item> MAIZE_DOUGH = dough("maize");
    public static final RegistryObject<Item> RICE_DOUGH = dough("rice");
    public static final RegistryObject<Item> SORGHUM_DOUGH = dough("sorghum");
    public static final RegistryObject<Item> BARLEY_DOUGH = dough("barley");
    public static final RegistryObject<Item> MILLET_DOUGH = dough("millet");
    public static final RegistryObject<Item> QUINOA_DOUGH = dough("quinoa");
    public static final RegistryObject<Item> BUCKWHEAT_DOUGH = dough("buckwheat");

    private static RegistryObject<Item> registerSimpleCerealItem(String id) {
        var regObj = ITEMS.register(id, () -> new Item(new Item.Properties()));
        CEREALS.add(regObj);
        return regObj;
    }

    public static RegistryObject<Item> grain(String id) {
        return registerSimpleCerealItem(id + "_grain");
    }

    public static RegistryObject<Item> flour(String id) {
        return registerSimpleCerealItem(id + "_flour");
    }

    public static RegistryObject<Item> dough(String id) {
        return registerSimpleCerealItem(id + "_dough");
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    private static RegistryObject<Item> registry(String bus, Item.Properties properties) {
        return ITEMS.register(bus, () -> new Item(properties));
    }

}
