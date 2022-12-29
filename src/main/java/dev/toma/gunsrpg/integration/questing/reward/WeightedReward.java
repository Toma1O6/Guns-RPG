package dev.toma.gunsrpg.integration.questing.reward;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.toma.gunsrpg.common.init.QuestRegistry;
import dev.toma.gunsrpg.util.math.WeightedRandom;
import dev.toma.questing.common.quest.Quest;
import dev.toma.questing.common.reward.Reward;
import dev.toma.questing.common.reward.RewardType;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Arrays;
import java.util.List;

public class WeightedReward implements Reward {

    public static final Codec<WeightedReward> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            WeightedRewardEntry.ENTRY_CODEC.listOf().fieldOf("values").forGetter(t -> Arrays.asList(t.entries.getValues())),
            WeightedRewardEntry.ENTRY_CODEC.optionalFieldOf("generated", null).forGetter(t -> t.selected)
    ).apply(instance, WeightedReward::new));
    private final WeightedRandom<WeightedRewardEntry> entries;
    private WeightedRewardEntry selected;

    public WeightedReward(List<WeightedRewardEntry> entries, WeightedRewardEntry selected) {
        this.entries = new WeightedRandom<>(WeightedRewardEntry::getWeight, entries.toArray(new WeightedRewardEntry[0]));
        this.selected = selected;
    }

    private WeightedReward(WeightedRandom<WeightedRewardEntry> entries) {
        this.entries = entries;
    }

    @Override
    public void generate(PlayerEntity playerEntity, Quest quest) {
        this.selected = this.entries.getRandom();
    }

    @Override
    public void awardPlayer(PlayerEntity player, Quest quest) {
        if (this.selected != null) {
            this.selected.getRewardEntry().awardPlayer(player, quest);
        }
    }

    @Override
    public RewardType<?> getType() {
        return QuestRegistry.WEIGHTED_REWARD;
    }

    @Override
    public Reward copy() {
        return new WeightedReward(this.entries);
    }

    public static final class WeightedRewardEntry {

        public static final Codec<WeightedRewardEntry> ENTRY_CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.intRange(0, Integer.MAX_VALUE).optionalFieldOf("weight", 1).forGetter(t -> t.weight),
                RewardType.CODEC.fieldOf("entry").forGetter(t -> t.entry)
        ).apply(instance, WeightedRewardEntry::new));
        private final int weight;
        private final Reward entry;

        public WeightedRewardEntry(int weight, Reward value) {
            this.weight = weight;
            this.entry = value;
        }

        public int getWeight() {
            return weight;
        }

        public Reward getRewardEntry() {
            return entry;
        }
    }
}
