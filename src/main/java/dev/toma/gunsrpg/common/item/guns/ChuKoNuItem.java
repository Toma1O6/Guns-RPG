package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.IReloadManager;
import dev.toma.gunsrpg.api.common.IWeaponConfig;
import dev.toma.gunsrpg.api.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.api.common.data.ISkillProvider;
import dev.toma.gunsrpg.client.render.RenderConfigs;
import dev.toma.gunsrpg.client.render.item.ChuKoNuRenderer;
import dev.toma.gunsrpg.common.attribute.Attribs;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.entity.projectile.AbstractProjectile;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterials;
import dev.toma.gunsrpg.common.item.guns.reload.ReloadManagers;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponBuilder;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponCategory;
import dev.toma.gunsrpg.common.item.guns.util.Firemode;
import dev.toma.gunsrpg.common.item.guns.util.ScopeDataRegistry;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.config.ModConfig;
import lib.toma.animations.api.IRenderConfig;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;

public class ChuKoNuItem extends AbstractCrossbow {

    private static final ResourceLocation RELOAD = GunsRPG.makeResource("chukonu/reload");
    private static final ResourceLocation BULLET = GunsRPG.makeResource("chukonu/load_bullet");
    private static final ResourceLocation UNJAM = GunsRPG.makeResource("chukonu/unjam");
    private static final ResourceLocation[] AIM = {
            GunsRPG.makeResource("chukonu/aim"),
            GunsRPG.makeResource("chukonu/aim_scoped")
    };

    public ChuKoNuItem(String name) {
        super(name, new Properties().setISTER(() -> ChuKoNuRenderer::new).durability(450));
    }

    @Override
    public void initializeWeapon(WeaponBuilder builder) {
        builder
                .config(ModConfig.weaponConfig.chukonu)
                .ammo(WeaponCategory.CROSSBOW)
                    .define(AmmoMaterials.WOOD, 0)
                    .define(AmmoMaterials.STONE, 2)
                    .define(AmmoMaterials.IRON, 4)
                    .define(AmmoMaterials.LAPIS, 3)
                    .define(AmmoMaterials.GOLD, 6)
                    .define(AmmoMaterials.REDSTONE, 5)
                    .define(AmmoMaterials.EMERALD, 9)
                    .define(AmmoMaterials.QUARTZ, 7)
                    .define(AmmoMaterials.DIAMOND, 11)
                    .define(AmmoMaterials.AMETHYST, 13)
                    .define(AmmoMaterials.NETHERITE, 15)
                .build();

        ScopeDataRegistry.getRegistry().register(this, 25.0F, 0.4F, provider -> provider.hasSkill(Skills.CHUKONU_SCOPE));
    }

    @Override
    protected float getInitialVelocity(IWeaponConfig config, LivingEntity livingEntity) {
        float velocity = config.getVelocity();
        if (livingEntity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) livingEntity;
            if (PlayerData.hasActiveSkill(player, Skills.CHUKONU_TOUGH_BOWSTRING)) {
                velocity *= 2;
            }
        }
        return velocity;
    }

    @Override
    public float modifyProjectileDamage(AbstractProjectile projectile, LivingEntity entity, PlayerEntity shooter, float damage) {
        if (PlayerData.hasActiveSkill(shooter, Skills.CHUKONU_HEAVY_BOLTS)) {
            return damage + 3.0F;
        }
        return damage;
    }

    @Override
    protected Firemode getDefaultFiremode() {
        return Firemode.FULL_AUTO;
    }

    @Override
    public int getReloadTime(IAttributeProvider provider, ItemStack stack) {
        return Attribs.CHUKONU_RELOAD.intValue(provider);
    }

    @Override
    public int getFirerate(IAttributeProvider provider) {
        return provider.getAttribute(Attribs.CHUKONU_FIRERATE).intValue();
    }

    @Override
    public IReloadManager getReloadManager(PlayerEntity player, IAttributeProvider attributeProvider) {
        return ReloadManagers.singleBulletLoading(15, player, this, player.getMainHandItem(), BULLET);
    }

    @Override
    public int getUnjamTime(ItemStack stack) {
        return 70;
    }

    @Override
    public int getMaxAmmo(IAttributeProvider provider) {
        return provider.getAttribute(Attribs.CHUKONU_MAG_CAPACITY).intValue();
    }

    @Override
    public void onHitEntity(AbstractProjectile bullet, LivingEntity victim, ItemStack stack, LivingEntity shooter) {
        if (shooter instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) shooter;
            PlayerData.get(player).ifPresent(data -> {
                ISkillProvider skillProvider = data.getSkillProvider();
                if (skillProvider.hasSkill(Skills.CHUKONU_HEAVY_BOLTS)) {
                    victim.addEffect(new EffectInstance(Effects.WEAKNESS, 90, 0));
                }
                if (skillProvider.hasSkill(Skills.CHUKONU_POISONED_BOLTS)) {
                    victim.addEffect(new EffectInstance(Effects.WITHER, 100, 1));
                }
            });

        }
    }

    @Override
    public SkillType<?> getRequiredSkill() {
        return Skills.CHUKONU_ASSEMBLY;
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
    public ResourceLocation getAimAnimationPath(ItemStack stack, PlayerEntity player) {
        return AIM[PlayerData.hasActiveSkill(player, Skills.CHUKONU_SCOPE) ? 1 : 0];
    }

    @Override
    public IRenderConfig left() {
        return RenderConfigs.CHUKONU_LEFT;
    }

    @Override
    public IRenderConfig right() {
        return RenderConfigs.CHUKONU_RIGHT;
    }
}
