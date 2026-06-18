package com.wf.firearms.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

/**
 * 持枪怪物握姿校正：在标准 BEWLR 翻转之后，按握把/枪身轴摆正并微调位置。
 */
public final class MobHeldFirearmTransforms {
    private MobHeldFirearmTransforms() {}

    /**
     * 枪口朝前 + 握把朝下，再略向身前平移。
     */
    public static void applyMobGripOrientation(PoseStack pose, String weaponKey) {
        pose.mulPose(Axis.YP.rotationDegrees(180F));
        pose.mulPose(Axis.ZP.rotationDegrees(-90F));
        if (isLauncher(weaponKey)) {
            pose.translate(0.04F, 0.02F, 0.12F);
            return;
        }
        if (isLongGun(weaponKey)) {
            pose.translate(0.04F, 0.02F, 0.14F);
            return;
        }
        pose.translate(-0.04F, 0.12F, 0.16F);
    }

    private static boolean isLauncher(String weaponKey) {
        return weaponKey.contains("launcher");
    }

    private static boolean isLongGun(String weaponKey) {
        return switch (weaponKey) {
            case "akm", "kar98k", "winchester", "awm", "vss", "vector", "thompson", "s686", "minigun" -> true;
            default -> false;
        };
    }
}
