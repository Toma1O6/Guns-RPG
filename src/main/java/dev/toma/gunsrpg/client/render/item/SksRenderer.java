package dev.toma.gunsrpg.client.render.item;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.client.model.WeaponModels;
import dev.toma.gunsrpg.client.model.weapon.AbstractWeaponModel;
import dev.toma.gunsrpg.client.render.RenderConfigs;
import dev.toma.gunsrpg.common.capability.object.PlayerSkills;
import dev.toma.gunsrpg.common.init.Skills;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;

public class SksRenderer extends AbstractWeaponRenderer {

    @Override
    public AbstractWeaponModel getWeaponModel() {
        return WeaponModels.SKS;
    }

    @Override
    public void positionModel(MatrixStack stack, ItemCameraTransforms.TransformType transform) {
        switch (transform) {
            case FIRST_PERSON_RIGHT_HAND:
                stack.translate(-0.1, 0.25, 0.7);
                break;
            case THIRD_PERSON_RIGHT_HAND:
                stack.translate(-0.2, 0.2, 0.5);
                break;
        }
    }

    @Override
    protected float scaleForTransform(ItemCameraTransforms.TransformType transform) {
        if (transform == ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND)
            return super.scaleForTransform(transform);
        return 0.2F;
    }

    @Override
    protected void transformUI(MatrixStack matrix) {
        matrix.translate(-0.3, 0.8, 0.0);
    }

    @Override
    protected boolean hasCustomAttachments() {
        return true;
    }

    @Override
    protected void renderAttachments(IPlayerData data, MatrixStack matrix, IRenderTypeBuffer typeBuffer, int light, int overlay, float progress) {
        PlayerSkills skills = data.getSkills();
        if (skills.hasSkill(Skills.SKS_SUPPRESSOR)) {
            renderSuppressor(RenderConfigs.SKS_SUPPRESSOR, matrix, typeBuffer, light, overlay, progress);
        }
        if (skills.hasSkill(Skills.SKS_RED_DOT)) {
            renderReflex(RenderConfigs.SKS_REFLEX, matrix, typeBuffer, light, overlay, progress);
        }
    }
}
