package dev.toma.gunsrpg.common.entity.projectile;

import dev.toma.gunsrpg.util.object.LazyLoader;
import dev.toma.gunsrpg.util.properties.Properties;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class Rocket extends AbstractExplosive {

    private final GuidedController controller = new GuidedController();
    private final LazyLoader<TrackedTarget> target = new LazyLoader<>(this::getTarget);
    private float velocity;

    public Rocket(EntityType<? extends Rocket> type, World world) {
        super(type, world);
    }

    public Rocket(EntityType<? extends Rocket> type, World world, LivingEntity entity) {
        super(type, world, entity);
    }

    @Override
    public void setup(float damage, float velocity, int delay) {
        super.setup(damage, velocity, delay);
        this.velocity = velocity;
    }

    @Override
    public void writeSpawnData(PacketBuffer buffer) {
        super.writeSpawnData(buffer);
        buffer.writeFloat(velocity);
    }

    @Override
    public void readSpawnData(PacketBuffer buffer) {
        super.readSpawnData(buffer);
        velocity = buffer.readFloat();
    }

    @Override
    protected void updateDirection() {
        if (this.isGuided() && tickCount > 2) {
            TrackedTarget trackedTarget = target.get();
            trackedTarget.update();
            controller.update(trackedTarget);
            xRotO = xRot;
            yRotO = yRot;
        } else {
            Vector3d delta = getDeltaMovement();
            double dx = delta.x;
            double dy = delta.y;
            double dz = delta.z;
            float motionSqrt = MathHelper.sqrt(dx * dx + dz * dz);
            xRot = -(float) (MathHelper.atan2(dy, motionSqrt) * (180.0F / Math.PI));
            xRotO = xRot;
        }
    }

    @Override
    public void onCollided(Vector3d impact) {
        IReaction reaction = this.getProperty(Properties.REACTION);
        if (reaction != null) {
            reaction.react(this, impact, level);
        }
        if (!level.isClientSide) {
            remove();
        }
    }

    @Override
    public void postTick() {
        boolean propelled = this.getProperty(Properties.FUELED);
        if (propelled) {
            if (level.isClientSide && canApplyGravity())  {
                Vector3d movement = this.getDeltaMovement().multiply(-0.3, -0.3, -0.3);
                for (int i = 0; i < 5; i++) {
                    double rx = (random.nextDouble() - random.nextDouble()) * 0.05F;
                    double ry = (random.nextDouble() - random.nextDouble()) * 0.05F;
                    double rz = (random.nextDouble() - random.nextDouble()) * 0.05F;
                    level.addParticle(ParticleTypes.FLAME, true, this.getX(), this.getY(), this.getZ(), movement.x + rx, movement.y + ry, movement.z + rz);
                }
                level.addParticle(ParticleTypes.CLOUD, true, this.getX(), this.getY(), this.getZ(), movement.x, movement.y, movement.z);
            }
            if (tickCount > 100) {
                this.onCollided(this.position());
            }
            if (isGuided()) { // Proximity fuse
                Vector3d currentPos = this.position();
                Vector3d nextPos = currentPos.add(this.getDeltaMovement());
                TrackedTarget target = this.getTarget();
                if (target.entity != null) {
                    Vector3d targetPos = TrackedTarget.atCenterOfEntity(target.entity);
                    double distance1 = currentPos.distanceToSqr(targetPos);
                    double distance2 = nextPos.distanceToSqr(targetPos);
                    if (distance2 > distance1 && distance1 <= 16.0) {
                        this.onCollided(currentPos);
                    }
                }
            }
        } else  {
            applyGravity(0.02F);
            if (level.isClientSide) {
                Vector3d movement = this.getDeltaMovement().multiply(-0.4, -0.4, -0.4);
                level.addParticle(ParticleTypes.SMOKE, true, this.getX(), this.getY(), this.getZ(), movement.x, movement.y, movement.z);
            }
        }
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return true;
    }

    private boolean isGuided() {
        return this.getTarget().entity != null;
    }

    private TrackedTarget getTarget() {
        int id = this.getProperty(Properties.ENTITY_ID);
        Entity entity = this.level.getEntity(id);
        return new TrackedTarget(entity);
    }

    private final class GuidedController {

        void update(TrackedTarget target) {
            Rocket rocket = Rocket.this;
            Vector3d position = target.getTrackedPosition();
            double angleDegrees = getAngleTowardsTarget(rocket.getDeltaMovement(), position.subtract(rocket.position()));
            boolean hasGuidance = angleDegrees < 90.0;
            float x = rocket.xRot;
            float y = rocket.yRot;
            if (hasGuidance) {
                rocket.xRot = this.rotateTowards(x, this.getVerticalDifference(position), 8.0F);
                rocket.yRot = this.rotateTowards(y, this.getHorizontalDifference(position), 8.0F);
            }
            float f = rocket.velocity;
            if (!rocket.getProperty(Properties.FUELED) && tickCount > 15) {
                return;
            }
            Vector3d look = Vector3d.directionFromRotation(rocket.xRot, rocket.yRot);
            setDeltaMovement(look.multiply(f, f, f));
        }

        float getVerticalDifference(Vector3d target) {
            Vector3d pos = Rocket.this.position();
            double x = target.x - pos.x;
            double y = target.y - pos.y;
            double z = target.z - pos.z;
            double dist = MathHelper.sqrt(x * x + z * z);
            return (float) -(MathHelper.atan2(y, dist) * (180F / Math.PI));
        }

        float getHorizontalDifference(Vector3d target) {
            Vector3d pos = Rocket.this.position();
            double x = target.x - pos.x;
            double z = target.z - pos.z;
            return (float) (MathHelper.atan2(z, x) * (180F / Math.PI)) - 90.0F;
        }

        float rotateTowards(float currentRotation, float targetRotation, float maxStepSize) {
            float difference = MathHelper.degreesDifference(currentRotation, targetRotation);
            float clampedMovement = MathHelper.clamp(difference, -maxStepSize, maxStepSize);
            return currentRotation + clampedMovement;
        }

        double getAngleTowardsTarget(Vector3d vecA, Vector3d vecB) {
            double aLen = vecA.length();
            double bLen = vecB.length();
            double a = vecA.x * vecB.x + vecA.y + vecB.y + vecA.z * vecB.z;
            double b = aLen * bLen;
            double angleRad = Math.acos(a / b);
            return Math.toDegrees(angleRad);
        }
    }

    private static class TrackedTarget {

        private final Entity entity;
        private Vector3d pos;

        public TrackedTarget(Entity entity) {
            this.entity = entity;
        }

        Vector3d getTrackedPosition() {
            return pos;
        }

        void update() {
            if (entity == null && pos.y > 0) {
                pos = new Vector3d(pos.x, 0, pos.z);
                return;
            }
            pos = atCenterOfEntity(entity);
        }

        static Vector3d atCenterOfEntity(Entity entity) {
            EntitySize size = entity.getDimensions(entity.getPose());
            Vector3d position = entity.position();
            return position.add(size.width / 2.0F, size.height / 2.0F, size.width / 2.0F);
        }
    }
}
