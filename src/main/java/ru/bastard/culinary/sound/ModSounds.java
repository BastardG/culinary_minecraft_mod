package ru.bastard.culinary.sound;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.IEventExceptionHandler;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import ru.bastard.culinary.Culinary;

public class ModSounds {

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Culinary.MOD_ID);

    public static final RegistryObject<SoundEvent> TELL_ME_WHY_SONG =
            registerSoundEvent("tell_me_why");

    public static final RegistryObject<SoundEvent> SQUEEZE_FIRST =
            registerSoundEvent("squeeze_sound");

    public static final RegistryObject<SoundEvent> SQUEEZE_SECOND =
            registerSoundEvent("squeeze_sound_2");

    public static final RegistryObject<SoundEvent> SQUEEZE_THIRD =
            registerSoundEvent("squeeze_sound_3");

    private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        ResourceLocation id = new ResourceLocation(Culinary.MOD_ID, name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }

}
