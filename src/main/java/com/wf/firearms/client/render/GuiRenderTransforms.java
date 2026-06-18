package com.wf.firearms.client.render;



import com.mojang.blaze3d.vertex.PoseStack;

import org.joml.Quaternionf;



/**

 * 创造栏 / 物品栏 GUI 微调（仅 BEWLR 3D 图标）。

 *

 * <p>缩放改 {@link #guiScale}；平移改 {@link #apply} 与各枪 case。全局微调用 {@link #applyGlobalSlotNudge}。

 */

public final class GuiRenderTransforms {

    private GuiRenderTransforms() {}



    /** 所有枪共用的槽位微调（Y 正 = 略往上）。 */

    public static void applyGlobalSlotNudge(PoseStack matrix) {

        matrix.translate(0.0F, 0.05F, 0.0F);

    }



    public static void apply(PoseStack matrix, String weaponKey) {

        switch (weaponKey) {

            case "m1911" -> matrix.translate(-0.2F, 0.0F, 0.0F);

            case "kar98k" -> {

                matrix.translate(-0.08F, 0.02F, 0.0F);

                matrix.mulPose(new Quaternionf().rotationY((float) (-Math.PI / 2)));

            }

            case "winchester" -> matrix.translate(-0.08F, 0.02F, 0.0F);

            case "thompson" -> matrix.translate(-0.1F, 0.0F, 0.0F);

            case "akm" -> matrix.translate(-0.06F, 0.03F, 0.0F);

            case "vss" -> matrix.translate(-0.1F, 0.0F, 0.0F);

            case "vector" -> matrix.translate(-0.08F, 0.0F, 0.0F);

            case "s686" -> matrix.translate(-0.1F, 0.0F, 0.0F);

            default -> {}

        }

    }



    public static float guiScale(String weaponKey) {

        return switch (weaponKey) {

            case "m1911", "r45", "desert_eagle" -> 0.42F;

            case "kar98k" -> 0.13F;

            case "winchester" -> 0.14F;

            case "akm" -> 0.17F;

            case "vss", "s686" -> 0.15F;

            case "vector" -> 0.16F;

            case "thompson" -> 0.17F;

            default -> 0.48F;

        };

    }

}

