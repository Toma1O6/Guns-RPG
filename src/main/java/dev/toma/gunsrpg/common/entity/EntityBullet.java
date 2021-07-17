package dev.toma.gunsrpg.common.entity;

import dev.toma.gunsrpg.common.GunDamageSourceHack;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.init.GRPGEffects;
import dev.toma.gunsrpg.common.init.GRPGItems;
import dev.toma.gunsrpg.common.init.GRPGSounds;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.config.gun.IWeaponConfiguration;
import dev.toma.gunsrpg.util.ModUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SSpawnParticlePacket;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.*;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.entity.PartEntity;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.List;

public class EntityBullet extends Entity {

    protected LivingEntity shooter;
    protected ItemStack stack;
    protected float damage;
    protected boolean canPenetrateEntity;
    private int effect;
    private float ogDamage;
    private Entity lastHitEntity;

    public EntityBullet(EntityType<? extends EntityBullet> type, World worldIn) {
        super(type, worldIn);
    }

    public EntityBullet(EntityType<? extends EntityBullet> type, World worldIn, LivingEntity shooter, GunItem gun, ItemStack stack) {
        this(type, worldIn);
        this.noPhysics = true;
        this.shooter = shooter;
        this.stack = stack;
        IWeaponConfiguration config = gun.getWeaponConfig();
        this.effect = config.getGravityDelay();
        this.ogDamage = config.getDamage() + gun.getDamageBonus(stack);
        this.damage = ogDamage;
        EffectInstance effect = shooter.getEffect(GRPGEffects.GUN_DAMAGE_BUFF.get());
        if (effect != null) {
            damage *= 1.0F + (0.2F * (effect.getAmplifier() + 1));
        }
        this.setPos(shooter.getX(), shooter.getY() + shooter.getEyeHeight(), shooter.getZ());
        if (shooter instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) shooter;
            canPenetrateEntity = gun == GRPGItems.SNIPER_RIFLE && PlayerDataFactory.hasActiveSkill(player, Skills.SR_PENETRATOR);
        }
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public void fire(float pitch, float yaw, float velocity) {
        Vector3d vec = Vector3d.directionFromRotation(pitch, yaw);
        setDeltaMovement(vec.multiply(velocity, velocity, velocity));
        updateHeading();
        if (!level.isClientSide && shooter instanceof PlayerEntity && !((GunItem) stack.getItem()).isSilenced((PlayerEntity) shooter)) {
            List<MobEntity> entityList = level.getEntitiesOfClass(MobEntity.class, this.getBoundingBox().inflate(25));
            for (MobEntity mobEntity : entityList) {
                if (mobEntity == shooter) continue;
                mobEntity.setTarget(shooter);
            }
        }
    }

    public LivingEntity getShooter() {
        return shooter;
    }

    protected void modifyPenetrationDamage() {
        damage /= 2;
    }

    protected void hit(RayTraceResult result) {
        RayTraceResult.Type type = result.getType();
        if (type == RayTraceResult.Type.ENTITY) {
            onEntityHit((EntityRayTraceResult) result);
        } else if (type == RayTraceResult.Type.BLOCK) {
            onBlockHit((BlockRayTraceResult) result);
        }
    }

    protected void onEntityHit(EntityRayTraceResult result) {
        Entity entity = result.getEntity();
        if (!level.isClientSide) {
            Vector3d hitLocation = result.getLocation();
            boolean validWeaponForHeadshot = stack.getItem() == GRPGItems.SNIPER_RIFLE;
            boolean canHeadshot = validWeaponForHeadshot && shooter instanceof PlayerEntity && PlayerDataFactory.hasActiveSkill((PlayerEntity) shooter, Skills.SR_DEAD_EYE);
            boolean isHeadshot = canHeadshot && canEntityGetHeadshot(entity) && hitLocation.y >= entity.position().y + entity.getEyeHeight() - 0.15F;
            if (isHeadshot)
                damage *= 1.5F;
            if (entity instanceof LivingEntity) {
                ModUtils.sendWorldPacketVanilla(level, new SSpawnParticlePacket(
                        new BlockParticleData(ParticleTypes.BLOCK, Blocks.REDSTONE_BLOCK.defaultBlockState()),
                        true,
                        hitLocation.x,
                        hitLocation.y,
                        hitLocation.z,
                        ModUtils.randomValue(random, 0.2F),
                        ModUtils.randomValue(random, 0.2F),
                        ModUtils.randomValue(random, 0.2F),
                        0.2F,
                        2 * Math.round(damage)
                ));
            }
            onEntityHit(isHeadshot, entity);
            entity.invulnerableTime = 0;
            if (canPenetrateEntity && lastHitEntity == null) {
                lastHitEntity = entity;
                modifyPenetrationDamage();
                doCollisionCheck(position(), position().add(getDeltaMovement()));
            } else {
                remove();
            }
        }
    }

