package dev.toma.gunsrpg.mixin;

import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.TargetGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NearestAttackableTargetGoal.class)
public abstract class NearestAttackableGoalMixin extends TargetGoal {

    @Shadow protected EntityPredicate targetConditions;

    public NearestAttackableGoalMixin(MobEntity p_i50309_1_, boolean p_i50309_2_, boolean p_i50309_3_) {
        super(p_i50309_1_, p_i50309_2_, p_i50309_3_);
    }

    @Inject(method = "<init>(Lnet/minecraft/entity/MobEntity;Ljava/lang/Class;IZZLjava/util/function/Predicate;)V", at = @At("TAIL"))
    public void gunsrpg_modifyTargetSelector(CallbackInfo ci) {
        if (!this.mustSee) {
            this.targetConditions.allowUnseeable();
        }
    }
}
