package dev.toma.gunsrpg.common.entity.projectile;

import dev.toma.gunsrpg.common.attribute.Attribs;
import dev.toma.gunsrpg.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.util.ModUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.play.server.SSpawnParticlePacket;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.Random;

public abstract class AbstractProjectileConfig implements IProjectileConfig {

    private final LivingEntity owner;
    private Projectile projectile;
    private IPenetrationConfig config;

    public AbstractProjectileConfig(LivingEntity owner) {
        this.owner = owner;
        this.config = createPenConfig();
    }

    public abstract IPenetrationConfig createPenConfig();

    @Override
    public void hitBlock(BlockRayTraceResult result) {
        World level = projectile.level;
        BlockPos pos = result.getBlockPos();
        BlockState state = level.getBlockState(pos);
        boolean canContinue = true;
        if (state.getMaterial() == Material.GLASS) {
            if (projectile.getProjectileDamage() < 2.0F) {
                canContinue = false;
            } else {
                level.destroyBlock(pos, false);
                projectile.reduceDamage(2.0F);
            }
        }
        if (!canContinue && !state.getCollisionShape(level, pos).isEmpty()) {
            state.onProjectileHit(level, state, result, projectile);
            Vector3d hitPos = result.getLocation();
            Random random = level.getRandom();
            ModUtils.sendWorldPacketVanilla(level, new SSpawnParticlePacket(
                    new BlockParticleData(ParticleTypes.BLOCK, state),
                    true,
                    hitPos.x,
                    hitPos.y,
                    hitPos.z,
                    ModUtils.randomValue(random, 0.15F),
                    ModUtils.randomValue(random, 0.30F),
                    ModUtils.randomValue(random, 0.15F),
                    0.15F,
                    7
            ));
            SoundType type = state.getSoundType();
            level.playSound(null, hitPos.x, hitPos.y, hitPos.z, type.getBreakSound(), SoundCategory.BLOCKS, 0.8F, type.getPitch() * 0.8F);
            projectile.markInvalid();
        }

        if (!projectile.invalid && projectile.getProjectileDamage() > 0) {
            Vector3d v1 = projectile.position();
            Vector3d v2 = v1.add(projectile.getDeltaMovement());
            projectile.checkForCollisions(v1, v2);
        }
    }

    @Override
    public void hitEntity(EntityRayTraceResult result) {
        World level = projectile.level;
        Entity entity = result.getEntity();
        if (!level.isClientSide()) {
            Vector3d hitPos = result.getLocation();
            Entity src = getOwner();
            boolean isHeadshot = projectile.canHeadshotEntity(entity) && hitPos.y >= entity.position().y + entity.getEyeHeight() - 0.15f;
            // blood particles
            boolean isLivingEntity = false;
            if (entity instanceof LivingEntity) {
                isLivingEntity = true;
                Random random = level.getRandom();
                ModUtils.sendWorldPacketVanilla(level, new SSpawnParticlePacket(
                        new BlockParticleData(ParticleTypes.BLOCK, Blocks.REDSTONE_BLOCK.defaultBlockState()),
                        true,
                        hitPos.x,
                        hitPos.y,
                        hitPos.z,
                        ModUtils.randomValue(random, 0.2F),
                        ModUtils.randomValue(random, 0.2F),
                        ModUtils.randomValue(random, 0.2F),
                        0.2F,
                        2 * Math.round(projectile.getProjectileDamage())
                ));
            }
            projectile.applyWeaponEffects(entity, src, isLivingEntity, isHeadshot); // damage, gun effects
            entity.invulnerableTime = 0;
            if (config.isSupported() && !config.hasPenetrated()) { // pen
                config.onHit(entity, projectile);
                Vector3d v1 = projectile.position();
                Vector3d v2 = v1.add(projectile.getDeltaMovement());
                projectile.checkForCollisions(v1, v2);
            } else {
                projectile.remove();
            }
        }
    }

    @Override
    public void updateDirection() {
        Vector3d delta = projectile.getDeltaMovement();
        float motionSqrt = MathHelper.sqrt(delta.x * delta.x + delta.z * delta.z);
        projectile.yRot = (float) (MathHelper.atan2(delta.x, delta.z) * (180.0F / Math.PI));
        projectile.xRot = (float) (MathHelper.atan2(delta.y, motionSqrt) * (180.0F / Math.PI));
        projectile.yRotO = projectile.yRot;
        projectile.xRotO = projectile.xRot;
    }

    @Override
    public double getNoiseMultiplier(IAttributeProvider provider) {
        return provider.getAttributeValue(Attribs.WEAPON_NOISE);
    }

    @Override
    public double getHeadshotMultiplier(IAttributeProvider provider) {
        return provider.getAttributeValue(Attribs.HEADSHOT_DAMAGE);
    }

    @Override
    public final LivingEntity getOwner() {
        return owner;
    }

    @Override
    public final void assignControlObj(Projectile projectile) {
        this.projectile = projectile;
    }

    @Override
    public final IPenetrationConfig getPenetrationConfig() {
        return config;
    }

    public final Projectile projectile() {
        return projectile;
    }
}
