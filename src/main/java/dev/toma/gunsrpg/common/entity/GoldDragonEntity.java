package dev.toma.gunsrpg.common.entity;

import dev.toma.gunsrpg.ai.AlwaysAggroOnGoal;
import dev.toma.gunsrpg.util.Interval;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonPartEntity;
import net.minecraft.entity.boss.dragon.phase.PhaseManager;
import net.minecraft.entity.boss.dragon.phase.PhaseType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.BossInfo;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerBossInfo;
import net.minecraft.world.server.ServerWorld;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class GoldDragonEntity extends EnderDragonEntity {

    private DragonStatsManager manager;
    private int cooldown;

    public GoldDragonEntity(EntityType<? extends GoldDragonEntity> type, World world) {
        super(type, world);
        phaseManager = new ExtendedPhaseManager(this);
        if (!world.isClientSide) {
            manager = new DragonStatsManager((ServerWorld) world, getDisplayName());
        }
        this.resetCooldown();
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MobEntity.createMobAttributes().add(Attributes.MAX_HEALTH, 1000);
    }

    @Override
    public boolean hurt(DamageSource p_70097_1_, float p_70097_2_) {
        return reallyHurt(p_70097_1_, p_70097_2_);
    }

    @Override
    public boolean hurt(EnderDragonPartEntity dragonPart, DamageSource source, float damage) {
        damage = this.getPhaseManager().getCurrentPhase().onHurt(source, damage);
        if (damage < 0.01F) {
            return false;
        } else {
            super.hurt(dragonPart, source, damage);
            if (this.getHealth() <= 0.0F && !this.getPhaseManager().getCurrentPhase().isSitting()) {
                this.setHealth(1.0F);
                this.getPhaseManager().setPhase(PhaseType.DYING);
            }
            return true;
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!level.isClientSide) {
            if (this.getHealth() < this.getMaxHealth() && tickCount % 12 == 0) {
                heal(1);
            }
            if (cooldown > 0) cooldown--;
        }
    }

    public void resetCooldown() {
        cooldown = Interval.minutes(1).getTicks();
    }

    @Override
    public void tick() {
        if (getPhaseManager().getCurrentPhase().getPhase() == PhaseType.DYING) {
            setDeltaMovement(0, 0, 0);
        }
        super.tick();
        if (!level.isClientSide) {
            manager.tick(this);
            trySummonHelp();
        }
    }

    @Override
    public void onRemovedFromWorld() {
        if (!level.isClientSide)
            manager.end();
        super.onRemovedFromWorld();
    }

    @Override
    protected boolean reallyHurt(DamageSource source, float amount) {
        amount *= 0.6;
        boolean result = super.reallyHurt(source, amount);
        invulnerableTime = 0;
        return result;
    }

    @Override
    public int findClosestNode() {
        if (this.nodes[0] == null) {
            for (int i = 0; i < 24; ++i) {
                int j = 5;
                int l;
                int i1;
                if (i < 12) {
                    l = (int) (60.0F * MathHelper.cos(2.0F * (-(float) Math.PI + 0.2617994F * (float) i)));
                    i1 = (int) (60.0F * MathHelper.sin(2.0F * (-(float) Math.PI + 0.2617994F * (float) i)));
                } else if (i < 20) {
                    int lvt_3_1_ = i - 12;
                    l = (int) (40.0F * MathHelper.cos(2.0F * (-(float) Math.PI + 0.3926991F * (float) lvt_3_1_)));
                    i1 = (int) (40.0F * MathHelper.sin(2.0F * (-(float) Math.PI + 0.3926991F * (float) lvt_3_1_)));
                    j += 10;
                } else {
                    int k1 = i - 20;
                    l = (int) (20.0F * MathHelper.cos(2.0F * (-(float) Math.PI + ((float) Math.PI / 4F) * (float) k1)));
                    i1 = (int) (20.0F * MathHelper.sin(2.0F * (-(float) Math.PI + ((float) Math.PI / 4F) * (float) k1)));
                }
                double x = getX();
                double z = getZ();
                int j1 = Math.max(this.level.getSeaLevel() + 30, this.level.getHeightmapPos(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, new BlockPos(x + l, 0, z + i1)).getY() + j);
                this.nodes[i] = new PathPoint((int) x + l, j1, (int) z + i1);
            }
            this.nodeAdjacency[0] = 6146;
            this.nodeAdjacency[1] = 8197;
            this.nodeAdjacency[2] = 8202;
            this.nodeAdjacency[3] = 16404;
            this.nodeAdjacency[4] = 32808;
            this.nodeAdjacency[5] = 32848;
            this.nodeAdjacency[6] = 65696;
            this.nodeAdjacency[7] = 131392;
            this.nodeAdjacency[8] = 131712;
            this.nodeAdjacency[9] = 263424;
            this.nodeAdjacency[10] = 526848;
            this.nodeAdjacency[11] = 525313;
            this.nodeAdjacency[12] = 1581057;
            this.nodeAdjacency[13] = 3166214;
            this.nodeAdjacency[14] = 2138120;
            this.nodeAdjacency[15] = 6373424;
            this.nodeAdjacency[16] = 4358208;
            this.nodeAdjacency[17] = 12910976;
            this.nodeAdjacency[18] = 9044480;
            this.nodeAdjacency[19] = 9706496;
            this.nodeAdjacency[20] = 15216640;
            this.nodeAdjacency[21] = 13688832;
            this.nodeAdjacency[22] = 11763712;
            this.nodeAdjacency[23] = 8257536;
        }

        return this.findClosestNode(this.getX(), this.getY(), this.getZ());
    }

    private void trySummonHelp() {
        if (cooldown == 0) {
            resetCooldown();
            List<? extends PlayerEntity> players = level.players();
            for (PlayerEntity player : players) {
                if (player.isSpectator() || !player.isAlive()) {
                    continue;
                }
                double distance = this.distanceToSqr(player);
                if (distance > 256 * 256) continue;
                BlockPos pos = player.blockPosition();
                spawnMob(pos, player, new ZombieGunnerEntity(level));
                spawnMob(pos, player, new ExplosiveSkeletonEntity(level));
                spawnMob(pos, player, new RocketAngelEntity(level), true);
                spawnMob(pos, player, new BloodmoonGolemEntity(level));
            }
        }
    }

    private void spawnMob(BlockPos pos, LivingEntity target, MobEntity mob) {
        spawnMob(pos, target, mob, false);
    }

    private void spawnMob(BlockPos pos, LivingEntity target, MobEntity mob, boolean airMob) {
        BlockPos spawnPos = genRandomPos(pos, level, airMob);
        mob.setPos(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
        mob.setTarget(target);
        mob.targetSelector.addGoal(0, new AlwaysAggroOnGoal<>(mob, false, target));
        mob.finalizeSpawn((ServerWorld) level, level.getCurrentDifficultyAt(spawnPos), SpawnReason.REINFORCEMENT, null, null);
        level.addFreshEntity(mob);
    }

    private BlockPos genRandomPos(BlockPos pos, World world, boolean inAir) {
        int minDist = 24;
        int x = pos.getX() + (random.nextBoolean() ? -minDist : minDist);
        int z = pos.getZ() + (random.nextBoolean() ? -minDist : minDist);
        int y = world.getHeight(Heightmap.Type.WORLD_SURFACE, x, z);
        if (inAir) {
            y += 16;
        }
        return new BlockPos(x, y, z);
    }

    static class ExtendedPhaseManager extends PhaseManager {

        private final GoldDragonEntity goldDragon;

        public ExtendedPhaseManager(GoldDragonEntity dragon) {
            super(dragon);
            this.goldDragon = dragon;
        }

        @Override
        public void setPhase(PhaseType<?> phaseIn) {
            boolean strafe = false;
            boolean charge = false;
            if (goldDragon == null) {
                super.setPhase(phaseIn);
                return;
            }
            LivingEntity entitylivingbase = goldDragon.level.getNearestPlayer(this.goldDragon, 150.0D);
            if (phaseIn == PhaseType.LANDING_APPROACH) {
                if (entitylivingbase == null) {
                    phaseIn = PhaseType.HOLDING_PATTERN;
                } else if (goldDragon.random.nextBoolean()) {
                    phaseIn = PhaseType.STRAFE_PLAYER;
                    strafe = true;
                } else {
                    phaseIn = PhaseType.CHARGING_PLAYER;
                    charge = true;
                }
            }
            super.setPhase(phaseIn);
            if (strafe) {
                if (entitylivingbase != null) {
                    getPhase(PhaseType.STRAFE_PLAYER).setTarget(entitylivingbase);
                }
            } else if (charge) {
                if (entitylivingbase != null) {
                    getPhase(PhaseType.CHARGING_PLAYER).setTarget(entitylivingbase.position());
                }
            }
        }
    }

    public static class DragonStatsManager {
        private final Predicate<Entity> inRange;
        private final ServerWorld level;
        private final ServerBossInfo bossInfoServer;
        private Vector3d dragonPos;
        private int ticksSinceLastCheck;

        protected DragonStatsManager(ServerWorld _level, ITextComponent displayName) {
            level = _level;
            inRange = e -> {
                double maxRange = 192.0 * 192.0;
                double dist = e.distanceToSqr(dragonPos.x, dragonPos.y, dragonPos.z);
                return e.isAlive() && dist <= maxRange;
            };
            bossInfoServer = (ServerBossInfo) (new ServerBossInfo(displayName, BossInfo.Color.YELLOW, BossInfo.Overlay.PROGRESS).setCreateWorldFog(true).setDarkenScreen(true));
            bossInfoServer.setVisible(true);
        }

        public void tick(GoldDragonEntity dragon) {
            bossInfoServer.setPercent(dragon.getHealth() / dragon.getMaxHealth());
            if (++ticksSinceLastCheck >= 20) {
                dragonPos = dragon.position();
                sendStatusUpdateToPlayers();
                ticksSinceLastCheck = 0;
            }
        }

        public void end() {
            bossInfoServer.removeAllPlayers();
            bossInfoServer.setVisible(false);
        }

        private void sendStatusUpdateToPlayers() {
            Set<ServerPlayerEntity> set = new HashSet<>();
            for (ServerPlayerEntity player : level.getPlayers(inRange)) {
                bossInfoServer.addPlayer(player);
                set.add(player);
            }
            Set<ServerPlayerEntity> set1 = new HashSet<>(bossInfoServer.getPlayers());
            set1.removeAll(set);
            for (ServerPlayerEntity player : set1) {
                bossInfoServer.removePlayer(player);
            }
        }
    }
}
