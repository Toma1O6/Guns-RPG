package dev.toma.gunsrpg.common.entity.projectile;

import dev.toma.gunsrpg.util.properties.Properties;
import dev.toma.gunsrpg.util.properties.PropertyKey;
import net.minecraft.entity.*;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class Grenade extends AbstractExplosive {

    private static final PropertyKey<IStickContext> STICK_CONTEXT = PropertyKey.newKey("stick_ctx");
    private static final float BOUNCE_MODIFIER = 0.2f;
    private static final Vector3d AIR_DRAG_MULTIPLIER = new Vector3d(0.97f, 0.97f, 0.97f);
    private static final Vector3d GROUND_DRAG_MULTIPLIER = new Vector3d(0.7, 0.7, 0.7);
    private static final int FUSE_DELAY = 70;
    private boolean bounced;

    public Grenade(EntityType<? extends Grenade> type, World world) {
        super(type, world);
    }

    public Grenade(EntityType<? extends Grenade> type, World world, LivingEntity owner) {
        super(type, world, owner);
    }

    @Override
    public void tick() {
        this.xo = this.getX();
        this.yo = this.getY();
        this.zo = this.getZ();
        Vector3d deltaMovement = getDeltaMovement();
        double prevMotionX = deltaMovement.x;
        double prevMotionY = deltaMovement.y;
        double prevMotionZ = deltaMovement.z;
        if (!level.isClientSide) {
            Vector3d from = this.position();
            Vector3d to = from.add(deltaMovement.x, deltaMovement.y, deltaMovement.z);
            RayTraceContext context = new RayTraceContext(from, to, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this);
            BlockRayTraceResult rayTraceResult = level.clip(context);
            if (rayTraceResult != null && rayTraceResult.getType() == RayTraceResult.Type.BLOCK) {
                onCollided(rayTraceResult.getLocation());
            }
        }
        updateDirection();
        Vector3d pos = this.position();
        checkForCollisions(pos, pos.add(deltaMovement));
        this.move(MoverType.SELF, deltaMovement);
        deltaMovement = this.getDeltaMovement();
        boolean bouncedThisTick = false;
        double bounceX = deltaMovement.x;
        double bounceY = deltaMovement.y;
        double bounceZ = deltaMovement.z;
        if (deltaMovement.x != prevMotionX) {
            bounceX = -BOUNCE_MODIFIER * prevMotionX;
            bouncedThisTick = true;
        }
        if (deltaMovement.y != prevMotionY) {
            bounceY = -BOUNCE_MODIFIER * prevMotionY;
            bouncedThisTick = true;
        }
        if (deltaMovement.z != prevMotionZ) {
            bounceZ = -BOUNCE_MODIFIER * prevMotionZ;
            bouncedThisTick = true;
        }
        if (bouncedThisTick) {
            bounced = true;
            onCollided(this.position());
            if (this.getProperty(Properties.STICKY)) {
                this.setProperty(STICK_CONTEXT, new BlockStickContext(this.position()));
            }
        }
        if (bounced && this.getProperty(Properties.STICKY)) {
            IStickContext context = this.getProperty(STICK_CONTEXT);
            if (context != null) {
                context.update();
            }
        } else {
            setDeltaMovement(bounceX, bounceY, bounceZ);
            if (!this.isNoGravity()) {
                setDeltaMovement(getDeltaMovement().add(0, -0.039, 0));
            }
            setDeltaMovement(getDeltaMovement().multiply(AIR_DRAG_MULTIPLIER));
            if (this.onGround) {
                setDeltaMovement(getDeltaMovement().multiply(GROUND_DRAG_MULTIPLIER));
            }
        }
        if (bounced && tickCount >= FUSE_DELAY) {
            onCollided(this.position());
        }
    }

    @Override
    protected void onHitBlock(BlockRayTraceResult result) {
        super.onHitBlock(result);
        if (this.getProperty(Properties.STICKY)) {
            this.setProperty(STICK_CONTEXT, new BlockStickContext(result.getLocation()));
        }
    }

    @Override
    protected void handleEntityCollision(EntityRayTraceResult result) {
        super.handleEntityCollision(result);
        if (this.getProperty(Properties.STICKY)) {
            this.setProperty(STICK_CONTEXT, new EntityStickContext(result.getEntity()));
        }
    }

    @Override
    public void onCollided(Vector3d impact) {
        boolean isImpact = this.getProperty(Properties.IMPACT);
        if (isImpact || tickCount >= FUSE_DELAY && isAlive()) {
            IReaction reaction = this.getProperty(Properties.REACTION);
            if (reaction != null) {
                reaction.react(this, impact, level);
            }
            remove();
        }
    }

    interface IStickContext {
        void update();
    }

    class BlockStickContext implements IStickContext {

        final Vector3d pos;

        public BlockStickContext(Vector3d pos) {
            this.pos = pos;
        }

        @Override
        public void update() {
            Grenade grenade = Grenade.this;
            grenade.setPos(pos.x, pos.y, pos.z);
            grenade.setDeltaMovement(0, 0, 0);
        }
    }

    class EntityStickContext implements IStickContext {

        final Entity entity;

        public EntityStickContext(Entity entity) {
            this.entity = entity;
        }

        @Override
        public void update() {
            if (entity.isAlive()) {
                Grenade grenade = Grenade.this;
                EntitySize size = entity.getDimensions(entity.getPose());
                double xz = size.width / 2.0;
                double yy = size.height / 2.0;
                grenade.setPos(entity.getX() + xz, entity.getY() + yy, entity.getZ() + xz);
                grenade.setDeltaMovement(0, 0, 0);
            } else {
                Grenade.this.setDeltaMovement(0, -0.039, 0);
            }
        }
    }
}
