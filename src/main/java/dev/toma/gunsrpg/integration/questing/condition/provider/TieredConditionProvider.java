package dev.toma.gunsrpg.integration.questing.condition.provider;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.toma.gunsrpg.integration.questing.condition.instance.TieredCondition;
import dev.toma.gunsrpg.util.math.WeightedRandom;
import dev.toma.questing.common.component.condition.ConditionType;
import dev.toma.questing.common.component.condition.instance.EmptyCondition;
import dev.toma.questing.common.component.condition.provider.ConditionProvider;
import dev.toma.questing.common.quest.instance.Quest;
import net.minecraft.util.ResourceLocation;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TieredConditionProvider implements ConditionProvider<TieredCondition> {

    public static final Codec<TieredConditionProvider> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Entry.CODEC.listOf().xmap(e -> (Set<Entry>) new HashSet<>(e), ArrayList::new).fieldOf("entries").forGetter(t -> t.entryList),
            ResourceLocation.CODEC.listOf().optionalFieldOf("exclude", Collections.emptyList()).forGetter(t -> t.exclusions),
            Codec.intRange(0, Integer.MAX_VALUE).optionalFieldOf("tier", 1).forGetter(t -> t.tier)
    ).apply(instance, TieredConditionProvider::new));
    private final Set<Entry> entryList;
    private final List<ResourceLocation> exclusions;
    private final int tier;

    public TieredConditionProvider(Set<Entry> entryList, List<ResourceLocation> exclusions, int tier) {
        this.entryList = entryList;
        this.exclusions = exclusions;
        this.tier = tier;
    }

    @Override
    public TieredCondition createCondition(Quest quest) {
        Map<ResourceLocation, Entry> map = entryList.stream().collect(Collectors.toMap(Entry::getIdentifier, Function.identity()));
        exclusions.forEach(map::remove);
        WeightedRandom<Entry> random = new WeightedRandom<>(Entry::getWeight, map.values().toArray(new Entry[0]));
        Entry entry = random.getRandom();
        return new TieredCondition(this, entry == null ? EmptyCondition.EMPTY_CONDITION : entry.provider.createCondition(quest), tier);
    }

    @Override
    public ConditionType<TieredCondition, ?> getType() {
        return null;
    }

    public static final class Entry {

        public static final Codec<Entry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.intRange(0, Integer.MAX_VALUE).optionalFieldOf("weight", 1).forGetter(Entry::getWeight),
                ResourceLocation.CODEC.fieldOf("id").forGetter(Entry::getIdentifier),
                ConditionType.PROVIDER_CODEC.fieldOf("condition").forGetter(t -> t.provider)
        ).apply(instance, Entry::new));
        private final int weight;
        private final ResourceLocation identifier;
        private final ConditionProvider<?> provider;

        public Entry(int weight, ResourceLocation identifier, ConditionProvider<?> provider) {
            this.weight = weight;
            this.identifier = identifier;
            this.provider = provider;
        }

        public int getWeight() {
            return weight;
        }

        public ResourceLocation getIdentifier() {
            return identifier;
        }
    }
}
