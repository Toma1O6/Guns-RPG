package dev.toma.gunsrpg.client.baked;

import dev.toma.gunsrpg.common.ModRegistry;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.skilltree.Ability;
import dev.toma.gunsrpg.config.GRPGConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.model.TRSRTransformation;
import org.apache.commons.lang3.tuple.Pair;

import javax.vecmath.Matrix4f;

public class CrossbowBakedModel extends GunBakedModel {

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
        Matrix4f matrix4f = new Matrix4f();
        matrix4f.setIdentity();
        TRSRTransformation trsrTransformation = new TRSRTransformation(matrix4f);
        EntityPlayer player = Minecraft.getMinecraft().player;
        GlStateManager.translate(0, 0.35, 0.2);
        GlStateManager.scale(0.01, 0.01, 0.01);
        GlStateManager.rotate(180, 1, 0, 0);
        GlStateManager.rotate(180, 0, 1, 0);
        switch (cameraTransformType) {
            case FIRST_PERSON_RIGHT_HAND: {
                if(PlayerDataFactory.get(player).getAimInfo().isAiming() && GRPGConfig.client.scopeRenderer.isTextureOverlay() && PlayerDataFactory.hasActiveSkill(player, Ability.CROSSBOW_SCOPE) && player.getHeldItemMainhand().getItem() == ModRegistry.GRPGItems.CROSSBOW) {
                    GlStateManager.scale(0, 0, 0);
                    return Pair.of(this, trsrTransformation.getMatrix());
                }
                GlStateManager.translate(30F, -5F, 20F);
                break;
            }
            case THIRD_PERSON_RIGHT_HAND: {
                GlStateManager.translate(0.0F, 20F, -20f);
                break;
            }
            case GUI: {
                GlStateManager.translate(-25, 35, 0);
                GlStateManager.rotate(90, 0, -1, 0);
                GlStateManager.rotate(30, -1, 0, 0);
                break;
            }
            default: break;
        }
        return Pair.of(this, trsrTransformation.getMatrix());
    }
}
