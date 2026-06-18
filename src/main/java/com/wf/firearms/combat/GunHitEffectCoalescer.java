package com.wf.firearms.combat;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

/** 同一次射击（多弹丸/同帧）只触发一次命中附加效果。 */
public final class GunHitEffectCoalescer {
    private static final String TICK = "wf_gun_fx_tick";
    private static final String SHOOTER = "wf_gun_fx_shooter";
    /** 略大于 S686 连射间隔，同帧多弹丸合并为一次。 */
    private static final int COALESCE_TICKS = 5;

    private GunHitEffectCoalescer() {}

    public static boolean shouldApply(LivingEntity target, Player shooter) {
        if (target.level().isClientSide) {
            return false;
        }
        long now = target.level().getGameTime();
        var data = target.getPersistentData();
        if (data.hasUUID(SHOOTER) && data.getUUID(SHOOTER).equals(shooter.getUUID())) {
            long last = data.getLong(TICK);
            if (now - last < COALESCE_TICKS) {
                return false;
            }
        }
        data.putLong(TICK, now);
        data.putUUID(SHOOTER, shooter.getUUID());
        return true;
    }
}
