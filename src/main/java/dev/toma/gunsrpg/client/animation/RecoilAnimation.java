package dev.toma.gunsrpg.client.animation;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.api.common.IWeaponConfig;
import dev.toma.gunsrpg.api.common.data.IAimInfo;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.api.common.data.ISkillProvider;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.common.item.guns.setup.AbstractGun;
import dev.toma.gunsrpg.common.item.guns.util.IDualWieldGun;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import lib.toma.animations.api.AnimationStage;
import lib.toma.animations.api.TickableAnimation;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;

public class RecoilAnimation extends TickableAnimation {

    private final float x;
    private final float y;
    private final float z;
    private IStagePredicate predicate;
    private boolean mainHand;

    public RecoilAnimation(float x, float y, GunItem source, ItemStack data, IPlayerData provider) {
        super(3);
        IWeaponConfig config = source.getWeaponConfig();
        IAimInfo aimInfo = provider.getAimInfo();
        float modifier = aimInfo.isAiming() ? 0.2F + 0.8F * config.getRecoilAnimationScale() : 1.0F;
        this.x = x * modifier;
        this.y = y * modifier;
        this.z = 0.09F * modifier;
        this.initDataStates(source, data, provider.getSkillProvider());
    }

    @Override
    public void animate(AnimationStage stage, MatrixStack matrixStack, IRenderTypeBuffer typeBuffer, int light, int overlay) {
        if (!predicate.canAnimate(stage, mainHand)) return;
        float interpolated = getInterpolatedProgress();
        float progress = getPartial(interpolated);
        float xRot = x * progress;
        float yRot = y * progress;
        float zKick = z * progress;
        matrixStack.translate(0.0, 0.0, zKick);
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(xRot));
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(yRot));
    }

    private float getPartial(float progress) {
        return progress <= 0.5F ? progress / 0.5F : 1.0F - (progress - 0.5F) / 0.5F;
    }

    private void initDataStates(GunItem item, ItemStack data, ISkillProvider provider) {
        boolean initialized = false;
        if (item instanceof IDualWieldGun) {
            IDualWieldGun gun = (IDualWieldGun) item;
            SkillType<?> requiredSkill = gun.getSkillForDualWield();
            if (provider.hasSkill(requiredSkill)) {
                initialized = true;
                this.predicate = IStagePredicate.DUAL;
                this.mainHand = isMainHand(data);
            }
        }
        if (!initialized) {
            this.predicate = IStagePredicate.DEFAULT;
            this.mainHand = true;
        }
    }

    private boolean isMainHand(ItemStack data) {
        return AbstractGun.getAmmoCount(data) % 2 == 0;
    }

    private static boolean canAnimateStageDualWield(AnimationStage stage, boolean main) {
        return main ? isRightSide(stage) : isLeftSide(stage);
    }

    private static boolean isRightSide(AnimationStage stage) {
        return stage == AnimationStage.RIGHT_HAND || stage == AnimationStage.HELD_ITEM;
    }

    private static boolean isLeftSide(AnimationStage stage) {
        return stage == AnimationStage.LEFT_HAND || stage == ModAnimations.DUAL_WIELD_ITEM;
    }

    interface IStagePredicate {

        IStagePredicate DEFAULT = (stage, flag) -> stage == AnimationStage.ITEM_AND_HANDS;
        IStagePredicate DUAL = RecoilAnimation::canAnimateStageDualWield;

        boolean canAnimate(AnimationStage stage, boolean flag);
    }
}
