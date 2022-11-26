package dev.toma.gunsrpg.common.quest.reward;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.toma.gunsrpg.util.math.WeightedRandom;
import dev.toma.questing.quest.Quest;
import dev.toma.questing.reward.IReward;
import dev.toma.questing.reward.RewardType;
import dev.toma.questing.utils.JsonHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.JSONUtils;

public class WeightedReward implements IReward {

    private final WeightedRandom<WeightedRewardEntry> entries;

    public WeightedReward(WeightedRewardEntry[] entries) {
        this.entries = new WeightedRandom<>(WeightedRewardEntry::getWeight, entries);
    }

    @Override
    public void awardPlayer(PlayerEntity player, Quest quest) {
        IReward reward = this.chooseReward();
        reward.awardPlayer(player, quest);
    }

    public IReward chooseReward() {
        return this.entries.getRandom().getRewardEntry();
    }

    public static final class WeightedRewardEntry {

        private final int weight;
        private final IReward entry;

        public WeightedRewardEntry(int weight, IReward value) {
            this.weight = weight;
            this.entry = value;
        }

        public int getWeight() {
            return weight;
        }

        public IReward getRewardEntry() {
            return entry;
        }

        public static WeightedRewardEntry fromJson(JsonElement element) {
            JsonObject data = JsonHelper.requireObject(element);
            int weight = JSONUtils.getAsInt(data, "weight", 1);
            IReward reward = RewardType.fromJson(JSONUtils.getAsJsonObject(data, "entry"));
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
