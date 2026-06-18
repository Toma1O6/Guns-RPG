package com.wf.firearms.bloodmoon;

import com.wf.firearms.config.BloodmoonConfig;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.phys.AABB;

/** 血月期间：玩家周围仇恨范围内的僵尸主动锁定玩家。 */
public final class BloodmoonAggroHandler {
    private static final int TICK_INTERVAL = 10;

    private BloodmoonAggroHandler() {}

    public static void tick(ServerLevel level) {
        if (!BloodmoonService.isBloodMoon(level)) {
            return;
        }
        if (level.getGameTime() % TICK_INTERVAL != 0) {
            return;
        }
        double range = BloodmoonConfig.bloodMoonMobAgroRange();
        for (ServerPlayer player : level.players()) {
            if (player.isCreative() || player.isSpectator()) {
                continue;
            }
            AABB box = player.getBoundingBox().inflate(range);
            for (Zombie zombie : level.getEntitiesOfClass(Zombie.class, box, z -> z.isAlive() && !z.isBaby())) {
                if (zombie.distanceToSqr(player) > range * range) {
                    continue;
                }
                if (zombie.getTarget() == null || zombie.getTarget() instanceof ServerPlayer) {
                    zombie.setTarget(player);
                }
            }
        }
    }
}
