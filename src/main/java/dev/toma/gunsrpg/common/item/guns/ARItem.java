package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.client.animation.Animation;
import dev.toma.gunsrpg.client.animation.MultiStepAnimation;
import dev.toma.gunsrpg.client.animation.impl.AimingAnimation;
import dev.toma.gunsrpg.common.ModRegistry;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterial;
import dev.toma.gunsrpg.common.item.guns.util.Firemode;
import dev.toma.gunsrpg.common.item.guns.util.GunType;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.config.GRPGConfig;
import dev.toma.gunsrpg.config.gun.WeaponConfiguration;
import dev.toma.gunsrpg.util.SkillUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Map;

public class ARItem extends GunItem {

    public ARItem(String name) {
        super(name, GunType.AR);
    }

    @Override
    public WeaponConfiguration getWeaponConfig() {
        return GRPGConfig.weaponConfig.ar;
    }

    @Override
    public void fillAmmoMaterialData(Map<AmmoMaterial, Integer> data) {
        data.put(AmmoMaterial.WOOD, 0);
        data.put(AmmoMaterial.STONE, 2);
        data.put(AmmoMaterial.IRON, 4);
        data.put(AmmoMaterial.GOLD, 6);
        data.put(AmmoMaterial.DIAMOND, 9);
        data.put(AmmoMaterial.EMERALD, 11);
        data.put(AmmoMaterial.AMETHYST, 14);
    }

    @Override
    public SoundEvent getShootSound(EntityLivingBase entity) {
        return entity instanceof EntityPlayer && this.isSilenced((EntityPlayer) entity) ? ModRegistry.GRPGSounds.SKS_SILENT : ModRegistry.GRPGSounds.SKS;
    }

    @Override
    public SoundEvent getReloadSound(EntityPlayer player) {
        return ModRegistry.GRPGSounds.AR_RELOAD;
    }

    @Override
    public boolean isSilenced(EntityPlayer player) {
        return PlayerDataFactory.hasActiveSkill(player, ModRegistry.Skills.AR_SUPPRESSOR);
    }

    @Override
    public int getMaxAmmo(EntityPlayer player) {
        return PlayerDataFactory.hasActiveSkill(player, ModRegistry.Skills.AR_EXTENDED) ? 20 : 10;
    }

    @Override
    public int getFirerate(EntityPlayer player) {
        int firerate = PlayerDataFactory.hasActiveSkill(player, ModRegistry.Skills.AR_TOUGH_SPRING) ? GRPGConfig.weaponConfig.ar.upgraded : GRPGConfig.weaponConfig.ar.normal;
        if(PlayerDataFactory.hasActiveSkill(player, ModRegistry.Skills.AR_ADAPTIVE_CHAMBERING)) {
            firerate -= 2;
        }
        return Math.max(firerate, 1);
    }

    @Override
    public int getReloadTime(EntityPlayer player) {
        return (int)(32 * SkillUtil.getReloadTimeMultiplier(player));
    }

    @Override
    public float getVerticalRecoil(EntityPlayer player) {
        float f = super.getVerticalRecoil(player);
        float mod = PlayerDataFactory.hasActiveSkill(player, ModRegistry.Skills.AR_VERTICAL_GRIP) ? GRPGConfig.weaponConfig.general.verticalGrip : 1.0F;
        float mod2 = PlayerDataFactory.hasActiveSkill(player, ModRegistry.Skills.AR_CHEEKPAD) ? GRPGConfig.weaponConfig.general.cheekpad : 1.0F;
        return mod * mod2 * f;
    }

    @Override
    public float getHorizontalRecoil(EntityPlayer player) {
        float f = super.getHorizontalRecoil(player);
        float mod = PlayerDataFactory.hasActiveSkill(player, ModRegistry.Skills.AR_CHEEKPAD) ? GRPGConfig.weaponConfig.general.cheekpad : 1.0F;
        return mod * f;
    }

    @Override
    public boolean switchFiremode(ItemStack stack, EntityPlayer player) {
        Firemode firemode = this.getFiremode(stack);
        int newMode = 0;
        if(firemode == Firemode.SINGLE && PlayerDataFactory.hasActiveSkill(player, ModRegistry.Skills.AR_ADAPTIVE_CHAMBERING)) {
            newMode = 2;
        }
        stack.getTagCompound().setInteger("firemode", newMode);
        return firemode.ordinal() != newMode;
    }

    @Override
    public SkillType<?> getRequiredSkill() {
        return ModRegistry.Skills.ASSAULT_RIFLE_ASSEMBLY;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void renderRightArm() {
        GlStateManager.translate(-0.1F, -0.05F, 0.6F);
        GlStateManager.rotate(5.0F, 1.0F, 0.0F, 0.0F);
        renderArm(EnumHandSide.RIGHT);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void renderLeftArm() {
        GlStateManager.translate(0.32F, -0.1F, -0.1F);
        GlStateManager.rotate(5.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(-20.0F, 0.0F, 1.0F, 0.0F);
        renderArm(EnumHandSide.LEFT);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public AimingAnimation createAimAnimation() {
        return new AimingAnimation(-0.265F, 0.07F, 0.2F).animateRight(animation -> {
            float f = animation.smooth;
            GlStateManager.translate(-0.2F * f, 0.0F, 0.0F);
            GlStateManager.rotate(20.0F * f, 0.0F, 1.0F, 0.0F);
        }).animateLeft(animation -> {
            float f = animation.smooth;
            GlStateManager.translate(-0.33F * f, 0.1F * f, 0.2F * f);
        });
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Animation createReloadAnimation(EntityPlayer player) {
        return new MultiStepAnimation.Configurable(this.getReloadTime(player), "ar_reload");
    }
}
