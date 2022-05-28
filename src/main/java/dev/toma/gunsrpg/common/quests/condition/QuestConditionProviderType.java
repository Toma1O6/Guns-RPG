package dev.toma.gunsrpg.common.quests.condition;

import dev.toma.gunsrpg.common.quests.trigger.Trigger;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import java.util.function.BiFunction;

public class QuestConditionProviderType<Q extends IQuestConditionProvider<?>> {

    private final ResourceLocation id;
    private final IQuestConditionProviderSerializer<Q> serializer;
    private final BiFunction<QuestConditionProviderType<Q>, CompoundNBT, Q> fromNbtReader;
    private final Set<Trigger> triggerSet;
    private final boolean failsQuest;

    QuestConditionProviderType(ResourceLocation id, IQuestConditionProviderSerializer<Q> serializer, BiFunction<QuestConditionProviderType<Q>, CompoundNBT, Q> fromNbtReader, boolean failsQuest, Trigger... triggers) {
        this.id = id;
        this.serializer = serializer;
        this.fromNbtReader = fromNbtReader;
        this.failsQuest = failsQuest;
        this.triggerSet = triggers.length == 0 ? Collections.emptySet() : EnumSet.copyOf(Arrays.asList(triggers));
    }

    public ResourceLocation getId() {
        return id;
    }

    public IQuestConditionProviderSerializer<Q> getSerializer() {
        return serializer;
    }

    public Q fromNbt(CompoundNBT nbt) {
        return fromNbtReader.apply(this, nbt);
    }

    public boolean shouldFailQuest() {
        return failsQuest;
    }

    public Set<Trigger> getTriggerSet() {
        return triggerSet;
    }
}
