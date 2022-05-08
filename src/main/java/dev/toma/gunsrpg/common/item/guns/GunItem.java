package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.ModTabs;
import dev.toma.gunsrpg.api.common.IAmmoMaterial;
import dev.toma.gunsrpg.api.common.IJamConfig;
import dev.toma.gunsrpg.api.common.IReloadManager;
import dev.toma.gunsrpg.api.common.IWeaponConfig;
import dev.toma.gunsrpg.api.common.attribute.IAttributeId;
import dev.toma.gunsrpg.api.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.client.animation.BulletEjectAnimation;
import dev.toma.gunsrpg.client.animation.ModAnimations;
import dev.toma.gunsrpg.common.IShootProps;
import dev.toma.gunsrpg.common.attribute.Attribs;
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
import dev.toma.gunsrpg.common.item.guns.util.Firemode;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.util.properties.PropertyContext;
import lib.toma.animations.AnimationUtils;
import lib.toma.animations.Easings;
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
import java.util.function.BiFunction;

import static dev.toma.gunsrpg.util.properties.Properties.PENETRATION;
import static dev.toma.gunsrpg.util.properties.Properties.TRACER;

public abstract class GunItem extends AbstractGun implements IAnimationEntry {

    private final WeaponCategory weaponCategory;
    private final IWeaponConfig config;
    private final MaterialContainer container;
    private final AmmoType ammoType;
    private final BiFunction<PlayerEntity, Firemode, Firemode> firemodeSelector;

    public GunItem(String name, Properties properties) {
        super(name, properties.tab(ModTabs.WEAPON_TAB));
        WeaponBuilder builder = new WeaponBuilder();
        initializeWeapon(builder);
        builder.validate();
        this.weaponCategory = builder.getWeaponCategory();
        this.config = builder.getConfig();
        this.container = builder.getMaterialContainer();
        this.ammoType = builder.getAmmoType();
        this.firemodeSelector = builder.getFiremodeSelector();
    }

    /* ABSTRACT METHODS ------------------------------------------------------ */

    public abstract void initializeWeapon(WeaponBuilder builder);

    public abstract SkillType<?> getRequiredSkill();

    /* PROPERTIES FOR OVERRIDES ---------------------------------------------- */

    protected int getUnjamTime(ItemStack stack) {
        return 20;
    }

