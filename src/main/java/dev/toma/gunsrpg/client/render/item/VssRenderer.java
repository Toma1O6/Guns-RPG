package dev.toma.gunsrpg.client.render.item;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.client.model.WeaponModels;
import dev.toma.gunsrpg.client.model.weapon.AbstractWeaponModel;
import dev.toma.gunsrpg.common.init.Skills;
import lib.toma.animations.api.IRenderConfig;
import lib.toma.animations.engine.RenderConfig;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;

public class VssRenderer extends AbstractWeaponRenderer {

    @Override
    public AbstractWeaponModel getWeaponModel() {
        return WeaponModels.VSS;
    }

    @Override
    protected void positionModel(MatrixStack stack, ItemCameraTransforms.TransformType transform) {
        switch (transform) {
            case THIRD_PERSON_RIGHT_HAND:
                stack.translate(-0.2, -0.05, 0.35);
                break;
            case FIRST_PERSON_RIGHT_HAND:
                stack.translate(-0.2, -0.1, 0.2);
                break;
        }
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
    protected float scaleForTransform(ItemCameraTransforms.TransformType transform) {
        return transform == ItemCameraTransforms.TransformType.GUI ? 0.35F : 0.25F;
    }

    @Override
    protected void transformUI(MatrixStack matrix) {
        matrix.translate(0.9, 0.6, 0.0);
    }

    @Override
    protected boolean hasCustomAttachments() {
        return true;
    }

    @Override
    protected void renderAttachments(IPlayerData data, MatrixStack matrix, IRenderTypeBuffer typeBuffer, int light, int overlay, float progress) {
        if (data.getSkillProvider().hasSkill(Skills.VSS_SCOPE)) {
            renderPsoScope(IRenderConfig.empty(), matrix, typeBuffer, light, overlay, progress);
        }
    }
}
