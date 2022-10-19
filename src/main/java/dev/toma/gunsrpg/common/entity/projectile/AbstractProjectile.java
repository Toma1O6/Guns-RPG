package dev.toma.gunsrpg.common.entity.projectile;

import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.init.ModDamageSources;
import dev.toma.gunsrpg.common.init.ModSounds;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.config.ModConfig;
import dev.toma.gunsrpg.util.properties.Properties;
import dev.toma.gunsrpg.util.properties.PropertyContext;
import dev.toma.gunsrpg.util.properties.PropertyKey;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SPlaySoundEffectPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.entity.PartEntity;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.*;
import java.util.function.Consumer;

public abstract class AbstractProjectile extends ProjectileEntity implements IEntityAdditionalSpawnData {

    protected final PropertyContext propertyContext = PropertyContext.create();
    private final Set<UUID> passedPlayers;
    private final ItemStack weapon;
    private float projectileDamage;
    private float velocity;
    private int delay;
    private boolean supersonic;

    public AbstractProjectile(EntityType<? extends AbstractProjectile> type, World level) {
        super(type, level);
        this.passedPlayers = Collections.emptySet();
        this.weapon = ItemStack.EMPTY;
    }

    public AbstractProjectile(EntityType<? extends AbstractProjectile> type, World world, LivingEntity owner) {
        super(type, world);
        this.passedPlayers = new HashSet<>();
        this.setOwner(owner);
        this.passedPlayers.add(owner.getUUID()); // owner will never hear bullet whizz
        this.weapon = owner.getMainHandItem();
        this.noPhysics = true;
        Vector3d position = owner.position();
        setPos(position.x, position.y + owner.getEyeHeight(), position.z);
    }

    @Override
    public abstract boolean shouldRenderAtSqrDistance(double distance);

    public abstract void preTick();

    public abstract void postTick();

    @Override
    protected abstract void onHitBlock(BlockRayTraceResult result);

    @Override
    protected abstract void onHitEntity(EntityRayTraceResult result);

    /* Public methods */

    public boolean allowBaseTick() {
        return true;
    }

    public void setup(float damage, float velocity, int delay) {
        this.projectileDamage = damage;
        this.velocity = velocity;
        this.delay = delay;
        this.supersonic = velocity > 10.0F;
        onDamageChanged();
    }

