package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.IReloadManager;
import dev.toma.gunsrpg.api.common.IWeaponConfig;
import dev.toma.gunsrpg.api.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.client.animation.ModAnimations;
import dev.toma.gunsrpg.client.render.RenderConfigs;
import dev.toma.gunsrpg.client.render.item.S1897Renderer;
import dev.toma.gunsrpg.common.IShootProps;
import dev.toma.gunsrpg.common.attribute.Attribs;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.entity.projectile.AbstractProjectile;
import dev.toma.gunsrpg.common.init.ModSounds;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterials;
import dev.toma.gunsrpg.common.item.guns.reload.ReloadManagers;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponBuilder;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponCategory;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.config.ModConfig;
import dev.toma.gunsrpg.util.SkillUtil;
import lib.toma.animations.AnimationUtils;
import lib.toma.animations.api.Animation;
import lib.toma.animations.api.IAnimationPipeline;
import lib.toma.animations.api.IRenderConfig;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class S1897Item extends AbstractShotgun {

    private static final ResourceLocation AIM_ANIMATION = GunsRPG.makeResource("s1897/aim");
    private static final ResourceLocation RELOAD_ANIMATION = GunsRPG.makeResource("s1897/reload");
    private static final ResourceLocation LOAD_BULLET_ANIMATION = GunsRPG.makeResource("s1897/load_bullet");
    private static final ResourceLocation UNJAM = GunsRPG.makeResource("s1897/unjam");
    private static final ResourceLocation PUMP = GunsRPG.makeResource("s1897/pump");

    public S1897Item(String name) {
        super(name, new Properties().setISTER(() -> S1897Renderer::new).durability(320));
    }

    @Override
    public void initializeWeapon(WeaponBuilder builder) {
        builder
                .category(WeaponCategory.SG)
                .config(ModConfig.weaponConfig.s1897)
                .ammo()
                    .define(AmmoMaterials.WOOD, 0)
                    .define(AmmoMaterials.STONE, 1)
                    .define(AmmoMaterials.IRON, 2)
                    .define(AmmoMaterials.LAPIS, 1)
                    .define(AmmoMaterials.GOLD, 3)
                    .define(AmmoMaterials.REDSTONE, 2)
                    .define(AmmoMaterials.EMERALD, 5)
                    .define(AmmoMaterials.QUARTZ, 4)
                    .define(AmmoMaterials.DIAMOND, 6)
                    .define(AmmoMaterials.AMETHYST, 8)
                    .define(AmmoMaterials.NETHERITE, 10)
                .build();
    }

    @Override
    public int getUnjamTime(ItemStack stack) {
        return 70;
    }

    @Override
    public int getPelletCount(LivingEntity shooter, ItemStack stack) {
        return 6;
    }

    @Override
    public int getMaxAmmo(IAttributeProvider provider) {
        return provider.getAttribute(Attribs.S1897_MAG_CAPACITY).intValue();
    }

    @Override
    public int getReloadTime(IAttributeProvider provider, ItemStack stack) {
        return Attribs.S1897_RELOAD.intValue(provider);
    }

    @Override
    public int getFirerate(IAttributeProvider provider) {
        return provider.getAttribute(Attribs.S1897_FIRERATE).intValue();
    }

    @Override
    public float getVerticalRecoil(IAttributeProvider provider) {
        return 5.3F * super.getVerticalRecoil(provider);
    }

    @Override
    public float getHorizontalRecoil(IAttributeProvider provider) {
        return 1.9F * super.getHorizontalRecoil(provider);
    }

    @Override
    public IReloadManager getReloadManager(PlayerEntity player, IAttributeProvider attributeProvider) {
        int prep = (int) Attribs.S1897_RELOAD.getModifiedValue(attributeProvider, 30);
        return ReloadManagers.singleBulletLoading(prep, player, this, player.getMainHandItem(), LOAD_BULLET_ANIMATION);
    }

    @Override
    public SoundEvent getShootSound(PlayerEntity entity) {
        return ModSounds.GUN_S1897;
    }

    @Override
    protected SoundEvent getEntityShootSound(LivingEntity entity) {
        return ModSounds.S686;
    }

    @Override
    public void onKillEntity(AbstractProjectile bullet, LivingEntity victim, ItemStack stack, LivingEntity shooter) {
        if (shooter instanceof PlayerEntity && PlayerData.hasActiveSkill((PlayerEntity) shooter, Skills.S1897_NEVER_GIVE_UP)) {
            shooter.addEffect(new EffectInstance(Effects.DAMAGE_RESISTANCE, 100, 0, false, false));
        }
    }

    @Override
    protected float getInitialVelocity(IWeaponConfig config, LivingEntity shooter) {
        float velocity = config.getVelocity();
        if (shooter instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) shooter;
            if (PlayerData.hasActiveSkill(player, Skills.S1897_EXTENDED_BARREL)) {
                return velocity * SkillUtil.EXTENDED_BARREL_VELOCITY;
            }
        }
        return velocity;
    }

    @Override
    protected float getInaccuracy(IShootProps props, LivingEntity entity) {
        float spread = super.getInaccuracy(props, entity);
        if (entity instanceof PlayerEntity) {
            if (PlayerData.hasActiveSkill((PlayerEntity) entity, Skills.S1897_CHOKE)) {
                spread *= SkillUtil.CHOKE_SPREAD;
            }
        }
        return spread;
    }

    @Override
    public SkillType<?> getRequiredSkill() {
        return Skills.S1897_ASSEMBLY;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ResourceLocation getAimAnimationPath(ItemStack stack, PlayerEntity player) {
        return AIM_ANIMATION;
    }

    @OnlyIn(Dist.CLIENT)
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
        return PUMP;
    }

    @Override
    public IRenderConfig left() {
        return RenderConfigs.S1897_LEFT;
    }

    @Override
    public IRenderConfig right() {
        return RenderConfigs.S1897_RIGHT;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    protected void handleAnimation(ResourceLocation animationPath, int length, IAnimationPipeline pipeline) {
        pipeline.scheduleInsert(ModAnimations.CHAMBER, AnimationUtils.createAnimation(animationPath, provider -> new Animation(provider, length)), 4);
    }
}
