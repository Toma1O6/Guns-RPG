package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.ModTabs;
import dev.toma.gunsrpg.api.common.IAmmoMaterial;
import dev.toma.gunsrpg.api.common.IJamConfig;
import dev.toma.gunsrpg.api.common.IReloadManager;
import dev.toma.gunsrpg.api.common.IWeaponConfig;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.client.animation.BulletEjectAnimation;
import dev.toma.gunsrpg.client.animation.ModAnimations;
import dev.toma.gunsrpg.common.IShootProps;
import dev.toma.gunsrpg.common.attribute.Attribs;
import dev.toma.gunsrpg.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.entity.projectile.AbstractProjectile;
import dev.toma.gunsrpg.common.entity.projectile.Bullet;
import dev.toma.gunsrpg.common.entity.projectile.PenetrationData;
import dev.toma.gunsrpg.common.init.ModEntities;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoType;
import dev.toma.gunsrpg.common.item.guns.ammo.IMaterialData;
import dev.toma.gunsrpg.common.item.guns.ammo.IMaterialDataContainer;
import dev.toma.gunsrpg.common.item.guns.reload.ReloadManagers;
import dev.toma.gunsrpg.common.item.guns.setup.AbstractGun;
import dev.toma.gunsrpg.common.item.guns.setup.MaterialContainer;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponBuilder;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponCategory;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import lib.toma.animations.AnimationUtils;
import lib.toma.animations.api.AnimationList;
import lib.toma.animations.api.IAnimationEntry;
import lib.toma.animations.api.IRenderConfig;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;
import java.util.Set;

public abstract class GunItem extends AbstractGun implements IAnimationEntry {

    private final WeaponCategory weaponCategory;
    private final IWeaponConfig config;
    private final MaterialContainer container;
    private final AmmoType ammoType;

    public GunItem(String name, Properties properties) {
        super(name, properties.tab(ModTabs.ITEM_TAB));
        WeaponBuilder builder = new WeaponBuilder();
        initializeWeapon(builder);
        builder.validate();
        this.weaponCategory = builder.getWeaponCategory();
        this.config = builder.getConfig();
        this.container = builder.getMaterialContainer();
        this.ammoType = builder.getAmmoType();
    }

    /* ABSTRACT METHODS ------------------------------------------------------ */

    public abstract void initializeWeapon(WeaponBuilder builder);

    public abstract SkillType<?> getRequiredSkill();

    /* PROPERTIES FOR OVERRIDES ---------------------------------------------- */

    public int getUnjamTime(ItemStack stack, IPlayerData data) {
        return 20;
    }

    public float getWeaponDamage(ItemStack stack) {
        IWeaponConfig config = getWeaponConfig();
        float base = config.getDamage();
        return base + getDamageBonus(stack);
    }

    public int getMaxAmmo(IAttributeProvider provider) {
        return 1;
    }

    public int getReloadTime(IAttributeProvider provider) {
        return provider.getAttribute(Attribs.RELOAD_SPEED).intValue();
    }

    public int getFirerate(IAttributeProvider provider) {
        return 1;
    }

    public float getVerticalRecoil(IAttributeProvider provider) {
        return provider.getAttribute(Attribs.RECOIL_CONTROL).floatValue();
    }

    public float getHorizontalRecoil(IAttributeProvider provider) {
        return provider.getAttribute(Attribs.RECOIL_CONTROL).floatValue();
    }

    public double getNoiseMultiplier(IAttributeProvider provider) {
        return provider.getAttributeValue(Attribs.WEAPON_NOISE);
    }

    public double getHeadshotMultiplier(IAttributeProvider provider) {
        return provider.getAttributeValue(Attribs.HEADSHOT_DAMAGE);
    }

    public boolean switchFiremode(ItemStack stack, PlayerEntity player) {
        return false;
    }

    public void onHitEntity(AbstractProjectile bullet, LivingEntity victim, ItemStack stack, LivingEntity shooter) {
    }

    public void onKillEntity(AbstractProjectile bullet, LivingEntity victim, ItemStack stack, LivingEntity shooter) {
    }

    protected SoundEvent getShootSound(PlayerEntity entity) {
        return SoundEvents.LEVER_CLICK;
    }

    protected SoundEvent getEntityShootSound(LivingEntity entity) {
        return SoundEvents.LEVER_CLICK;
    }

    public IReloadManager getReloadManager(PlayerEntity player, IAttributeProvider attributeProvider) {
        return ReloadManagers.fullMagLoading();
    }

