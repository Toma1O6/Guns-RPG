package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.render.RenderConfigs;
import dev.toma.gunsrpg.client.render.item.M1911Renderer;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.entity.BulletEntity;
import dev.toma.gunsrpg.common.init.ModSounds;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterial;
import dev.toma.gunsrpg.common.item.guns.util.Firemode;
import dev.toma.gunsrpg.common.item.guns.util.GunType;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.config.ModConfig;
import dev.toma.gunsrpg.config.gun.IWeaponConfig;
import dev.toma.gunsrpg.util.SkillUtil;
import lib.toma.animations.api.IRenderConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Map;

public class M1911Item extends GunItem {

    private static final ResourceLocation EJECT = GunsRPG.makeResource("m1911/eject");
    private static final ResourceLocation[] AIM_ANIMATIONS = {
            GunsRPG.makeResource("m1911/aim"),
            GunsRPG.makeResource("m1911/aim_dual")
    };
    private static final ResourceLocation[] RELOAD_ANIMATIONS = {
            GunsRPG.makeResource("m1911/reload"),
            GunsRPG.makeResource("m1911/reload_dual")
    };

    public M1911Item(String name) {
        super(name, GunType.PISTOL, new Properties().setISTER(() -> M1911Renderer::new));
    }

    @Override
    public IWeaponConfig getWeaponConfig() {
        return ModConfig.weaponConfig.m1911;
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
    public boolean isSilenced(PlayerEntity player) {
        return PlayerData.hasActiveSkill(player, Skills.M1911_SUPPRESSOR);
    }

    @Override
    public void onHitEntity(BulletEntity bullet, LivingEntity victim, ItemStack stack, LivingEntity shooter) {
        if (shooter instanceof PlayerEntity && PlayerData.hasActiveSkill((PlayerEntity) shooter, Skills.M1911_HEAVY_BULLETS) && random.nextDouble() <= 0.35) {
            victim.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 100, 1, false, false));
            victim.addEffect(new EffectInstance(Effects.WEAKNESS, 100, 0, false, false));
        }
    }

    @Override
    protected SoundEvent getShootSound(PlayerEntity entity) {
        return this.isSilenced(entity) ? ModSounds.M1911_SILENT : ModSounds.M1911;
    }

    @Override
    protected SoundEvent getEntityShootSound(LivingEntity entity) {
        return ModSounds.M9;
    }

    @Override
    public SoundEvent getReloadSound(PlayerEntity player) {
        return PlayerData.hasActiveSkill(player, Skills.M1911_QUICKDRAW) ? ModSounds.M1911_RELOAD_SHORT : ModSounds.M1911_RELOAD;
    }

    @Override
    public int getMaxAmmo(PlayerEntity player) {
        boolean extended = PlayerData.hasActiveSkill(player, Skills.M1911_EXTENDED);
        return PlayerData.hasActiveSkill(player, Skills.M1911_DUAL_WIELD) ? extended ? 26 : 14 : extended ? 13 : 7;
    }

    @Override
    public int getFirerate(PlayerEntity player) {
        IWeaponConfig config = getWeaponConfig();
        return PlayerData.hasActiveSkill(player, Skills.M1911_TOUGH_SPRING) ? config.getUpgradedFirerate() : config.getFirerate();
    }

    @Override
    public int getReloadTime(PlayerEntity player) {
        boolean quickdraw = PlayerData.hasActiveSkill(player, Skills.M1911_QUICKDRAW);
        int time = PlayerData.hasActiveSkill(player, Skills.M1911_DUAL_WIELD) ? quickdraw ? 50 : 70 : quickdraw ? 25 : 35;
        return (int) (time * SkillUtil.getReloadTimeMultiplier(player));
    }

    @Override
    public float getVerticalRecoil(PlayerEntity player) {
        float f = super.getVerticalRecoil(player);
        float mod = PlayerData.hasActiveSkill(player, Skills.M1911_CARBON_BARREL) ? ModConfig.weaponConfig.general.carbonBarrel.floatValue() : 1.0F;
        return mod * f;
    }

    @Override
    public float getHorizontalRecoil(PlayerEntity player) {
        float f = super.getHorizontalRecoil(player);
        float mod = PlayerData.hasActiveSkill(player, Skills.M1911_CARBON_BARREL) ? ModConfig.weaponConfig.general.carbonBarrel.floatValue() : 1.0F;
        return mod * f;
    }

    @Override
    public boolean switchFiremode(ItemStack stack, PlayerEntity player) {
        Firemode firemode = this.getFiremode(stack);
        stack.getTag().putInt("firemode", firemode == Firemode.SINGLE ? 1 : 0);
        return true;
    }

    @Override
    public SkillType<?> getRequiredSkill() {
        return Skills.M1911_ASSEMBLY;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ResourceLocation getAimAnimationPath(ItemStack stack, PlayerEntity player) {
        return isDualWieldActive() ? AIM_ANIMATIONS[1] : AIM_ANIMATIONS[0];
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ResourceLocation getBulletEjectAnimationPath() {
        return EJECT;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ResourceLocation getReloadAnimation(PlayerEntity player) {
        return this.isDualWieldActive() ? RELOAD_ANIMATIONS[1] : RELOAD_ANIMATIONS[0];
    }

    @OnlyIn(Dist.CLIENT)
    private boolean isDualWieldActive() {
        return PlayerData.hasActiveSkill(Minecraft.getInstance().player, Skills.M1911_DUAL_WIELD);
    }

    @Override
    public IRenderConfig left() {
        return isDualWieldActive() ? RenderConfigs.M1911_LEFT_DUAL : RenderConfigs.M1911_LEFT;
    }

    @Override
    public IRenderConfig right() {
        return isDualWieldActive() ? RenderConfigs.M1911_RIGHT_DUAL : RenderConfigs.M1911_RIGHT;
    }
}
