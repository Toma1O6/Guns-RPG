package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.render.RenderConfigs;
import dev.toma.gunsrpg.client.render.item.SksRenderer;
import dev.toma.gunsrpg.common.attribute.Attribs;
import dev.toma.gunsrpg.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.init.ModSounds;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterials;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponBuilder;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponCategory;
import dev.toma.gunsrpg.common.item.guns.util.Firemode;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.config.ModConfig;
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
        super(name, new Properties().setISTER(() -> SksRenderer::new));
    }

    @Override
    public void initializeWeapon(WeaponBuilder builder) {
        builder
                .category(WeaponCategory.AR)
                .config(ModConfig.weaponConfig.sks)
                .ammo()
                    .define(AmmoMaterials.WOOD, 0)
                    .define(AmmoMaterials.STONE, 2)
                    .define(AmmoMaterials.IRON, 4)
                    .define(AmmoMaterials.GOLD, 6)
                    .define(AmmoMaterials.DIAMOND, 9)
                    .define(AmmoMaterials.EMERALD, 11)
                    .define(AmmoMaterials.AMETHYST, 14)
                .build();
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
    public int getMaxAmmo(IAttributeProvider provider) {
        return provider.getAttribute(Attribs.SKS_MAG_CAPACITY).intValue();
    }

    @Override
    public int getFirerate(IAttributeProvider provider) {
        return provider.getAttribute(Attribs.SKS_FIRERATE).intValue();
    }

    @Override
    public int getReloadTime(IAttributeProvider provider) {
        return (int) (32 * provider.getAttributeValue(Attribs.RELOAD_SPEED));
    }

    @Override
    public float getVerticalRecoil(IAttributeProvider provider) {
        return Attribs.SKS_VERTICAL.floatValue(provider);
    }

    @Override
    public float getHorizontalRecoil(IAttributeProvider provider) {
        return Attribs.SKS_HORIZONTAL.floatValue(provider);
    }

    @Override
    public double getNoiseMultiplier(IAttributeProvider provider) {
        return Attribs.SKS_LOUDNESS.value(provider);
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
