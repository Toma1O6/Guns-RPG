package com.wf.firearms.combat;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

/**
 * 弹药材质命中特效（材质相关）；枪族通用效果见 {@link GunHitEffects}。
 */
public final class AmmoHitEffects {
    private AmmoHitEffects() {}

    public static void onBulletHit(
            LivingEntity target,
            Player shooter,
            AmmoMaterial material,
            AmmoCaliber caliber,
            float damageDealt) {
        // 材质特效（毒/燃等）后续在此接入；枪族 debuff 由 GunHitEffectHandler 统一处理。
    }
}
