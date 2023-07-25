package ru.bastard.culinary.tags;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

public class ForgeTags {

    public static final TagKey<Item> TOOLS = forgeItemTag("tools");
    public static final TagKey<Item> TOOLS_AXES = forgeItemTag("tools/axes");
    public static final TagKey<Item> TOOLS_KNIVES = forgeItemTag("tools/knives");
    public static final TagKey<Item> TOOLS_PICKAXES = forgeItemTag("tools/pickaxes");
    public static final TagKey<Item> TOOLS_SHOVELS = forgeItemTag("tools/shovels");
    public static final TagKey<Item> TOOLS_HOES = forgeItemTag("tools/hoes");

    public static final TagKey<Item> SQUEEZABLE = forgeItemTag("food/squeezable");
    public static final TagKey<Item> SUGAR = forgeItemTag("food/sugars");

    private static TagKey<Block> forgeBlockTag(String path) {
        return TagKey.create(ForgeRegistries.BLOCKS.getRegistryKey(), new ResourceLocation("forge", path));
    }

    private static TagKey<Item> forgeItemTag(String path) {
        return TagKey.create(ForgeRegistries.ITEMS.getRegistryKey(), new ResourceLocation("forge", path));
    }

}
