package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.client.animation.Animation;
import dev.toma.gunsrpg.client.animation.AnimationManager;
import dev.toma.gunsrpg.client.animation.Animations;
import dev.toma.gunsrpg.client.animation.MultiStepAnimation;
import dev.toma.gunsrpg.client.animation.impl.AimingAnimation;
import dev.toma.gunsrpg.client.animation.impl.ImprovedAimAnimation;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.entity.EntityBullet;
import dev.toma.gunsrpg.common.init.GRPGSounds;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterial;
import dev.toma.gunsrpg.common.item.guns.util.Firemode;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Map;

public class PistolItem extends GunItem {

    public PistolItem(String name) {
        super(name, GunType.PISTOL);
    }

    @Override
    public WeaponConfiguration getWeaponConfig() {
        return GRPGConfig.weaponConfig.pistol;
    }

    @Override
    public void fillAmmoMaterialData(Map<AmmoMaterial, Integer> data) {
        data.put(AmmoMaterial.WOOD, 0);
        data.put(AmmoMaterial.STONE, 1);
        data.put(AmmoMaterial.IRON, 3);
        data.put(AmmoMaterial.GOLD, 4);
        data.put(AmmoMaterial.DIAMOND, 6);
        data.put(AmmoMaterial.EMERALD, 8);
        data.put(AmmoMaterial.AMETHYST, 11);
    }

    @Override
    public boolean isSilenced(EntityPlayer player) {
        return PlayerDataFactory.hasActiveSkill(player, Skills.PISTOL_SUPPRESSOR);
    }

