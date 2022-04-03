package dev.toma.gunsrpg.client.render.item;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.api.common.data.ISkillProvider;
import dev.toma.gunsrpg.client.model.WeaponModels;
import dev.toma.gunsrpg.client.model.weapon.AbstractWeaponModel;
import dev.toma.gunsrpg.client.render.RenderConfigs;
import dev.toma.gunsrpg.common.init.Skills;
import lib.toma.animations.api.IRenderConfig;
import lib.toma.animations.engine.RenderConfig;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;

public class S12KRenderer extends AbstractWeaponRenderer {

    @Override
    public AbstractWeaponModel getWeaponModel() {
        return WeaponModels.S12K;
    }

    @Override
    protected void positionModel(MatrixStack stack, ItemCameraTransforms.TransformType transform) {
        switch (transform) {
            case THIRD_PERSON_RIGHT_HAND:
                stack.translate(-0.2, -0.2, 0.35);
                break;
            case FIRST_PERSON_RIGHT_HAND:
                stack.translate(-0.3, -0.15, 0.6);
                break;
        }
    }

    @Override
    protected float scaleForTransform(ItemCameraTransforms.TransformType transform) {
        return 0.15F;
    }

    @Override
    protected void scaleModel(MatrixStack matrixStack, ItemCameraTransforms.TransformType transform) {
        if (transform.firstPerson()) {
            matrixStack.scale(0.19F, 0.19F, 0.13F);
            return;
        }
        super.scaleModel(matrixStack, transform);
    }

    @Override
    protected void transformUI(MatrixStack matrix) {
        matrix.translate(2.1, 1.5, 0.0);
    }

    @Override
    protected boolean hasCustomAttachments() {
        return true;
    }

    @Override
    protected void renderAttachments(IPlayerData data, MatrixStack matrix, IRenderTypeBuffer typeBuffer, int light, int overlay, float progress) {
        ISkillProvider provider = data.getSkillProvider();
        if (provider.hasSkill(Skills.S12K_SUPPRESSOR)) {
            renderSuppressor(RenderConfigs.S12K_SUPPRESSOR, matrix, typeBuffer, light, overlay, progress);
        }
        if (provider.hasSkill(Skills.S12K_RED_DOT)) {
            renderReflex(RenderConfigs.S12K_REFLEX, matrix, typeBuffer, light, overlay, progress);
        }
    }
}
