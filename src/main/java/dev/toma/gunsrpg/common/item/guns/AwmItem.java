package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.client.render.RenderConfigs;
import dev.toma.gunsrpg.client.render.item.AwmRenderer;
import dev.toma.gunsrpg.common.attribute.Attribs;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.entity.projectile.PenetrationData;
import dev.toma.gunsrpg.common.init.ModSounds;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterials;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponBuilder;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponCategory;
import dev.toma.gunsrpg.common.item.guns.util.ScopeDataRegistry;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.config.gun.RecoilParameters;
import lib.toma.animations.api.IRenderConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public class AwmItem extends AbstractBoltActionGun {

    private static final ResourceLocation RELOAD = GunsRPG.makeResource("awm/reload");
    private static final ResourceLocation UNJAM = GunsRPG.makeResource("awm/unjam");
    private static final ResourceLocation BOLT = GunsRPG.makeResource("awm/bolt");
    private static final ResourceLocation[] AIM = {
            GunsRPG.makeResource("awm/aim"),
            GunsRPG.makeResource("awm/aim_scoped")
    };
    private static final PenetrationData.Factory FACTORY = new PenetrationData.Factory(0.3f);
    private static final RecoilParameters RECOIL = new RecoilParameters().kick(0.2F);

    public AwmItem(String name) {
        super(name, new Properties().setISTER(() -> AwmRenderer::new).durability(350));
    }

    @Override
    public void initializeWeapon(WeaponBuilder builder) {
        builder
                .category(WeaponCategory.SR)
                .config(GunsRPG.config.weapon.awm)
                .ammo()
                    .define(AmmoMaterials.WOOD, 0)
                    .define(AmmoMaterials.STONE, 5)
                    .define(AmmoMaterials.IRON, 10)
                    .define(AmmoMaterials.LAPIS, 8)
                    .define(AmmoMaterials.GOLD, 15)
                    .define(AmmoMaterials.REDSTONE, 12)
                    .define(AmmoMaterials.EMERALD, 21)
                    .define(AmmoMaterials.QUARTZ, 16)
                    .define(AmmoMaterials.DIAMOND, 29)
                    .define(AmmoMaterials.AMETHYST, 35)
                    .define(AmmoMaterials.NETHERITE, 42)
                .build();
        ScopeDataRegistry.getRegistry().register(this, 8.0F, ScopeDataRegistry.ZOOM_6_0, provider -> provider.hasSkill(Skills.AWM_SCOPE));
    }

    @Override
    public RecoilParameters getRecoilParameters() {
        return RECOIL;
    }

    @Override
    protected boolean isSilenced(PlayerEntity player) {
        return PlayerData.hasActiveSkill(player, Skills.AWM_SUPPRESSOR);
    }

    @Override
    protected SoundEvent getShootSound(PlayerEntity entity) {
        return isSilenced(entity) ? ModSounds.GUN_AWM_SILENCED : ModSounds.GUN_AWM;
    }

    @Override
    public int getReloadTime(IAttributeProvider provider, ItemStack stack) {
        return Attribs.AWM_RELOAD.intValue(provider);
    }

    @Override
    public int getFirerate(IAttributeProvider provider) {
        return provider.getAttribute(Attribs.AWM_FIRERATE).intValue();
    }

    @Override
    public int getMaxAmmo(IAttributeProvider provider) {
        return provider.getAttribute(Attribs.AWM_MAG_CAPACITY).intValue();
    }

    @Override
    public float getVerticalRecoil(IAttributeProvider provider) {
        return Attribs.AWM_VERTICAL.floatValue(provider);
    }

    @Override
    public float getHorizontalRecoil(IAttributeProvider provider) {
        return Attribs.AWM_HORIZONTAL.floatValue(provider);
    }

    @Override
    public double getNoiseMultiplier(IAttributeProvider provider) {
        return Attribs.AWM_NOISE.value(provider);
    }

    @Override
    public double getHeadshotMultiplier(IAttributeProvider provider) {
        return Attribs.AWM_HEADSHOT.value(provider);
    }

    @Override
    public PenetrationData getPenetrationData(IPlayerData data) {
        return data.getSkillProvider().hasSkill(Skills.AWM_PENETRATOR) ? FACTORY.make() : null;
    }

    @Override
    public int getUnjamTime(ItemStack stack) {
        return 90;
    }

    @Override
    public SkillType<?> getRequiredSkill() {
        return Skills.AWM_ASSEMBLY;
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
        return BOLT;
    }

    @Override
    public ResourceLocation getAimAnimationPath(ItemStack stack, PlayerEntity player) {
        return AIM[PlayerData.hasActiveSkill(player, Skills.AWM_SCOPE) ? 1 : 0];
    }

    @Override
    public IRenderConfig left() {
        return RenderConfigs.AWM_LEFT;
    }

    @Override
    public IRenderConfig right() {
        return RenderConfigs.AWM_RIGHT;
    }
}
