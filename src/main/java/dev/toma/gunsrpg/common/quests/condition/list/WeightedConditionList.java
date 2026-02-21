package dev.toma.gunsrpg.common.quests.condition.list;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.quests.condition.IQuestConditionProvider;
import dev.toma.gunsrpg.common.quests.condition.NoConditionProvider;
import dev.toma.gunsrpg.common.quests.condition.QuestConditionLoader;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WeightedConditionList {

    public static WeightedConditionList EMPTY_LIST = singletonList(NoConditionProvider.NO_CONDITION);
    private final ConditionalWeightedProvider<PlayerEntity, WeightedProvider> randomProviderSelector;

    public WeightedConditionList(List<WeightedProvider> providers) {
        this.randomProviderSelector = new ConditionalWeightedProvider<>(providers, WeightedProvider::getWeight, WeightedProvider::canUse);
    }

    public static WeightedConditionList singletonList(IQuestConditionProvider<?> provider) {
        return new WeightedConditionList(
                Collections.singletonList(new WeightedProvider(1, GunsRPG.makeResource("no_name"), provider, Collections.emptyList()))
        );
    }

    public WeightedConditionList filter(Set<ResourceLocation> ignoredTypes) {
        return new WeightedConditionList(randomProviderSelector.getValues()
                        .filter(provider -> !ignoredTypes.contains(provider.getId()))
                        .collect(Collectors.toList())
        );
    }

    public IQuestConditionProvider<?> getProvider(PlayerEntity player) {
        return randomProviderSelector.getRandomItem(player).getProvider();
    }

    public IQuestConditionProvider<?>[] getProviders() {
        return randomProviderSelector.getValues()
                .map(WeightedProvider::getProvider)
                .filter(provider -> provider != NoConditionProvider.NO_CONDITION)
                .toArray(IQuestConditionProvider[]::new);
    }

    public static WeightedConditionList resolve(JsonElement element, QuestConditionLoader manager) {
        JsonArray array = JsonHelper.asJsonArray(element);
        List<WeightedProvider> providers = JsonHelper.deserialize(array, t -> new ArrayList<>(), json -> WeightedProvider.resolve(json, manager), List::add);
        return new WeightedConditionList(providers);
    }

    private static final class ConditionalWeightedProvider<CTX, T> {

        private final List<T> items;
        private final ToIntFunction<T> weightProvider;
        private final BiPredicate<T, CTX> filter;
        private final Random random = new Random();

        public ConditionalWeightedProvider(List<T> items, ToIntFunction<T> weightProvider, BiPredicate<T, CTX> filter) {
            this.items = items;
            this.weightProvider = weightProvider;
            this.filter = filter;
        }

        @Nullable
        public T getRandomItem(CTX ctx) {
            int totalWeight = 0;
            List<T> validItemCache = new ArrayList<>();
            for (T item : this.items) {
                if (this.filter.test(item, ctx)) {
                    totalWeight += weightProvider.applyAsInt(item);
                    validItemCache.add(item);
                }
            }
            int weight = totalWeight > 0 ? this.random.nextInt(totalWeight) : 0;
            for (int i = validItemCache.size() - 1; i >= 0; i--) {
                T item = validItemCache.get(i);
                weight -= weightProvider.applyAsInt(item);
                if (weight < 0) {
                    return item;
                }
            }
            return null;
        }

        public Stream<T> getValues() {
            return items.stream();
        }
    }
}
