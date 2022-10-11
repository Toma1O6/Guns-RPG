package dev.toma.gunsrpg.client;

import dev.toma.gunsrpg.util.object.LazyLoader;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.biome.MoodSoundAmbience;
import net.minecraft.world.biome.ParticleEffectAmbience;

public class BloodmoonAmbience {

    public static final LazyLoader<BloodmoonAmbience> AMBIENCE = new LazyLoader<>(() -> new BloodmoonAmbience(new ParticleEffectAmbience(ParticleTypes.WHITE_ASH, 0.10f), SoundEvents.AMBIENT_BASALT_DELTAS_LOOP, new MoodSoundAmbience(SoundEvents.AMBIENT_SOUL_SAND_VALLEY_MOOD, 500, 8, 2)));

    public final ParticleEffectAmbience effectAmbience;
    public final SoundEvent loopSound;
    public final MoodSoundAmbience ambience;

    public BloodmoonAmbience(ParticleEffectAmbience effectAmbience, SoundEvent loopSound, MoodSoundAmbience ambience) {
        this.effectAmbience = effectAmbience;
        this.loopSound = loopSound;
        this.ambience = ambience;
    }
}
