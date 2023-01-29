package dev.toma.gunsrpg.common.init;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.debuffs.event.*;

public final class ModDebuffStageEvents {

    public static final DebuffStageEventType<BleedDamageStageEvent> BLEED_DAMAGE = new DebuffStageEventType<>(GunsRPG.makeResource("bleed_damage"), BleedDamageStageEvent.CODEC);
    public static final DebuffStageEventType<HurtBySprintStageEvent> HURT_BY_SPRINT = new DebuffStageEventType<>(GunsRPG.makeResource("hurt_by_sprint"), HurtBySprintStageEvent.CODEC);
    public static final DebuffStageEventType<AddEffectStageEvent> ADD_EFFECT = new DebuffStageEventType<>(GunsRPG.makeResource("add_effect"), AddEffectStageEvent.CODEC);
    public static final DebuffStageEventType<InfectionDamageStageEvent> INFECTION_DAMAGE = new DebuffStageEventType<>(GunsRPG.makeResource("infection_damage"), InfectionDamageStageEvent.CODEC);
    public static final DebuffStageEventType<PoisonDamageStageEvent> POISON_DAMAGE = new DebuffStageEventType<>(GunsRPG.makeResource("poison_damage"), PoisonDamageStageEvent.CODEC);

    public static void register() {
        register(BLEED_DAMAGE);
        register(HURT_BY_SPRINT);
        register(ADD_EFFECT);
        register(INFECTION_DAMAGE);
        register(POISON_DAMAGE);
    }

    private static void register(DebuffStageEventType<?> type) {
        ModRegistries.DEBUFF_STAGE_EVENT_TYPES.register(type);
    }
}