    public void fire(float xRot, float yRot, float inaccuracy) {
        float inX = 0.0F;
        float inY = 0.0F;
        if (inaccuracy != 0.0) {
            float base = 3.5F;
            float x = (random.nextFloat() - random.nextFloat()) * inaccuracy;
            float y = (random.nextFloat() - random.nextFloat()) * inaccuracy;
            inX = base * x;
            inY = base * y;
        }
        Vector3d vector3d = Vector3d.directionFromRotation(xRot + inX, yRot + inY);
        setDeltaMovement(vector3d.multiply(velocity, velocity, velocity));
        this.yRot = yRot + inY;
        updateDirection();
        ifShotByPlayer(this::aggroNearby);
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public void checkForCollisions(Vector3d v1, Vector3d v2) {
        RayTraceResult result = level.clip(new RayTraceContext(v1, v2, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this));
        if (result.getType() != RayTraceResult.Type.MISS) {
            v2 = result.getLocation();
        }
        EntityRayTraceResult entityHit = traceEntity(v1, v2);
        if (entityHit != null) {
            result = entityHit;
        }
        if (result != null) {
            RayTraceResult.Type type = result.getType();
            if (type == RayTraceResult.Type.ENTITY) {
                this.onHitEntity((EntityRayTraceResult) result);
            } else if (type == RayTraceResult.Type.BLOCK) {
                this.onHitBlock((BlockRayTraceResult) result);
            }
        }
    }

    public EntityRayTraceResult traceEntity(Vector3d v1, Vector3d v2) {
        return ProjectileHelper.getEntityHitResult(level, this, v1, v2, getBoundingBox().expandTowards(getDeltaMovement()).inflate(1.0), this::canHitEntity);
    }

    public float getProjectileDamage() {
        return projectileDamage;
    }

    @Override
    public void tick() {
        preTick();
        if (allowBaseTick())
            super.tick();
        updateDirection();
        Vector3d v1 = position();
        Vector3d v2 = v1.add(getDeltaMovement());
        checkForCollisions(v1, v2);
        if (supersonic)
            passAround();
        postTick();
        Vector3d delta = this.getDeltaMovement();
        double x = this.getX() + delta.x;
        double y = this.getY() + delta.y;
        double z = this.getZ() + delta.z;
        this.setPos(x, y, z);
    }

    public ItemStack getWeaponSource() {
        return weapon;
    }

    @Override
    public void writeSpawnData(PacketBuffer buffer) {
        Vector3d position = this.position();
        Vector3d delta = this.getDeltaMovement();
        buffer.writeDouble(position.x);
        buffer.writeDouble(position.y);
        buffer.writeDouble(position.z);
        buffer.writeDouble(delta.x);
        buffer.writeDouble(delta.y);
        buffer.writeDouble(delta.z);
        buffer.writeFloat(yRot);
        propertyContext.encode(buffer);
    }

    @Override
    public void readSpawnData(PacketBuffer buffer) {
        double px = buffer.readDouble();
        double py = buffer.readDouble();
        double pz = buffer.readDouble();
        double dx = buffer.readDouble();
        double dy = buffer.readDouble();
        double dz = buffer.readDouble();
        yRot = buffer.readFloat();
        propertyContext.decode(buffer);
        setPos(px, py, pz);
        setDeltaMovement(dx, dy, dz);
    }

    public <V> V getProperty(PropertyKey<V> key) {
        return propertyContext.getProperty(key);
    }

    public <V> void setProperty(PropertyKey<V> key, V value) {
        propertyContext.setProperty(key, value);
    }

    public void addProperties(PropertyContext context) {
        context.moveContents(propertyContext);
    }

    /* Protected methods */

    @Override
    protected void defineSynchedData() {}

    protected boolean canApplyGravity() {
        return tickCount >= delay;
    }

    protected void applyGravity(float amount) {
        setDeltaMovement(getDeltaMovement().add(0.0, -amount, 0.0));
    }

    protected void applyGravity() {
        if (this.canApplyGravity()) {
            applyGravity(0.05F);
        }
    }

    protected void reduceDamage(float amount) {
        this.projectileDamage -= amount;
        onDamageChanged();
    }

    protected void mulDamage(float f) {
        projectileDamage *= f;
        onDamageChanged();
    }

    /* Internal methods */

    /**
     * Updates projectile direction
     */
    protected void updateDirection() {
        Vector3d delta = getDeltaMovement();
        double dx = delta.x;
        double dy = delta.y;
        double dz = delta.z;
        float motionSqrt = MathHelper.sqrt(dx * dx + dz * dz);
        xRot = -(float) (MathHelper.atan2(dy, motionSqrt) * (180.0F / Math.PI));
        xRotO = xRot;
    }

    protected DamageSource getDamageSource(Entity owner) {
        int lootingLevel = this.getProperty(Properties.LOOT_LEVEL);
        return lootingLevel > 0 ? ModDamageSources.dealSpecialWeaponDamage(owner, this, weapon) : ModDamageSources.dealWeaponDamage(owner, this, weapon);
    }

    /**
     * Executes specific action when projectile owner is player
     * @param event Event with player parameter
     */
    void ifShotByPlayer(Consumer<PlayerEntity> event) {
        Entity entity = getOwner();
        if (entity instanceof PlayerEntity) {
            event.accept((PlayerEntity) entity);
        }
    }

    /**
     * Checks whether entity is tall enough for headshot damage
     * @param victim Entity being tested
     * @return If entity is valid for headshots
     */
    boolean canHeadshotEntity(Entity victim) {
        EntitySize size = victim.getDimensionsForge(victim.getPose());
        double ratio = size.height / size.width;
        return ratio > 1.0;
    }

    protected void hurtTarget(Entity entity, Entity owner) {
        propertyContext.handleConditionally(Properties.IS_HEADSHOT, value -> value, headshot -> mulDamage(this.getHeadshotMultiplier()));
        if (weapon.getItem() instanceof GunItem) {
            if (entity instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity) entity;
                GunItem gun = (GunItem) weapon.getItem();
                if (owner instanceof PlayerEntity) {
                    projectileDamage = gun.modifyProjectileDamage(this, livingEntity, (PlayerEntity) owner, projectileDamage);
                }
                livingEntity.hurt(this.getDamageSource(owner), projectileDamage);
                if (livingEntity.isDeadOrDying()) {
                    gun.onKillEntity(this, livingEntity, weapon, (LivingEntity) owner);
                } else {
                    gun.onHitEntity(this, livingEntity, weapon, (LivingEntity) owner);
                }
            } else if (entity instanceof PartEntity<?>) {
                DamageSource source = ModDamageSources.dealSpecialWeaponDamage(owner, this, weapon);
                entity.hurt(source, projectileDamage);
            }
        } else {
            entity.hurt(ModDamageSources.dealSpecialWeaponDamage(owner, entity, weapon), projectileDamage);
        }
    }

    /* Private methods */

    private void passAround() {
        if (!level.isClientSide) {
            ServerWorld serverLevel = (ServerWorld) level;
            Vector3d pos = position();
            List<ServerPlayerEntity> players = serverLevel.getEntitiesOfClass(ServerPlayerEntity.class, getBoundingBox().inflate(3.0F), pl -> !passedPlayers.contains(pl.getUUID()));
            for (ServerPlayerEntity player : players) {
                passedPlayers.add(player.getUUID());
                player.connection.send(new SPlaySoundEffectPacket(ModSounds.BULLET_WHIZZ, SoundCategory.MASTER, pos.x, pos.y, pos.z, 0.2F, 1.0F));
            }
        }
    }

    protected void aggroNearby(PlayerEntity player) {
        if (player.isCreative() || player.isSpectator())
            return;
        PlayerData.get(player).ifPresent(data -> {
            double mobScareRange = ModConfig.worldConfig.shootingMobAggroRange.get();
            GunItem gun = (GunItem) weapon.getItem();
            double noiseMultiplier = gun.getNoiseMultiplier(data.getAttributes());
            double actualScareRange = mobScareRange * noiseMultiplier;
            Entity owner = getOwner();
            List<MonsterEntity> list = level.getEntitiesOfClass(MonsterEntity.class, getBoundingBox().inflate(actualScareRange), ent -> ent != owner);
            for (MonsterEntity entity : list) {
                entity.setTarget((PlayerEntity) owner);
            }
        });
    }

    private float getHeadshotMultiplier() {
        Entity src = getOwner();
        if (src instanceof PlayerEntity && weapon.getItem() instanceof GunItem) {
            LazyOptional<IPlayerData> optional = PlayerData.get((PlayerEntity) src);
            if (optional.isPresent()) {
                IPlayerData data = optional.orElse(null);
                GunItem item = (GunItem) weapon.getItem();
                return (float) item.getHeadshotMultiplier(data.getAttributes());
            }
        }
        return 1.0f;
    }

    protected void onDamageChanged() {
        if (projectileDamage <= 0.0F && !level.isClientSide) {
            remove();
        }
    }
}
