package dev.toma.gunsrpg.common.quests.condition;

import dev.toma.gunsrpg.util.math.WeightedRandom;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

public class ConditionList {

    private final ResourceLocation listID;
    private final WeightedRandom<ConditionEntry> entries;

    public ConditionList(ResourceLocation listID, ConditionEntry... entries) {
        this.listID = listID;
        this.entries = new WeightedRandom<>(ConditionEntry::getWeight, entries);
    }

    public IQuestCondition selectRandom() {
        return selectRandom(Collections.emptySet());
    }

    public IQuestCondition selectRandom(Set<IQuestCondition> exlusions) {
        if (exlusions.isEmpty()) {
            return entries.getRandom().getNested();
        } else {
            ConditionEntry[] filtered = Arrays.stream(entries.getValues()).filter(entry -> !exlusions.contains(entry.getNested())).toArray(ConditionEntry[]::new);
            WeightedRandom<ConditionEntry> weightedRandom = new WeightedRandom<>(ConditionEntry::getWeight, filtered);
            return weightedRandom.getRandom().getNested();
        }
    }

    public static class ConditionEntry {
        private final int weight;
        private final IQuestCondition condition;

        private ConditionEntry(int weight, IQuestCondition condition) {
            this.weight = weight;
            this.condition = condition;
        }

        public static ConditionEntry of(int weight, IQuestCondition condition) {
            return new ConditionEntry(weight, condition);
        }

        public int getWeight() {
            return weight;
        }

        public IQuestCondition getNested() {
            return condition;
        }
    }
}
