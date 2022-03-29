package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.IReloadManager;
import dev.toma.gunsrpg.api.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.client.render.RenderConfigs;
import dev.toma.gunsrpg.client.render.item.R45Renderer;
import dev.toma.gunsrpg.common.attribute.Attribs;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.entity.projectile.AbstractProjectile;
import dev.toma.gunsrpg.common.init.ModSounds;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterials;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoType;
import dev.toma.gunsrpg.common.item.guns.reload.ReloadManagers;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponBuilder;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponCategory;
import dev.toma.gunsrpg.common.item.guns.util.IDualWieldGun;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.config.ModConfig;
import dev.toma.gunsrpg.util.SkillUtil;
import lib.toma.animations.api.IRenderConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class R45Item extends GunItem implements IDualWieldGun {

    private static final ResourceLocation AIM = GunsRPG.makeResource("r45/aim");
    private static final ResourceLocation AIM_DUAL = GunsRPG.makeResource("r45/aim_dual");
    private static final ResourceLocation RELOAD = GunsRPG.makeResource("r45/reload");
    private static final ResourceLocation RELOAD_BULLET = GunsRPG.makeResource("r45/load_bullet");
    private static final ResourceLocation UNJAM = GunsRPG.makeResource("r45/unjam");

    public R45Item(String name) {
        super(name, new Properties().setISTER(() -> R45Renderer::new).durability(350));
    }

    @Override
    public void initializeWeapon(WeaponBuilder builder) {
        builder
                .config(ModConfig.weaponConfig.r45)
                .caliber(AmmoType.AMMO_45ACP)
                .ammo(WeaponCategory.PISTOL)
                    .define(AmmoMaterials.WOOD, 0)
                    .define(AmmoMaterials.STONE, 2)
                    .define(AmmoMaterials.IRON, 4)
                    .define(AmmoMaterials.LAPIS, 3)
                    .define(AmmoMaterials.GOLD, 6)
                    .define(AmmoMaterials.REDSTONE, 5)
                    .define(AmmoMaterials.EMERALD, 9)
                    .define(AmmoMaterials.QUARTZ, 7)
                    .define(AmmoMaterials.DIAMOND, 11)
                    .define(AmmoMaterials.AMETHYST, 14)
                    .define(AmmoMaterials.NETHERITE, 17)
                .build();
    }

    @Override
    public int getFirerate(IAttributeProvider provider) {
        return provider.getAttribute(Attribs.R45_FIRERATE).intValue();
    }

    @Override
    protected boolean isSilenced(PlayerEntity player) {
        return PlayerData.hasActiveSkill(player, Skills.R45_SUPPRESSOR);
    }

    @Override
    protected SoundEvent getShootSound(PlayerEntity entity) {
        return isSilenced(entity) ? ModSounds.GUN_R1895_SILENCED : ModSounds.GUN_R45;
    }

    @Override
    public int getUnjamTime(ItemStack stack) {
        return 60;
    }

    @Override
    public SkillType<?> getSkillForDualWield() {
        return Skills.R45_DUAL_WIELD;
    }

    @Override
    public int getMaxAmmo(IAttributeProvider provider) {
        return provider.getAttribute(Attribs.R45_MAG_CAPACITY).intValue();
    }

    @Override
    public int getReloadTime(IAttributeProvider provider) {
        return Attribs.R45_RELOAD.intValue(provider);
    }

    @Override
    public float getVerticalRecoil(IAttributeProvider provider) {
        return Attribs.R45_VERTICAL.floatValue(provider);
    }

    @Override
    public float getHorizontalRecoil(IAttributeProvider provider) {
        return Attribs.R45_HORIZONTAL.floatValue(provider);
    }

    @Override
    public double getNoiseMultiplier(IAttributeProvider provider) {
        return Attribs.R45_LOUDNESS.value(provider);
    }

    @Override
    public void onHitEntity(AbstractProjectile bullet, LivingEntity victim, ItemStack stack, LivingEntity shooter) {
        handleHeadshotHealing(bullet, shooter);
    }

    @Override
    public void onKillEntity(AbstractProjectile bullet, LivingEntity victim, ItemStack stack, LivingEntity shooter) {
        handleHeadshotHealing(bullet, shooter);
    }

    @Override
    public IReloadManager getReloadManager(PlayerEntity player, IAttributeProvider attributeProvider) {
        return ReloadManagers.singleBulletLoading(26, player, this, player.getMainHandItem(), RELOAD_BULLET);
    }

    @Override
    public SkillType<?> getRequiredSkill() {
        return Skills.R45_ASSEMBLY;
    }

    @Override
    public ResourceLocation getReloadAnimation(PlayerEntity player) {
        return RELOAD;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ResourceLocation getUnjamAnimationPath() {
        return UNJAM;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ResourceLocation getAimAnimationPath(ItemStack stack, PlayerEntity player) {
        return isDualWieldActive() ? AIM_DUAL : AIM;
    }

    @Override
    public IRenderConfig left() {
        return isDualWieldActive() ? RenderConfigs.R45_LEFT_DUAL : RenderConfigs.R45_LEFT;
    }

    @Override
    public IRenderConfig right() {
        return isDualWieldActive() ? RenderConfigs.R45_RIGHT_DUAL : RenderConfigs.R45_RIGHT;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void onShoot(PlayerEntity player, ItemStack stack) {
    }

    @OnlyIn(Dist.CLIENT)
    private boolean isDualWieldActive() {
        return PlayerData.hasActiveSkill(Minecraft.getInstance().player, Skills.R45_DUAL_WIELD);
    }

    private void handleHeadshotHealing(AbstractProjectile projectile, LivingEntity shooter) {
        if (!(shooter instanceof PlayerEntity)) return;
        PlayerEntity player = (PlayerEntity) shooter;
        if (PlayerData.hasActiveSkill(player, Skills.R45_ACE_OF_HEARTS)) {
            boolean headshot = projectile.getProperty(dev.toma.gunsrpg.util.properties.Properties.IS_HEADSHOT);
            System.out.println(headshot);
            if (headshot) {
                SkillUtil.heal(player, 1.0F);
            }
        }
    }
}
