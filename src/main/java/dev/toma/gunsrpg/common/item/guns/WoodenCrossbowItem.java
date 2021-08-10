package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.animation.Animations;
import dev.toma.gunsrpg.client.animation.IAnimation;
import dev.toma.gunsrpg.client.render.item.WoodenCrossbowRenderer;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.entity.BulletEntity;
import dev.toma.gunsrpg.common.entity.CrossbowBoltEntity;
import dev.toma.gunsrpg.common.init.ModEntities;
import dev.toma.gunsrpg.common.init.ModSounds;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterial;
import dev.toma.gunsrpg.common.item.guns.util.GunType;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.config.ModConfig;
import dev.toma.gunsrpg.config.gun.IWeaponConfig;
import dev.toma.gunsrpg.util.SkillUtil;
import lib.toma.animations.IRenderConfig;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Map;

public class WoodenCrossbowItem extends GunItem {

    private static final ResourceLocation AIM = GunsRPG.makeResource("wooden_crossbow/aim");
    private static final ResourceLocation AIM_SCOPED = GunsRPG.makeResource("wooden_crossbow/aim_scoped");

    public WoodenCrossbowItem(String name) {
        super(name, GunType.CROSSBOW, new Properties().setISTER(() -> WoodenCrossbowRenderer::new));
    }

    @Override
    public boolean isSilenced(PlayerEntity player) {
        return true;
    }

    @Override
    public int getMaxAmmo(PlayerEntity player) {
        return PlayerData.hasActiveSkill(player, Skills.CROSSBOW_REPEATER) ? 3 : 1;
    }

    @Override
    public int getFirerate(PlayerEntity player) {
        return ModConfig.weaponConfig.crossbow.getFirerate();
    }

    @Override
    public int getReloadTime(PlayerEntity player) {
        int base = 60;
        if (PlayerData.hasActiveSkill(player, Skills.CROSSBOW_QUIVER)) {
            base = (int) (base * 0.65);
        }
        if (PlayerData.hasActiveSkill(player, Skills.CROSSBOW_REPEATER)) {
            base = (int) (base * 1.25);
        }
        return (int) (base * SkillUtil.getReloadTimeMultiplier(player));
    }

    @Override
    public void shootBullet(World world, LivingEntity entity, ItemStack stack) {
        CrossbowBoltEntity bolt = new CrossbowBoltEntity(ModEntities.CROSSBOW_BOLT.get(), world, entity, this, stack);
        boolean isPlayer = entity instanceof PlayerEntity;
        boolean aim = isPlayer && PlayerData.getUnsafe((PlayerEntity) entity).getAimInfo().isAiming();
        float pitch = entity.xRot + (aim ? 0.0F : (random.nextFloat() - random.nextFloat()) * 5);
        float yaw = entity.yRot + (aim ? 0.0F : (random.nextFloat() - random.nextFloat()) * 5);
        float baseVelocity = getWeaponConfig().getVelocity();
        float velocity = isPlayer && PlayerData.hasActiveSkill((PlayerEntity) entity, Skills.CROSSBOW_TOUGH_BOWSTRING) ? 1.5F * baseVelocity : baseVelocity;
        bolt.fire(pitch, yaw, velocity);
        world.addFreshEntity(bolt);
    }

    @Override
    public void onHitEntity(BulletEntity bullet, LivingEntity victim, ItemStack stack, LivingEntity shooter) {
        if (!bullet.level.isClientSide && shooter instanceof PlayerEntity && PlayerData.hasActiveSkill((PlayerEntity) shooter, Skills.CROSSBOW_POISONED_BOLTS)) {
            victim.addEffect(new EffectInstance(Effects.WITHER, 140, 1, false, false));
        }
    }

    @Override
    public void onKillEntity(BulletEntity bullet, LivingEntity victim, ItemStack stack, LivingEntity shooter) {
        if (!bullet.level.isClientSide && shooter instanceof PlayerEntity && PlayerData.hasActiveSkill((PlayerEntity) shooter, Skills.CROSSBOW_HUNTER)) {
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
    public IWeaponConfig getWeaponConfig() {
        return ModConfig.weaponConfig.crossbow;
    }

    @Override
    public SoundEvent getReloadSound(PlayerEntity player) {
        return PlayerData.hasActiveSkill(player, Skills.CROSSBOW_QUIVER) ? ModSounds.CROSSBOW_RELOAD_FAST : ModSounds.CROSSBOW_RELOAD;
    }

    @Override
    protected SoundEvent getShootSound(PlayerEntity entity) {
        return ModSounds.CROSSBOW_SHOOT;
    }

    @Override
    protected SoundEvent getEntityShootSound(LivingEntity entity) {
        return ModSounds.CROSSBOW_SHOOT;
    }

    @Override
    public SkillType<?> getRequiredSkill() {
        return Skills.CROSSBOW_ASSEMBLY;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public IRenderConfig right() {
        return IRenderConfig.empty();
    }

    @Override
    public ResourceLocation getAimAnimationPath(ItemStack stack, PlayerEntity player) {
        return PlayerData.hasActiveSkill(player, Skills.CROSSBOW_SCOPE) ? AIM_SCOPED : AIM;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public IAnimation createReloadAnimation(PlayerEntity player) {
        return new Animations.ReloadCrossbow(this.getReloadTime(player));
    }
}
