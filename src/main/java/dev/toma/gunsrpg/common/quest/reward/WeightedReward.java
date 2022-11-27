package dev.toma.gunsrpg.common.quest.reward;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.toma.gunsrpg.util.math.WeightedRandom;
import dev.toma.questing.quest.Quest;
import dev.toma.questing.reward.NestedReward;
import dev.toma.questing.reward.Reward;
import dev.toma.questing.reward.RewardType;
import dev.toma.questing.utils.JsonHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.JSONUtils;

public class WeightedReward implements NestedReward {

    private final WeightedRandom<WeightedRewardEntry> entries;

    public WeightedReward(WeightedRewardEntry[] entries) {
        this.entries = new WeightedRandom<>(WeightedRewardEntry::getWeight, entries);
    }

    @Override
    public void awardPlayer(PlayerEntity player, Quest quest) {
        Reward reward = this.chooseReward();
        reward.awardPlayer(player, quest);
    }

    @Override
    public Reward getActualReward(PlayerEntity player, Quest quest) {
        return this.chooseReward();
    }

    public Reward chooseReward() {
        return this.entries.getRandom().getRewardEntry();
    }

    public static final class WeightedRewardEntry {

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

        public static WeightedRewardEntry fromJson(JsonElement element) {
            JsonObject data = JsonHelper.requireObject(element);
            int weight = JSONUtils.getAsInt(data, "weight", 1);
            Reward reward = RewardType.fromJson(JSONUtils.getAsJsonObject(data, "entry"));
            return new WeightedRewardEntry(weight, reward);
        }
    }

    public static final class Serializer implements RewardType.RewardSerializer<WeightedReward> {

        @Override
        public WeightedReward rewardFromJson(JsonObject data) {
            JsonArray values = JSONUtils.getAsJsonArray(data, "values");
            WeightedRewardEntry[] entries = JsonHelper.mapArray(values, WeightedRewardEntry[]::new, WeightedRewardEntry::fromJson);
            return new WeightedReward(entries);
        }
    }
}
