package dev.toma.gunsrpg.common.quests.trigger;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.quests.trigger.types.EntityTrigger;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public final class Triggers {

    private static final Map<ResourceLocation, TriggerType<?>> TRIGGER_REGISTRY = new HashMap<>();

    public static final TriggerType<Entity> KILL_ENTITY = create("kill_entity", new EntityTrigger.Serializer());

    public static <C> TriggerType<C> register(TriggerType<C> triggerType) {
        if (TRIGGER_REGISTRY.put(triggerType.getRegistryName(), triggerType) != null) {
            throw new IllegalStateException("Duplicate trigger " + triggerType.getRegistryName().toString());
        }
        return triggerType;
    }

    @SuppressWarnings("unchecked")
    public static <CTX> TriggerType<CTX> getTriggerByKey(ResourceLocation key) {
        return (TriggerType<CTX>) TRIGGER_REGISTRY.get(key);
    }

    private static <CTX> TriggerType<CTX> create(String name, ITriggerSerializer<CTX> serializer) {
        TriggerType<CTX> type = TriggerType.newType(GunsRPG.makeResource(name), serializer);
        return register(type);
    }
}
