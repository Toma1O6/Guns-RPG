package dev.toma.gunsrpg.resource.progression;

import dev.toma.gunsrpg.api.common.data.IKillData;
import net.minecraft.entity.player.PlayerEntity;

public class FakeLevelingStrategy implements IProgressionStrategy {

    public static final IProgressionStrategy INSTANCE = new FakeLevelingStrategy();

    private FakeLevelingStrategy() {}

    @Override
    public int getRequiredKillCount(int level) {
        return 0;
    }

    @Override
    public void applyRewards(PlayerEntity player, IKillData data, int level) {

    }
}
