package dev.toma.gunsrpg.common.debuffs.event;

import com.mojang.serialization.Codec;
import dev.toma.gunsrpg.common.init.ModDamageSources;
import dev.toma.gunsrpg.common.init.ModDebuffStageEvents;

public class PoisonDamageStageEvent extends AbstractIntervalDamageStageEvent {

    public static final Codec<PoisonDamageStageEvent> CODEC = Codec.floatRange(1.0F, Float.MAX_VALUE)
            .optionalFieldOf("amount", 1.0F).xmap(PoisonDamageStageEvent::new, AbstractIntervalDamageStageEvent::getAmount)
            .codec();

    public PoisonDamageStageEvent(float amount) {
        super(ModDamageSources.POISON_DAMAGE, 0, amount);
    }

    @Override
    public DebuffStageEventType<?> getType() {
        return ModDebuffStageEvents.POISON_DAMAGE;
    }
}
