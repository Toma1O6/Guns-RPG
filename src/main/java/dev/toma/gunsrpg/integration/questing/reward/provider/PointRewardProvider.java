package dev.toma.gunsrpg.integration.questing.reward.provider;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.api.common.data.IPointProvider;
import dev.toma.gunsrpg.common.init.QuestRegistry;
import dev.toma.gunsrpg.integration.questing.reward.instance.PointReward;
import dev.toma.questing.common.component.reward.RewardType;
import dev.toma.questing.common.component.reward.provider.VolumeBasedRewardProvider;
import dev.toma.questing.common.component.reward.transformer.RewardTransformer;
import dev.toma.questing.common.quest.Quest;
import dev.toma.questing.utils.Codecs;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Collections;
import java.util.List;

public final class PointRewardProvider extends VolumeBasedRewardProvider<PointReward> {

    public static final Codec<PointRewardProvider> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            VolumeBasedRewardProvider.VOLUME_ADJUSTER_CODEC.listOf().optionalFieldOf("countFunctions", Collections.emptyList()).forGetter(VolumeBasedRewardProvider::getCountAdjusters),
            Codecs.enumCodec(Target.class).fieldOf("target").forGetter(t -> t.target),
            Codec.INT.optionalFieldOf("count", 1).forGetter(t -> t.count)
    ).apply(instance, PointRewardProvider::new));
    private final Target target;
    private final int count;

    public PointRewardProvider(List<RewardTransformer<Integer>> countAdjusters, Target target, int count) {
        super(countAdjusters);
        this.target = target;
        this.count = count;
    }

    @Override
    public PointReward createReward(PlayerEntity playerEntity, Quest quest) {
        return new PointReward(this, getCount(this.count, playerEntity, quest, this.getCountAdjusters()));
    }

    @Override
    public RewardType<PointReward, ?> getType() {
        return QuestRegistry.POINT_REWARD;
    }

    public Target getTarget() {
        return target;
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
