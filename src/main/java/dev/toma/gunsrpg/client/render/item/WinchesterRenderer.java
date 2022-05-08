package dev.toma.gunsrpg.client.render.item;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.client.model.WeaponModels;
import dev.toma.gunsrpg.client.model.component.ScopeModel;
import dev.toma.gunsrpg.client.model.weapon.AbstractWeaponModel;
import dev.toma.gunsrpg.client.render.RenderConfigs;
import dev.toma.gunsrpg.common.init.Skills;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;

public class WinchesterRenderer extends AbstractWeaponRenderer {

    @Override
    public AbstractWeaponModel getWeaponModel() {
        return WeaponModels.WINCHESTER;
    }

    @Override
    protected void positionModel(MatrixStack stack, ItemCameraTransforms.TransformType transform) {
        switch (transform) {
            case THIRD_PERSON_RIGHT_HAND:
                stack.translate(-0.2, 0.2, 0.4);
                break;
            case FIRST_PERSON_RIGHT_HAND:
                stack.translate(-0.2, 0.25, 0.3);
                break;
        }
    }

    @Override
    protected float scaleForTransform(ItemCameraTransforms.TransformType transform) {
        return 0.25F;
    }

    @Override
    protected void scaleModel(MatrixStack matrixStack, ItemCameraTransforms.TransformType transform) {
        if (transform.firstPerson()) {
            matrixStack.scale(0.35F, 0.35F, 0.25F);
            return;
        }
        super.scaleModel(matrixStack, transform);
    }

    @Override
    protected void transformUI(MatrixStack matrix) {
        matrix.translate(0.4, 0.4, 0.0);
    }

    @Override
    protected boolean hasCustomAttachments() {
        return true;
    }

    @Override
    protected void renderAttachments(IPlayerData data, MatrixStack matrix, IRenderTypeBuffer typeBuffer, int light, int overlay, float progress) {
        if (data.getSkillProvider().hasSkill(Skills.WINCHESTER_SCOPE)) {
            renderScope(RenderConfigs.WINCHESTER_SCOPE, matrix, typeBuffer, light, overlay, progress, ScopeModel.PSO_RETICLE);
        }
    }
}
