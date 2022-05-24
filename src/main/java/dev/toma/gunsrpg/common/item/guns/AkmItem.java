package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.client.render.RenderConfigs;
import dev.toma.gunsrpg.client.render.item.AkmRenderer;
import dev.toma.gunsrpg.common.attribute.Attribs;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.entity.projectile.AbstractProjectile;
import dev.toma.gunsrpg.common.init.ModSounds;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterials;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoType;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponBuilder;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponCategory;
import dev.toma.gunsrpg.common.item.guns.util.Firemode;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.config.ModConfig;
import dev.toma.gunsrpg.util.SkillUtil;
import lib.toma.animations.api.IRenderConfig;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public class AkmItem extends GunItem {

    private static final ResourceLocation RELOAD = GunsRPG.makeResource("akm/reload");
    private static final ResourceLocation UNJAM = GunsRPG.makeResource("akm/unjam");
    private static final ResourceLocation EJECT = GunsRPG.makeResource("akm/eject");
    private static final ResourceLocation AIM = GunsRPG.makeResource("akm/aim");

    public AkmItem(String name) {
        super(name, new Properties().setISTER(() -> AkmRenderer::new).durability(1000));
    }

    @Override
    public void initializeWeapon(WeaponBuilder builder) {
        builder
                .category(WeaponCategory.AR)
                .caliber(AmmoType.AMMO_762MM)
                .config(ModConfig.weaponConfig.akm)
                .firemodeSelector(Firemode::singleAndFullAuto)
                .ammo()
                    .define(AmmoMaterials.WOOD)
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
    protected boolean isSilenced(PlayerEntity player) {
        return PlayerData.hasActiveSkill(player, Skills.AKM_SUPPRESSOR);
    }

    @Override
    protected SoundEvent getShootSound(PlayerEntity entity) {
        return isSilenced(entity) ? ModSounds.GUN_AKM_SILENCED : ModSounds.GUN_AKM;
    }

    @Override
    public int getReloadTime(IAttributeProvider provider, ItemStack stack) {
        return Attribs.AKM_RELOAD.intValue(provider);
    }

    @Override
    public int getFirerate(IAttributeProvider provider) {
        return provider.getAttribute(Attribs.AKM_FIRERATE).intValue();
    }

    @Override
    public double getNoiseMultiplier(IAttributeProvider provider) {
        return Attribs.AKM_NOISE.value(provider);
    }

    @Override
    public float getVerticalRecoil(IAttributeProvider provider) {
        return 3.3F * super.getVerticalRecoil(provider);
    }

    @Override
    public float getHorizontalRecoil(IAttributeProvider provider) {
        return 1.8F * super.getHorizontalRecoil(provider);
    }

    @Override
    public int getUnjamTime(ItemStack stack) {
        return 70;
    }

    @Override
    public int getMaxAmmo(IAttributeProvider provider) {
        return provider.getAttribute(Attribs.AKM_MAG_CAPACITY).intValue();
    }

    @Override
    public void onHitEntity(AbstractProjectile bullet, LivingEntity victim, ItemStack stack, LivingEntity shooter) {
        if (shooter instanceof PlayerEntity && PlayerData.hasActiveSkill((PlayerEntity) shooter, Skills.AKM_HEAVY_BULLETS) && shooter.getRandom().nextFloat() < 0.35f) {
            victim.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 100, 1, false, false));
            victim.addEffect(new EffectInstance(Effects.WEAKNESS, 100, 0, false, false));
        }
    }

    @Override
    protected float getModifiedDamageChance(float damageChance, IPlayerData data) {
        if (data.getSkillProvider().hasSkill(Skills.AKM_RELIABLE)) {
            return 0.85F * damageChance;
        }
        return damageChance;
    }

    @Override
    protected float getModifiedJamChance(float jamChance, IPlayerData data) {
        if (data.getSkillProvider().hasSkill(Skills.AKM_RELIABLE)) {
            return 0.8F * jamChance;
        }
        return jamChance;
    }

    @Override
    public float modifyProjectileDamage(AbstractProjectile projectile, LivingEntity entity, PlayerEntity shooter, float damage) {
        ItemStack weapon = projectile.getWeaponSource();
        if (weapon.getItem() instanceof GunItem && PlayerData.hasActiveSkill(shooter, Skills.AKM_EVERY_BULLET_COUNTS)) {
            int ammo = this.getAmmo(weapon);
            if (ammo == 0) {
                return damage * SkillUtil.EVERY_BULLET_COUNTS_DAMAGE;
            }
        }
        return damage;
    }

    @Override
    public SkillType<?> getRequiredSkill() {
        return Skills.AKM_ASSEMBLY;
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
        return AIM;
    }

    @Override
    public IRenderConfig left() {
        return RenderConfigs.AKM_LEFT;
    }

    @Override
    public IRenderConfig right() {
        return RenderConfigs.AKM_RIGHT;
    }
}
