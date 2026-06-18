package com.wf.firearms.grenade;



import net.minecraft.core.BlockPos;

import net.minecraft.core.particles.ParticleTypes;

import net.minecraft.server.level.ServerLevel;

import net.minecraft.sounds.SoundEvents;

import net.minecraft.sounds.SoundSource;

import net.minecraft.util.Mth;

import net.minecraft.world.damagesource.DamageSource;

import net.minecraft.world.entity.Entity;

import net.minecraft.world.entity.LivingEntity;

import net.minecraft.world.item.ItemStack;

import net.minecraft.world.item.enchantment.EnchantmentHelper;

import net.minecraft.world.item.enchantment.Enchantments;

import net.minecraft.world.level.Level;

import net.minecraft.world.level.block.state.BlockState;

import net.minecraft.world.phys.AABB;

import net.minecraft.world.phys.Vec3;



import javax.annotation.Nullable;



/** 手雷爆炸：TNT 特效 + 实体伤害 + 仅破坏低硬度方块。 */

public final class GrenadeExplosionHelper {

    /** 可破坏方块的最大硬度（约等于土/木板/玻璃；石头 1.5、黑曜石更高）。 */

    private static final float MAX_BLOCK_HARDNESS = 2.0f;



    private GrenadeExplosionHelper() {}



    public static void explode(

            Level level,

            Entity source,

            @Nullable Entity owner,

            Vec3 center,

            float blastRadius,

            float maxDamage) {

        if (level.isClientSide || maxDamage <= 0f || blastRadius <= 0f) {

            return;

        }

        Vec3 pos = center.add(0, 0.5, 0);

        playExplosionEffects(level, pos, blastRadius);

        if (level instanceof ServerLevel server) {
            server.explode(
                    source,
                    pos.x,
                    pos.y,
                    pos.z,
                    Math.max(1.0f, blastRadius * 0.65f),
                    Level.ExplosionInteraction.NONE);
        }

        breakSoftBlocks(level, pos, blastRadius);

        applyEntityDamage(level, source, owner, pos, blastRadius, maxDamage);

    }



    private static void playExplosionEffects(Level level, Vec3 pos, float radius) {

        float pitch = 0.9f + level.random.nextFloat() * 0.2f;

        level.playSound(

                null,

                pos.x,

                pos.y,

                pos.z,

                SoundEvents.GENERIC_EXPLODE,

                SoundSource.BLOCKS,

                Math.min(4.0f, radius),

                pitch);

        if (level instanceof ServerLevel server) {

            server.sendParticles(

                    ParticleTypes.EXPLOSION_EMITTER,

                    pos.x,

                    pos.y,

                    pos.z,

                    1,

                    0,

                    0,

                    0,

                    0);

            server.sendParticles(

                    ParticleTypes.EXPLOSION,

                    pos.x,

                    pos.y,

                    pos.z,

                    (int) (radius * 2),

                    0.2,

                    0.2,

                    0.2,

                    0.05);

        }

    }



    private static void breakSoftBlocks(Level level, Vec3 center, float radius) {

        int r = Mth.ceil(radius);

        BlockPos origin = BlockPos.containing(center);

        BlockPos.betweenClosedStream(

                        origin.offset(-r, -r, -r), origin.offset(r, r, r))

                .forEach(pos -> {

                    if (center.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5)

                            > radius * radius) {

                        return;

                    }

                    BlockState state = level.getBlockState(pos);

                    if (state.isAir()) {

                        return;

                    }

                    float hardness = state.getDestroySpeed(level, pos);

                    if (hardness < 0 || hardness > MAX_BLOCK_HARDNESS) {

                        return;

                    }

                    level.destroyBlock(pos, true);

                });

    }



    /** 实体伤害：仅按与爆心距离衰减，不受方块遮挡（原版 TNT 式 {@code getSeenPercent} 已移除）。 */
    private static void applyEntityDamage(

            Level level,

            Entity source,

            @Nullable Entity owner,

            Vec3 explosionPos,

            float blastRadius,

            float maxDamage) {

        double reach = blastRadius * 2.0;

        AABB box =

                new AABB(

                        explosionPos.x - reach,

                        explosionPos.y - reach,

                        explosionPos.z - reach,

                        explosionPos.x + reach,

                        explosionPos.y + reach,

                        explosionPos.z + reach);

        DamageSource dmg = level.damageSources().explosion(source, owner);

        for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class, box, Entity::isAlive)) {

            if (owner != null && target == owner) {

                continue;

            }

            double distNorm = Math.sqrt(target.distanceToSqr(explosionPos)) / blastRadius;
            if (distNorm > 1.0) {
                continue;
            }
            double impact = 1.0 - distNorm;
            float amount = (float) (impact * maxDamage);

            amount *= blastProtectionFactor(target);

            if (amount > 0.5f) {

                target.invulnerableTime = 0;

                target.hurt(dmg, amount);

            }

        }

    }



    private static float blastProtectionFactor(LivingEntity target) {

        int prot = 0;

        for (ItemStack stack : target.getArmorSlots()) {

            prot += EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLAST_PROTECTION, stack);

        }

        float reduction = Math.min(20, prot) / 25.0f;

        return Math.max(0.05f, 1.0f - reduction);

    }

}