    public void shootProjectile(World level, LivingEntity shooter, ItemStack stack, IShootProps props) {
        Bullet bullet = new Bullet(ModEntities.BULLET.get(), level, shooter);
        IWeaponConfig config = this.getWeaponConfig();
        float damage = this.getWeaponDamage(stack) * props.getDamageMultiplier();
        float velocity = config.getVelocity();
        int delay = config.getGravityDelay();
        bullet.setup(damage, velocity, delay);
        bullet.fire(shooter.xRot, shooter.yRot, props.getInaccuracy());
        if (shooter instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) shooter;
            PlayerData.get(player).ifPresent(data -> {
                PenetrationData penetrationData = getPenetrationData(data);
                if (penetrationData != null) {
                    bullet.setPenetrationData(penetrationData);
                }
            });
        }
        level.addFreshEntity(bullet);
    }

    /* FINAL METHODS ---------------------------------------------------------------- */

    public final IWeaponConfig getWeaponConfig() {
        return config;
    }

    public final SoundEvent getWeaponShootSound(LivingEntity entity) {
        if (entity instanceof PlayerEntity) {
            return getShootSound((PlayerEntity) entity);
        }
        return getEntityShootSound(entity);
    }

    public final void shoot(World world, LivingEntity entity, ItemStack stack, IShootProps props) {
        shoot(world, entity, stack, props, getWeaponShootSound(entity));
    }

    public final void shoot(World world, LivingEntity entity, ItemStack stack, IShootProps props, SoundEvent event) {
        if (stack.getDamageValue() >= stack.getMaxDamage()) {
            GunsRPG.log.warn("{} has tried to shoot with weapon which has no durability", entity.getName().getString());
            return;
        }
        shootProjectile(world, entity, stack, props);
        if (entity instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) entity;
            Random random = world.getRandom();
            AmmoType type = getAmmoType();
            IAmmoMaterial material = getMaterialFromNBT(stack);
            IMaterialDataContainer container = type.getContainer();
            IMaterialData materialData = container.getMaterialData(material);
            float noDamageChance = 1.0F - materialData.getAddedDurability();
            if (random.nextFloat() >= noDamageChance) {
                stack.hurt(1, world.getRandom(), player);
            }
            IWeaponConfig config = this.getWeaponConfig();
            IJamConfig jamConfig = config.getJamConfig();
            float jamChance = jamConfig.getJamChance(stack) + materialData.getAddedJamChance();
            if (entity.getRandom().nextFloat() <= jamChance) {
                setJammedState(stack, true);
            }
        }
        this.setAmmoCount(stack, this.getAmmo(stack) - 1);
        world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), event, SoundCategory.MASTER, 15.0F, 1.0F);
    }

    public final AmmoType getAmmoType() {
        return ammoType;
    }

    public final WeaponCategory getWeaponCategory() {
        return weaponCategory;
    }

    public final Set<IAmmoMaterial> getCompatibleMaterials() {
        return container.getCompatible();
    }

    @Override
    public final MaterialContainer getContainer() {
        return container;
    }

    protected boolean isSilenced(PlayerEntity player) {
        return PlayerData.getValueSafe(player, data -> getNoiseMultiplier(data.getAttributes()) <= 0.2F, false);
    }

    /* CLIENT-SIDE STUFF -------------------------------------------------------------------------- */

    @OnlyIn(Dist.CLIENT)
    public abstract ResourceLocation getReloadAnimation(PlayerEntity player);

    @OnlyIn(Dist.CLIENT)
    public ResourceLocation getAimAnimationPath(ItemStack stack, PlayerEntity player) {
        return null;
    }

    @OnlyIn(Dist.CLIENT)
    public ResourceLocation getBulletEjectAnimationPath() {
        return null;
    }

    @OnlyIn(Dist.CLIENT)
    public ResourceLocation getUnjamAnimationPath() {
        return null;
    }

    @OnlyIn(Dist.CLIENT)
    public void onShoot(PlayerEntity player, ItemStack stack) {
        ResourceLocation bulletEjectPath = getBulletEjectAnimationPath();
        BulletEjectAnimation animation = AnimationUtils.createAnimation(bulletEjectPath, BulletEjectAnimation::new);
        AnimationList.enqueue(ModAnimations.BULLET_EJECTION, animation);
    }

    @Override
    public IRenderConfig right() {
        return IRenderConfig.empty();
    }

    @Override
    public IRenderConfig left() {
        return IRenderConfig.empty();
    }

    @Override
    public boolean disableVanillaAnimations() {
        return true;
    }

    @Override
    public int getRGBDurabilityForDisplay(ItemStack stack) {
        return 0xFFFF;
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return true;
    }
}
