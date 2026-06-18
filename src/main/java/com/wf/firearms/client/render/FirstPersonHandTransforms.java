package com.wf.firearms.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.item.ItemDisplayContext;
import org.joml.Quaternionf;

/**
 * 第一人称持枪姿态：枪身从屏幕右下指向左上（与截图 AK 一致），高度适中。
 *
 * <p>在 {@link AbstractWeaponRenderer} 的 Blockbench 枢轴与手持旋转之后应用。
 */
public final class FirstPersonHandTransforms {
    /** 腰射基准（AK 截图）：略向左上抬枪，避免过高/过低 */
    private static final float HIP_TX = -0.07F;
    private static final float HIP_TY = 0.45F;
    private static final float HIP_TZ = -0.05F;
    private static final float HIP_ROT_Y = -6F;
    private static final float HIP_ROT_X = -3F;

    private FirstPersonHandTransforms() {}

    public static void applyHipFire(PoseStack pose, String weaponKey) {
        switch (weaponKey) {
            case "m1911", "r45", "desert_eagle" -> applyDiagonal(pose, -0.05F, 0.40F, -0.04F, -5F, -2F);
            case "minigun" -> applyDiagonal(pose, -0.06F, 0.43F, -0.04F, -5.5F, -2.5F);
            case "kar98k", "winchester", "awm", "akm", "vss", "vector", "thompson", "s686" -> applyDiagonal(
                    pose, HIP_TX, HIP_TY, HIP_TZ, HIP_ROT_Y, HIP_ROT_X);
            default -> applyDiagonal(pose, HIP_TX, HIP_TY, HIP_TZ, HIP_ROT_Y, HIP_ROT_X);
        }
    }

    /** 瞄准：与腰射同基准，仅微调抬高/伸前（避免右键后枪位跳变过大） */
    public static void applyAim(PoseStack pose, String weaponKey) {
        switch (weaponKey) {
            case "m1911", "r45", "desert_eagle" -> applyDiagonal(pose, -0.05F, 0.42F, -0.06F, -5F, -3F);
            case "kar98k", "winchester", "awm" -> applyDiagonal(pose, -0.07F, 0.46F, -0.06F, -6F, -4F);
            case "akm", "vss", "vector", "thompson", "s686", "minigun" -> applyDiagonal(
                    pose, HIP_TX, HIP_TY, -0.06F, HIP_ROT_Y, HIP_ROT_X);
            default -> applyDiagonal(pose, HIP_TX, HIP_TY, -0.06F, HIP_ROT_Y, HIP_ROT_X);
        }
    }

    public static void applyThirdPerson(PoseStack pose, String weaponKey) {
        if (isLauncher(weaponKey)) {
            pose.translate(-0.10F, 0.42F, 0.18F);
            return;
        }
        if (isPistol(weaponKey)) {
            pose.translate(-0.10F, 0.50F, 0.22F);
            return;
        }
        if (isLongGun(weaponKey)) {
            pose.translate(-0.14F, 0.48F, 0.22F);
            return;
        }
        pose.translate(-0.12F, 0.47F, 0.20F);
    }

    /** 怪物抬臂：保持肩高 Y，加大 Z 沿手臂伸向双手。 */
    public static void applyMobThirdPerson(PoseStack pose, String weaponKey) {
        if (isLauncher(weaponKey)) {
            pose.translate(-0.08F, 0.30F, 0.40F);
            return;
        }
        if (isPistol(weaponKey)) {
            pose.translate(-0.10F, 0.30F, 0.40F);
            return;
        }
        if (isLongGun(weaponKey)) {
            pose.translate(-0.10F, 0.28F, 0.42F);
            return;
        }
        pose.translate(-0.08F, 0.29F, 0.39F);
    }

    public static float handScale(String weaponKey, ItemDisplayContext transform) {
        boolean fp = transform.firstPerson();
        return switch (weaponKey) {
            case "m1911", "r45" -> fp ? 0.35F : 0.32F;
            case "desert_eagle" -> fp ? 0.32F : 0.30F;
            case "akm", "s686" -> fp ? 0.18F : 0.15F;
            case "thompson", "vector", "vss" -> fp ? 0.16F : 0.14F;
            case "kar98k", "winchester", "awm" -> fp ? 0.14F : 0.13F;
            case "minigun" -> fp ? 0.15F : 0.13F;
            default -> fp ? 0.18F : 0.15F;
        };
    }

    private static void applyDiagonal(
            PoseStack pose, float tx, float ty, float tz, float rotYDeg, float rotXDeg) {
        pose.translate(tx, ty, tz);
        pose.mulPose(new Quaternionf().rotationY((float) Math.toRadians(rotYDeg)));
        if (rotXDeg != 0F) {
            pose.mulPose(new Quaternionf().rotationX((float) Math.toRadians(rotXDeg)));
        }
    }

    private static boolean isPistol(String key) {
        return "m1911".equals(key) || "r45".equals(key) || "desert_eagle".equals(key);
    }

    private static boolean isLongGun(String key) {
        return "kar98k".equals(key)
                || "winchester".equals(key)
                || "awm".equals(key)
                || "akm".equals(key)
                || "vss".equals(key)
                || "thompson".equals(key)
                || "vector".equals(key)
                || "minigun".equals(key);
    }

    private static boolean isLauncher(String key) {
        return "grenade_launcher".equals(key) || "rocket_launcher".equals(key);
    }
}
