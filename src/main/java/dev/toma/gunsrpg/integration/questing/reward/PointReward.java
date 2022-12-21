package dev.toma.gunsrpg.integration.questing.reward;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.api.common.data.IPointProvider;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.init.QuestRegistry;
import dev.toma.questing.common.quest.Quest;
import dev.toma.questing.common.reward.RewardTransformer;
import dev.toma.questing.common.reward.RewardType;
import dev.toma.questing.common.reward.VolumeBasedReward;
import dev.toma.questing.utils.Codecs;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Collections;
import java.util.List;

public final class PointReward extends VolumeBasedReward {

    public static final Codec<PointReward> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            VolumeBasedReward.VOLUME_ADJUSTER_CODEC.listOf().optionalFieldOf("countFunctions", Collections.emptyList()).forGetter(VolumeBasedReward::getCountAdjusters),
            Codecs.enumCodec(Target.class).fieldOf("target").forGetter(t -> t.target),
            Codec.INT.optionalFieldOf("count", 1).forGetter(t -> t.count)
    ).apply(instance, PointReward::new));
    private final Target target;
    private final int count;

    public PointReward(List<RewardTransformer<Integer>> countAdjusters, Target target, int count) {
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

    @Override
    public RewardType<?> getType() {
        return QuestRegistry.POINT_REWARD;
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
}
