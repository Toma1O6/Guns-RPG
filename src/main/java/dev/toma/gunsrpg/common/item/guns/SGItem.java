package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.client.animation.Animation;
import dev.toma.gunsrpg.client.animation.AnimationManager;
import dev.toma.gunsrpg.client.animation.Animations;
import dev.toma.gunsrpg.client.animation.MultiStepAnimation;
import dev.toma.gunsrpg.client.animation.impl.AimingAnimation;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.entity.EntityBullet;
import dev.toma.gunsrpg.common.entity.EntityShotgunPellet;
import dev.toma.gunsrpg.common.init.GRPGSounds;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterial;
import dev.toma.gunsrpg.common.item.guns.reload.IReloadManager;
import dev.toma.gunsrpg.common.item.guns.reload.ReloadManagerSingle;
import dev.toma.gunsrpg.common.item.guns.util.GunType;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.config.GRPGConfig;
import dev.toma.gunsrpg.config.gun.WeaponConfiguration;
import dev.toma.gunsrpg.util.SkillUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Map;

public class SGItem extends GunItem {

    public SGItem(String name) {
        super(name, GunType.SG);
    }

    @Override
    public WeaponConfiguration getWeaponConfig() {
        return GRPGConfig.weaponConfig.shotgun;
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
    public SoundEvent getShootSound(EntityLivingBase entity) {
        return GRPGSounds.S1897;
    }

    @Override
    public SoundEvent getReloadSound(EntityPlayer player) {
        return PlayerDataFactory.hasActiveSkill(player, Skills.SHOTGUN_BULLET_LOOPS) ? GRPGSounds.SG_RELOAD_SHORT : GRPGSounds.SG_RELOAD;
    }

    @Override
    public int getReloadTime(EntityPlayer player) {
        int time = PlayerDataFactory.hasActiveSkill(player, Skills.SHOTGUN_BULLET_LOOPS) ? 12 : 17;
        return (int) (time * SkillUtil.getReloadTimeMultiplier(player));
    }

    @Override
    public int getMaxAmmo(EntityPlayer player) {
        return PlayerDataFactory.hasActiveSkill(player, Skills.SHOTGUN_EXTENDED) ? 8 : 5;
    }

    @Override
    public int getFirerate(EntityPlayer player) {
        return PlayerDataFactory.hasActiveSkill(player, Skills.SHOTGUN_PUMP_IN_ACTION) ? GRPGConfig.weaponConfig.shotgun.upgraded : GRPGConfig.weaponConfig.shotgun.normal;
    }

    @Override
    public void onKillEntity(EntityBullet bullet, EntityLivingBase victim, ItemStack stack, EntityLivingBase shooter) {
        if(!shooter.world.isRemote && shooter instanceof EntityPlayer && PlayerDataFactory.hasActiveSkill((EntityPlayer) shooter, Skills.SHOTGUN_NEVER_GIVE_UP)) {
            shooter.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 100, 0, false, false));
        }
    }

    @Override
    public void shootBullet(World world, EntityLivingBase entity, ItemStack stack) {
        boolean choke = entity instanceof EntityPlayer && PlayerDataFactory.hasActiveSkill((EntityPlayer) entity, Skills.SHOTGUN_CHOKE);
        float modifier = 3.0F;
        float velocity = this.getWeaponConfig().velocity;
        for(int i = 0; i < 6; i++) {
            EntityShotgunPellet bullet = new EntityShotgunPellet(world, entity, this, stack);
            float pitch = choke ? entity.rotationPitch + (random.nextFloat() * modifier - random.nextFloat() * modifier) : entity.rotationPitch + (random.nextFloat() * modifier * 2 - random.nextFloat() * modifier * 2);
            float yaw = choke ? entity.rotationYaw + (random.nextFloat() * modifier - random.nextFloat() * modifier) : entity.rotationYaw + (random.nextFloat() * modifier * 2 - random.nextFloat() * modifier * 2);
            bullet.fire(pitch, yaw, velocity);
            world.spawnEntity(bullet);
        }
    }

    @Override
    public SkillType<?> getRequiredSkill() {
        return Skills.SHOTGUN_ASSEMBLY;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void renderRightArm() {
        GlStateManager.translate(0.16F, -0.1F, 0.35F);
        GlStateManager.rotate(15.0F, 0.0F, 1.0F, 0.0F);
        renderArm(EnumHandSide.RIGHT);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void renderLeftArm() {
        GlStateManager.translate(0.3F, -0.1F, -0.15F);
        GlStateManager.rotate(-20.0F, 0.0F, 1.0F, 0.0F);
        renderArm(EnumHandSide.LEFT);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public AimingAnimation createAimAnimation() {
        return new AimingAnimation(-0.267F, 0.22F, -0.1F).animateRight(animation -> {
            float f = animation.smooth;
            GlStateManager.translate(-0.267F * f, 0.22F * f, 0.1F * f);
        }).animateLeft(animation -> {
            float f = animation.smooth;
            GlStateManager.translate(-0.267F * f, 0.22F * f, 0.2F * f);
        });
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Animation createReloadAnimation(EntityPlayer player) {
        return new MultiStepAnimation.Configurable(this.getReloadTime(player), "sg_reload");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void onShoot(EntityPlayer player, ItemStack stack) {
        super.onShoot(player, stack);
        AnimationManager.sendNewAnimation(Animations.REBOLT, new Animations.ReboltSG(this.getFirerate(player)));
    }
}
