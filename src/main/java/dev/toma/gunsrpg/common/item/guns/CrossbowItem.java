package dev.toma.gunsrpg.common.item.guns;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.client.animation.Animations;
import dev.toma.gunsrpg.client.animation.IAnimation;
import dev.toma.gunsrpg.client.animation.impl.AimingAnimation;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.entity.EntityBullet;
import dev.toma.gunsrpg.common.entity.EntityCrossbowBolt;
import dev.toma.gunsrpg.common.init.GRPGEntityTypes;
import dev.toma.gunsrpg.common.init.GRPGSounds;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterial;
import dev.toma.gunsrpg.common.item.guns.util.GunType;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.config.GRPGConfig;
import dev.toma.gunsrpg.config.gun.IWeaponConfiguration;
import dev.toma.gunsrpg.util.SkillUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Map;

public class CrossbowItem extends GunItem {

    public CrossbowItem(String name) {
        super(name, GunType.CROSSBOW);
    }

    @Override
    public boolean isSilenced(PlayerEntity player) {
        return true;
    }

    @Override
    public int getMaxAmmo(PlayerEntity player) {
        return PlayerDataFactory.hasActiveSkill(player, Skills.CROSSBOW_REPEATER) ? 3 : 1;
    }

    @Override
    public int getFirerate(PlayerEntity player) {
        return GRPGConfig.weaponConfig.crossbow.getFirerate();
    }

    @Override
    public int getReloadTime(PlayerEntity player) {
        int base = 60;
        if (PlayerDataFactory.hasActiveSkill(player, Skills.CROSSBOW_QUIVER)) {
            base = (int) (base * 0.65);
        }
        if (PlayerDataFactory.hasActiveSkill(player, Skills.CROSSBOW_REPEATER)) {
            base = (int) (base * 1.25);
        }
        return (int) (base * SkillUtil.getReloadTimeMultiplier(player));
    }

    @Override
    public void shootBullet(World world, LivingEntity entity, ItemStack stack) {
        EntityCrossbowBolt bolt = new EntityCrossbowBolt(GRPGEntityTypes.CROSSBOW_BOLT.get(), world, entity, this, stack);
        boolean isPlayer = entity instanceof PlayerEntity;
        boolean aim = isPlayer && PlayerDataFactory.getUnsafe((PlayerEntity) entity).getAimInfo().isAiming();
        float pitch = entity.xRot + (aim ? 0.0F : (random.nextFloat() - random.nextFloat()) * 5);
        float yaw = entity.yRot + (aim ? 0.0F : (random.nextFloat() - random.nextFloat()) * 5);
        float baseVelocity = getWeaponConfig().getVelocity();
        float velocity = isPlayer && PlayerDataFactory.hasActiveSkill((PlayerEntity) entity, Skills.CROSSBOW_TOUGH_BOWSTRING) ? 1.5F * baseVelocity : baseVelocity;
        bolt.fire(pitch, yaw, velocity);
        world.addFreshEntity(bolt);
    }

    @Override
    public void onHitEntity(EntityBullet bullet, LivingEntity victim, ItemStack stack, LivingEntity shooter) {
        if (!bullet.level.isClientSide && shooter instanceof PlayerEntity && PlayerDataFactory.hasActiveSkill((PlayerEntity) shooter, Skills.CROSSBOW_POISONED_BOLTS)) {
            victim.addEffect(new EffectInstance(Effects.WITHER, 140, 1, false, false));
        }
    }

    @Override
    public void onKillEntity(EntityBullet bullet, LivingEntity victim, ItemStack stack, LivingEntity shooter) {
        if (!bullet.level.isClientSide && shooter instanceof PlayerEntity && PlayerDataFactory.hasActiveSkill((PlayerEntity) shooter, Skills.CROSSBOW_HUNTER)) {
            shooter.heal(4.0F);
        }
    }

    @Override
    public void fillAmmoMaterialData(Map<AmmoMaterial, Integer> data) {
        data.put(AmmoMaterial.STONE, 3);
        data.put(AmmoMaterial.IRON, 6);
        data.put(AmmoMaterial.GOLD, 9);
        data.put(AmmoMaterial.DIAMOND, 14);
        data.put(AmmoMaterial.EMERALD, 16);
        data.put(AmmoMaterial.AMETHYST, 20);
    }

    @Override
    public IWeaponConfiguration getWeaponConfig() {
        return GRPGConfig.weaponConfig.crossbow;
    }

    @Override
    public SoundEvent getReloadSound(PlayerEntity player) {
        return PlayerDataFactory.hasActiveSkill(player, Skills.CROSSBOW_QUIVER) ? GRPGSounds.CROSSBOW_RELOAD_FAST : GRPGSounds.CROSSBOW_RELOAD;
    }

    @Override
    protected SoundEvent getShootSound(PlayerEntity entity) {
        return GRPGSounds.CROSSBOW_SHOOT;
    }

    @Override
    protected SoundEvent getEntityShootSound(LivingEntity entity) {
        return GRPGSounds.CROSSBOW_SHOOT;
    }

    @Override
    public SkillType<?> getRequiredSkill() {
        return Skills.CROSSBOW_ASSEMBLY;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void transformRightArm(MatrixStack matrix) {
        matrix.translate(0.05F, -0.1F, 0.7F);
        matrix.mulPose(Vector3f.YP.rotationDegrees(10));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void transformLeftArm(MatrixStack matrix) {
        matrix.translate(0.3F, -0.1F, 0.0F);
        matrix.mulPose(Vector3f.YP.rotationDegrees(-30));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public AimingAnimation createAimAnimation() {
        boolean scoped = PlayerDataFactory.hasActiveSkill(Minecraft.getInstance().player, Skills.CROSSBOW_SCOPE);
        return new AimingAnimation(-0.265F, scoped ? 0.14F : 0.18F, -0.1F).animateRight((stack, f) -> {
            stack.translate(-0.265F * f, 0.16F * f, -0.1F * f);
        }).animateLeft((stack, f) -> {
            stack.translate(-0.1F * f, 0.15F * f, 0.15F * f);
            stack.mulPose(Vector3f.XP.rotationDegrees(20 * f));
        });
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public IAnimation createReloadAnimation(PlayerEntity player) {
        return new Animations.ReloadCrossbow(this.getReloadTime(player));
    }
}
