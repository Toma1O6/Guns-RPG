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
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> p_i48580_1_, World p_i48580_2_) {
        super(p_i48580_1_, p_i48580_2_);
    }

    @Inject(method = "canSee", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;clip(Lnet/minecraft/util/math/RayTraceContext;)Lnet/minecraft/util/math/BlockRayTraceResult;"), cancellable = true)
    public void gunsrpg_canEntitySee(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (WorldData.isBloodMoon(entity.level)) {
            cir.setReturnValue(true);
        }
    }

    @Redirect(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;knockback(FDD)V"))
    public void gunsrpg_disableKnockback(LivingEntity instance, float knockbackAmount, double x, double z, DamageSource source) {
        if (!(source instanceof WeaponDamageSource)) {
            instance.knockback(knockbackAmount, x, z);
        }
    }
}
