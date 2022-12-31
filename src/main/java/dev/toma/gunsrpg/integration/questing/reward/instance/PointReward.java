package dev.toma.gunsrpg.integration.questing.reward.instance;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.toma.gunsrpg.api.common.data.IPointProvider;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.integration.questing.reward.provider.PointRewardProvider;
import dev.toma.questing.common.component.reward.instance.Reward;
import dev.toma.questing.common.quest.Quest;
import net.minecraft.entity.player.PlayerEntity;

public class PointReward implements Reward {

    public static final Codec<PointReward> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            PointRewardProvider.CODEC.fieldOf("provider").forGetter(t -> t.provider),
            Codec.INT.fieldOf("value").forGetter(t -> t.value)
    ).apply(instance, PointReward::new));
    private final PointRewardProvider provider;
    private final int value;

    public PointReward(PointRewardProvider provider, int value) {
        this.provider = provider;
        this.value = value;
    }

    @Override
    public void award(PlayerEntity player, Quest quest) {
        PlayerData.get(player).ifPresent(data -> {
            IPointProvider provider = this.provider.getTarget().getProvider(data);
            provider.awardPoints(this.value);
        });
    }

    @Override
    public PointRewardProvider getProvider() {
        return provider;
    }
}
