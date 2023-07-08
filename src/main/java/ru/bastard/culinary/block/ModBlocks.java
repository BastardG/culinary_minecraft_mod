package ru.bastard.culinary.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import ru.bastard.culinary.Culinary;
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

    /* Example to create new Cake
    public static final Block MY_CAKE = cake("my_cake", Items.SWEET_BERRY);
    It will be named "my_cake" and when you right-click to this cake, it will be return SWEET_BERRY item
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

    private static Block cake(String name, Item cakeSlice) {
        return registerBlock(name,
                () -> new ModCakeBlock(cakeSlice, BlockBehaviour.Properties.of(Material.CAKE))).get();
    }

}
