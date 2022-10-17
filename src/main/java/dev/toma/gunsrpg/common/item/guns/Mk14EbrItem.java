package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.client.render.RenderConfigs;
import dev.toma.gunsrpg.client.render.item.Mk14EbrRenderer;
import dev.toma.gunsrpg.common.attribute.Attribs;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.init.ModSounds;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterials;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponBuilder;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponCategory;
import dev.toma.gunsrpg.common.item.guns.util.Firemode;
import dev.toma.gunsrpg.common.item.guns.util.ScopeDataRegistry;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.config.ModConfig;
import lib.toma.animations.api.IRenderConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public class Mk14EbrItem extends GunItem {

    private static final ResourceLocation RELOAD = GunsRPG.makeResource("mk14/reload");
    private static final ResourceLocation UNJAM = GunsRPG.makeResource("mk14/unjam");
    private static final ResourceLocation EJECT = GunsRPG.makeResource("mk14/eject");
    private static final ResourceLocation[] AIM = {
            GunsRPG.makeResource("mk14/aim"),
            GunsRPG.makeResource("mk14/aim_scoped")
    };

    public Mk14EbrItem(String name) {
        super(name, new Properties().setISTER(() -> Mk14EbrRenderer::new).durability(850));
    }

    @Override
    public void initializeWeapon(WeaponBuilder builder) {
        builder
                .config(ModConfig.weaponConfig.mk14)
                .firemodeSelector(Firemode::singleAndFullAuto)
                .ammo(WeaponCategory.DMR)
                    .define(AmmoMaterials.WOOD)
                    .define(AmmoMaterials.STONE, 3)
                    .define(AmmoMaterials.IRON, 5)
                    .define(AmmoMaterials.LAPIS, 4)
                    .define(AmmoMaterials.GOLD, 8)
                    .define(AmmoMaterials.REDSTONE, 6)
                    .define(AmmoMaterials.EMERALD, 11)
                    .define(AmmoMaterials.QUARTZ, 8)
                    .define(AmmoMaterials.DIAMOND, 15)
                    .define(AmmoMaterials.AMETHYST, 18)
                    .define(AmmoMaterials.NETHERITE, 22)
                .build();
        ScopeDataRegistry.getRegistry().register(this, 15.0F, ScopeDataRegistry.ZOOM_4_0, provider -> provider.hasSkill(Skills.MK14EBR_SCOPE));
    }

    @Override
    protected boolean isSilenced(PlayerEntity player) {
        return PlayerData.hasActiveSkill(player, Skills.MK14EBR_SUPPRESSOR);
    }

    @Override
    protected SoundEvent getShootSound(PlayerEntity entity) {
        return isSilenced(entity) ? ModSounds.GUN_MK14_SILENCED : ModSounds.GUN_MK14;
    }

    @Override
    public int getReloadTime(IAttributeProvider provider, ItemStack stack) {
        return Attribs.MK14_RELOAD.intValue(provider);
    }

    @Override
    public int getFirerate(IAttributeProvider provider) {
        return provider.getAttribute(Attribs.MK14_FIRERATE).intValue();
    }

    @Override
    public int getMaxAmmo(IAttributeProvider provider) {
        return provider.getAttribute(Attribs.MK14_MAG_CAPACITY).intValue();
    }

    @Override
    public float getVerticalRecoil(IAttributeProvider provider) {
        return Attribs.MK14_VERTICAL.floatValue(provider);
    }

    @Override
    public float getHorizontalRecoil(IAttributeProvider provider) {
        return Attribs.MK14_HORIZONTAL.floatValue(provider);
    }

    @Override
    public double getNoiseMultiplier(IAttributeProvider provider) {
        return Attribs.MK14_NOISE.value(provider);
    }

    @Override
    public double getHeadshotMultiplier(IAttributeProvider provider) {
        return Attribs.MK14_HEADSHOT.value(provider);
    }

    @Override
    public int getUnjamTime(ItemStack stack) {
        return 70;
    }

    @Override
    public SkillType<?> getRequiredSkill() {
        return Skills.MK14EBR_ASSEMBLY;
    }

    @Override
    public ResourceLocation getReloadAnimation(PlayerEntity player) {
        return RELOAD;
    }

    @Override
    public ResourceLocation getUnjamAnimationPath() {
        return UNJAM;
    }

    @Override
    public ResourceLocation getBulletEjectAnimationPath() {
        return EJECT;
    }

    @Override
    public ResourceLocation getAimAnimationPath(ItemStack stack, PlayerEntity player) {
        return AIM[PlayerData.hasActiveSkill(player, Skills.MK14EBR_SCOPE) ? 1 : 0];
    }

    @Override
    public float getOpticalRecoilXY() {
        return 0.75F;
    }

    @Override
    public float getOpticalRecoilZ() {
        return 0.4F;
    }

    @Override
    public IRenderConfig left() {
        return RenderConfigs.MK14_LEFT;
    }

    @Override
    public IRenderConfig right() {
        return RenderConfigs.MK14_RIGHT;
    }
}
