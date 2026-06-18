package com.wf.firearms.client.model.weapon;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.wf.firearms.client.model.AbstractSolidEntityModel;
import com.wf.firearms.client.model.legacy.LegacyModelRenderer;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/** Guns RPG 武器模型基类（仅静态几何，无换弹动画立方体）。 */
public abstract class AbstractWeaponModel extends AbstractSolidEntityModel {

    /** 动画挂点零件（slide、barrels 等），须与 {@link #renderWeapon} 一并绘制。 */
    private final List<LegacyModelRenderer> attachmentParts = new ArrayList<>();

    public final void render(
            ItemStack stack,
            PoseStack matrix,
            VertexConsumer builder,
            int light,
            int overlay) {
        renderWeapon(stack, matrix, builder, light, overlay);
        for (LegacyModelRenderer part : attachmentParts) {
            part.render(matrix, builder, light, overlay);
        }
    }

    protected abstract void renderWeapon(
            ItemStack stack, PoseStack matrix, VertexConsumer builder, int light, int overlay);

    /** 兼容 Guns RPG 生成代码中的动画挂点；静态展示时由 {@link #render} 统一绘制。 */
    protected LegacyModelRenderer setSpecialRenderer(Object stage, LegacyModelRenderer renderer) {
        if (renderer != null && !attachmentParts.contains(renderer)) {
            attachmentParts.add(renderer);
        }
        return renderer;
    }

    protected LegacyModelRenderer setSpecialRenderer(Object stage, Object selector) {
        return new LegacyModelRenderer(this);
    }

    protected LegacyModelRenderer setBulletRenderer(LegacyModelRenderer renderer) {
        if (renderer != null && !attachmentParts.contains(renderer)) {
            attachmentParts.add(renderer);
        }
        return renderer;
    }
}
