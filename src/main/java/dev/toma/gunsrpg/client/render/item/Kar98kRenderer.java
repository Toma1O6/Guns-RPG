package dev.toma.gunsrpg.client.render.item;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.model.WeaponModels;
import dev.toma.gunsrpg.client.model.weapon.AbstractWeaponModel;
import dev.toma.gunsrpg.client.render.RenderConfigs;
import dev.toma.gunsrpg.common.capability.IPlayerData;
import dev.toma.gunsrpg.common.capability.object.PlayerSkills;
import dev.toma.gunsrpg.common.init.Skills;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.util.ResourceLocation;

public class Kar98kRenderer extends AbstractWeaponRenderer {

    private static final ResourceLocation KAR98K_SCOPE_RETICLE = GunsRPG.makeResource("textures/scope/kar98k_reticle.png");

    @Override
    public AbstractWeaponModel getWeaponModel() {
        return WeaponModels.KAR98K;
    }

    @Override
    public void positionModel(MatrixStack stack, ItemCameraTransforms.TransformType transform) {
        if (transform == ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND) {
            stack.translate(0.2, 0.392, 0.0);
        } else if(transform == ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND) {
            stack.translate(-0.2, 0.3, 0.3);
        }
    }

    @Override
    protected float scaleForTransform(ItemCameraTransforms.TransformType transform) {
        if (transform == ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND) {
            return 0.4F;
        }
        return 0.15F;
    }

    @Override
    protected boolean hasCustomAttachments() {
        return true;
    }

    @Override
    protected void renderAttachments(IPlayerData data, MatrixStack matrix, IRenderTypeBuffer typeBuffer, int light, int overlay, float progress) {
        PlayerSkills skills = data.getSkills();
        if (skills.hasSkill(Skills.KAR98K_SUPPRESSOR)) {
            renderSuppressor(RenderConfigs.KAR98K_SUPPRESSOR, matrix, typeBuffer, light, overlay, progress);
        }
        if (skills.hasSkill(Skills.KAR98K_SCOPE)) {
            renderScope(RenderConfigs.KAR98K_SCOPE, matrix, typeBuffer, light, overlay, progress, KAR98K_SCOPE_RETICLE);
        }
    }
}
