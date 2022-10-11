package dev.toma.gunsrpg.mixin;

import dev.toma.gunsrpg.client.BloodmoonAmbience;
import dev.toma.gunsrpg.client.ClientEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MoodSoundAmbience;
import net.minecraft.world.biome.ParticleEffectAmbience;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(Biome.class)
public abstract class BiomeMixin extends net.minecraftforge.registries.ForgeRegistryEntry.UncheckedRegistryEntry<Biome> {

    @Inject(method = "getAmbientParticle", at = @At("HEAD"), cancellable = true)
    public void gunsrpg_getAmbientParticle(CallbackInfoReturnable<Optional<ParticleEffectAmbience>> ci) {
        if (ClientEventHandler.bloodmoon) {
            long time = Minecraft.getInstance().level.dayTime() % 24000L;
            if (time > 13500 && time < 22500) {
                ci.setReturnValue(Optional.of(BloodmoonAmbience.AMBIENCE.get().effectAmbience));
            }
        }
    }

    @Inject(method = "getAmbientLoop", at = @At("HEAD"), cancellable = true)
    public void gunsrpg_getAmbientLoop(CallbackInfoReturnable<Optional<SoundEvent>> ci) {
        if (ClientEventHandler.bloodmoon) {
            ci.setReturnValue(Optional.of(BloodmoonAmbience.AMBIENCE.get().loopSound));
        }
    }

    @Inject(method = "getAmbientMood", at = @At("HEAD"), cancellable = true)
    public void gunsrpg_getAmbientMood(CallbackInfoReturnable<Optional<MoodSoundAmbience>> ci) {
        if (ClientEventHandler.bloodmoon) {
            ci.setReturnValue(Optional.of(BloodmoonAmbience.AMBIENCE.get().ambience));
        }
    }
}
