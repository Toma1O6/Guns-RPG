package dev.toma.gunsrpg.mixin;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.world.cap.WorldData;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.TargetGoal;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TargetGoal.class)
public abstract class TargetGoalMixin extends Goal {

    @Shadow @Final protected MobEntity mob;

    @Inject(method = "getFollowDistance", at = @At("HEAD"), cancellable = true)
    public void gunsrpg_getModifiedFollowDistance(CallbackInfoReturnable<Double> ci) {
        World world = mob.level;
        double baseFollowDistance = mob.getAttributeValue(Attributes.FOLLOW_RANGE);
        if (WorldData.isBloodMoon(world)) {
            ci.setReturnValue(Math.max(baseFollowDistance, GunsRPG.config.world.mobConfig.bloodMoonMobAgroRange));
        }
    }

    @Inject(method = "<init>(Lnet/minecraft/entity/MobEntity;ZZ)V", at = @At("TAIL"))
    public void gunsrpg_adjustVisibilityFilter(MobEntity entity, boolean mustSee, boolean mustReach, CallbackInfo ci) {
        if (mustSee && WorldData.isBloodMoon(entity.level)) {
            ((TargetGoal) (Object) this).mustSee = false;
        }
    }
}
