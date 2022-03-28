package dev.toma.gunsrpg.client.render.item;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.api.common.data.ISkillProvider;
import dev.toma.gunsrpg.client.model.WeaponModels;
import dev.toma.gunsrpg.client.model.weapon.AbstractWeaponModel;
import dev.toma.gunsrpg.client.render.RenderConfigs;
import dev.toma.gunsrpg.common.init.Skills;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;

public class VectorRenderer extends AbstractWeaponRenderer {

    @Override
    public AbstractWeaponModel getWeaponModel() {
        return WeaponModels.VECTOR;
    }

    @Override
    protected void positionModel(MatrixStack stack, ItemCameraTransforms.TransformType transform) {
        switch (transform) {
            case THIRD_PERSON_RIGHT_HAND:
                stack.translate(-0.2, 0.28, 0.3);
                break;
            case FIRST_PERSON_RIGHT_HAND:
                stack.translate(-0.1, 0.4, 0.0);
                break;
        }
    }

    @Override
    protected float scaleForTransform(ItemCameraTransforms.TransformType transform) {
        return transform.firstPerson() ? 0.35F : 0.25F;
    }

    @Override
    protected void transformUI(MatrixStack matrix) {
        matrix.translate(0.6, -0.2, 0.0);
    }

    @Override
    protected boolean hasCustomAttachments() {
        return true;
    }

    @Override
    protected void renderAttachments(IPlayerData data, MatrixStack matrix, IRenderTypeBuffer typeBuffer, int light, int overlay, float progress) {
        ISkillProvider provider = data.getSkillProvider();
        if (provider.hasSkill(Skills.VECTOR_RED_DOT))
            renderReflex(RenderConfigs.VECTOR_REFLEX, matrix, typeBuffer, light, overlay, progress);
        if (provider.hasSkill(Skills.VECTOR_SUPPRESSOR))
            renderSuppressor(RenderConfigs.VECTOR_SUPPRESSOR, matrix, typeBuffer, light, overlay, progress);
    }
}
