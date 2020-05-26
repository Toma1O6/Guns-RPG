package dev.toma.gunsrpg.common.entity;

import com.google.common.base.Predicate;
import dev.toma.gunsrpg.common.ModRegistry;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.common.skilltree.Ability;
import dev.toma.gunsrpg.config.gun.WeaponConfiguration;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.CPacketParticle;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.List;

@SuppressWarnings("Guava")
public class EntityBullet extends Entity {

    private static final Predicate<Entity> ARROW_TARGETS = e -> EntitySelectors.NOT_SPECTATING.apply(e) && EntitySelectors.IS_ALIVE.apply(e) && e.canBeCollidedWith();
    private EntityLivingBase shooter;
    private ItemStack stack;
    private RayTraceResult entityRaytrace;

    private int effect;
    private float ogDamage;
    private float damage;

    public EntityBullet(World worldIn) {
        super(worldIn);
        this.setSize(0.1f, 0.1f);
        preventEntitySpawning = true;
    }

    public EntityBullet(World worldIn, EntityLivingBase shooter, GunItem gun, ItemStack stack) {
        this(worldIn);
        this.setSize(0.1f, 0.1f);
        this.noClip = true;
        this.shooter = shooter;
        this.stack = stack;
        WeaponConfiguration config = gun.getWeaponConfig();
        this.effect = config.effect;
        this.ogDamage = config.damage + gun.getDamageBonus(stack);
        this.damage = ogDamage;
        Vec3d direct = getVectorForRotation(shooter.rotationPitch, shooter.getRotationYawHead());
        this.setPosition(shooter.posX, shooter.posY + shooter.getEyeHeight(), shooter.posZ);
    }

