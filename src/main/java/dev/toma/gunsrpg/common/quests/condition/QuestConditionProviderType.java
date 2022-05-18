package dev.toma.gunsrpg.common.quests.condition;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

import java.util.function.BiFunction;

public class QuestConditionProviderType<Q extends IQuestConditionProvider<?>> {

    private final ResourceLocation id;
    private final IQuestConditionProviderSerializer<Q> serializer;
    private final BiFunction<QuestConditionProviderType<Q>, CompoundNBT, Q> fromNbtReader;
    private final boolean isFatal;

    QuestConditionProviderType(ResourceLocation id, IQuestConditionProviderSerializer<Q> serializer, BiFunction<QuestConditionProviderType<Q>, CompoundNBT, Q> fromNbtReader, boolean isFatal) {
        this.id = id;
        this.serializer = serializer;
        this.fromNbtReader = fromNbtReader;
        this.isFatal = isFatal;
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
}
