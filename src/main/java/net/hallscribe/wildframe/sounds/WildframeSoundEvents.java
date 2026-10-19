package net.hallscribe.wildframe.sounds;

import net.hallscribe.wildframe.Wildframe;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

public class WildframeSoundEvents {
    public static final SoundEvent MOOHORN_AMBIENT = registerSoundEvent("ambient");
    public static final SoundEvent MOOHORN_HURT = registerSoundEvent("hurt");
    public static final SoundEvent MOOHORN_DEATH = registerSoundEvent("death");
    public static final SoundEvent MOOHORN_ATTACK = registerSoundEvent("attack");
    public static final SoundEvent MOOHORN_EAT = registerSoundEvent("eat");
    public static final SoundEvent MOOHORN_ANGRY = registerSoundEvent("angry");
    public static final SoundEvent MOOHORN_BREATHE = registerSoundEvent("breathe");
    public static final SoundEvent MOOHORN_SOFT = registerSoundEvent("soft");
    public static final SoundEvent MOOHORN_WOOD = registerSoundEvent("wood");

    public static final SoundEvent MOOHORN_BABY_AMBIENT = registerSoundEvent("baby_ambient");
    public static final SoundEvent MOOHORN_BABY_HURT = registerSoundEvent("baby_hurt");
    public static final SoundEvent MOOHORN_BABY_DEATH = registerSoundEvent("baby_death");
    public static final SoundEvent MOOHORN_BABY_ANGRY = registerSoundEvent("baby_angry");

    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = Identifier.fromNamespaceAndPath(Wildframe.MOD_ID, name);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
    }

    public static void registerWildframeSoundEvents() {
        Wildframe.LOGGER.info("Registering sounds for " + Wildframe.MOD_ID);
    }
}
