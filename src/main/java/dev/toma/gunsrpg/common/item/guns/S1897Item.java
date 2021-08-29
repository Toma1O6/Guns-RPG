package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.render.RenderConfigs;
import dev.toma.gunsrpg.client.render.item.S1897Renderer;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.entity.BulletEntity;
import dev.toma.gunsrpg.common.entity.ShotgunPelletEntity;
import dev.toma.gunsrpg.common.init.ModEntities;
import dev.toma.gunsrpg.common.init.ModSounds;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterial;
import dev.toma.gunsrpg.common.item.guns.reload.IReloadManager;
import dev.toma.gunsrpg.common.item.guns.reload.ReloadManagers;
import dev.toma.gunsrpg.common.item.guns.util.GunType;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.config.ModConfig;
import dev.toma.gunsrpg.config.gun.IWeaponConfig;
import dev.toma.gunsrpg.util.SkillUtil;
import lib.toma.animations.api.IRenderConfig;
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

public class S1897Item extends GunItem {

    private static final ResourceLocation AIM_ANIMATION = GunsRPG.makeResource("s1897/aim");
    private static final ResourceLocation RELOAD_ANIMATION = GunsRPG.makeResource("s1897/reload");
    private static final ResourceLocation LOAD_BULLET_ANIMATION = GunsRPG.makeResource("s1897/load_bullet");

    public S1897Item(String name) {
        super(name, GunType.SG, new Properties().setISTER(() -> S1897Renderer::new));
    }

    @Override
    public IWeaponConfig getWeaponConfig() {
        return ModConfig.weaponConfig.s1897;
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
    public IReloadManager getReloadManager(PlayerEntity player) {
        return ReloadManagers.singleBulletLoading(30, player, this, player.getMainHandItem(), LOAD_BULLET_ANIMATION);
    }

    @Override
    public SoundEvent getShootSound(PlayerEntity entity) {
        return ModSounds.S1897;
    }

    @Override
    protected SoundEvent getEntityShootSound(LivingEntity entity) {
        return ModSounds.S686;
    }

    @Override
    public SoundEvent getReloadSound(PlayerEntity player) {
        return PlayerData.hasActiveSkill(player, Skills.S1897_BULLET_LOOPS) ? ModSounds.SHOTGUN_RELOAD_SHORT : ModSounds.SHOTGUN_RELOAD;
    }

    @Override
    public int getReloadTime(PlayerEntity player) {
        int time = PlayerData.hasActiveSkill(player, Skills.S1897_BULLET_LOOPS) ? 12 : 17;
        return (int) (time * SkillUtil.getReloadTimeMultiplier(player));
    }

    @Override
    public int getMaxAmmo(PlayerEntity player) {
        return PlayerData.hasActiveSkill(player, Skills.S1897_EXTENDED) ? 8 : 5;
    }

    @Override
    public int getFirerate(PlayerEntity player) {
        IWeaponConfig cfg = getWeaponConfig();
        return PlayerData.hasActiveSkill(player, Skills.S1897_PUMP_IN_ACTION) ? cfg.getUpgradedFirerate() : cfg.getFirerate();
    }

    @Override
    public void onKillEntity(BulletEntity bullet, LivingEntity victim, ItemStack stack, LivingEntity shooter) {
        if (!shooter.level.isClientSide && shooter instanceof PlayerEntity && PlayerData.hasActiveSkill((PlayerEntity) shooter, Skills.S1897_NEVER_GIVE_UP)) {
            shooter.addEffect(new EffectInstance(Effects.DAMAGE_RESISTANCE, 100, 0, false, false));
        }
    }

    @Override
    public void shootBullet(World world, LivingEntity entity, ItemStack stack) {
        boolean choke = entity instanceof PlayerEntity && PlayerData.hasActiveSkill((PlayerEntity) entity, Skills.S1897_CHOKE);
        float modifier = 3.0F;
        float velocity = this.getWeaponConfig().getVelocity();
        for (int i = 0; i < 6; i++) {
            ShotgunPelletEntity bullet = new ShotgunPelletEntity(ModEntities.SHOTGUN_PELLET.get(), world, entity, this, stack);
            float pitch = choke ? entity.xRot + (random.nextFloat() * modifier - random.nextFloat() * modifier) : entity.xRot + (random.nextFloat() * modifier * 2 - random.nextFloat() * modifier * 2);
            float yaw = choke ? entity.yRot + (random.nextFloat() * modifier - random.nextFloat() * modifier) : entity.yRot + (random.nextFloat() * modifier * 2 - random.nextFloat() * modifier * 2);
            bullet.fire(pitch, yaw, velocity);
            world.addFreshEntity(bullet);
        }
    }

    @Override
    public SkillType<?> getRequiredSkill() {
        return Skills.S1897_ASSEMBLY;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ResourceLocation getAimAnimationPath(ItemStack stack, PlayerEntity player) {
        return AIM_ANIMATION;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ResourceLocation getReloadAnimation(PlayerEntity player) {
        return RELOAD_ANIMATION;
    }

    // TODO
    @OnlyIn(Dist.CLIENT)
    @Override
    public void onShoot(PlayerEntity player, ItemStack stack) {
        // ClientSideManager.instance().processor().play(Animations.REBOLT, new Animations.ReboltS1897(this.getFirerate(player)));
    }

    @Override
    public IRenderConfig left() {
        return RenderConfigs.S1897_LEFT;
    }

    @Override
    public IRenderConfig right() {
        return RenderConfigs.S1897_RIGHT;
    }
}
