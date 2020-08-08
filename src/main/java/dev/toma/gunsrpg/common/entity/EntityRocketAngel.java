package dev.toma.gunsrpg.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class EntityRocketAngel extends EntityMob {

    public EntityRocketAngel(World world) {
        super(world);
        this.setSize(0.8F, 1.6F);
        experienceValue = 8;
        moveHelper = new MoveController(this);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        setNoGravity(true);
    }

    @Override
    protected void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(4, new ArrowAttack(this));
        this.tasks.addTask(8, new MoveRandom());
        this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 3.0F, 1.0F));
        this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, false));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(45.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(48.0D);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_VEX_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_VEX_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_VEX_HURT;
    }

    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_VEX;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getBrightnessForRender() {
        return 15728880;
    }

    @Override
    public boolean attackEntityAsMob(Entity entityIn) {
        float f = (float) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
        boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), f);
        if (flag) {
            Vec3d look = getLookVec();
            Vec3d knockback = look.scale(3);
            entityIn.motionX = knockback.x;
            entityIn.motionY = Math.abs(knockback.y);
            entityIn.motionZ = knockback.z;
        }
        return flag;
    }

    @Override
    public void fall(float distance, float damageMultiplier) {

    }

    static class ArrowAttack extends EntityAIBase {

        private final EntityRocketAngel entity;
        private int toFire;
        private int cooldown;

        public ArrowAttack(EntityRocketAngel entity) {
            this.entity = entity;
            setMutexBits(3);
        }

        @Override
        public boolean shouldExecute() {
            EntityLivingBase target = entity.getAttackTarget();
            return target != null && target.isEntityAlive();
        }

        @Override
        public void startExecuting() {
            toFire = 4;
            cooldown = 15;
        }

        @Override
        public void resetTask() {
            cooldown = Integer.MAX_VALUE;
        }

        @Override
        public void updateTask() {
            --cooldown;
            EntityLivingBase target = entity.getAttackTarget();
            double distance = entity.getDistanceSq(target);
            if (distance < 4.0D) {
                if (cooldown <= 0) {
                    cooldown = 10;
                    entity.attackEntityAsMob(target);
                }
            } else if (distance < (getFollowDistance() * getFollowDistance()) / 1.5F) {
                this.entity.getLookHelper().setLookPositionWithEntity(target, 30.0F, 30.0F);
                int heightDiff = (int)(entity.posY - target.posY);
                if(heightDiff < 10) {
                    entity.motionY = 0.2;
                }
                if(cooldown <= 0) {
                    cooldown = 6;
                    EntityExplosiveArrow arrow = new EntityExplosiveArrow(entity.world, entity, 1);
                    double x = target.posX - entity.posX;
                    double y = target.getEntityBoundingBox().minY + (double) (target.height / 3.0F) - arrow.posY;
                    double z = target.posZ - entity.posZ;
                    double dist = MathHelper.sqrt(x * x + z * z);
                    arrow.shoot(x, y + dist * 0.2D, z, 1.6F, (float) (23 - entity.world.getDifficulty().getDifficultyId() * 4));
                    entity.world.spawnEntity(arrow);
                    --toFire;
                    if(toFire < 0) {
                        toFire = 4;
                        cooldown = 100;
                    }
                }
            } else {
                entity.getNavigator().clearPath();
                this.entity.getMoveHelper().setMoveTo(target.posX, target.posY, target.posZ, 0.4D);
            }
        }

        private double getFollowDistance() {
            IAttributeInstance iattributeinstance = this.entity.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
            return iattributeinstance == null ? 16.0D : iattributeinstance.getAttributeValue();
        }
    }

    class MoveRandom extends EntityAIBase {

        public MoveRandom() {
            setMutexBits(1);
        }

        public boolean shouldExecute() {
            return !EntityRocketAngel.this.getMoveHelper().isUpdating() && EntityRocketAngel.this.rand.nextInt(7) == 0;
        }

        public boolean shouldContinueExecuting() {
            return false;
        }

        public void updateTask() {
            BlockPos blockpos = EntityRocketAngel.this.getPosition();
            for (int i = 0; i < 3; ++i) {
                BlockPos blockpos1 = blockpos.add(EntityRocketAngel.this.rand.nextInt(15) - 7, EntityRocketAngel.this.rand.nextInt(11) - 5, EntityRocketAngel.this.rand.nextInt(15) - 7);
                if (EntityRocketAngel.this.world.isAirBlock(blockpos1)) {
                    EntityRocketAngel.this.moveHelper.setMoveTo((double) blockpos1.getX() + 0.5D, (double) blockpos1.getY() + 0.5D, (double) blockpos1.getZ() + 0.5D, 0.25D);
                    if (EntityRocketAngel.this.getAttackTarget() == null) {
                        EntityRocketAngel.this.getLookHelper().setLookPosition((double) blockpos1.getX() + 0.5D, (double) blockpos1.getY() + 0.5D, (double) blockpos1.getZ() + 0.5D, 180.0F, 20.0F);
                    }
                    break;
                }
            }
        }
    }

    class MoveController extends EntityMoveHelper {
        public MoveController(EntityRocketAngel angel) {
            super(angel);
        }

        public void onUpdateMoveHelper() {
            if (this.action == EntityMoveHelper.Action.MOVE_TO) {
                double d0 = this.posX - EntityRocketAngel.this.posX;
                double d1 = this.posY - EntityRocketAngel.this.posY;
                double d2 = this.posZ - EntityRocketAngel.this.posZ;
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                d3 = MathHelper.sqrt(d3);

                if (d3 < EntityRocketAngel.this.getEntityBoundingBox().getAverageEdgeLength()) {
                    this.action = EntityMoveHelper.Action.WAIT;
                    EntityRocketAngel.this.motionX *= 0.5D;
                    EntityRocketAngel.this.motionY *= 0.5D;
                    EntityRocketAngel.this.motionZ *= 0.5D;
                } else {
                    EntityRocketAngel.this.motionX += d0 / d3 * 0.05D * this.speed;
                    EntityRocketAngel.this.motionY += d1 / d3 * 0.05D * this.speed;
                    EntityRocketAngel.this.motionZ += d2 / d3 * 0.05D * this.speed;

                    if (EntityRocketAngel.this.getAttackTarget() == null) {
                        EntityRocketAngel.this.rotationYaw = -((float) MathHelper.atan2(EntityRocketAngel.this.motionX, EntityRocketAngel.this.motionZ)) * (180F / (float) Math.PI);
                        EntityRocketAngel.this.renderYawOffset = EntityRocketAngel.this.rotationYaw;
                    } else {
                        double d4 = EntityRocketAngel.this.getAttackTarget().posX - EntityRocketAngel.this.posX;
                        double d5 = EntityRocketAngel.this.getAttackTarget().posZ - EntityRocketAngel.this.posZ;
                        EntityRocketAngel.this.rotationYaw = -((float) MathHelper.atan2(d4, d5)) * (180F / (float) Math.PI);
                        EntityRocketAngel.this.renderYawOffset = EntityRocketAngel.this.rotationYaw;
                    }
                }
            }
        }
    }
}
