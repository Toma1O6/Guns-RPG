package dev.toma.gunsrpg.common.quest.reward;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.api.common.data.IPointProvider;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.questing.quest.Quest;
import dev.toma.questing.reward.RewardTransformer;
import dev.toma.questing.reward.RewardType;
import dev.toma.questing.reward.VolumeBasedReward;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.JSONUtils;

public final class PointReward extends VolumeBasedReward {

    private final Target target;
    private final int count;

    public PointReward(RewardTransformer<Integer>[] countAdjusters, Target target, int count) {
        super(countAdjusters);
        this.target = target;
        this.count = count;
    }

    @Override
    public void awardPlayer(PlayerEntity player, Quest quest) {
        int volume = this.getCount(this.count, player, quest);
        PlayerData.get(player).ifPresent(data -> {
            IPointProvider provider = this.target.getProvider(data);
            provider.awardPoints(volume);
        });
    }

    public enum Target {

        SKILL,
        PERK;

        public IPointProvider getProvider(IPlayerData data) {
            switch (this) {
                case SKILL: return data.getProgressData();
                case PERK: return data.getPerkProvider();
            }
            throw new UnsupportedOperationException();
        }
    }

    public static final class Serializer implements RewardType.RewardSerializer<PointReward> {

        @Override
        public PointReward rewardFromJson(JsonObject data) {
            String targetName = JSONUtils.getAsString(data, "target");
            Target target;
            try {
                target = Target.valueOf(targetName);
            } catch (IllegalArgumentException e) {
                throw new JsonSyntaxException("Unknown target: " + targetName);
            }
            int count = JSONUtils.getAsInt(data, "count", 1);
            RewardTransformer<Integer>[] countFns = resolveCountTransformers(JSONUtils.getAsJsonArray(data, "countFunctions", new JsonArray()));
            return new PointReward(countFns, target, count);
        }
    }
}
