package dev.toma.gunsrpg.client.sound;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.TickableSound;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

import java.util.function.Predicate;

public class ContinuousSound extends TickableSound {

    private final PlayerEntity client;
    private final Predicate<PlayerEntity> canPlay;

    public ContinuousSound(SoundEvent event, SoundCategory category, Predicate<PlayerEntity> canPlay) {
        super(event, category);
        this.looping = true;
        this.client = Minecraft.getInstance().player;
        this.canPlay = canPlay;
    }

    @Override
    public void tick() {
        if (!canPlay.test(client)) {
            stop();
            return;
        }
        this.x = client.getX();
        this.y = client.getY();
        this.z = client.getZ();
    }
}
