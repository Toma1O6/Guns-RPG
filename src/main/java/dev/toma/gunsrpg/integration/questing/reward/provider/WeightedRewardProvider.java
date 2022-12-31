package dev.toma.gunsrpg.integration.questing.reward.provider;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.toma.gunsrpg.common.init.QuestRegistry;
import dev.toma.gunsrpg.integration.questing.reward.instance.WeightedReward;
import dev.toma.gunsrpg.util.math.WeightedRandom;
import dev.toma.questing.common.component.reward.RewardType;
import dev.toma.questing.common.component.reward.provider.RewardProvider;
import dev.toma.questing.common.quest.Quest;
import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WeightedRewardProvider implements RewardProvider<WeightedReward> {

    public static final Codec<WeightedRewardProvider> CODEC = WeightedRewardEntry.ENTRY_CODEC.listOf()
            .xmap(WeightedRewardProvider::new, t -> new ArrayList<>(Arrays.asList(t.entries.getValues())))
            .fieldOf("values").codec();
    private final WeightedRandom<WeightedRewardEntry> entries;

    public WeightedRewardProvider(List<WeightedRewardEntry> entries) {
        this.entries = new WeightedRandom<>(WeightedRewardEntry::getWeight, entries.toArray(new WeightedRewardEntry[0]));
    }

    @Override
    public WeightedReward createReward(PlayerEntity playerEntity, Quest quest) {
        return new WeightedReward(this, playerEntity, quest);
    }

    @Override
    public RewardType<WeightedReward, ?> getType() {
        return QuestRegistry.WEIGHTED_REWARD;
    }

    public WeightedRandom<WeightedRewardEntry> getEntries() {
        return entries;
    }

    public static final class WeightedRewardEntry {

        public static final Codec<WeightedRewardEntry> ENTRY_CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.intRange(0, Integer.MAX_VALUE).optionalFieldOf("weight", 1).forGetter(t -> t.weight),
                RewardType.PROVIDER_CODEC.fieldOf("entry").forGetter(t -> t.entry)
        ).apply(instance, WeightedRewardEntry::new));
        private final int weight;
        private final RewardProvider<?> entry;

        public WeightedRewardEntry(int weight, RewardProvider<?> value) {
            this.weight = weight;
            this.entry = value;
        }

        public int getWeight() {
            return weight;
        }

        public RewardProvider<?> getRewardEntry() {
            return entry;
        }
    }
}
