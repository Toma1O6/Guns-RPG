package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.attribute.IAttributeId;
import dev.toma.gunsrpg.api.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.api.common.data.ISkillProvider;
import dev.toma.gunsrpg.client.render.RenderConfigs;
import dev.toma.gunsrpg.client.render.item.M1911Renderer;
import dev.toma.gunsrpg.common.attribute.Attribs;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.entity.projectile.AbstractProjectile;
import dev.toma.gunsrpg.common.init.ModSounds;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterials;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponBuilder;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponCategory;
import dev.toma.gunsrpg.common.skills.KillingSpreeSkill;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.util.SkillUtil;
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

public class M1911Item extends GunItem {

    private static final ResourceLocation EJECT = GunsRPG.makeResource("m1911/eject");
    private static final ResourceLocation AIM_ANIMATION = GunsRPG.makeResource("m1911/aim");
    private static final ResourceLocation RELOAD_ANIMATION = GunsRPG.makeResource("m1911/reload");
    private static final ResourceLocation UNJAM = GunsRPG.makeResource("m1911/unjam");

    public M1911Item(String name) {
        super(name, new Properties().setISTER(() -> M1911Renderer::new).durability(550));
    }

    @Override
    public void initializeWeapon(WeaponBuilder builder) {
        builder
                .config(GunsRPG.config.weapon.m1911)
                .ammo(WeaponCategory.PISTOL)
                    .define(AmmoMaterials.WOOD, 0)
                    .define(AmmoMaterials.STONE, 1)
                    .define(AmmoMaterials.IRON, 3)
                    .define(AmmoMaterials.LAPIS, 2)
                    .define(AmmoMaterials.GOLD, 4)
                    .define(AmmoMaterials.REDSTONE, 3)
                    .define(AmmoMaterials.EMERALD, 6)
                    .define(AmmoMaterials.QUARTZ, 5)
                    .define(AmmoMaterials.DIAMOND, 8)
                    .define(AmmoMaterials.AMETHYST, 11)
                    .define(AmmoMaterials.NETHERITE, 13)
                .build();
    }

    @Override
    public boolean isSilenced(PlayerEntity player) {
        return PlayerData.hasActiveSkill(player, Skills.M1911_SUPPRESSOR);
    }

    @Override
    public int getUnjamTime(ItemStack stack) {
        return 50;
    }

    @Override
    public void onHitEntity(AbstractProjectile bullet, LivingEntity victim, ItemStack stack, LivingEntity shooter) {
        if (shooter instanceof PlayerEntity && PlayerData.hasActiveSkill((PlayerEntity) shooter, Skills.M1911_HEAVY_BULLETS)) {
            victim.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 100, 1, false, false));
            victim.addEffect(new EffectInstance(Effects.WEAKNESS, 100, 0, false, false));
        }
    }

    @Override
    public void onKillEntity(AbstractProjectile bullet, LivingEntity victim, ItemStack stack, LivingEntity shooter) {
        if (!(shooter instanceof PlayerEntity)) return;
        PlayerEntity player = (PlayerEntity) shooter;
        PlayerData.get(player).ifPresent(data -> {
            ISkillProvider skillProvider = data.getSkillProvider();
            IAttributeProvider attributeProvider = data.getAttributes();
            KillingSpreeSkill killingSpreeSkill = skillProvider.getSkill(Skills.M1911_KILLING_SPREE);
            if (killingSpreeSkill != null) {
                IAttributeId id = isSilenced(player) ? Attribs.SILENT_WEAPON_DAMAGE : Attribs.LOUD_WEAPON_DAMAGE;
                killingSpreeSkill.applyBonus(attributeProvider, id);
            }
        });
    }

    @Override
    protected SoundEvent getShootSound(PlayerEntity entity) {
        return isSilenced(entity) ? ModSounds.M1911_SILENT : ModSounds.M1911;
    }

    @Override
    public float modifyProjectileDamage(AbstractProjectile projectile, LivingEntity entity, PlayerEntity shooter, float damage) {
        ItemStack weapon = projectile.getWeaponSource();
        if (weapon.getItem() instanceof GunItem && PlayerData.hasActiveSkill(shooter, Skills.M1911_EVERY_BULLET_COUNTS)) {
            int ammo = this.getAmmo(weapon);
            if (ammo == 0) {
                return damage * SkillUtil.EVERY_BULLET_COUNTS_DAMAGE;
            }
        }
        return damage;
    }

    @Override
    protected SoundEvent getEntityShootSound(LivingEntity entity) {
        return ModSounds.M9;
    }

    @Override
    public int getMaxAmmo(IAttributeProvider provider) {
        return provider.getAttribute(Attribs.M1911_MAG_CAPACITY).intValue();
    }

    @Override
    public int getFirerate(IAttributeProvider provider) {
        return provider.getAttribute(Attribs.M1911_FIRERATE).intValue();
    }

    @Override
    public int getReloadTime(IAttributeProvider provider, ItemStack stack) {
        return Attribs.M1911_RELOAD.intValue(provider);
    }

    @Override
    public double getNoiseMultiplier(IAttributeProvider provider) {
        return Attribs.M1911_LOUDNESS.value(provider);
    }

    @Override
    public float getVerticalRecoil(IAttributeProvider provider) {
        return Attribs.M1911_VERTICAL.floatValue(provider);
    }

    @Override
    public float getHorizontalRecoil(IAttributeProvider provider) {
        return Attribs.M1911_HORIZONTAL.floatValue(provider);
    }

    @Override
    public SkillType<?> getRequiredSkill() {
        return Skills.M1911_ASSEMBLY;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ResourceLocation getAimAnimationPath(ItemStack stack, PlayerEntity player) {
        return AIM_ANIMATION;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ResourceLocation getBulletEjectAnimationPath() {
        return EJECT;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ResourceLocation getUnjamAnimationPath() {
        return UNJAM;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ResourceLocation getReloadAnimation(PlayerEntity player) {
        return RELOAD_ANIMATION;
    }

    @Override
    public IRenderConfig left() {
        return RenderConfigs.M1911_LEFT;
    }

    @Override
    public IRenderConfig right() {
        return RenderConfigs.M1911_RIGHT;
    }
}
