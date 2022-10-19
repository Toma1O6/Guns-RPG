package dev.toma.gunsrpg.mixin;

import dev.toma.gunsrpg.api.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.common.attribute.Attribs;
import dev.toma.gunsrpg.common.capability.PlayerData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerMixin extends LivingEntity {

    public PlayerMixin(EntityType<? extends LivingEntity> type, World world) {
        super(type, world);
    }

    @Inject(method = "getCurrentItemAttackStrengthDelay", at = @At("HEAD"), cancellable = true)
    public void gunsrpg_getModifiedAttackStrengthDelay(CallbackInfoReturnable<Float> ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        double attrValue = player.getAttributeValue(Attributes.ATTACK_SPEED);
        LazyOptional<IPlayerData> optional = PlayerData.get(player);
        ci.setReturnValue(optional.map(data -> {
            IAttributeProvider provider = data.getAttributes();
            double gunsrpgMeleeCooldown = provider.getAttributeValue(Attribs.MELEE_COOLDOWN);
            return this.getCurrentCooldownValue(gunsrpgMeleeCooldown * attrValue);
        }).orElse(this.getCurrentCooldownValue(attrValue)));
    }

    private float getCurrentCooldownValue(double in) {
        return (float) ((1.0F / in) * 20.0F);
    }
}
