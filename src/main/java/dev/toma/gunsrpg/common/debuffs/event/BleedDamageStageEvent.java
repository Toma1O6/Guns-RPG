package dev.toma.gunsrpg.common.debuffs.event;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.toma.gunsrpg.common.init.ModDamageSources;
import dev.toma.gunsrpg.common.init.ModDebuffStageEvents;

public class BleedDamageStageEvent extends AbstractIntervalDamageStageEvent {

    public static final Codec<BleedDamageStageEvent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.intRange(0, Integer.MAX_VALUE).optionalFieldOf("interval", 0).forGetter(AbstractIntervalDamageStageEvent::getInterval),
            Codec.floatRange(1.0F, Float.MAX_VALUE).optionalFieldOf("amount", 1.0F).forGetter(AbstractIntervalDamageStageEvent::getAmount)
    ).apply(instance, BleedDamageStageEvent::new));

    public BleedDamageStageEvent(int interval, float amount) {
        super(ModDamageSources.BLEED_DAMAGE, interval, amount);
    }

    @Override
    public DebuffStageEventType<?> getType() {
        return ModDebuffStageEvents.BLEED_DAMAGE;
    }
}