    public float getWeaponDamage(ItemStack stack, LivingEntity shooter) {
        IWeaponConfig config = getWeaponConfig();
        float base = config.getDamage();
        float damageMultiplier = 1.0F;
        if (shooter instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) shooter;
            IPlayerData data = PlayerData.getUnsafe(player);
            IAttributeProvider provider = data.getAttributes();
            IAttributeId attributeId = isSilenced(player) ? Attribs.SILENT_WEAPON_DAMAGE : Attribs.LOUD_WEAPON_DAMAGE;
            damageMultiplier = provider.getAttribute(attributeId).floatValue();
            WeaponCategory category = this.getWeaponCategory();
            if (category.hasBonusDamage()) {
                IAttributeId categoryId = category.getBonusDamageAttribute();
                damageMultiplier *= provider.getAttribute(categoryId).floatValue();
            }
        }
        return damageMultiplier * (base + getDamageBonus(stack));
    }

    public int getMaxAmmo(IAttributeProvider provider) {
        return 1;
    }

    public int getReloadTime(IAttributeProvider provider, ItemStack stack) {
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

    public final boolean switchFiremode(ItemStack stack, PlayerEntity player) {
        Firemode firemode = getFiremode(stack);
        Firemode next = firemodeSelector.apply(player, firemode);
        if (next != firemode) {
            setFiremode(stack, next);
            return true;
        }
        return false;
    }

    public float modifyProjectileDamage(AbstractProjectile projectile, LivingEntity entity, PlayerEntity shooter, float damage) {
        return damage;
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
        AbstractProjectile projectile = this.makeProjectile(level, shooter);
        PropertyContext context = props.getExtraData();
        projectile.addProperties(context);
        IWeaponConfig config = this.getWeaponConfig();
        float damage = this.getWeaponDamage(stack, shooter) * props.getDamageMultiplier();
        float velocity = this.getInitialVelocity(config, shooter);
        int delay = config.getGravityDelay();
        projectile.setup(damage, velocity, delay);
        projectile.fire(shooter.xRot, shooter.yRot, this.getInaccuracy(props, shooter));
        this.prepareForShooting(projectile, shooter);
        IAmmoMaterial material = this.getMaterialFromNBT(stack);
        if (material != null) {
            Integer tracer = material.getTracerColor();
            if (tracer != null) {
                projectile.setProperty(TRACER, tracer);
            }
        }
        if (shooter instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) shooter;
            PlayerData.get(player).ifPresent(data -> {
                PenetrationData penetrationData = getPenetrationData(data);
                if (penetrationData != null) {
                    projectile.setProperty(PENETRATION, penetrationData);
                }
            });
        }
        level.addFreshEntity(projectile);
    }

    protected float getInaccuracy(IShootProps props, LivingEntity entity) {
        return props.getInaccuracy();
    }

    protected float getInitialVelocity(IWeaponConfig config, LivingEntity shooter) {
        return config.getVelocity();
    }

    protected AbstractProjectile makeProjectile(World level, LivingEntity shooter) {
        return new Bullet(ModEntities.BULLET.get(), level, shooter);
    }

    protected void prepareForShooting(AbstractProjectile projectile, LivingEntity shooter) {

    }

    /* FINAL METHODS ---------------------------------------------------------------- */

    public final int getUnjamTime(ItemStack stack, IAttributeProvider provider) {
        return (int) (this.getUnjamTime(stack) * provider.getAttributeValue(Attribs.UNJAMMING_SPEED));
    }

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
        handleShootProjectileAction(world, entity, stack, props);
        if (entity instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) entity;
            IPlayerData data = PlayerData.getUnsafe(player);
            IAttributeProvider attributeProvider = data.getAttributes();
            Random random = world.getRandom();
            AmmoType type = getAmmoType();
            IAmmoMaterial material = getMaterialFromNBT(stack);
            IMaterialDataContainer container = type.getContainer();
            IWeaponConfig config = this.getWeaponConfig();
            IJamConfig jamConfig = config.getJamConfig();
            float baseJamChance = jamConfig.getJamChance(stack);
            float playerJamChanceMultiplier = this.getModifiedJamChance(attributeProvider.getAttribute(Attribs.JAM_CHANCE).floatValue(), data);
            float damageChance = this.getModifiedDamageChance(attributeProvider.getAttribute(Attribs.WEAPON_DURABILITY).floatValue(), data);
            if (container != null) {
                IMaterialData materialData = container.getMaterialData(material);
                float ammoJamChanceMultiplier = materialData.getAddedJamChance();
                playerJamChanceMultiplier += ammoJamChanceMultiplier;
                damageChance *= 1.0F - materialData.getAddedDurability();
            }
            if (damageChance < 1.0F) {
                if (random.nextFloat() < damageChance) {
                    stack.hurt(1, random, player);
                }
            } else {
                int amount = 1;
                if (random.nextFloat() < damageChance - 1.0F) {
                    amount = Math.min(stack.getMaxDamage() - stack.getDamageValue(), 2);
                }
                stack.hurt(amount, random, player);
            }
            float jamChance = baseJamChance * playerJamChanceMultiplier;
            if (random.nextFloat() <= jamChance) {
                setJammedState(stack, true);
            }
        }
        if (consumeAmmo(stack, entity)) {
            int ammo = this.getAmmo(stack);
            setAmmoCount(stack, ammo - 1);
        }
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

    protected void handleShootProjectileAction(World world, LivingEntity entity, ItemStack stack, IShootProps props) {
        shootProjectile(world, entity, stack, props);
    }

    protected boolean isSilenced(PlayerEntity player) {
        return false;
    }

    protected boolean consumeAmmo(ItemStack stack, LivingEntity consumer) {
        return true;
    }

    protected float getModifiedDamageChance(float damageChance, IPlayerData data) {
        return damageChance;
    }

    protected float getModifiedJamChance(float jamChance, IPlayerData data) {
        return jamChance;
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
    public double getDurabilityForDisplay(ItemStack stack) {
        return 1.0F - getDurability(stack);
    }

    @Override
    public int getRGBDurabilityForDisplay(ItemStack stack) {
        float damage = this.getDurability(stack);
        return getDurabilityColor(1.0F - damage);
    }

    public static int getDurabilityColor(float durability) {
        if (durability < 0.4) {
            float f = durability / 0.4F;
            int blue = (int) ((1.0F - f) * 255);
            return 0xFF << 8 | blue;
        } else {
            float value = Easings.EASE_OUT_CUBIC.ease((durability - 0.4F) / 0.6F);
            int red = (int) (255 * value);
            int green = (int) (255 * (1.0F - value));
            return red << 16 | green << 8;
        }
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return getDurability(stack) < 1.0F;
    }

    private float getDurability(ItemStack stack) {
        float durabilityMultiplier = getDurabilityLimit(stack);
        int stackLimit = stack.getMaxDamage();
        int limit = (int) (stackLimit * durabilityMultiplier);
        int damage = stack.getDamageValue();
        return (stackLimit - damage) / (float) limit;
    }
}
