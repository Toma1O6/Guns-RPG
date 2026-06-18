package com.wf.firearms.spawn;

import com.wf.firearms.entity.RocketAngelEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.ServerLevelAccessor;

public final class MobSpawnPlacement {
    private MobSpawnPlacement() {}

    public static boolean checkRocketAngelSpawn(
            EntityType<RocketAngelEntity> type,
            ServerLevelAccessor level,
            MobSpawnType reason,
            BlockPos pos,
            net.minecraft.util.RandomSource random) {
        return level.getCurrentDifficultyAt(pos).isHarderThan(random.nextFloat() * 0.5f);
    }
}
