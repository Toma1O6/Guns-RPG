package dev.toma.gunsrpg.mixin;

import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerModel.class)
public abstract class PlayerModelMixin<T extends LivingEntity> extends BipedModel<T> {

    @Shadow @Final public ModelRenderer leftSleeve;
    @Shadow @Final public ModelRenderer rightSleeve;

    public PlayerModelMixin(float p_i1148_1_) {
        super(p_i1148_1_);
    }

    @Inject(method = "setupAnim(Lnet/minecraft/entity/LivingEntity;FFFFF)V", at = @At("RETURN"))
    void gunsrpg_doWeaponAnimations(T entity, float limbSwing, float limbSwingAmount, float age, float yHead, float xHead, CallbackInfo ci) {
        if (entity.getType() != EntityType.PLAYER)
            return;
        Minecraft client = Minecraft.getInstance();
        PlayerEntity player = (PlayerEntity) entity;
        Entity cameraEntity = client.getCameraEntity();
        if (cameraEntity != player || client.options.getCameraType().isFirstPerson()) {
            return;
        }
        ItemStack stack = player.getMainHandItem();
        if (!(stack.getItem() instanceof GunItem))
            return;
        PlayerData.get(player).ifPresent(data -> {
            float xHeadPos = (float) Math.toRadians(xHead);
            float yHeadPos = (float) Math.toRadians(yHead);
            boolean aiming = data.getAimInfo().isAiming();
            if (aiming) {
                float f0 = (float) Math.toRadians(-90.0F);
                float f1 = (float) Math.toRadians(-15.0F);
                float f2 = (float) Math.toRadians(45.0F);
                rightArm.xRot = xHeadPos + f0;
                rightArm.yRot = yHeadPos;
                rightArm.zRot = -f1;
                leftArm.xRot = xHeadPos + f0;
                leftArm.yRot = yHeadPos + f2;
            } else {
                float f0 = (float) Math.toRadians(-55.0F);
                float f1 = (float) Math.toRadians(-40.0F);
                float f2 = (float) Math.toRadians(60.0F);
                float f3 = (float) Math.toRadians(-60.0F);
                rightArm.xRot = f0;
                leftArm.xRot = f3;
                rightArm.yRot = f1;
                leftArm.yRot = f2;
            }
            leftSleeve.copyFrom(leftArm);
            rightSleeve.copyFrom(rightArm);
        });
    }
}
