package dev.toma.gunsrpg.client.sound;

import net.minecraft.client.audio.TickableSound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

public class ContinuousSound extends TickableSound {

    public ContinuousSound(SoundEvent event, SoundCategory category) {
        super(event, category);
    }

    @Override
    public void tick() {

    }
}
