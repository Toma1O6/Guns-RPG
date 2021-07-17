package dev.toma.gunsrpg.common.item.guns;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.client.animation.Animations;
import dev.toma.gunsrpg.client.animation.IAnimation;
import dev.toma.gunsrpg.client.animation.impl.AimingAnimation;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.init.GRPGSounds;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterial;
import dev.toma.gunsrpg.common.item.guns.reload.IReloadManager;
import dev.toma.gunsrpg.common.item.guns.reload.ReloadManagerClipOrSingle;
import dev.toma.gunsrpg.common.item.guns.util.GunType;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.config.GRPGConfig;
import dev.toma.gunsrpg.config.gun.IWeaponConfiguration;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.SPacketSetAiming;
import dev.toma.gunsrpg.sided.ClientSideManager;
import dev.toma.gunsrpg.util.SkillUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;

import java.util.Map;

public class SRItem extends GunItem {

    public SRItem(String name) {
        super(name, GunType.SR);
    }

    @Override
    public IWeaponConfiguration getWeaponConfig() {
        return GRPGConfig.weaponConfig.kar98k;
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
    public SoundEvent getShootSound(PlayerEntity entity) {
        return this.isSilenced(entity) ? GRPGSounds.KAR98K_SILENT : GRPGSounds.KAR98K;
    }

    @Override
    protected SoundEvent getEntityShootSound(LivingEntity entity) {
        return GRPGSounds.M24;
    }

    @Override
    public SoundEvent getReloadSound(PlayerEntity player) {
        ItemStack stack = player.getMainHandItem();
        if (stack.getItem() == this) {
            int ammo = getAmmo(stack);
            if (ammo == 0) {
                return PlayerDataFactory.hasActiveSkill(player, Skills.SR_FAST_HANDS) ? GRPGSounds.KAR98K_RELOAD_CLIP_FAST : GRPGSounds.KAR98K_RELOAD_CLIP;
            }
        }
        return PlayerDataFactory.hasActiveSkill(player, Skills.SR_FAST_HANDS) ? GRPGSounds.SR_RELOAD_SHORT : GRPGSounds.SR_RELOAD;
    }

    @Override
    public int getMaxAmmo(PlayerEntity player) {
        return PlayerDataFactory.hasActiveSkill(player, Skills.SR_EXTENDED) ? 10 : 5;
    }

    @Override
    public int getFirerate(PlayerEntity player) {
        IWeaponConfiguration cfg = getWeaponConfig();
        return PlayerDataFactory.hasActiveSkill(player, Skills.SR_FAST_HANDS) ? cfg.getUpgradedFirerate() : cfg.getFirerate();
    }

    @Override
    public int getReloadTime(PlayerEntity player) {
        boolean empty = this.getAmmo(player.getMainHandItem()) == 0;
        boolean magSkill = PlayerDataFactory.hasActiveSkill(player, Skills.SR_FAST_HANDS);
        int time = magSkill ? empty ? 40 : 20 : empty ? 66 : 33;
        return (int) (time * SkillUtil.getReloadTimeMultiplier(player));
    }

    @Override
    public boolean isSilenced(PlayerEntity player) {
        return PlayerDataFactory.hasActiveSkill(player, Skills.SR_SUPPRESSOR);
    }

    @Override
    public float getVerticalRecoil(PlayerEntity player) {
        float f = super.getVerticalRecoil(player);
        float mod = PlayerDataFactory.hasActiveSkill(player, Skills.SR_CHEEKPAD) ? GRPGConfig.weaponConfig.general.cheekpad.floatValue() : 1.0F;
        return mod * f;
    }

    @Override
    public float getHorizontalRecoil(PlayerEntity player) {
        float f = super.getHorizontalRecoil(player);
        float mod = PlayerDataFactory.hasActiveSkill(player, Skills.SR_CHEEKPAD) ? GRPGConfig.weaponConfig.general.cheekpad.floatValue() : 1.0F;
        return mod * f;
    }

    @Override
    public SkillType<?> getRequiredSkill() {
        return Skills.SNIPER_RIFLE_ASSEMBLY;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void transformRightArm(MatrixStack matrix) {
        matrix.translate(0.25F, -0.02F, 0.45F);
        matrix.mulPose(Vector3f.YP.rotationDegrees(20));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void transformLeftArm(MatrixStack matrix) {
        matrix.translate(0.3F, -0.05F, -0.1F);
        matrix.mulPose(Vector3f.YP.rotationDegrees(-20));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public AimingAnimation createAimAnimation() {
        boolean scope = PlayerDataFactory.hasActiveSkill(Minecraft.getInstance().player, Skills.SR_SCOPE);
        return new AimingAnimation(-0.265F, scope ? 0.14F : 0.175F, 0.3F).animateRight((stack, f) -> {
            stack.translate(-0.265F * f, 0.175F * f, 0.3F * f);
        }).animateLeft((stack, f) -> {
            stack.translate(-0.265F * f, 0.175F * f, 0.3F * f);
        });
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public IAnimation createReloadAnimation(PlayerEntity player) {
        return new Animations.SniperReload(this.getReloadTime(player));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void onShoot(PlayerEntity player, ItemStack stack) {
        super.onShoot(player, stack);
        ClientSideManager.instance().processor().play(Animations.REBOLT, new Animations.ReboltSR(this.getFirerate(player)));
        NetworkManager.sendServerPacket(new SPacketSetAiming(false));
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> () -> ClientSideManager.instance().playDelayedSound(player.blockPosition(), 1.0F, 1.0F, GRPGSounds.SR_BOLT, SoundCategory.MASTER, 15));
    }
}
