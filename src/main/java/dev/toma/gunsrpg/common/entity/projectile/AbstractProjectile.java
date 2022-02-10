package dev.toma.gunsrpg.common.entity.projectile;

import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.init.ModDamageSources;
import dev.toma.gunsrpg.common.init.ModSounds;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SPlaySoundEffectPacket;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.entity.PartEntity;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.*;
import java.util.function.Consumer;

public abstract class AbstractProjectile extends ProjectileEntity {

    private final Set<UUID> passedPlayers;

    private final ItemStack weapon;
    private float projectileDamage;
    private float velocity;
    private int delay;
    private boolean supersonic;
    boolean invalid;

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
        move(MoverType.SELF, getDeltaMovement());
    }

    public ItemStack getWeapon() {
        return weapon;
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
        applyGravity(0.05F);
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
        float motionSqrt = MathHelper.sqrt(delta.x * delta.x + delta.z * delta.z);
        yRot = (float) (MathHelper.atan2(delta.x, delta.z) * (180.0F / Math.PI));
        xRot = (float) (MathHelper.atan2(delta.y, motionSqrt) * (180.0F / Math.PI));
        yRotO = yRot;
        xRotO = xRot;
    }

    /**
     * Marks projectile as ready for removal
     */
    void markInvalid() {
        invalid = true;
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
     * Checks whether entity is high enough for headshot damage
     * @param victim Entity being tested
     * @return If entity is valid for headshots
     */
    boolean canHeadshotEntity(Entity victim) {
        EntitySize size = victim.getDimensions(victim.getPose());
        double ratio = size.height / size.width;
        return ratio > 1.0F;
    }

    void applyWeaponEffects(Entity entity, Entity owner, boolean living, boolean headshot) {
        float damage = headshot ? projectileDamage * getHeadshotMultiplier() : projectileDamage;
        if (living) {
            LivingEntity livingEntity = (LivingEntity) entity;
            boolean willDie = livingEntity.getHealth() - projectileDamage <= 0.0F;
            livingEntity.hurt(ModDamageSources.dealWeaponDamage(owner, this, weapon), damage);
            if (weapon.getItem() instanceof GunItem) {
                GunItem gun = (GunItem) weapon.getItem();
                if (willDie)
                    gun.onKillEntity(this, livingEntity, weapon, (LivingEntity) owner);
                else
                    gun.onHitEntity(this, livingEntity, weapon, (LivingEntity) owner);
            }
        } else if (entity instanceof PartEntity<?>) {
            entity.hurt(ModDamageSources.dealWeaponDamage(owner, this, weapon), damage);
        }
    }

    /* Private methods */

    private void passAround() {
        if (!level.isClientSide) {
            ServerWorld serverLevel = (ServerWorld) level;
            Vector3d pos = position();
            List<ServerPlayerEntity> players = serverLevel.getEntitiesOfClass(ServerPlayerEntity.class, getBoundingBox().inflate(6.0F), pl -> !passedPlayers.contains(pl.getUUID()));
            for (ServerPlayerEntity player : players) {
                passedPlayers.add(player.getUUID());
                player.connection.send(new SPlaySoundEffectPacket(ModSounds.BULLET_WHIZZ, SoundCategory.MASTER, pos.x, pos.y, pos.z, 0.6F, 1.0F));
            }
        }
    }

    private void aggroNearby(PlayerEntity player) {
        PlayerData.get(player).ifPresent(data -> {
            double mobScareRange = 25.0;
            GunItem gun = (GunItem) weapon.getItem();
            double noiseMultiplier = gun.getNoiseMultiplier(data.getAttributes());
            double actualScareRange = mobScareRange * noiseMultiplier;
            Entity owner = getOwner();
            List<MobEntity> list = level.getEntitiesOfClass(MobEntity.class, getBoundingBox().inflate(actualScareRange), ent -> ent != owner);
            for (MobEntity mobEntity : list) {
                mobEntity.setTarget((PlayerEntity) owner);
            }
        });
    }

    private float getHeadshotMultiplier() {
        Entity src = getOwner();
        if (src instanceof PlayerEntity) {
            LazyOptional<IPlayerData> optional = PlayerData.get((PlayerEntity) src);
            if (optional.isPresent()) {
                IPlayerData data = optional.orElse(null);
                GunItem item = (GunItem) weapon.getItem();
                return (float) item.getHeadshotMultiplier(data.getAttributes());
            }
        }
        return 1.0f;
    }

    private void onDamageChanged() {
        if (projectileDamage <= 0.0F) {
            remove();
        }
    }
}