    protected void onBlockHit(BlockRayTraceResult result) {
        BlockPos pos = result.getBlockPos();
        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();
        boolean canPen = false;
        if (state.getMaterial() == Material.GLASS) {
            level.destroyBlock(pos, false);
            canPen = true;
        } else if (!block.getCollisionShape(state, level, pos, ISelectionContext.empty()).isEmpty()) {
            Vector3d hitLocation = result.getLocation();
            ModUtils.sendWorldPacketVanilla(level, new SSpawnParticlePacket(
                    new BlockParticleData(ParticleTypes.BLOCK, state),
                    true,
                    hitLocation.x,
                    hitLocation.y,
                    hitLocation.z,
                    ModUtils.randomValue(random, 0.15F),
                    ModUtils.randomValue(random, 0.30F),
                    ModUtils.randomValue(random, 0.15F),
                    0.15F,
                    7
            ));
            SoundType type = block.getSoundType(state, level, pos, this);
            level.playSound(null, hitLocation.x, hitLocation.y, hitLocation.z, type.getBreakSound(), SoundCategory.BLOCKS, 0.8F, type.getPitch() * 0.8F);
            remove();
        }

        if(canPen && damage > 0) {
            doCollisionCheck(position(), position().add(getDeltaMovement()));
        }
    }

    public boolean isLimitedLifetime() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        updateHeading();

        Vector3d vec1 = position();
        Vector3d vec2 = vec1.add(getDeltaMovement());
        doCollisionCheck(vec1, vec2);

        if (this.tickCount > effect && !level.isClientSide) {
            setDeltaMovement(getDeltaMovement().add(0.0, -0.05, 0.0));
        }

        if (tickCount > 2) {
            level.playSound(null, getX(), getY(), getZ(), GRPGSounds.BULLET_WHIZZ, SoundCategory.MASTER, 0.6F, 1.0F);
        }

        if (isLimitedLifetime() && this.tickCount >= 80) {
            this.remove();
        }

        move(MoverType.SELF, getDeltaMovement());
    }

    protected void doCollisionCheck(Vector3d vec1, Vector3d vec2) {
        RayTraceResult raytraceresult = this.level.clip(new RayTraceContext(vec1, vec2, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this));
        if (raytraceresult.getType() != RayTraceResult.Type.MISS) {
            vec2 = raytraceresult.getLocation();
        }
        EntityRayTraceResult entityRayTraceResult = findEntityOnPath(vec1, vec2);
        if (entityRayTraceResult != null) {
            raytraceresult = entityRayTraceResult;
        }
        if (raytraceresult != null && raytraceresult.getType() == RayTraceResult.Type.ENTITY) {
            Entity target = ((EntityRayTraceResult) raytraceresult).getEntity();
            Entity owner = shooter;
            if (target instanceof PlayerEntity && owner instanceof PlayerEntity && !((PlayerEntity) owner).canHarmPlayer((PlayerEntity) target)) {
                raytraceresult = null;
            }
        }
        if (raytraceresult != null && raytraceresult.getType() != RayTraceResult.Type.MISS && !ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
            hit(raytraceresult);
        }
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double sqrDist) {
        return false;
    }

    protected EntityRayTraceResult findEntityOnPath(Vector3d start, Vector3d end) {
        return ProjectileHelper.getEntityHitResult(level, this, start, end, getBoundingBox().expandTowards(getDeltaMovement()).inflate(1.0), this::canHitEntity);
    }

    protected boolean canHitEntity(Entity entity) {
        return entity.canBeCollidedWith() && !entity.isSpectator() && entity.isAlive() && entity != lastHitEntity && entity != shooter;
    }

    protected void damageTargetEntity(Entity target, boolean isHeadshot) {
        target.hurt(new GunDamageSourceHack(shooter, this, stack), damage);
    }

    protected void onEntityHit(boolean isHeadshot, Entity entity) {
        if (entity instanceof LivingEntity) {
            LivingEntity entityLivingBase = (LivingEntity) entity;
            boolean dead = entityLivingBase.getHealth() - damage <= 0;
            damageTargetEntity(entity, isHeadshot);
            if (stack.getItem() instanceof GunItem) {
                if (dead) {
                    ((GunItem) stack.getItem()).onKillEntity(this, entityLivingBase, stack, shooter);
                } else {
                    ((GunItem) stack.getItem()).onHitEntity(this, entityLivingBase, stack, shooter);
                }
            }
        } else if (entity instanceof PartEntity<?>) {
            entity.hurt(new GunDamageSourceHack(shooter, this, stack), damage);
        }
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT compound) {
        compound.putFloat("bullet_damage", this.damage);
        compound.putFloat("damage", this.ogDamage);
        compound.putInt("effect", effect);
        compound.put("stack", stack.save(new CompoundNBT()));
    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT compound) {
        damage = compound.getFloat("bullet_damage");
        ogDamage = compound.getFloat("damage");
        effect = compound.getInt("effect");
        stack = ItemStack.of(compound.contains("stack", Constants.NBT.TAG_COMPOUND) ? compound.getCompound("stack") : new CompoundNBT());
    }

    private boolean canEntityGetHeadshot(Entity e) {
        EntitySize size = e.getDimensions(Pose.STANDING);
        double ratio = size.height / size.width;
        return ratio > 1.0F;
    }

    private void updateHeading() {
        Vector3d motion = getDeltaMovement();
        float f = MathHelper.sqrt(motion.x * motion.x + motion.z * motion.z);
        this.yRot = (float) (MathHelper.atan2(motion.x, motion.z) * (180D / Math.PI));
        this.xRot = (float) (MathHelper.atan2(motion.y, f) * (180D / Math.PI));
        this.yRotO = this.yRot;
        this.xRotO = this.xRot;
    }

    public float getOriginalDamage() {
        return ogDamage;
    }

    public float getDamage() {
        return damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public double getDistanceTo(Vector3d vec3d) {
        double x = getX() - vec3d.x;
        double y = getY() - vec3d.y;
        double z = getZ() - vec3d.z;
        return Math.sqrt(x * x + y * y + z * z);
    }
}
