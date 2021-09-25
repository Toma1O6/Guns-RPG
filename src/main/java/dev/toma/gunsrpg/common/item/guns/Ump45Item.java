package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.IWeaponConfig;
import dev.toma.gunsrpg.client.render.RenderConfigs;
import dev.toma.gunsrpg.client.render.item.Ump45Renderer;
import dev.toma.gunsrpg.common.attribute.Attribs;
import dev.toma.gunsrpg.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.entity.projectile.Projectile;
import dev.toma.gunsrpg.common.init.ModSounds;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterials;
import dev.toma.gunsrpg.common.item.guns.util.Firemode;
import dev.toma.gunsrpg.common.item.guns.util.MaterialContainer;
import dev.toma.gunsrpg.common.item.guns.util.WeaponCategory;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.config.ModConfig;
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

public class Ump45Item extends GunItem {

    private static final ResourceLocation EJECT = GunsRPG.makeResource("ump45/eject");
    private static final ResourceLocation[] AIM_ANIMATIONS = {
            GunsRPG.makeResource("ump45/aim"),
            GunsRPG.makeResource("ump45/aim_red_dot")
    };
    private static final ResourceLocation RELOAD_ANIMATION = GunsRPG.makeResource("ump45/reload");

    public Ump45Item(String name) {
        super(name, WeaponCategory.SMG, new Properties().setISTER(() -> Ump45Renderer::new));
    }

    @Override
    public IWeaponConfig getWeaponConfig() {
        return ModConfig.weaponConfig.ump;
    }

    @Override
    public void fillAmmoMaterialData(MaterialContainer container) {
        container
                .add(AmmoMaterials.WOOD, 0)
                .add(AmmoMaterials.STONE, 1)
                .add(AmmoMaterials.IRON, 3)
                .add(AmmoMaterials.GOLD, 4)
                .add(AmmoMaterials.DIAMOND, 6)
                .add(AmmoMaterials.EMERALD, 7)
                .add(AmmoMaterials.AMETHYST, 9);
    }

    @Override
    public SoundEvent getShootSound(PlayerEntity entity) {
        return this.isSilenced(entity) ? ModSounds.UMP45_SILENT : ModSounds.UMP45;
    }

    @Override
    protected SoundEvent getEntityShootSound(LivingEntity entity) {
        return ModSounds.MP5;
    }

    @Override
    public int getMaxAmmo(IAttributeProvider provider) {
        return provider.getAttribute(Attribs.UMP45_MAG_CAPACITY).intValue();
    }

    @Override
    public int getMaxAmmo(PlayerEntity player) {
        return PlayerData.hasActiveSkill(player, Skills.UMP45_EXTENDED) ? 40 : 25;
    }

    @Override
    public int getFirerate(PlayerEntity player) {
        IWeaponConfig cfg = getWeaponConfig();
        return PlayerData.hasActiveSkill(player, Skills.UMP45_TOUGH_SPRING) ? cfg.getUpgradedFirerate() : cfg.getFirerate();
    }

    @Override
    public int getReloadTime(PlayerEntity player) {
        int time = PlayerData.hasActiveSkill(player, Skills.UMP45_QUICKDRAW) ? 40 : 52;
        return (int) (time * SkillUtil.getReloadTimeMultiplier(player));
    }

    @Override
    public void onKillEntity(Projectile bullet, LivingEntity victim, ItemStack stack, LivingEntity shooter) {
        if (!shooter.level.isClientSide && shooter instanceof PlayerEntity && PlayerData.hasActiveSkill((PlayerEntity) shooter, Skills.UMP45_COMMANDO)) {
            shooter.addEffect(new EffectInstance(Effects.MOVEMENT_SPEED, 100, 1, false, false));
            shooter.addEffect(new EffectInstance(Effects.REGENERATION, 60, 2, false, false));
        }
    }

    @Override
    public float getVerticalRecoil(PlayerEntity player) {
        float f = super.getVerticalRecoil(player);
        float mod = PlayerData.hasActiveSkill(player, Skills.UMP45_VERTICAL_GRIP) ? ModConfig.weaponConfig.general.verticalGrip.floatValue() : 1.0F;
        return mod * f;
    }

    @Override
    public boolean switchFiremode(ItemStack stack, PlayerEntity player) {
        Firemode mode = this.getFiremode(stack);
        stack.getTag().putInt("firemode", mode == Firemode.SINGLE ? 2 : 0);
        return true;
    }

    @Override
    public SkillType<?> getRequiredSkill() {
        return Skills.UMP45_ASSEMBLY;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ResourceLocation getAimAnimationPath(ItemStack stack, PlayerEntity player) {
        boolean rds = PlayerData.hasActiveSkill(Minecraft.getInstance().player, Skills.UMP45_RED_DOT);
        return AIM_ANIMATIONS[rds ? 1 : 0];
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ResourceLocation getBulletEjectAnimationPath() {
        return EJECT;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ResourceLocation getReloadAnimation(PlayerEntity player) {
        return RELOAD_ANIMATION;
    }

    @Override
    public IRenderConfig left() {
        return RenderConfigs.UMP45_LEFT;
    }

    @Override
    public IRenderConfig right() {
        return RenderConfigs.UMP45_RIGHT;
    }
}
