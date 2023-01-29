package dev.toma.gunsrpg.common.debuffs.event;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.toma.gunsrpg.common.init.ModDamageSources;
import dev.toma.gunsrpg.common.init.ModDebuffStageEvents;
import net.minecraft.entity.player.PlayerEntity;

public class HurtBySprintStageEvent extends AbstractIntervalDamageStageEvent {

    public static final Codec<HurtBySprintStageEvent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.intRange(0, Integer.MAX_VALUE).optionalFieldOf("interval", 0).forGetter(AbstractIntervalDamageStageEvent::getInterval),
            Codec.floatRange(1.0F, Float.MAX_VALUE).optionalFieldOf("amount", 1.0F).forGetter(AbstractIntervalDamageStageEvent::getAmount)
    ).apply(instance, HurtBySprintStageEvent::new));

    public HurtBySprintStageEvent(int interval, float amount) {
        super(ModDamageSources.FRACTURE_DAMAGE, interval, amount);
    }

    @Override
    protected boolean canApply(PlayerEntity player) {
        return player.isSprinting();
    }

    @Override
    public DebuffStageEventType<?> getType() {
        return ModDebuffStageEvents.HURT_BY_SPRINT;
    }
}
