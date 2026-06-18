package com.wf.firearms.combat;

/** 原版散布换算：{@code AbstractShotgun} 基础 2.4° × {@code AbstractProjectile.fire} 随机倍率 3.5。 */
public final class FirearmSpread {
    private static final float DEG_TO_RAD = (float) (Math.PI / 180.0);

    private FirearmSpread() {}

    public static float shotgunDegrees(float degrees) {
        return degrees * 3.5f * DEG_TO_RAD;
    }

    public static float shotgun() {
        return shotgunDegrees(2.4f);
    }
}
