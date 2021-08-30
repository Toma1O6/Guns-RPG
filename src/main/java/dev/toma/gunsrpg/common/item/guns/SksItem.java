package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.render.RenderConfigs;
import dev.toma.gunsrpg.client.render.item.SksRenderer;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.init.ModSounds;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterials;
import dev.toma.gunsrpg.common.item.guns.util.Firemode;
import dev.toma.gunsrpg.common.item.guns.util.GunType;
import dev.toma.gunsrpg.common.item.guns.util.MaterialContainer;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.config.ModConfig;
import dev.toma.gunsrpg.config.gun.IWeaponConfig;
import dev.toma.gunsrpg.util.SkillUtil;
import lib.toma.animations.api.IRenderConfig;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SksItem extends GunItem {

    private static final ResourceLocation EJECT = GunsRPG.makeResource("sks/eject");
    private static final ResourceLocation[] AIM_ANIMATIONS = {
            GunsRPG.makeResource("sks/aim"),
            GunsRPG.makeResource("sks/aim_red_dot")
    };
    private static final ResourceLocation RELOAD_ANIMATION = GunsRPG.makeResource("sks/reload");

    public SksItem(String name) {
        super(name, GunType.AR, new Properties().setISTER(() -> SksRenderer::new));
    }

    @Override
    public IWeaponConfig getWeaponConfig() {
        return ModConfig.weaponConfig.sks;
    }

    @Override
    public void fillAmmoMaterialData(MaterialContainer container) {
        container
                .add(AmmoMaterials.WOOD, 0)
                .add(AmmoMaterials.STONE, 2)
                .add(AmmoMaterials.IRON, 4)
                .add(AmmoMaterials.GOLD, 6)
                .add(AmmoMaterials.DIAMOND, 9)
                .add(AmmoMaterials.EMERALD, 11)
                .add(AmmoMaterials.AMETHYST, 14);
    }

    @Override
    protected SoundEvent getShootSound(PlayerEntity entity) {
        return this.isSilenced(entity) ? ModSounds.SKS_SILENT : ModSounds.SKS;
    }

    @Override
    protected SoundEvent getEntityShootSound(LivingEntity entity) {
        return ModSounds.SLR;
    }

    @Override
    public boolean isSilenced(PlayerEntity player) {
        return PlayerData.hasActiveSkill(player, Skills.SKS_SUPPRESSOR);
    }

    @Override
    public int getMaxAmmo(PlayerEntity player) {
        return PlayerData.hasActiveSkill(player, Skills.SKS_EXTENDED) ? 20 : 10;
    }

    @Override
    public int getFirerate(PlayerEntity player) {
        IWeaponConfig cfg = getWeaponConfig();
        int firerate = PlayerData.hasActiveSkill(player, Skills.SKS_TOUGH_SPRING) ? cfg.getUpgradedFirerate() : cfg.getFirerate();
        if (PlayerData.hasActiveSkill(player, Skills.SKS_ADAPTIVE_CHAMBERING)) {
            firerate -= 2;
        }
        return Math.max(firerate, 1);
    }

    @Override
    public int getReloadTime(PlayerEntity player) {
        return (int) (32 * SkillUtil.getReloadTimeMultiplier(player));
    }

    @Override
    public float getVerticalRecoil(PlayerEntity player) {
        float f = super.getVerticalRecoil(player);
        float mod = PlayerData.hasActiveSkill(player, Skills.SKS_VERTICAL_GRIP) ? ModConfig.weaponConfig.general.verticalGrip.floatValue() : 1.0F;
        float mod2 = PlayerData.hasActiveSkill(player, Skills.SKS_CHEEKPAD) ? ModConfig.weaponConfig.general.cheekpad.floatValue() : 1.0F;
        return mod * mod2 * f;
    }

    @Override
    public float getHorizontalRecoil(PlayerEntity player) {
        float f = super.getHorizontalRecoil(player);
        float mod = PlayerData.hasActiveSkill(player, Skills.SKS_CHEEKPAD) ? ModConfig.weaponConfig.general.cheekpad.floatValue() : 1.0F;
        return mod * f;
    }

    @Override
    public boolean switchFiremode(ItemStack stack, PlayerEntity player) {
        Firemode firemode = this.getFiremode(stack);
        int newMode = 0;
        if (firemode == Firemode.SINGLE && PlayerData.hasActiveSkill(player, Skills.SKS_ADAPTIVE_CHAMBERING)) {
            newMode = 2;
        }
        stack.getTag().putInt("firemode", newMode);
        return firemode.ordinal() != newMode;
    }

    @Override
    public SkillType<?> getRequiredSkill() {
        return Skills.SKS_ASSEMBLY;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ResourceLocation getAimAnimationPath(ItemStack stack, PlayerEntity player) {
        boolean scoped = PlayerData.hasActiveSkill(player, Skills.SKS_RED_DOT);
        return AIM_ANIMATIONS[scoped ? 1 : 0];
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
        return RenderConfigs.SKS_LEFT;
    }

    @Override
    public IRenderConfig right() {
        return RenderConfigs.SKS_RIGHT;
    }
}
