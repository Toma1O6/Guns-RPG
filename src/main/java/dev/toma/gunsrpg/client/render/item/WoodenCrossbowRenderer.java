package dev.toma.gunsrpg.client.render.item;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.model.WeaponModels;
import dev.toma.gunsrpg.client.model.weapon.AbstractWeaponModel;
import dev.toma.gunsrpg.client.render.RenderConfigs;
import dev.toma.gunsrpg.common.capability.IPlayerData;
import dev.toma.gunsrpg.common.init.Skills;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.util.ResourceLocation;

public class WoodenCrossbowRenderer extends AbstractWeaponRenderer {

    public static final ResourceLocation RETICLE = GunsRPG.makeResource("textures/scope/crossbow_reticle.png");

    @Override
    public AbstractWeaponModel getWeaponModel() {
        return WeaponModels.WOODEN_CROSSBOW;
    }

    @Override
    public void positionModel(MatrixStack stack, ItemCameraTransforms.TransformType transform) {
        if (transform == ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND) {
            stack.translate(0.2, 0.6, -0.3);
        } else if (transform == ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND) {
            stack.translate(-0.2, 0.45, 0.25);
        }
    }

    @Override
    protected boolean hasCustomAttachments() {
        return true;
    }

    @Override
    protected void renderAttachments(IPlayerData data, MatrixStack matrix, IRenderTypeBuffer typeBuffer, int light, int overlay, float progress) {
        if (data.getSkills().hasSkill(Skills.CROSSBOW_SCOPE)) {
            renderScope(RenderConfigs.WOODEN_CROSSBOW_SCOPE, matrix, typeBuffer, light, overlay, progress, RETICLE);
        }
    }

    @Override
    protected float scaleForTransform(ItemCameraTransforms.TransformType transform) {
        if (transform != ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND) {
            return 0.35F;
        }
        return 0.6F;
    }
}
