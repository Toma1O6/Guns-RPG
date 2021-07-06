package dev.toma.gunsrpg.client.baked;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.capability.object.AimInfo;
import dev.toma.gunsrpg.common.init.GRPGItems;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.config.GRPGConfig;
import dev.toma.gunsrpg.config.util.ScopeRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.common.util.LazyOptional;

public class CrossbowBakedModel extends GunBakedModel {

    @Override
    public IBakedModel handlePerspective(ItemCameraTransforms.TransformType cameraTransformType, MatrixStack mat) {
        mat.translate(0, 0.35, 0.2);
        mat.scale(0.01f, 0.01f, 0.01f);
        mat.mulPose(Vector3f.XP.rotationDegrees(180));
        mat.mulPose(Vector3f.YP.rotationDegrees(180));
        PlayerEntity player = Minecraft.getInstance().player;
        LazyOptional<PlayerData> opt = PlayerDataFactory.get(player);
        switch (cameraTransformType) {
            case FIRST_PERSON_RIGHT_HAND:
                if (opt.isPresent()) {
                    PlayerData data = opt.orElse(null);
                    ScopeRenderer scopeRenderer = GRPGConfig.clientConfig.scopeRenderer.get();
                    ItemStack held = player.getMainHandItem();
                    AimInfo info = data.getAimInfo();
                    if (info.isAiming() && scopeRenderer == ScopeRenderer.TEXTURE && PlayerDataFactory.hasActiveSkill(player, Skills.CROSSBOW_SCOPE) && held.getItem() == GRPGItems.CROSSBOW) {
                        mat.scale(0.0F, 0.0F, 0.0F);
                    }
                    return super.handlePerspective(cameraTransformType, mat);
                }
                mat.translate(30.0, -5.0, 20.0);
                break;
            case THIRD_PERSON_RIGHT_HAND:
                mat.translate(0.0, 20.0, -20.0);
                break;
            case GUI:
                mat.translate(-25.0, 35.0, 0);
                mat.mulPose(Vector3f.YN.rotationDegrees(90.0F));
                mat.mulPose(Vector3f.XN.rotationDegrees(30.0F));
                break;
        }
        return super.handlePerspective(cameraTransformType, mat);
    }
}
