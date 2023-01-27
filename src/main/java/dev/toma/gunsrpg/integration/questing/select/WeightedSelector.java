package dev.toma.gunsrpg.integration.questing.select;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.toma.gunsrpg.common.init.QuestRegistry;
import dev.toma.gunsrpg.util.math.WeightedRandom;
import dev.toma.questing.common.component.selector.Selector;
import dev.toma.questing.common.component.selector.SelectorType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class WeightedSelector<T> implements Selector<T> {

    private final WeightedRandom<WeightedRandom.Entry<T>> weightedRandom;
    private final int selectionCount;

    public WeightedSelector(List<WeightedRandom.Entry<T>> entries, int selectionCount) {
        this.weightedRandom = WeightedRandom.fromEntries(entries);
        this.selectionCount = Math.max(0, selectionCount);
    }

    public static <T> Codec<WeightedSelector<T>> codec(Codec<T> elementCodec) {
        return RecordCodecBuilder.create(instance -> instance.group(
                WeightedRandom.Entry.codec(elementCodec).listOf().fieldOf("values").forGetter(t -> Arrays.asList(t.weightedRandom.getValues())),
                Codec.intRange(0, Integer.MAX_VALUE).optionalFieldOf("selectCount", 1).forGetter(t -> t.selectionCount)
        ).apply(instance, WeightedSelector::new));
    }

    @Override
    public SelectorType<?, ?> getType() {
        return QuestRegistry.WEIGHTED_SELECTOR;
    }

    @Override
    public List<T> getElements() {
        WeightedRandom.Entry<T> entry = this.weightedRandom.getRandom();
        if (entry == null || selectionCount == 0)
            return Collections.emptyList();
        if (this.selectionCount > 1) {
            List<T> results = new ArrayList<>();
            results.add(entry.get());
            for (int i = 1; i < this.selectionCount; i++) {
                entry = this.weightedRandom.getRandom();
                results.add(entry.get());
            }
            return results;
        }
        return Collections.singletonList(entry.get());
    }
}
