package dev.toma.gunsrpg.util;

import com.google.common.collect.ImmutableList;
import dev.toma.gunsrpg.common.init.ModEntities;
import dev.toma.gunsrpg.common.init.ModSounds;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

import java.util.List;
import java.util.Random;

public final class ParrotMobSoundMapCustomizer {

    public static void customizeParrotMobSoundMap() {
        List<SoundEvent> gunSounds = new ImmutableList.Builder<SoundEvent>()
                .add(ModSounds.M9)
                .add(ModSounds.MP5)
                .add(ModSounds.SLR)
                .add(ModSounds.M16)
                .build();
        ParrotEntity.MOB_SOUND_MAP.put(ModEntities.ZOMBIE_GUNNER.get(), new RandomSoundEvent(gunSounds));
        ParrotEntity.MOB_SOUND_MAP.put(ModEntities.EXPLOSIVE_SKELETON.get(), ModSounds.GL_SHOT1);
    }

    private ParrotMobSoundMapCustomizer() {}

    private static final class RandomSoundEvent extends SoundEvent {

        private final List<SoundEvent> soundEvents;
        private final Random random;

        public RandomSoundEvent(List<SoundEvent> soundEvents) {
            super(null); // might be safe
            this.soundEvents = soundEvents;
            this.random = new Random();
        }

        @Override
        public ResourceLocation getLocation() {
            SoundEvent event = ModUtils.getRandomListElement(soundEvents, random);
            return event.getLocation();
        }
    }
}
