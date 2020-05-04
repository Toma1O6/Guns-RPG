package dev.toma.gunsrpg.common.entity;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

import java.util.List;

public class EntityBullet extends Entity implements IEntityAdditionalSpawnData {

    private EntityLivingBase shooter;

    public EntityBullet(World world) {
        super(world);
        this.setSize(0.1F, 0.1F);
    }

    public EntityBullet(World world, EntityLivingBase shooter, ItemStack stack) {
        super(world);
        this.shooter = shooter;
        this.setSize(0.1F, 0.1F);
        this.setRotation(shooter.rotationYaw, shooter.rotationPitch);
        this.setPosition(shooter.posX, shooter.posY + shooter.getEyeHeight(), shooter.posZ);
    }

    @Override
    public void writeSpawnData(ByteBuf buffer) {
        buffer.writeInt(shooter.getEntityId());
    }

    @Override
    public void readSpawnData(ByteBuf additionalData) {
        Entity entity = world.getEntityByID(additionalData.readInt());
        if(entity instanceof EntityLivingBase) shooter = (EntityLivingBase) entity;
    }

    @Override
    public void onUpdate() {
        float velocity = 20.0F;
        Vec3d lookVec = this.getLookVec();
        this.motionX = lookVec.x * velocity;
        this.motionY = lookVec.y * velocity;
        this.motionZ = lookVec.z * velocity;
        Vec3d position = new Vec3d(posX, posY, posZ);
        Vec3d motion = position.addVector(motionX, motionY, motionZ);
        RayTraceResult traceResult = world.rayTraceBlocks(position, motion, false, true, false);
        if(traceResult != null) {
            if(traceResult.typeOfHit == RayTraceResult.Type.BLOCK) {
                setDead();
                if(world.isRemote) {
                    int id = Block.getIdFromBlock(world.getBlockState(traceResult.getBlockPos()).getBlock());
                    Vec3d vec = traceResult.hitVec;
                    for(int i = 0; i < 50; i++) {
                        double x = vec.x + (rand.nextDouble() - rand.nextDouble()) / 5;
                        double y = vec.y + (rand.nextDouble() - rand.nextDouble()) / 5;
                        double z = vec.z + (rand.nextDouble() - rand.nextDouble()) / 5;
                        double sx = (rand.nextDouble() - rand.nextDouble()) / 2;
                        double sy = (rand.nextDouble() - rand.nextDouble()) / 2;
                        double sz = (rand.nextDouble() - rand.nextDouble()) / 2;
                        world.spawnParticle(EnumParticleTypes.BLOCK_DUST, true, x, y, z, sx, sy, sz, id);
                    }
                }
            }
        }
        RayTraceResult entityRay = null;
        Entity hitEntity = null;
        List<Entity> list = world.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox().expand(motionX, motionY, motionZ).grow(1.0F), entity -> entity.isEntityAlive() && entity.canBeCollidedWith());
        double distance = 0.0;
        for(Entity e : list) {
            if(e == shooter) continue;
            AxisAlignedBB aabb = e.getEntityBoundingBox();
            RayTraceResult ray = aabb.calculateIntercept(position, motion);
            if(ray != null) {
                if(traceResult != null && traceResult.typeOfHit == RayTraceResult.Type.BLOCK) {
                    Vec3d blockHit = traceResult.hitVec;
                    Vec3d entityHit = ray.hitVec;
                    RayTraceResult verification = world.rayTraceBlocks(entityHit, blockHit, false, true, false);
                    if(verification != null && verification.typeOfHit == RayTraceResult.Type.BLOCK) {
                        continue;
                    }
                }
                double d = position.squareDistanceTo(ray.hitVec);
                if(d < distance || distance == 0) {
                    entityRay = ray;
                    hitEntity = e;
                    distance = d;
                }
            }
        }
        if(entityRay != null) {
            RayTraceResult entityRayTrace = new RayTraceResult(hitEntity);
            Vec3d hit = entityRay.hitVec;
            boolean canHeadshot = hitEntity.getEyeHeight() > 1.25D;
            boolean isHeadshot = hit.y >= hitEntity.posY + hitEntity.getEyeHeight() - 0.1F;
            float damage = 8.0F;
            if(!world.isRemote) {
                hitEntity.attackEntityFrom(DamageSource.GENERIC, canHeadshot && isHeadshot ? damage * 2.5F : damage);
                hitEntity.hurtResistantTime = 0;
                setDead();
            } else {
                world.spawnParticle(EnumParticleTypes.FLAME, hit.x, hit.y, hit.z, 0, 0, 0);
            }
        }

        super.onUpdate();
        this.move(MoverType.SELF, motionX, motionY, motionZ);
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {

    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {

    }

    @Override
    protected void entityInit() {

    }
}
