package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.client.animation.Animation;
import dev.toma.gunsrpg.client.animation.AnimationManager;
import dev.toma.gunsrpg.client.animation.Animations;
import dev.toma.gunsrpg.client.animation.MultiStepAnimation;
import dev.toma.gunsrpg.client.animation.impl.AimingAnimation;
import dev.toma.gunsrpg.common.ModRegistry;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterial;
import dev.toma.gunsrpg.common.item.guns.reload.IReloadManager;
import dev.toma.gunsrpg.common.item.guns.reload.ReloadManagerClipOrSingle;
import dev.toma.gunsrpg.common.item.guns.util.GunType;
import dev.toma.gunsrpg.common.skilltree.Ability;
import dev.toma.gunsrpg.config.GRPGConfig;
import dev.toma.gunsrpg.config.gun.WeaponConfiguration;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.SPacketSetAiming;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Map;

public class SRItem extends GunItem {

    public SRItem(String name) {
        super(name, GunType.SR);
    }

    @Override
    public WeaponConfiguration getWeaponConfig() {
        return GRPGConfig.weapon.sr;
    }

    @Override
    public void fillAmmoMaterialData(Map<AmmoMaterial, Integer> data) {
        data.put(AmmoMaterial.WOOD, 0);
        data.put(AmmoMaterial.STONE, 4);
        data.put(AmmoMaterial.IRON, 9);
        data.put(AmmoMaterial.GOLD, 13);
        data.put(AmmoMaterial.DIAMOND, 17);
        data.put(AmmoMaterial.EMERALD, 20);
        data.put(AmmoMaterial.AMETHYST, 25);
    }

    @Override
    public IReloadManager getReloadManager() {
        return ReloadManagerClipOrSingle.CLIP_OR_SINGLE;
    }

    @Override
    public SoundEvent getShootSound(EntityLivingBase entity) {
        return entity instanceof EntityPlayer && this.isSilenced((EntityPlayer) entity) ? ModRegistry.GRPGSounds.KAR98K_SILENT : ModRegistry.GRPGSounds.KAR98K;
    }

    @Override
    public SoundEvent getReloadSound(EntityPlayer player) {
        return PlayerDataFactory.hasActiveSkill(player, Ability.FAST_HANDS) ? ModRegistry.GRPGSounds.SR_RELOAD_SHORT : ModRegistry.GRPGSounds.SR_RELOAD;
    }

    @Override
    public int getMaxAmmo(EntityPlayer player) {
        return PlayerDataFactory.hasActiveSkill(player, Ability.SR_EXTENDED) ? 10 : 5;
    }

    @Override
    public int getFirerate(EntityPlayer player) {
        return PlayerDataFactory.hasActiveSkill(player, Ability.FAST_HANDS) ? 25 : 40;
    }

    @Override
    public int getReloadTime(EntityPlayer player) {
        // it's safe to assume player is holding the weapon when this is called. Maybe
        boolean empty = this.getAmmo(player.getHeldItemMainhand()) == 0;
        boolean magSkill = PlayerDataFactory.hasActiveSkill(player, Ability.FAST_HANDS);
        return magSkill ? empty ? 40 : 20 : empty ? 66 : 33;
    }

    @Override
    public boolean isSilenced(EntityPlayer player) {
        return PlayerDataFactory.hasActiveSkill(player, Ability.SR_SUPPRESSOR);
    }

    @Override
    public float getVerticalRecoil(EntityPlayer player) {
        float f = super.getVerticalRecoil(player);
        float mod = PlayerDataFactory.hasActiveSkill(player, Ability.SR_CHEEKPAD) ? GRPGConfig.weapon.general.cheekpad : 1.0F;
        return mod * f;
    }

    @Override
    public float getHorizontalRecoil(EntityPlayer player) {
        float f = super.getHorizontalRecoil(player);
        float mod = PlayerDataFactory.hasActiveSkill(player, Ability.SR_CHEEKPAD) ? GRPGConfig.weapon.general.cheekpad : 1.0F;
        return mod * f;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void renderRightArm() {
        GlStateManager.translate(0.25F, -0.02F, 0.45F);
        GlStateManager.rotate(20.0F, 0.0F, 1.0F, 0.0F);
        renderArm(EnumHandSide.RIGHT);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void renderLeftArm() {
        GlStateManager.translate(0.3F, -0.05F, -0.1F);
        GlStateManager.rotate(-20.0F, 0.0F, 1.0F, 0.0F);
        renderArm(EnumHandSide.LEFT);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public AimingAnimation createAimAnimation() {
        return new AimingAnimation(-0.265F, 0.175F, 0.3F).animateRight(animation -> {
            float f = animation.smooth;
            GlStateManager.translate(-0.265F * f, 0.175F * f, 0.3F * f);
        }).animateLeft(animation -> {
            float f = animation.smooth;
            GlStateManager.translate(-0.265F * f, 0.175F * f, 0.3F * f);
        });
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Animation createReloadAnimation(EntityPlayer player) {
        return new MultiStepAnimation.Configurable(this.getReloadTime(player), "sr_reload");
    }

    @Override
    public void onShoot(EntityPlayer player, ItemStack stack) {
        super.onShoot(player, stack);
        AnimationManager.sendNewAnimation(Animations.REBOLT, new MultiStepAnimation.ReboltSR(this.getFirerate(player)));
        NetworkManager.toServer(new SPacketSetAiming(false));
    }
}
