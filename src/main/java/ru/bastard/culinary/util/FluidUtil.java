package ru.bastard.culinary.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;

import java.util.HashMap;
import java.util.Map;

public class FluidUtil {

    public static Map<Fluid, Item> fluidToBottlesMap = new HashMap<>();

    static {
        fluidToBottlesMap.put(Fluids.WATER, Items.POTION);
    }

    public static FluidStack readFluid(JsonObject jsonObject) {
        return FluidStack.CODEC.decode(JsonOps.INSTANCE, jsonObject).result().orElseThrow().getFirst();
    }

    public static JsonElement writeFluid(FluidStack stack) {
        return FluidStack.CODEC.encodeStart(JsonOps.INSTANCE, stack).result().orElseThrow();
    }

}
