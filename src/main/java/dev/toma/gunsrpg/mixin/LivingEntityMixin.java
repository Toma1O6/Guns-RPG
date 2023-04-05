package dev.toma.gunsrpg.mixin;

import dev.toma.gunsrpg.common.init.WeaponDamageSource;
import dev.toma.gunsrpg.world.cap.WorldData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    private DamageSource lastDamageSource;

    public LivingEntityMixin(EntityType<?> p_i48580_1_, World p_i48580_2_) {
        super(p_i48580_1_, p_i48580_2_);
    }

    @Inject(method = "canSee", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;clip(Lnet/minecraft/util/math/RayTraceContext;)Lnet/minecraft/util/math/BlockRayTraceResult;"), cancellable = true)
    private void gunsrpg_canEntitySee(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (WorldData.isBloodMoon(entity.level)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;knockback(FDD)V", shift = At.Shift.BEFORE))
    private void gunsrpg_captureDamageSourceForKnockback(DamageSource damageSource, float amount, CallbackInfoReturnable<Boolean> cir) {
        lastDamageSource = damageSource;
    }

    @Inject(method = "knockback", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraftforge/event/entity/living/LivingKnockBackEvent;getStrength()F"), cancellable = true)
    private void gunsrpg_disableKnockback(float strength, double x, double z, CallbackInfo ci) {
        if (lastDamageSource instanceof WeaponDamageSource) {
            ci.cancel();
        }
    }
}