    @Override
    public void onHitEntity(EntityBullet bullet, EntityLivingBase victim, ItemStack stack, EntityLivingBase shooter) {
        if(shooter instanceof EntityPlayer && PlayerDataFactory.hasActiveSkill((EntityPlayer) shooter, Skills.PISTOL_HEAVY_BULLETS) && random.nextDouble() <= 0.35) {
            victim.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 100, 1, false, false));
            victim.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 100, 0, false, false));
        }
    }

    @Override
    public SoundEvent getShootSound(EntityLivingBase entity) {
        return entity instanceof EntityPlayer && this.isSilenced((EntityPlayer) entity) ? GRPGSounds.P1911_SILENT : GRPGSounds.P1911;
    }

    @Override
    public SoundEvent getReloadSound(EntityPlayer player) {
        return PlayerDataFactory.hasActiveSkill(player, Skills.PISTOL_QUICKDRAW) ? GRPGSounds.P1911_RELOAD_SHORT : GRPGSounds.P1911_RELOAD;
    }

    @Override
    public int getMaxAmmo(EntityPlayer player) {
        boolean extended = PlayerDataFactory.hasActiveSkill(player, Skills.PISTOL_EXTENDED);
        return PlayerDataFactory.hasActiveSkill(player, Skills.PISTOL_DUAL_WIELD) ? extended ? 26 : 14 : extended ? 13 : 7;
    }

    @Override
    public int getFirerate(EntityPlayer player) {
        return PlayerDataFactory.hasActiveSkill(player, Skills.PISTOL_TOUGH_SPRING) ? GRPGConfig.weaponConfig.pistol.upgraded : GRPGConfig.weaponConfig.pistol.normal;
    }

    @Override
    public int getReloadTime(EntityPlayer player) {
        boolean quickdraw = PlayerDataFactory.hasActiveSkill(player, Skills.PISTOL_QUICKDRAW);
        int time = PlayerDataFactory.hasActiveSkill(player, Skills.PISTOL_DUAL_WIELD) ? quickdraw ? 50 : 70 : quickdraw ? 25 : 35;
        return (int)(time * SkillUtil.getReloadTimeMultiplier(player));
    }

    @Override
    public float getVerticalRecoil(EntityPlayer player) {
        float f = super.getVerticalRecoil(player);
        float mod = PlayerDataFactory.hasActiveSkill(player, Skills.PISTOL_CARBON_BARREL) ? GRPGConfig.weaponConfig.general.carbonBarrel : 1.0F;
        return mod * f;
    }

    @Override
    public float getHorizontalRecoil(EntityPlayer player) {
        float f = super.getHorizontalRecoil(player);
        float mod = PlayerDataFactory.hasActiveSkill(player, Skills.PISTOL_CARBON_BARREL) ? GRPGConfig.weaponConfig.general.carbonBarrel : 1.0F;
        return mod * f;
    }

    @Override
    public boolean switchFiremode(ItemStack stack, EntityPlayer player) {
        Firemode firemode = this.getFiremode(stack);
        stack.getTagCompound().setInteger("firemode", firemode == Firemode.SINGLE ? 1 : 0);
        return true;
    }

    @Override
    public SkillType<?> getRequiredSkill() {
        return Skills.PISTOL_ASSEMBLY;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public AimingAnimation createAimAnimation() {
        return this.isDualWieldActive() ? new ImprovedAimAnimation(-0.4F, 0.06F, 0.0F).animateItem(animation -> {
            float f = animation.smooth;
            if(AnimationManager.renderingDualWield) {
                GlStateManager.translate(0.0F, -0.33F * f, 0.0F);
                GlStateManager.rotate(-30.0F * f, 0.0F, 0.0F, 1.0F);
            } else {
                GlStateManager.translate(0.0F, -0.33F * f, 0.0F);
                GlStateManager.rotate(30.0F * f, 0.0F, 0.0F, 1.0F);
            }
        }).animateRight(animation -> {
            float f = animation.smooth;
            GlStateManager.translate(-0.13F * f, -0.12F * f, 0.1F * f);
            GlStateManager.rotate(30.0F * f, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotate(16.0F * f, 0.0F, 1.0F, 0.0F);
        }).animateLeft(animation -> {
            float f = animation.smooth;
            GlStateManager.translate(0.13F * f, -0.12F * f, 0.1F * f);
            GlStateManager.rotate(-30.0F * f, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotate(-16.0F * f, 0.0F, 1.0F, 0.0F);
        }) : new AimingAnimation(-0.54F, 0.06F, 0.0F).animateRight(animation -> {
            float f = animation.smooth;
            GlStateManager.translate(-0.18F * f, 0.0F, 0.1F * f);
            GlStateManager.rotate(3.0F * f, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(20.0F * f, 0.0F, 1.0F, 0.0F);
        }).animateLeft(animation -> {
            float f = animation.smooth;
            GlStateManager.translate(-0.2F * f, 0.0F, 0.0F);
            GlStateManager.rotate(3.0F * f, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(15.0F * f, 0.0F, 1.0F, 0.0F);
        });
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Animation createReloadAnimation(EntityPlayer player) {
        return this.isDualWieldActive() ? new Animations.ReloadDual(this.getReloadTime(player)) : new MultiStepAnimation.Configurable(this.getReloadTime(player), "pistol_reload");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void renderRightArm() {
        GlStateManager.translate(-0.05F, -0.02F, 0.0F);
        GlStateManager.rotate(5.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(-10F, 0F, 1.0F, 0.0F);
        renderArm(EnumHandSide.RIGHT);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void renderLeftArm() {
        if(this.isDualWieldActive()) {
            GlStateManager.translate(-0.05F, -0.05F, -0.15F);
            GlStateManager.rotate(5.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(10F, 0F, 1.0F, 0.0F);
            renderArm(EnumHandSide.LEFT);
            return;
        }
        GlStateManager.translate(0.35F, -0.08F, 0.05F);
        GlStateManager.rotate(5.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(-30.0F, 0.0F, 1.0F, 0.0F);
        renderArm(EnumHandSide.LEFT);
    }

    @SideOnly(Side.CLIENT)
    private boolean isDualWieldActive() {
        return PlayerDataFactory.hasActiveSkill(Minecraft.getMinecraft().player, Skills.PISTOL_DUAL_WIELD);
    }
}
