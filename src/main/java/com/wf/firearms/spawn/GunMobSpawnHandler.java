package com.wf.firearms.spawn;

import com.wf.firearms.GunsRpg;
import com.wf.firearms.config.MobSpawnConfig;
import com.wf.firearms.config.MobSpawnConfig.DimensionSpawn;
import com.wf.firearms.config.MobSpawnConfig.MobEntry;
import com.wf.firearms.entity.ExplosiveSkeletonEntity;
import com.wf.firearms.entity.RocketAngelEntity;
import com.wf.firearms.entity.ZombieGunnerEntity;
import com.wf.firearms.mob.GunMobDifficulty;
import com.wf.firearms.registry.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GunsRpg.MOD_ID)
public final class GunMobSpawnHandler {
    private GunMobSpawnHandler() {}

    @SubscribeEvent
    public static void onSpawnPlacement(SpawnPlacementRegisterEvent event) {
        event.register(
                ModEntities.ZOMBIE_GUNNER.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkMonsterSpawnRules,
                SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(
                ModEntities.EXPLOSIVE_SKELETON.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkMonsterSpawnRules,
                SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(
                ModEntities.ROCKET_ANGEL.get(),
                SpawnPlacements.Type.NO_RESTRICTIONS,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MobSpawnPlacement::checkRocketAngelSpawn,
                SpawnPlacementRegisterEvent.Operation.REPLACE);
    }

    @SubscribeEvent
    public static void onFinalizeSpawn(MobSpawnEvent.FinalizeSpawn event) {
        if (event.getLevel().isClientSide() || !(event.getLevel() instanceof ServerLevel level)) {
            return;
        }
        MobSpawnType spawnType = event.getSpawnType();
        if (spawnType != MobSpawnType.NATURAL
                && spawnType != MobSpawnType.PATROL
                && spawnType != MobSpawnType.REINFORCEMENT) {
            return;
        }
        Mob mob = event.getEntity();
        ResourceLocation dim = level.dimension().location();
        BlockPos pos = mob.blockPosition();

        if (mob.getType() == EntityType.ZOMBIE && mob instanceof Zombie zombie) {
            tryReplaceZombieWithGunner(level, zombie, dim, pos, event);
            return;
        }
        if (mob.getType() == EntityType.SKELETON && mob instanceof Skeleton skeleton) {
            tryReplaceSkeletonWithGrenadier(level, skeleton, dim, pos, event);
            return;
        }
        if (mob instanceof RocketAngelEntity angel) {
            handleRocketAngel(level, angel, dim, pos, event);
        } else if (mob instanceof ZombieGunnerEntity) {
            handleGunner(level, dim, pos, event);
        } else if (mob instanceof ExplosiveSkeletonEntity) {
            handleExplosiveSkeleton(level, dim, pos, event);
        }
    }

    private static void tryReplaceZombieWithGunner(
            ServerLevel level,
            Zombie zombie,
            ResourceLocation dim,
            BlockPos pos,
            MobSpawnEvent.FinalizeSpawn event) {
        MobEntry entry = MobSpawnConfig.mob("gunsrpg:zombie_gunner");
        if (entry == null || !entry.enabled()) {
            return;
        }
        if (shouldExcludeBiome(level.getBiome(pos), entry)) {
            return;
        }
        long day = level.getDayTime() / 24000L;
        int start = Math.max(entry.spawnStartDay(), MobSpawnConfig.globalStartDayForDimension(dim));
        if (day < start) {
            return;
        }
        DimensionSpawn ds = entry.dimensions().get(dim.toString());
        if (ds == null || ds.weight() <= 0) {
            return;
        }
        GunMobDifficulty.Context ctx = GunMobDifficulty.evaluate(level, pos, dim, start);
        if (level.random.nextFloat() > GunMobDifficulty.passChance(ds.weight(), ctx.spawnPassMultiplier())) {
            return;
        }
        ZombieGunnerEntity gunner = ModEntities.ZOMBIE_GUNNER.get().create(level);
        if (gunner == null) {
            return;
        }
        gunner.moveTo(zombie.getX(), zombie.getY(), zombie.getZ(), zombie.getYRot(), zombie.getXRot());
        gunner.finalizeSpawn(
                level,
                level.getCurrentDifficultyAt(pos),
                event.getSpawnType(),
                event.getSpawnData(),
                null);
        level.addFreshEntity(gunner);
        event.setSpawnCancelled(true);
    }

    private static void tryReplaceSkeletonWithGrenadier(
            ServerLevel level,
            Skeleton skeleton,
            ResourceLocation dim,
            BlockPos pos,
            MobSpawnEvent.FinalizeSpawn event) {
        MobEntry entry = MobSpawnConfig.mob("gunsrpg:explosive_skeleton");
        if (entry == null || !entry.enabled()) {
            return;
        }
        if (shouldExcludeBiome(level.getBiome(pos), entry)) {
            return;
        }
        long day = level.getDayTime() / 24000L;
        int start = Math.max(entry.spawnStartDay(), MobSpawnConfig.globalStartDayForDimension(dim));
        if (day < start) {
            return;
        }
        DimensionSpawn ds = entry.dimensions().get(dim.toString());
        if (ds == null || ds.weight() <= 0) {
            return;
        }
        GunMobDifficulty.Context ctx = GunMobDifficulty.evaluate(level, pos, dim, start);
        if (level.random.nextFloat() > GunMobDifficulty.passChance(ds.weight(), ctx.spawnPassMultiplier())) {
            return;
        }
        ExplosiveSkeletonEntity grenadier = ModEntities.EXPLOSIVE_SKELETON.get().create(level);
        if (grenadier == null) {
            return;
        }
        grenadier.moveTo(skeleton.getX(), skeleton.getY(), skeleton.getZ(), skeleton.getYRot(), skeleton.getXRot());
        grenadier.finalizeSpawn(
                level,
                level.getCurrentDifficultyAt(pos),
                event.getSpawnType(),
                event.getSpawnData(),
                null);
        level.addFreshEntity(grenadier);
        event.setSpawnCancelled(true);
    }

    private static void handleExplosiveSkeleton(
            ServerLevel level, ResourceLocation dim, BlockPos pos, MobSpawnEvent.FinalizeSpawn event) {
        MobEntry entry = MobSpawnConfig.mob("gunsrpg:explosive_skeleton");
        if (entry == null || !entry.enabled()) {
            event.setSpawnCancelled(true);
            return;
        }
        if (shouldExcludeBiome(level.getBiome(pos), entry)) {
            event.setSpawnCancelled(true);
            return;
        }
        long day = level.getDayTime() / 24000L;
        int start = Math.max(entry.spawnStartDay(), MobSpawnConfig.globalStartDayForDimension(dim));
        if (day < start) {
            event.setSpawnCancelled(true);
            return;
        }
        DimensionSpawn ds = entry.dimensions().get(dim.toString());
        if (ds == null || ds.weight() <= 0) {
            event.setSpawnCancelled(true);
            return;
        }
        GunMobDifficulty.Context ctx = GunMobDifficulty.evaluate(level, pos, dim, start);
        if (level.random.nextFloat() > GunMobDifficulty.passChance(ds.weight(), ctx.spawnPassMultiplier())) {
            event.setSpawnCancelled(true);
        }
    }

    private static void handleGunner(
            ServerLevel level, ResourceLocation dim, BlockPos pos, MobSpawnEvent.FinalizeSpawn event) {
        MobEntry entry = MobSpawnConfig.mob("gunsrpg:zombie_gunner");
        if (entry == null || !entry.enabled()) {
            event.setSpawnCancelled(true);
            return;
        }
        if (shouldExcludeBiome(level.getBiome(pos), entry)) {
            event.setSpawnCancelled(true);
            return;
        }
        long day = level.getDayTime() / 24000L;
        int start = Math.max(entry.spawnStartDay(), MobSpawnConfig.globalStartDayForDimension(dim));
        if (day < start) {
            event.setSpawnCancelled(true);
            return;
        }
        DimensionSpawn ds = entry.dimensions().get(dim.toString());
        if (ds == null || ds.weight() <= 0) {
            event.setSpawnCancelled(true);
            return;
        }
        GunMobDifficulty.Context ctx = GunMobDifficulty.evaluate(level, pos, dim, start);
        if (level.random.nextFloat() > GunMobDifficulty.passChance(ds.weight(), ctx.spawnPassMultiplier())) {
            event.setSpawnCancelled(true);
        }
    }

    private static void handleRocketAngel(
            ServerLevel level,
            RocketAngelEntity angel,
            ResourceLocation dim,
            BlockPos pos,
            MobSpawnEvent.FinalizeSpawn event) {
        MobEntry entry = MobSpawnConfig.mob("gunsrpg:rocket_angel");
        if (entry == null || !entry.enabled()) {
            event.setSpawnCancelled(true);
            return;
        }
        DimensionSpawn ds = entry.dimensions().get(dim.toString());
        if (ds == null) {
            return;
        }
        long day = level.getDayTime() / 24000L;
        int start = Math.max(entry.spawnStartDay(), MobSpawnConfig.globalStartDayForDimension(dim));
        if ("minecraft:overworld".equals(dim.toString()) && day < start) {
            event.setSpawnCancelled(true);
            return;
        }
        if (ds.mainIslandOnly() && "minecraft:the_end".equals(dim.toString())) {
            double dist = Math.sqrt((double) pos.getX() * pos.getX() + (double) pos.getZ() * pos.getZ());
            if (dist > ds.maxHorizontalDistanceFromOrigin() || pos.getY() < ds.minY()) {
                event.setSpawnCancelled(true);
                return;
            }
            applyEndIslandBoost(angel, entry);
        }
        if (ds.weight() > 0 && day >= start) {
            GunMobDifficulty.Context ctx = GunMobDifficulty.evaluate(level, pos, dim, start);
            if (level.random.nextFloat() > GunMobDifficulty.passChance(ds.weight(), ctx.spawnPassMultiplier())) {
                event.setSpawnCancelled(true);
            }
        }
    }

    static void applyEndIslandBoost(RocketAngelEntity angel, MobEntry entry) {
        var boost = entry.endBoost();
        if (boost == null || !boost.enabled()) {
            return;
        }
        AttributeInstance health = angel.getAttribute(Attributes.MAX_HEALTH);
        if (health != null) {
            health.setBaseValue(health.getBaseValue() * boost.healthMultiplier());
            angel.setHealth(angel.getMaxHealth());
        }
        AttributeInstance dmg = angel.getAttribute(Attributes.ATTACK_DAMAGE);
        if (dmg != null) {
            dmg.setBaseValue(dmg.getBaseValue() * boost.attackDamageMultiplier());
        }
        AttributeInstance follow = angel.getAttribute(Attributes.FOLLOW_RANGE);
        if (follow != null && boost.followRangeBonus() > 0) {
            follow.setBaseValue(follow.getBaseValue() + boost.followRangeBonus());
        }
    }

    private static boolean shouldExcludeBiome(net.minecraft.core.Holder<Biome> biomeHolder, MobEntry entry) {
        if (entry.excludeBiomeCategories().contains("ocean") && biomeHolder.is(BiomeTags.IS_OCEAN)) {
            return true;
        }
        return entry.excludeBiomeCategories().contains("river") && biomeHolder.is(BiomeTags.IS_RIVER);
    }
}
