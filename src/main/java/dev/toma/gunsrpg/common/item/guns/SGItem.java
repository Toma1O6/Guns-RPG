package dev.toma.gunsrpg.common.item.guns;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.client.animation.Animations;
import dev.toma.gunsrpg.client.animation.IAnimation;
import dev.toma.gunsrpg.client.animation.MultiStepAnimation;
import dev.toma.gunsrpg.client.animation.impl.AimingAnimation;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.entity.EntityBullet;
import dev.toma.gunsrpg.common.entity.EntityShotgunPellet;
import dev.toma.gunsrpg.common.init.GRPGEntityTypes;
import dev.toma.gunsrpg.common.init.GRPGSounds;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterial;
import dev.toma.gunsrpg.common.item.guns.reload.IReloadManager;
import dev.toma.gunsrpg.common.item.guns.reload.ReloadManagerSingle;
import dev.toma.gunsrpg.common.item.guns.util.GunType;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.config.GRPGConfig;
import dev.toma.gunsrpg.config.gun.IWeaponConfiguration;
import dev.toma.gunsrpg.sided.ClientSideManager;
import dev.toma.gunsrpg.util.SkillUtil;
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

public class SGItem extends GunItem {

    public SGItem(String name) {
        super(name, GunType.SG);
    }

    @Override
    public IWeaponConfiguration getWeaponConfig() {
        return GRPGConfig.weaponConfig.s1897;
    }

    @Override
    public void fillAmmoMaterialData(Map<AmmoMaterial, Integer> data) {
        data.put(AmmoMaterial.WOOD, 0);
        data.put(AmmoMaterial.STONE, 1);
        data.put(AmmoMaterial.IRON, 2);
        data.put(AmmoMaterial.GOLD, 3);
        data.put(AmmoMaterial.DIAMOND, 5);
        data.put(AmmoMaterial.EMERALD, 6);
        data.put(AmmoMaterial.AMETHYST, 8);
    }

    @Override
    public IReloadManager getReloadManager() {
        return ReloadManagerSingle.SINGLE;
    }

    @Override
    public SoundEvent getShootSound(PlayerEntity entity) {
        return GRPGSounds.S1897;
    }

    @Override
    protected SoundEvent getEntityShootSound(LivingEntity entity) {
        return GRPGSounds.S686;
    }

    @Override
    public SoundEvent getReloadSound(PlayerEntity player) {
        return PlayerDataFactory.hasActiveSkill(player, Skills.SHOTGUN_BULLET_LOOPS) ? GRPGSounds.SG_RELOAD_SHORT : GRPGSounds.SG_RELOAD;
    }

    @Override
    public int getReloadTime(PlayerEntity player) {
        int time = PlayerDataFactory.hasActiveSkill(player, Skills.SHOTGUN_BULLET_LOOPS) ? 12 : 17;
        return (int) (time * SkillUtil.getReloadTimeMultiplier(player));
    }

    @Override
    public int getMaxAmmo(PlayerEntity player) {
        return PlayerDataFactory.hasActiveSkill(player, Skills.SHOTGUN_EXTENDED) ? 8 : 5;
    }

    @Override
    public int getFirerate(PlayerEntity player) {
        IWeaponConfiguration cfg = getWeaponConfig();
        return PlayerDataFactory.hasActiveSkill(player, Skills.SHOTGUN_PUMP_IN_ACTION) ? cfg.getUpgradedFirerate() : cfg.getFirerate();
    }

    @Override
    public void onKillEntity(EntityBullet bullet, LivingEntity victim, ItemStack stack, LivingEntity shooter) {
        if (!shooter.level.isClientSide && shooter instanceof PlayerEntity && PlayerDataFactory.hasActiveSkill((PlayerEntity) shooter, Skills.SHOTGUN_NEVER_GIVE_UP)) {
            shooter.addEffect(new EffectInstance(Effects.DAMAGE_RESISTANCE, 100, 0, false, false));
        }
    }

    @Override
    public void shootBullet(World world, LivingEntity entity, ItemStack stack) {
        boolean choke = entity instanceof PlayerEntity && PlayerDataFactory.hasActiveSkill((PlayerEntity) entity, Skills.SHOTGUN_CHOKE);
        float modifier = 3.0F;
        float velocity = this.getWeaponConfig().getVelocity();
        for (int i = 0; i < 6; i++) {
            EntityShotgunPellet bullet = new EntityShotgunPellet(GRPGEntityTypes.SHOTGUN_PELLET.get(), world, entity, this, stack);
            float pitch = choke ? entity.xRot + (random.nextFloat() * modifier - random.nextFloat() * modifier) : entity.xRot + (random.nextFloat() * modifier * 2 - random.nextFloat() * modifier * 2);
            float yaw = choke ? entity.yRot + (random.nextFloat() * modifier - random.nextFloat() * modifier) : entity.yRot + (random.nextFloat() * modifier * 2 - random.nextFloat() * modifier * 2);
            bullet.fire(pitch, yaw, velocity);
            world.addFreshEntity(bullet);
        }
    }

    @Override
    public SkillType<?> getRequiredSkill() {
        return Skills.SHOTGUN_ASSEMBLY;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void transformRightArm(MatrixStack matrix) {
        matrix.translate(0.16F, -0.1F, 0.35F);
        matrix.mulPose(Vector3f.YP.rotationDegrees(15));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void transformLeftArm(MatrixStack matrix) {
        matrix.translate(0.3F, -0.1F, -0.15F);
        matrix.mulPose(Vector3f.YP.rotationDegrees(-20));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public AimingAnimation createAimAnimation() {
        return new AimingAnimation(-0.267F, 0.22F, -0.1F).animateRight((stack, f) -> {
            stack.translate(-0.267F * f, 0.22F * f, 0.1F * f);
        }).animateLeft((stack, f) -> {
            stack.translate(-0.267F * f, 0.22F * f, 0.2F * f);
        });
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public IAnimation createReloadAnimation(PlayerEntity player) {
        return new MultiStepAnimation.Configurable(this.getReloadTime(player), "sg_reload");
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void onShoot(PlayerEntity player, ItemStack stack) {
        super.onShoot(player, stack);
        ClientSideManager.instance().processor().play(Animations.REBOLT, new Animations.ReboltSG(this.getFirerate(player)));
    }
}
