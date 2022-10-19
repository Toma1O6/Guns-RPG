package dev.toma.gunsrpg.common.entity;

import dev.toma.gunsrpg.common.entity.projectile.*;
import dev.toma.gunsrpg.common.init.ModEntities;
import dev.toma.gunsrpg.util.math.WeightedRandom;
import dev.toma.gunsrpg.util.properties.Properties;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.EnumSet;

public class RocketAngelEntity extends MonsterEntity implements IEntityAdditionalSpawnData {

    private Type type = Type.SELECTOR.getRandom();

    public RocketAngelEntity(World world) {
        this(ModEntities.ROCKET_ANGEL.get(), world);
    }

    public RocketAngelEntity(EntityType<? extends MonsterEntity> type, World world) {
        super(type, world);
        xpReward = 8;
        moveControl = new MoveController(this);
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return createMonsterAttributes().add(Attributes.MAX_HEALTH, 45.0).add(Attributes.ATTACK_DAMAGE, 4.0).add(Attributes.FOLLOW_RANGE, 96.0);
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void tick() {
        super.tick();
        setNoGravity(true);
    }

    @Override
    public boolean causeFallDamage(float p_225503_1_, float p_225503_2_) {
        return false;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(4, new RocketAttack(this));
        this.goalSelector.addGoal(8, new MoveRandom());
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, LivingEntity.class, 8.0F));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
    }

    public int getTextureIndex() {
        return type.ordinal();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.VEX_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.VEX_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.VEX_HURT;
    }

    @Override
    public float getBrightness() {
        return type == Type.STUN ? 0.7F : 1.0F;
    }

    @Override
    public boolean doHurtTarget(Entity entityIn) {
        float f = (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
        boolean flag = entityIn.hurt(DamageSource.mobAttack(this), f);
        if (flag) {
            Vector3d look = getLookAngle();
            Vector3d knockback = look.scale(3);
            entityIn.setDeltaMovement(knockback.x, Math.abs(knockback.y), knockback.z);
        }
        return flag;
    }

    @Override
    public void writeSpawnData(PacketBuffer buffer) {
        buffer.writeEnum(type);
    }

    @Override
    public void readSpawnData(PacketBuffer additionalData) {
        type = additionalData.readEnum(Type.class);
    }

    enum Type {

        STANDARD(35, 0xFFFF9B, new ExplosiveReaction(2.0f, Explosion.Mode.DESTROY)),
        HE(10, 0x63C0FF, new ExplosiveReaction(3.0f, Explosion.Mode.DESTROY)),
        INCENDIARY(8, 0xFF8968, MultipartReaction.multi(new ExplosiveReaction(2.0f, Explosion.Mode.DESTROY), new NapalmReaction(3.0, 0.3f))),
        TOXIN(16, 0xB993FF, MultipartReaction.multi(new ExplosiveReaction(2.0f, Explosion.Mode.NONE), new EffectSpreadReaction(0xB993FF, () -> new EffectInstance(Effects.WITHER, 120, 1)))),
        STUN(12, 0xD3D3D3, MultipartReaction.multi(new ExplosiveReaction(2.0f, Explosion.Mode.NONE), new EffectSpreadReaction(0xD3D3D3, () -> new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 80, 9))));

        private static final WeightedRandom<Type> SELECTOR = new WeightedRandom<>(Type::getWeight, Type.values());
        private final int weight;
        private final int color;
        private final IReaction reaction;

        Type(int weight, int color, IReaction reaction) {
            this.weight = weight;
            this.color = color;
            this.reaction = reaction;
        }

        public int getWeight() {
            return weight;
        }

        public int getColor() {
            return color;
        }

        public IReaction getReaction() {
            return reaction;
        }
    }

    static class RocketAttack extends Goal {

        private final RocketAngelEntity entity;
        private int toFire;
        private int cooldown;

        public RocketAttack(RocketAngelEntity entity) {
            this.entity = entity;
            setFlags(EnumSet.of(Flag.TARGET, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = entity.getTarget();
            return target != null && target.isAlive();
        }

        @Override
        public void start() {
            toFire = 3;
            cooldown = 15;
        }

        @Override
        public void stop() {
            cooldown = Integer.MAX_VALUE;
        }

        @Override
        public void tick() {
            --cooldown;
            LivingEntity target = entity.getTarget();
            if (target == null) {
                return;
            }
            double distance = entity.distanceToSqr(target);
            boolean canSee = entity.getSensing().canSee(target);
            if (canSee) {
                if (distance < 4.0D) {
                    if (cooldown <= 0) {
                        cooldown = 10;
                        entity.doHurtTarget(target);
                    }
                } else if (distance < (getFollowDistance() * getFollowDistance()) / 1.1F) {
                    this.entity.lookAt(target, 30.0F, 30.0F);
                    this.entity.getLookControl().setLookAt(target, 30.0F, 30.0F);
                    int heightDiff = (int) (entity.getY() - target.getY());
                    if (heightDiff < 10) {
                        Vector3d currentMovement = entity.getDeltaMovement();
                        entity.setDeltaMovement(currentMovement.x, 0.2, currentMovement.z);
                    }
                    if (cooldown <= 0) {
                        cooldown = 6;
                        Difficulty difficulty = entity.level.getDifficulty();
                        Rocket rocket = new Rocket(ModEntities.ROCKET.get(), entity.level, entity);
                        rocket.setup(0.0F, 2.0F, 0);
                        rocket.fire(entity.xRot, entity.yRot, 6.4F - difficulty.getId() * 0.3F);
                        rocket.setProperty(Properties.FUELED, true);
                        rocket.setProperty(Properties.REACTION, entity.type.getReaction());
                        entity.level.addFreshEntity(rocket);
                        entity.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.FIREWORK_ROCKET_LAUNCH, SoundCategory.MASTER, 10.0F, 1.0F);
                        --toFire;
                        if (toFire < 0) {
                            toFire = 3;
                            cooldown = 100;
                        }
                    }
                } else {
                    entity.getNavigation().stop();
                    this.entity.getMoveControl().setWantedPosition(target.getX(), target.getY(), target.getZ(), 0.4D);
                }
            } else {
                entity.getNavigation().stop();
                this.entity.getMoveControl().setWantedPosition(target.getX(), target.getY(), target.getZ(), 0.4D);
            }
        }

        private double getFollowDistance() {
            double dist = this.entity.getAttributeValue(Attributes.FOLLOW_RANGE);
            return dist == 0 ? 16.0D : dist;
        }
    }

    class MoveRandom extends Goal {

        public MoveRandom() {
            setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return !RocketAngelEntity.this.getMoveControl().hasWanted() && RocketAngelEntity.this.random.nextInt(7) == 0;
        }

        @Override
        public boolean canContinueToUse() {
            return false;
        }

        @Override
        public void tick() {
            BlockPos blockpos = RocketAngelEntity.this.getOnPos();
            for (int i = 0; i < 3; ++i) {
                BlockPos blockpos1 = blockpos.offset(RocketAngelEntity.this.random.nextInt(15) - 7, RocketAngelEntity.this.random.nextInt(11) - 5, RocketAngelEntity.this.random.nextInt(15) - 7);
                if (RocketAngelEntity.this.level.isEmptyBlock(blockpos1)) {
                    RocketAngelEntity.this.moveControl.setWantedPosition((double) blockpos1.getX() + 0.5D, (double) blockpos1.getY() + 0.5D, (double) blockpos1.getZ() + 0.5D, 0.25D);
                    if (RocketAngelEntity.this.getTarget() == null) {
                        RocketAngelEntity.this.getLookControl().setLookAt((double) blockpos1.getX() + 0.5D, (double) blockpos1.getY() + 0.5D, (double) blockpos1.getZ() + 0.5D, 180.0F, 20.0F);
                    }
                    break;
                }
            }
        }
    }

    class MoveController extends MovementController {

        public MoveController(RocketAngelEntity angel) {
            super(angel);
        }

        @Override
        public void tick() {
            if (this.operation == Action.MOVE_TO) {
                double x = this.getWantedX() - RocketAngelEntity.this.getX();
                double y = this.getWantedY() - RocketAngelEntity.this.getY();
                double z = this.getWantedZ() - RocketAngelEntity.this.getZ();
                double dist = x * x + y * y + z * z;
                dist = MathHelper.sqrt(dist);

                if (dist < RocketAngelEntity.this.getBoundingBox().getSize()) {
                    this.operation = Action.WAIT;
                    RocketAngelEntity.this.setDeltaMovement(RocketAngelEntity.this.getDeltaMovement().scale(0.5D));
                } else {
                    RocketAngelEntity.this.setDeltaMovement(RocketAngelEntity.this.getDeltaMovement().add(x / dist * 0.05 * speedModifier, y / dist * 0.05 * speedModifier, z / dist * 0.05 * speedModifier));

                    if (RocketAngelEntity.this.getTarget() == null) {
                        Vector3d vector3d1 = RocketAngelEntity.this.getDeltaMovement();
                        RocketAngelEntity.this.yRot = -((float) MathHelper.atan2(vector3d1.x, vector3d1.z)) * (180F / (float) Math.PI);
                    } else {
                        double px = RocketAngelEntity.this.getTarget().getX() - RocketAngelEntity.this.getX();
                        double pz = RocketAngelEntity.this.getTarget().getZ() - RocketAngelEntity.this.getZ();
                        RocketAngelEntity.this.yRot = -((float) MathHelper.atan2(px, pz)) * (180F / (float) Math.PI);
                    }
                    RocketAngelEntity.this.yBodyRot = RocketAngelEntity.this.yRot;
                }
            }
        }
    }
}
