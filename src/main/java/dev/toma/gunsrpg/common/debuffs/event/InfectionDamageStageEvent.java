package dev.toma.gunsrpg.common.debuffs.event;

import com.mojang.serialization.Codec;
import dev.toma.gunsrpg.common.init.ModDamageSources;
import dev.toma.gunsrpg.common.init.ModDebuffStageEvents;

public class InfectionDamageStageEvent extends AbstractIntervalDamageStageEvent {

    public static final Codec<InfectionDamageStageEvent> CODEC = Codec.floatRange(1.0F, Float.MAX_VALUE)
            .optionalFieldOf("amount", 1.0F).xmap(InfectionDamageStageEvent::new, AbstractIntervalDamageStageEvent::getAmount)
            .codec();

    public InfectionDamageStageEvent(float amount) {
        super(ModDamageSources.INFECTION_DAMAGE, 0, amount);
    }

    @Override
    public DebuffStageEventType<?> getType() {
        return ModDebuffStageEvents.INFECTION_DAMAGE;
    }
}
