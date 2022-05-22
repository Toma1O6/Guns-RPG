package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.IReloadManager;
import dev.toma.gunsrpg.api.common.IWeaponConfig;
import dev.toma.gunsrpg.api.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.client.render.RenderConfigs;
import dev.toma.gunsrpg.client.render.item.GrenadeLauncherRenderer;
import dev.toma.gunsrpg.common.attribute.Attribs;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.entity.projectile.AbstractProjectile;
import dev.toma.gunsrpg.common.entity.projectile.Grenade;
import dev.toma.gunsrpg.common.init.ModEntities;
import dev.toma.gunsrpg.common.init.ModSounds;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterials;
import dev.toma.gunsrpg.common.item.guns.reload.ReloadManagers;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponBuilder;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponCategory;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.config.ModConfig;
import lib.toma.animations.api.IRenderConfig;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

import static dev.toma.gunsrpg.util.properties.Properties.EXPLOSION_POWER;

public class GrenadeLauncherItem extends AbstractExplosiveLauncher {

    private static final ResourceLocation RELOAD = GunsRPG.makeResource("gl/reload");
    private static final ResourceLocation LOAD_BULLET = GunsRPG.makeResource("gl/load_bullet");
    private static final ResourceLocation UNJAM = GunsRPG.makeResource("gl/unjam");
    private static final ResourceLocation AIM = GunsRPG.makeResource("gl/aim");

    public GrenadeLauncherItem(String name) {
        super(name, new Properties().setISTER(() -> GrenadeLauncherRenderer::new).durability(220));
    }

    @Override
    public void initializeWeapon(WeaponBuilder builder) {
        builder
                .config(ModConfig.weaponConfig.grenadeLauncher)
                .ammo(WeaponCategory.GRENADE_LAUNCHER)
                    .define(AmmoMaterials.GRENADE)
                    .define(AmmoMaterials.TEAR_GAS)
                    .define(AmmoMaterials.STICKY)
                    .define(AmmoMaterials.HE_GRENADE)
                    .define(AmmoMaterials.IMPACT)
                .build();
    }

    @Override
    protected SoundEvent getShootSound(PlayerEntity entity) {
        return ModSounds.GL_SHOT1;
    }

    @Override
    public int getMaxAmmo(IAttributeProvider provider) {
        return provider.getAttribute(Attribs.GL_MAG_CAPACITY).intValue();
    }

    @Override
    public int getReloadTime(IAttributeProvider provider, ItemStack stack) {
        return Attribs.GL_RELOAD.intValue(provider);
    }

    @Override
    public IReloadManager getReloadManager(PlayerEntity player, IAttributeProvider attributeProvider) {
        int prepTime = (int) Attribs.GL_RELOAD.getModifiedValue(attributeProvider, 30);
        return ReloadManagers.singleBulletLoading(prepTime, player, this, player.getMainHandItem(), LOAD_BULLET);
    }

    @Override
    public int getFirerate(IAttributeProvider provider) {
        return provider.getAttribute(Attribs.GL_FIRERATE).intValue();
    }

    @Override
    public int getUnjamTime(ItemStack stack) {
        return 75;
    }

    @Override
    protected float getInitialVelocity(IWeaponConfig config, LivingEntity shooter) {
        float velocity = super.getInitialVelocity(config, shooter);
        if (shooter instanceof PlayerEntity && PlayerData.hasActiveSkill((PlayerEntity) shooter, Skills.GRENADE_LAUNCHER_BETTER_CARTRIDGE)) {
            velocity *= 1.6f;
        }
        return velocity;
    }

    @Override
    protected void prepareForShooting(AbstractProjectile projectile, LivingEntity shooter) {
        super.prepareForShooting(projectile, shooter);
        if (shooter instanceof PlayerEntity && PlayerData.hasActiveSkill((PlayerEntity) shooter, Skills.GRENADE_LAUNCHER_DEMOLITION_EXPERT)) {
            projectile.setProperty(EXPLOSION_POWER, 1);
        }
    }

    @Override
    protected AbstractProjectile makeProjectile(World level, LivingEntity shooter) {
        return new Grenade(ModEntities.GRENADE_SHELL.get(), level, shooter);
    }

    @Override
    public SkillType<?> getRequiredSkill() {
        return Skills.GRENADE_LAUNCHER_ASSEMBLY;
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
        return AIM;
    }

    @Override
    public void onShoot(PlayerEntity player, ItemStack stack) {
    }

    @Override
    public IRenderConfig left() {
        return RenderConfigs.M203_LEFT;
    }

    @Override
    public IRenderConfig right() {
        return RenderConfigs.M203_RIGHT;
    }
}