    public void fire(float pitch, float yaw, float velocity) {
        Vec3d vec = getVectorForRotation(pitch, yaw);
        this.motionX = velocity * vec.x;
        this.motionY = velocity * vec.y;
        this.motionZ = velocity * vec.z;
        updateHeading();
        if(!world.isRemote && shooter instanceof EntityPlayer && !((GunItem) stack.getItem()).isSilenced((EntityPlayer) shooter)) {
            List<EntityLivingBase> entityList = world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(30));
            for(EntityLivingBase entityLivingBase : entityList) {
                if (entityLivingBase == shooter) continue;
                entityLivingBase.setRevengeTarget(shooter);
            }
        }
    }

    public EntityLivingBase getShooter() {
        return shooter;
    }

    public void onBulletCollided(RayTraceResult rayTraceResult) {
        if(rayTraceResult == null) {
            return;
        }
        Entity entity = rayTraceResult.entityHit;
        if(entity != null && !world.isRemote) {
            boolean validPlayer = shooter instanceof EntityPlayer && PlayerDataFactory.hasActiveSkill((EntityPlayer) shooter, Ability.DEAD_EYE);
            boolean validWeapon = stack.getItem() == ModRegistry.GRPGItems.SNIPER_RIFLE;
            boolean isHeadshot = validWeapon && validPlayer && this.canEntityGetHeadshot(entity) && entityRaytrace.hitVec.y >= entity.getPosition().getY() + entity.getEyeHeight() - 0.15f;
            Vec3d vec = rayTraceResult.hitVec;
            Block block = Blocks.REDSTONE_BLOCK;
            if(isHeadshot) {
                damage *= 1.5;
            }
            if(entity instanceof EntityLivingBase) {
                NetworkManager.toDimension(CPacketParticle.multipleParticles(EnumParticleTypes.BLOCK_CRACK, vec.x, entityRaytrace.hitVec.y, vec.z, Block.getIdFromBlock(block), 2*Math.round(damage), 0), this.dimension);
            }
            this.onEntityHit(isHeadshot, entity);
            entity.hurtResistantTime = 0;
            this.setDead();
        } else if(rayTraceResult.getBlockPos() != null && !world.isRemote) {
            BlockPos pos = rayTraceResult.getBlockPos();
            IBlockState state = world.getBlockState(pos);
            Block block = state.getBlock();
            boolean canPenetrate = false;
            if(state.getMaterial() == Material.GLASS) {
                world.setBlockToAir(pos);
                world.playSound(null, posX, posY, posZ, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.BLOCKS, 3.0F, 1.0F);
                canPenetrate = true;
            } else if(!block.isReplaceable(world, pos)) {
                Vec3d vec = rayTraceResult.hitVec;
                NetworkManager.toDimension(CPacketParticle.multipleParticles(EnumParticleTypes.BLOCK_CRACK, vec.x, vec.y, vec.z, Block.getIdFromBlock(block), 10, 0), this.dimension);
                world.playSound(null, posX, posY, posZ, block.getSoundType().getBreakSound(), SoundCategory.BLOCKS, 0.5F, block.getSoundType().getPitch() * 0.8F);
                this.setDead();
            }

            if(canPenetrate && damage > 0) {
                Vec3d startVec = new Vec3d(posX, posY, posZ);
                Vec3d nextPos = new Vec3d(motionX, motionY, motionZ).add(startVec);
                RayTraceResult trace = world.rayTraceBlocks(nextPos, startVec, false, true, false);
                Entity e = this.findEntityOnPath(nextPos, startVec, trace);
                if(e != null) {
                    trace = new RayTraceResult(e);
                }
                if(trace != null && rayTraceResult.entityHit instanceof EntityPlayer) {
                    EntityPlayer player = (EntityPlayer) rayTraceResult.entityHit;
                    if(shooter instanceof EntityPlayer && !((EntityPlayer)shooter).canAttackPlayer(player)) {
                        trace = null;
                    }
                }
                if(trace != null) {
                    this.onBulletCollided(trace);
                }
            }
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        updateHeading();

        Vec3d vec3d1 = new Vec3d(this.posX, this.posY, this.posZ);
        Vec3d vec3d = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        RayTraceResult raytraceresult = this.world.rayTraceBlocks(vec3d1, vec3d, false, true, false);
        if (this.ticksExisted > effect && !world.isRemote) {
            this.motionY -= 0.05;
        }

        if (this.ticksExisted >= 80) {
            this.setDead();
        }
        if (!world.isRemote && stack.getItem() instanceof GunItem) {
            ((GunItem) stack.getItem()).updateBullet(this);
        }
        Entity entity = this.findEntityOnPath(vec3d1, vec3d, raytraceresult);
        if (entity != null) {
            raytraceresult = new RayTraceResult(entity);
        }
        if (raytraceresult != null && raytraceresult.entityHit instanceof EntityPlayer) {
            EntityPlayer entityplayer = (EntityPlayer) raytraceresult.entityHit;
            if (this.shooter instanceof EntityPlayer && !((EntityPlayer) this.shooter).canAttackPlayer(entityplayer)) {
                raytraceresult = null;
            }
        }
        if (raytraceresult != null && !ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
            this.onBulletCollided(raytraceresult);
        }

        move(MoverType.SELF, motionX, motionY, motionZ);
    }

    @Override
    public boolean isInRangeToRenderDist(double distance) {
        return false;
    }

    protected Entity findEntityOnPath(Vec3d start, Vec3d end, RayTraceResult trace) {
        Entity entity = null;
        List<Entity> list = this.world.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox().expand(this.motionX, this.motionY, this.motionZ).grow(1.0D), ARROW_TARGETS::test);
        double d0 = 0.0D;
        for (int i = 0; i < list.size(); ++i) {
            Entity entity1 = list.get(i);
            if (entity1 != this.shooter) {
                AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox();
                RayTraceResult raytraceresult = axisalignedbb.calculateIntercept(start, end);
                if (raytraceresult != null) {
                    if (trace != null) {
                        if (this.getDistanceTo(trace.hitVec) < this.getDistanceTo(raytraceresult.hitVec)) {
                            return entity;
                        }
                    }
                    double d1 = start.squareDistanceTo(raytraceresult.hitVec);
                    entityRaytrace = raytraceresult;

                    if (d1 < d0 || d0 == 0.0D) {
                        entity = entity1;
                        d0 = d1;
                    }
                }
            }
        }
        return entity;
    }

    protected void onEntityHit(boolean isHeadshot, Entity entity) {
        if (entity instanceof EntityLivingBase) {
            EntityLivingBase entityLivingBase = (EntityLivingBase) entity;
            boolean dead = entityLivingBase.getHealth() - damage <= 0;
            entity.attackEntityFrom(DamageSource.causeMobDamage(shooter), damage);
            if(stack.getItem() instanceof GunItem) {
                if(dead) {
                    ((GunItem) stack.getItem()).onKillEntity(this, entityLivingBase, stack, shooter);
                } else {
                    ((GunItem) stack.getItem()).onHitEntity(this, entityLivingBase, stack, shooter);
                }
            }
        }
    }

    @Override
    protected void entityInit() {
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        compound.setFloat("bullet_damage", this.damage);
        compound.setFloat("damage", this.ogDamage);
        compound.setInteger("effect", effect);
        NBTTagCompound nbt = new NBTTagCompound();
        stack.writeToNBT(nbt);
        compound.setTag("stack", nbt);
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        damage = compound.getFloat("bullet_damage");
        ogDamage = compound.getFloat("damage");
        effect = compound.getInteger("effect");
        stack = new ItemStack(compound.hasKey("stack") ? compound.getCompoundTag("stack") : new NBTTagCompound());
    }

    private boolean canEntityGetHeadshot(Entity e) {
        double ratio = e.height / e.width;
        return ratio > 1.0F;
    }

    private void updateHeading() {
        float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
        this.rotationYaw = (float) (MathHelper.atan2(this.motionX, this.motionZ) * (180D / Math.PI));
        this.rotationPitch = (float) (MathHelper.atan2(this.motionY, f) * (180D / Math.PI));
        this.prevRotationYaw = this.rotationYaw;
        this.prevRotationPitch = this.rotationPitch;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public float getOriginalDamage() {
        return ogDamage;
    }

    public float getDamage() {
        return damage;
    }

    public double getDistanceTo(Vec3d vec3d) {
        double x = posX - vec3d.x;
        double y = posY - vec3d.y;
        double z = posZ - vec3d.z;
        return Math.sqrt(x * x + y * y + z * z);
    }

    private void executeIfPlayer(Entity entity, Runnable runnable) {
        if(entity instanceof EntityPlayer) runnable.run();
    }
}
