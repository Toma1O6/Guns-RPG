package com.wf.firearms.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.item.ItemDisplayContext;
import org.joml.Quaternionf;
/**
 * 第一/三人称持枪姿态，数据移植自 Guns RPG {@code RenderConfigs}（1.16.5）。
 */
public final class HandRenderTransforms {
    private HandRenderTransforms() {}

    public static void apply(PoseStack pose, String weaponKey, ItemDisplayContext ctx) {
        Transform t = resolve(weaponKey, ctx);
        if (t == null) {
            return;
        }
        pose.translate(t.x, t.y, t.z);
        if (t.sx != 1f || t.sy != 1f || t.sz != 1f) {
            pose.scale(t.sx, t.sy, t.sz);
        }
        pose.mulPose(new Quaternionf(t.qx, t.qy, t.qz, t.qw));
    }

    private static Transform resolve(String weaponKey, ItemDisplayContext ctx) {
        boolean left =
                ctx == ItemDisplayContext.FIRST_PERSON_LEFT_HAND
                        || ctx == ItemDisplayContext.THIRD_PERSON_LEFT_HAND;
        boolean first =
                ctx == ItemDisplayContext.FIRST_PERSON_LEFT_HAND
                        || ctx == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND;
        boolean third =
                ctx == ItemDisplayContext.THIRD_PERSON_LEFT_HAND
                        || ctx == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND;
        if (!first && !third) {
            return null;
        }
        return switch (weaponKey) {
            case "m1911" -> left ? t(-0.1f, -0.55f, 0.33f, 1, 1, 1, 0.140f, -0.609f, 0f, 0.793f)
                    : t(0.3f, -0.25f, -0.3f, 1, 1, 1, 0.152f, -0.216f, 0f, 0.976f);
            case "r45" -> left ? t(0.35f, -0.54f, 0.3f, 1, 1, 1, 0.1f, -0.5f, 0f, 0.866f)
                    : t(0.5f, -0.27f, -0.5f, 1, 1, 1, 0.087f, -0.216f, 0f, 0.976f);
            case "desert_eagle" -> left ? t(0.3f, -0.45f, 0.3f, 1, 1, 1, 0.05f, -0.5f, 0f, 0.866f)
                    : t(0.4f, -0.35f, -0.5f, 1, 1, 1, 0.104f, -0.259f, 0f, 0.966f);
            case "thompson" -> left
                    ? t(0.1f, -0.7f, 0.3f, 0.7f, 1f, 2f, 0.141f, -0.707f, 0f, 0.707f)
                    : t(0.4f, -0.5f, -0.6f, 1, 1, 1, 0.153f, -0.383f, 0f, 0.924f);
            case "minigun" -> left
                    ? t(0.15f, -0.75f, 0.35f, 0.85f, 1.1f, 2.2f, 0.141f, -0.707f, 0f, 0.707f)
                    : t(0.45f, -0.52f, -0.65f, 1.15f, 1.15f, 1.15f, 0.153f, -0.383f, 0f, 0.924f);
            case "vector" -> left ? t(0.13f, -0.57f, -0.15f, 1, 1, 1, 0.107f, -0.537f, 0f, 0.843f)
                    : t(0.25f, -0.38f, -0.4f, 1, 1, 1, 0.134f, -0.383f, 0f, 0.924f);
            case "akm" -> left ? t(-0.05f, -0.45f, 0.15f, 0.8f, 1f, 1f, 0.122f, -0.609f, 0f, 0.793f)
                    : t(0.34f, -0.36f, -0.30f, 1, 1, 1, 0.10f, -0.28f, 0.03f, 0.95f);
            case "vss" -> left ? t(0.13f, -0.5f, -0.15f, 0.7f, 0.7f, 1f, 0.086f, -0.574f, 0f, 0.819f)
                    : t(0.15f, -0.34f, -0.4f, 1, 1, 1, 0.077f, -0.383f, 0f, 0.924f);
            case "kar98k" -> left ? t(0.4f, -1f, -0.4f, 1.3f, 1f, 1.5f, 0.15f, -0.5f, 0f, 0.866f)
                    : t(0.4f, -0.6f, -0.5f, 1, 1, 1, 0.115f, -0.383f, 0f, 0.924f);
            case "winchester" -> left ? t(0.15f, -0.5f, -0.1f, 0.6f, 0.6f, 1f, 0.086f, -0.574f, 0f, 0.819f)
                    : t(0.15f, -0.35f, -0.4f, 1, 1, 1, 0.085f, -0.423f, 0f, 0.906f);
            case "s686" -> left ? t(0.05f, -0.42f, 0f, 1, 1, 1, 0.086f, -0.574f, 0f, 0.819f)
                    : t(0.35f, -0.27f, -0.2f, 0.7f, 0.7f, 1f, 0.085f, -0.423f, 0f, 0.906f);
            default -> null;
        };
    }

    private static Transform t(
            float x,
            float y,
            float z,
            float sx,
            float sy,
            float sz,
            float qx,
            float qy,
            float qz,
            float qw) {
        return new Transform(x, y, z, sx, sy, sz, qx, qy, qz, qw);
    }

    private record Transform(
            float x, float y, float z, float sx, float sy, float sz, float qx, float qy, float qz, float qw) {}
}
