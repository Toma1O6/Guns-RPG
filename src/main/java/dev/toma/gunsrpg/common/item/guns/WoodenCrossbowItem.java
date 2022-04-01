package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.render.RenderConfigs;
import dev.toma.gunsrpg.client.render.item.WoodenCrossbowRenderer;
import dev.toma.gunsrpg.common.attribute.Attribs;
import dev.toma.gunsrpg.api.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.entity.projectile.AbstractProjectile;
import dev.toma.gunsrpg.common.init.ModSounds;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterials;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponBuilder;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponCategory;
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
import net.minecraft.util.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class WoodenCrossbowItem extends GunItem {

    private static final ResourceLocation[] AIM_ANIMATIONS = {
            GunsRPG.makeResource("wooden_crossbow/aim"),
            GunsRPG.makeResource("wooden_crossbow/aim_scoped")
    };
    private static final ResourceLocation RELOAD_ANIMATION = GunsRPG.makeResource("wooden_crossbow/reload");
    private static final ResourceLocation UNJAM = GunsRPG.makeResource("wooden_crossbow/unjam");

    public WoodenCrossbowItem(String name) {
        super(name, new Properties().setISTER(() -> WoodenCrossbowRenderer::new).durability(350));
    }

    @Override
    public void initializeWeapon(WeaponBuilder builder) {
        builder
                .category(WeaponCategory.CROSSBOW)
                .config(ModConfig.weaponConfig.crossbow)
                .ammo()
                    .define(AmmoMaterials.WOOD, 0)
                    .define(AmmoMaterials.STONE, 3)
                    .define(AmmoMaterials.IRON, 6)
                    .define(AmmoMaterials.LAPIS, 5)
                    .define(AmmoMaterials.GOLD, 9)
                    .define(AmmoMaterials.REDSTONE, 7)
                    .define(AmmoMaterials.EMERALD, 14)
                    .define(AmmoMaterials.QUARTZ, 11)
                    .define(AmmoMaterials.DIAMOND, 16)
                    .define(AmmoMaterials.AMETHYST, 20)
                    .define(AmmoMaterials.NETHERITE, 25)
                .build();

        ScopeDataRegistry.getRegistry().register(this, 25.0F, 0.4F);
    }

    @Override
    public int getUnjamTime(ItemStack stack) {
        return 60;
    }

    @Override
    public int getMaxAmmo(IAttributeProvider provider) {
        return provider.getAttribute(Attribs.CROSSBOW_MAG_CAPACITY).intValue();
    }

    @Override
    public int getFirerate(IAttributeProvider provider) {
        return 10;
    }

    @Override
    public int getReloadTime(IAttributeProvider provider, ItemStack stack) {
        return Attribs.CROSSBOW_RELOAD.intValue(provider);
    }

    @Override
    public float getVerticalRecoil(IAttributeProvider provider) {
        return 0.1F * super.getVerticalRecoil(provider);
    }

    @Override
    public float getHorizontalRecoil(IAttributeProvider provider) {
        return 0.1F * super.getHorizontalRecoil(provider);
    }

    @Override
    public void onHitEntity(AbstractProjectile bullet, LivingEntity victim, ItemStack stack, LivingEntity shooter) {
        if (!bullet.level.isClientSide && shooter instanceof PlayerEntity && PlayerData.hasActiveSkill((PlayerEntity) shooter, Skills.CROSSBOW_POISONED_BOLTS)) {
            victim.addEffect(new EffectInstance(Effects.WITHER, 140, 1, false, false));
        }
    }

    @Override
    public void onKillEntity(AbstractProjectile bullet, LivingEntity victim, ItemStack stack, LivingEntity shooter) {
        if (!bullet.level.isClientSide && shooter instanceof PlayerEntity && PlayerData.hasActiveSkill((PlayerEntity) shooter, Skills.CROSSBOW_HUNTER)) {
            shooter.heal(4.0F);
        }
    }

    @Override
    protected SoundEvent getShootSound(PlayerEntity entity) {
        return ModSounds.CROSSBOW_SHOOT;
    }

    @Override
    protected SoundEvent getEntityShootSound(LivingEntity entity) {
        return ModSounds.CROSSBOW_SHOOT;
    }

    @Override
    public SkillType<?> getRequiredSkill() {
        return Skills.CROSSBOW_ASSEMBLY;
    }

    @Override
    public IRenderConfig left() {
        return RenderConfigs.CROSSBOW_LEFT;
    }

    @Override
    public IRenderConfig right() {
        return RenderConfigs.CROSSBOW_RIGHT;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ResourceLocation getAimAnimationPath(ItemStack stack, PlayerEntity player) {
        return AIM_ANIMATIONS[PlayerData.hasActiveSkill(player, Skills.CROSSBOW_SCOPE) ? 1 : 0];
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

    @OnlyIn(Dist.CLIENT)
    @Override
    public void onShoot(PlayerEntity player, ItemStack stack) {
    }
}
