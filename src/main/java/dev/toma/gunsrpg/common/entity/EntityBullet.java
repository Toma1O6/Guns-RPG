package dev.toma.gunsrpg.common.entity;

import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.config.gun.WeaponConfiguration;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
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
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

import java.util.List;

public class EntityBullet extends Entity implements IEntityAdditionalSpawnData {

    private EntityLivingBase shooter;
    private ItemStack gun;

    public EntityBullet(World world) {
        super(world);
        this.setSize(0.01F, 0.01F);
    }

    public EntityBullet(World world, EntityLivingBase shooter, ItemStack stack) {
        super(world);
        this.shooter = shooter;
        this.setSize(0.1F, 0.1F);
        this.setRotation(shooter.rotationYaw, shooter.rotationPitch);
        this.setPosition(shooter.posX, shooter.posY + shooter.getEyeHeight(), shooter.posZ);
        this.gun = stack;
    }

    @Override
    public void writeSpawnData(ByteBuf buffer) {
        buffer.writeInt(shooter != null ? shooter.getEntityId() : 0);
        ByteBufUtils.writeItemStack(buffer, gun);
    }

    @Override
    public void readSpawnData(ByteBuf additionalData) {
        Entity entity = world.getEntityByID(additionalData.readInt());
        if(entity instanceof EntityLivingBase) shooter = (EntityLivingBase) entity;
        gun = ByteBufUtils.readItemStack(additionalData);
    }

    @Override
    public void onUpdate() {
        if(gun == null) {
            setDead();
            return;
        }
        WeaponConfiguration stats = this.getGun().getWeaponConfig();
        float velocity = 0.8F;
        Vec3d lookVec = this.getLookVec();
        this.motionX = lookVec.x * stats.velocity;
        this.motionY = lookVec.y * stats.velocity;
        this.motionZ = lookVec.z * stats.velocity;
        this.checkForCollisions();
        super.onUpdate();
        this.move(MoverType.SELF, motionX, motionY, motionZ);
        if(!world.isRemote && collided) setDead();
    }

    protected void checkForCollisions() {
        Vec3d position = new Vec3d(posX, posY, posZ);
        Vec3d motion = position.addVector(motionX, motionY, motionZ);
        RayTraceResult traceResult = world.rayTraceBlocks(position, motion, false, true, false);
        if(traceResult != null) {
            if(traceResult.typeOfHit == RayTraceResult.Type.BLOCK) {
                IBlockState state = world.getBlockState(traceResult.getBlockPos());
                boolean isGlass = state.getMaterial() == Material.GLASS;
                if(!world.isRemote) {
                    if(isGlass) {
                        world.destroyBlock(traceResult.getBlockPos(), false);
                        this.checkForCollisions();
                    } else setDead();
                } else {
                    if(!isGlass) {
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
            float damage = this.getGun().getWeaponConfig().damage;
            if(!world.isRemote) {
                hitEntity.attackEntityFrom(DamageSource.causeMobDamage(shooter), canHeadshot && isHeadshot ? damage * 2.5F : damage);
                hitEntity.hurtResistantTime = 0;
                this.setDead();
            } else {
                world.spawnParticle(EnumParticleTypes.FLAME, hit.x, hit.y, hit.z, 0, 0, 0);
            }
        }
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        NBTTagCompound stack = new NBTTagCompound();
        gun.writeToNBT(stack);
        compound.setTag("stack", stack);
        compound.setInteger("entID", shooter != null ? shooter.getEntityId() : 0);
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        gun = new ItemStack(compound.getCompoundTag("stack"));
        Entity entity = world.getEntityByID(compound.getInteger("entID"));
        if(!(entity instanceof EntityLivingBase)) {
            this.setDead();
            return;
        }
        shooter = (EntityLivingBase) entity;
    }

    @Override
    protected void entityInit() {

    }

    protected GunItem getGun() {
        return (GunItem) gun.getItem();
    }
}
