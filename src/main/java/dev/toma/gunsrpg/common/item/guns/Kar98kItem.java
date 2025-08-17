package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.IReloadManager;
import dev.toma.gunsrpg.api.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.client.render.RenderConfigs;
import dev.toma.gunsrpg.client.render.item.Kar98kRenderer;
import dev.toma.gunsrpg.common.attribute.Attribs;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.entity.projectile.PenetrationData;
import dev.toma.gunsrpg.common.init.ModSounds;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterials;
import dev.toma.gunsrpg.common.item.guns.reload.ReloadManagers;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponBuilder;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponCategory;
import dev.toma.gunsrpg.common.item.guns.util.ScopeDataRegistry;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.config.gun.RecoilParameters;
import lib.toma.animations.api.IRenderConfig;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public class Kar98kItem extends AbstractBoltActionGun {

    private static final ResourceLocation BOLT = GunsRPG.makeResource("kar98k/bolt");
    private static final ResourceLocation[] AIM_ANIMATIONS = {
            GunsRPG.makeResource("kar98k/aim"),
            GunsRPG.makeResource("kar98k/aim_scoped")
    };
    private static final ResourceLocation RELOAD_ANIMATION = GunsRPG.makeResource("kar98k/reload");
    private static final ResourceLocation LOAD_BULLET_ANIMATION = GunsRPG.makeResource("kar98k/load_bullet");
    private static final ResourceLocation RELOAD_CLIP_ANIMATION = GunsRPG.makeResource("kar98k/reload_clip");
    private static final ResourceLocation UNJAM = GunsRPG.makeResource("kar98k/unjam");
    private static final PenetrationData.Factory PENETRATION_DATA_FACTORY = new PenetrationData.Factory(0.5f);
    private static final RecoilParameters RECOIL = new RecoilParameters().kick(0.2F);

    public Kar98kItem(String name) {
        super(name, new Properties().setISTER(() -> Kar98kRenderer::new).durability(300));
    }

    @Override
    public void initializeWeapon(WeaponBuilder builder) {
        builder
                .category(WeaponCategory.SR)
                .config(GunsRPG.config.weapon.kar98k)
                .ammo()
                    .define(AmmoMaterials.WOOD, 0)
                    .define(AmmoMaterials.STONE, 4)
                    .define(AmmoMaterials.IRON, 9)
                    .define(AmmoMaterials.LAPIS, 7)
                    .define(AmmoMaterials.GOLD, 14)
                    .define(AmmoMaterials.REDSTONE, 11)
                    .define(AmmoMaterials.EMERALD, 17)
                    .define(AmmoMaterials.QUARTZ, 15)
                    .define(AmmoMaterials.DIAMOND, 20)
                    .define(AmmoMaterials.AMETHYST, 24)
                    .define(AmmoMaterials.NETHERITE, 29)
                .build();

        ScopeDataRegistry.getRegistry().register(this, 15.0F, ScopeDataRegistry.ZOOM_4_0, provider -> provider.hasSkill(Skills.KAR98K_SCOPE));
    }

    @Override
    public RecoilParameters getRecoilParameters() {
        return RECOIL;
    }

    @Override
    public int getUnjamTime(ItemStack stack) {
        return 80;
    }

    @Override
    public int getMaxAmmo(IAttributeProvider provider) {
        return provider.getAttribute(Attribs.KAR98K_MAG_CAPACITY).intValue();
    }

    @Override
    public int getReloadTime(IAttributeProvider provider, ItemStack stack) {
        return (int) (this.getAmmo(stack) > 0 ? Attribs.KAR98K_RELOAD.value(provider) : Attribs.KAR98K_RELOAD.getModifiedValue(provider, 100));
    }

    @Override
    public int getFirerate(IAttributeProvider provider) {
        return provider.getAttribute(Attribs.KAR98K_FIRERATE).intValue();
    }

    @Override
    public float getVerticalRecoil(IAttributeProvider provider) {
        return Attribs.KAR98K_VERTICAL.floatValue(provider);
    }

    @Override
    public float getHorizontalRecoil(IAttributeProvider provider) {
        return Attribs.KAR98K_HORIZONTAL.floatValue(provider);
    }

    @Override
    public double getNoiseMultiplier(IAttributeProvider provider) {
        return Attribs.KAR98K_LOUDNESS.value(provider);
    }

    @Override
    public double getHeadshotMultiplier(IAttributeProvider provider) {
        return Attribs.KAR98K_HEADSHOT.value(provider);
    }

    @Override
    public PenetrationData getPenetrationData(IPlayerData data) {
        return data.getSkillProvider().hasSkill(Skills.KAR98K_PENETRATOR) ? PENETRATION_DATA_FACTORY.make() : null;
    }

    @Override
    protected float getModifiedDamageChance(float damageChance, IPlayerData data) {
        if (data.getSkillProvider().hasSkill(Skills.KAR98K_RELIABLE)) {
            return 0.85F * damageChance;
        }
        return damageChance;
    }

    @Override
    protected float getModifiedJamChance(float jamChance, IPlayerData data) {
        if (data.getSkillProvider().hasSkill(Skills.KAR98K_RELIABLE)) {
            return 0.8F * jamChance;
        }
        return jamChance;
    }

    @Override
    public IReloadManager getReloadManager(PlayerEntity player, IAttributeProvider attributeProvider) {
        ItemStack stack = player.getMainHandItem();
        int prepTime = (int) Attribs.KAR98K_RELOAD.getModifiedValue(attributeProvider, 50);
        return ReloadManagers.either(
                getAmmo(stack) > 0,
                ReloadManagers.singleBulletLoading(prepTime, player, this, stack, LOAD_BULLET_ANIMATION),
                ReloadManagers.clip(RELOAD_CLIP_ANIMATION)
        );
    }

    @Override
    protected boolean isSilenced(PlayerEntity player) {
        return PlayerData.hasActiveSkill(player, Skills.KAR98K_SUPPRESSOR);
    }

    @Override
    public SoundEvent getShootSound(PlayerEntity entity) {
        return this.isSilenced(entity) ? ModSounds.GUN_KAR98K_SILENCED : ModSounds.GUN_KAR98K;
    }

    @Override
    protected SoundEvent getEntityShootSound(LivingEntity entity) {
        return ModSounds.M24;
    }

    @Override
    public SkillType<?> getRequiredSkill() {
        return Skills.KAR98K_ASSEMBLY;
    }

    @Override
    public ResourceLocation getAimAnimationPath(ItemStack stack, PlayerEntity player) {
        return AIM_ANIMATIONS[PlayerData.hasActiveSkill(player, Skills.KAR98K_SCOPE) ? 1 : 0];
    }

    @Override
    public ResourceLocation getReloadAnimation(PlayerEntity player) {
        return RELOAD_ANIMATION;
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
    public IRenderConfig left() {
        return RenderConfigs.KAR98K_LEFT;
    }

    @Override
    public IRenderConfig right() {
        return RenderConfigs.KAR98K_RIGHT;
    }
}
