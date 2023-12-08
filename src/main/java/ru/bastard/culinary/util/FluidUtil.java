package ru.bastard.culinary.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.JsonOps;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FluidUtil {

    public static FluidStack readFluid(JsonObject jsonObject) {
        Optional<Pair<FluidStack, JsonElement>> fls =
                FluidStack.CODEC.decode(JsonOps.INSTANCE, jsonObject).result();
        return fls.isPresent()? fls.get().getFirst() : FluidStack.EMPTY;
    }

    public static JsonElement writeFluid(FluidStack stack) {
        return FluidStack.CODEC.encodeStart(JsonOps.INSTANCE, stack).result().orElseThrow();
    }

}
