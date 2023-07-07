package ru.bastard.culinary.block.entity;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import ru.bastard.culinary.Culinary;
import ru.bastard.culinary.block.ModBlocks;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Culinary.MOD_ID);

    public static final RegistryObject<BlockEntityType<CuttingBoardEntity>> CUTTING_BOARD =
            BLOCK_ENTITIES.register("cutting_board", () ->
                BlockEntityType.Builder.of(
                        CuttingBoardEntity::new, ModBlocks.CUTTING_BOARD.get()).build(null));

    public static final RegistryObject<BlockEntityType<FootTubEntity>> FOOT_TUB =
            BLOCK_ENTITIES.register("foot_tub", () ->
                    BlockEntityType.Builder.of(
                            FootTubEntity::new, ModBlocks.FOOT_TUB.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }

}
