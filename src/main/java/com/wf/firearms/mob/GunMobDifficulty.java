package com.wf.firearms.mob;

import com.wf.firearms.config.MobSpawnConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;

/** 综合世界天数、原版区域难度、L2 恶意，计算 mob 刷怪通过率与可用枪阶。 */
public final class GunMobDifficulty {
    public record Context(float spawnPassMultiplier, float combinedDifficulty, int gunLevelCap) {}

    private GunMobDifficulty() {}

    public static Context evaluate(ServerLevel level, BlockPos pos, ResourceLocation dimension, int mobStartDay) {
        MobSpawnConfig.Global g = MobSpawnConfig.global();
        long day = level.getDayTime() / 24000L;
        int dimStart = startDayForDimension(dimension, mobStartDay, g);
        float timeMul = 1f;
        int gunBonus = 0;
        if (g.timeScalingEnabled() && day >= dimStart) {
            int steps = (int) ((day - dimStart) / Math.max(1, g.daysAfterStartPerStep()));
            timeMul = 1f + steps * g.spawnChanceMultiplierPerStep();
            timeMul = Math.min(timeMul, g.maxSpawnChanceMultiplier());
            gunBonus = Math.min(steps * g.gunLevelBonusPerStep(), g.maxGunLevelBonus());
        }

        DifficultyInstance diff = level.getCurrentDifficultyAt(pos);
        float regional = diff.getEffectiveDifficulty();
        float regMul = 1f;
        int regGun = 0;
        if (g.regionalScalingEnabled()) {
            regMul = 1f + regional * g.spawnChancePerEffectiveDifficulty();
            regGun = Mth.floor(regional * g.gunLevelPerEffectiveDifficulty());
        }

        float l2Mul = 1f;
        int l2Gun = 0;
        if (g.l2Enabled()) {
            var l2Opt = L2HostilityBridge.sectionDifficulty(level, pos);
            if (l2Opt.isPresent()) {
                int l2 = l2Opt.getAsInt();
                l2Mul = 1f + (l2 / 100f) * g.l2SpawnChancePer100();
                l2Mul = Math.min(l2Mul, g.l2MaxSpawnChanceMultiplier());
                l2Gun = Math.min((l2 / 50) * g.l2GunLevelPer50(), g.l2MaxGunLevelBonus());
            }
        }

        float spawnMul = timeMul * regMul * l2Mul;
        float combined = regional + (day - dimStart) * 0.05f + gunBonus * 0.1f + regGun * 0.15f + l2Gun * 0.2f;
        int cap = MobSpawnConfig.gunLevelCapForDimension(dimension) + gunBonus + regGun + l2Gun;
        cap = Math.min(cap, MobSpawnConfig.gunLevelCapForDimension(dimension) + g.maxGunLevelBonus() + g.l2MaxGunLevelBonus());
        return new Context(spawnMul, combined, cap);
    }

    private static int startDayForDimension(ResourceLocation dim, int mobStartDay, MobSpawnConfig.Global g) {
        if ("minecraft:the_nether".equals(dim.toString())) {
            return Math.max(mobStartDay, g.spawnStartDayNether());
        }
        if ("minecraft:the_end".equals(dim.toString())) {
            return Math.max(mobStartDay, g.spawnStartDayEnd());
        }
        return Math.max(mobStartDay, g.spawnStartDayOverworld());
    }

    /** 将配置 weight 与动态倍率合成 0~1 通过率。weight 20 约等于 20% 基础通过率。 */
    public static float passChance(int baseWeight, float spawnMul) {
        float base = Mth.clamp(baseWeight / 100f, 0.01f, 0.95f);
        return Mth.clamp(base * spawnMul, 0.01f, 0.95f);
    }
}
