package dev.toma.gunsrpg.common.entity.projectile;

import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.common.capability.PlayerData;
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
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.entity.PartEntity;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.*;
import java.util.function.Consumer;

public final class Projectile extends ProjectileEntity {

    private final IProjectileConfig config;
    private final EnumProjectileType renderType;
    private final ProjectileSettings settings;
    private final Set<UUID> passedPlayers;

    private final ItemStack weapon;
    private float projectileDamage;
    boolean invalid;

    public Projectile(EntityType<? extends Projectile> type, World world, IProjectileConfig config) {
        super(type, world);
        this.config = config;
        this.renderType = config.getRenderType();
        this.settings = config.getSettings();
        this.config.assignControlObj(this);

        this.passedPlayers = settings.isSuperSonic() ? new HashSet<>() : Collections.emptySet();

        LivingEntity owner = config.getOwner();
        this.setOwner(owner);
        this.weapon = owner.getMainHandItem();
    }

    /* Public methods */

    public void fire(float xRot, float yRot, float inaccuracy) {
        float velocity = settings.getActualVelocity();
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

    public void reduceDamage(float amount) {
        this.projectileDamage -= amount;
    }

    public float getProjectileDamage() {
        return projectileDamage;
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double dist) {
        return renderType.shouldRender();
    }

    @Override
    public void tick() {
        config.tickPre();
        if (config.allowBaseTick())
            super.tick();
        updateDirection();
        Vector3d v1 = position();
        Vector3d v2 = v1.add(getDeltaMovement());
        checkForCollisions(v1, v2);
        if (settings.isSuperSonic())
            passAround();
        config.tickSettings(settings);
        config.tickPost();
    }

    public ItemStack getWeapon() {
        return weapon;
    }

    public void applyGravity() {
        setDeltaMovement(getDeltaMovement().add(0.0, -0.05, 0.0));
    }

    /* Protected methods */

    @Override
    protected void onHitBlock(BlockRayTraceResult rayTrace) {
        config.hitBlock(rayTrace);
    }

    @Override
    protected void onHitEntity(EntityRayTraceResult rayTrace) {
        config.hitEntity(rayTrace);
    }

    @Override
    protected boolean canHitEntity(Entity entity) {
        return entity != config.getPenetrationConfig().getLastHit() && super.canHitEntity(entity);
    }

    @Override
    protected void defineSynchedData() {}

    /* Package methods */

    /**
     * Updates projectile direction
     */
    void updateDirection() {
        config.updateDirection();
    }

    /**
     * Multiplies projectile damage by f value
     * @param f The multiplier
     */
    void mulDamage(float f) {
        projectileDamage *= f;
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
            config.hurtTarget(owner, entity, damage);
            if (weapon.getItem() instanceof GunItem) {
                GunItem gun = (GunItem) weapon.getItem();
                if (willDie)
                    gun.onKillEntity(this, livingEntity, weapon, (LivingEntity) owner);
                else
                    gun.onHitEntity(this, livingEntity, weapon, (LivingEntity) owner);
            }
        } else if (entity instanceof PartEntity<?>) {
            config.hurtTarget(owner, entity, damage);
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
                player.connection.send(new SPlaySoundEffectPacket(settings.getPassBySound(), SoundCategory.MASTER, pos.x, pos.y, pos.z, 0.6F, 1.0F));
            }
        }
    }

    private void aggroNearby(PlayerEntity player) {
        PlayerData.get(player).ifPresent(data -> {
            double mobScareRange = 25.0;
            double noiseMultiplier = config.getNoiseMultiplier(data.getAttributes());
            double actualScareRange = mobScareRange * noiseMultiplier;
            Entity owner = getOwner();
            if (owner instanceof PlayerEntity) {
                List<MobEntity> list = level.getEntitiesOfClass(MobEntity.class, getBoundingBox().inflate(actualScareRange), ent -> ent != owner);
                for (MobEntity mobEntity : list) {
                    mobEntity.setTarget((PlayerEntity) owner);
                }
            }
        });
    }

    private float getHeadshotMultiplier() {
        Entity src = getOwner();
        if (src instanceof PlayerEntity) {
            LazyOptional<IPlayerData> optional = PlayerData.get((PlayerEntity) src);
            if (optional.isPresent()) {
                IPlayerData data = optional.orElse(null);
                return (float) config.getHeadshotMultiplier(data.getAttributes());
            }
        }
        return 1.0f;
    }
}
