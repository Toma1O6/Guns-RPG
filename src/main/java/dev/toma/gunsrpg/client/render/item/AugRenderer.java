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

public class AugRenderer extends AbstractWeaponRenderer {

    @Override
    public AbstractWeaponModel getWeaponModel() {
        return WeaponModels.AUG;
    }

    @Override
    protected void positionModel(MatrixStack stack, ItemCameraTransforms.TransformType transform) {
        switch (transform) {
            case THIRD_PERSON_RIGHT_HAND:
                stack.translate(-0.2, 0.15, 0.8);
                break;
            case FIRST_PERSON_RIGHT_HAND:
                stack.translate(-0.2, 0.2, 0.85);
                break;
        }
    }

    @Override
    protected float scaleForTransform(ItemCameraTransforms.TransformType transform) {
        return transform.firstPerson() ? 0.4F : 0.25F;
    }

    @Override
    protected void transformUI(MatrixStack matrix) {
        matrix.translate(0.0, 0.5, 0.0);
    }

    @Override
    protected boolean hasCustomAttachments() {
        return true;
    }

    @Override
    protected void renderAttachments(IPlayerData data, MatrixStack matrix, IRenderTypeBuffer typeBuffer, int light, int overlay, float progress) {
        ISkillProvider provider = data.getSkillProvider();
        if (provider.hasSkill(Skills.AUG_SUPPRESSOR)) {
            renderSuppressor(RenderConfigs.AUG_SUPPRESSOR, matrix, typeBuffer, light, overlay, progress);
        }
        if (provider.hasSkill(Skills.AUG_RED_DOT)) {
            renderReflex(RenderConfigs.AUG_REFLEX, matrix, typeBuffer, light, overlay, progress);
        }
    }
}
