package ru.bastard.culinary.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import ru.bastard.culinary.Culinary;
import ru.bastard.culinary.fluid.ModFluids;
import ru.bastard.culinary.item.ModItems;

import java.util.function.Supplier;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS
            = DeferredRegister.create(ForgeRegistries.BLOCKS, Culinary.MOD_ID);

    public static final RegistryObject<Block> CUTTING_BOARD =
            registerBlock("cutting_board", () ->
                    new CuttingBoard(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).noOcclusion()));

    public static final RegistryObject<Block> FOOT_TUB =
            registerBlock("foot_tub", () ->
                    new FootTub(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).noOcclusion()));

    public static final RegistryObject<Block> POT =
            registerBlock("pot", () ->
                    new Pot(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).noOcclusion()));

    public static final RegistryObject<Block> FERMENTING_BARREL =
            registerBlock("fermenting_barrel", () ->
                    new FermentingBarrel(BlockBehaviour.Properties.copy(Blocks.OAK_LOG).noOcclusion()));

    public static final RegistryObject<LiquidBlock> SUGAR_CANE_JUICE_BLOCK = BLOCKS.register("sugar_cane_juice_fluid_block",
            () -> new LiquidBlock(ModFluids.SOURCE_SUGAR_CANE_JUICE, BlockBehaviour.Properties.copy(Blocks.WATER)));

    public static final RegistryObject<LiquidBlock> MOLASSES_FLUID_BLOCK = BLOCKS.register("molasses_fluid_block",
            () -> new LiquidBlock(ModFluids.SOURCE_MOLASSES, BlockBehaviour.Properties.copy(Blocks.WATER)));

    public static final RegistryObject<LiquidBlock> HONEY_FLUID_BLOCK = BLOCKS.register("honey_fluid_block",
            () -> new LiquidBlock(ModFluids.SOURCE_HONEY,
                    BlockBehaviour.Properties.copy(Blocks.WATER).sound(SoundType.HONEY_BLOCK)));

    /* Example to create new Cake
    public static final RegistryObject<Block> MY_CAKE = cake("my_cake", Items.SWEET_BERRY);
    It will be named "my_cake" and when you right-click to this cake with knife in hand,
     it will be return SWEET_BERRY item
     */

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(
            String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    private static RegistryObject<Block> cake(String name, Item cakeSlice) {
        return registerBlock(name,
                () -> new ModCakeBlock(cakeSlice, BlockBehaviour.Properties.of(Material.CAKE)));
    }

}
