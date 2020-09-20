package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.client.animation.Animation;
import dev.toma.gunsrpg.client.animation.Animations;
import dev.toma.gunsrpg.client.animation.impl.AimingAnimation;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.entity.EntityBullet;
import dev.toma.gunsrpg.common.entity.EntityCrossbowBolt;
import dev.toma.gunsrpg.common.init.GRPGSounds;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterial;
import dev.toma.gunsrpg.common.item.guns.util.GunType;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.config.GRPGConfig;
import dev.toma.gunsrpg.config.gun.WeaponConfiguration;
import dev.toma.gunsrpg.util.SkillUtil;
import net.minecraft.client.Minecraft;
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

public class CrossbowItem extends GunItem {

    public CrossbowItem(String name) {
        super(name, GunType.CROSSBOW);
    }

    @Override
    public boolean isSilenced(EntityPlayer player) {
        return true;
    }

    @Override
    public int getMaxAmmo(EntityPlayer player) {
        return PlayerDataFactory.hasActiveSkill(player, Skills.CROSSBOW_REPEATER) ? 3 : 1;
    }

    @Override
    public int getFirerate(EntityPlayer player) {
        return GRPGConfig.weaponConfig.crossbow.normal;
    }

    @Override
    public int getReloadTime(EntityPlayer player) {
        int base = 60;
        if(PlayerDataFactory.hasActiveSkill(player, Skills.CROSSBOW_QUIVER)) {
            base = (int) (base * 0.65);
        }
        if(PlayerDataFactory.hasActiveSkill(player, Skills.CROSSBOW_REPEATER)) {
            base = (int) (base * 1.25);
        }
        return (int) (base * SkillUtil.getReloadTimeMultiplier(player));
    }

    @Override
    public void shootBullet(World world, EntityLivingBase entity, ItemStack stack) {
        EntityCrossbowBolt bolt = new EntityCrossbowBolt(world, entity, this, stack);
        boolean aim = entity instanceof EntityPlayer && PlayerDataFactory.get((EntityPlayer) entity).getAimInfo().isAiming();
        float pitch = entity.rotationPitch + (aim ? 0.0F : (random.nextFloat() - random.nextFloat()) * 5);
        float yaw = entity.rotationYaw + (aim ? 0.0F : (random.nextFloat() - random.nextFloat()) * 5);
        float baseVelocity = getWeaponConfig().velocity;
        float velocity = entity instanceof EntityPlayer && PlayerDataFactory.hasActiveSkill((EntityPlayer) entity, Skills.CROSSBOW_TOUGH_BOWSTRING) ? 1.5F * baseVelocity : baseVelocity;
        bolt.fire(pitch, yaw, velocity);
        world.spawnEntity(bolt);
    }

    @Override
    public void onHitEntity(EntityBullet bullet, EntityLivingBase victim, ItemStack stack, EntityLivingBase shooter) {
        if(!bullet.world.isRemote && shooter instanceof EntityPlayer && PlayerDataFactory.hasActiveSkill((EntityPlayer) shooter, Skills.CROSSBOW_POISONED_BOLTS)) {
            victim.addPotionEffect(new PotionEffect(MobEffects.WITHER, 140, 1, false, false));
        }
    }

    @Override
    public void onKillEntity(EntityBullet bullet, EntityLivingBase victim, ItemStack stack, EntityLivingBase shooter) {
        if(!bullet.world.isRemote && shooter instanceof EntityPlayer && PlayerDataFactory.hasActiveSkill((EntityPlayer) shooter, Skills.CROSSBOW_HUNTER)) {
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
    public WeaponConfiguration getWeaponConfig() {
        return GRPGConfig.weaponConfig.crossbow;
    }

    @Override
    public SoundEvent getReloadSound(EntityPlayer player) {
        return PlayerDataFactory.hasActiveSkill(player, Skills.CROSSBOW_QUIVER) ? GRPGSounds.CROSSBOW_RELOAD_FAST : GRPGSounds.CROSSBOW_RELOAD;
    }

    @Override
    public SoundEvent getShootSound(EntityLivingBase entity) {
        return GRPGSounds.CROSSBOW_SHOOT;
    }

    @Override
    public SkillType<?> getRequiredSkill() {
        return Skills.CROSSBOW_ASSEMBLY;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void renderRightArm() {
        GlStateManager.translate(0.05F, -0.1F, 0.7F);
        GlStateManager.rotate(10.0F, 0.0F, 1.0F, 0.0F);
        renderArm(EnumHandSide.RIGHT);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void renderLeftArm() {
        GlStateManager.translate(0.3F, -0.1F, 0.0F);
        GlStateManager.rotate(-30.0F, 0.0F, 1.0F, 0.0F);
        renderArm(EnumHandSide.LEFT);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public AimingAnimation createAimAnimation() {
        boolean scoped = PlayerDataFactory.hasActiveSkill(Minecraft.getMinecraft().player, Skills.CROSSBOW_SCOPE);
        return new AimingAnimation(-0.265F, scoped ? 0.14F : 0.18F, -0.1F).animateRight(f -> {
            float f1 = f.smooth;
            GlStateManager.translate(-0.265F * f1, 0.16F * f1, -0.1F * f1);
        }).animateLeft(f -> {
            float f1 = f.smooth;
            GlStateManager.translate(-0.1F * f1, 0.15F * f1, 0.15F * f1);
            GlStateManager.rotate(20.0F * f1, 0.0F, 1.0F, 0.0F);
        });
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Animation createReloadAnimation(EntityPlayer player) {
        return new Animations.ReloadCrossbow(this.getReloadTime(player));
    }
}
