package dev.toma.gunsrpg.common.entity;

import dev.toma.gunsrpg.GunsRPG;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
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
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.BossInfo;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerBossInfo;
import net.minecraft.world.server.ServerWorld;

import java.util.*;
import java.util.function.Predicate;

public class GoldDragonEntity extends EnderDragonEntity {

    private static final AttributeModifier MAX_HEALTH_MODIFIER = new AttributeModifier(UUID.fromString("9651DBF8-5926-45FC-9608-9EBED5F2A0D0"), "maxHealthModifier", 800.0D, AttributeModifier.Operation.ADDITION);
    private DragonStatsManager manager;
    private int cooldown;

    public GoldDragonEntity(EntityType<? extends GoldDragonEntity> type, World world) {
        super(type, world);
        phaseManager = new ExtendedPhaseManager(this);
        if (!world.isClientSide) {
            manager = new DragonStatsManager((ServerWorld) world, getDisplayName());
        }
        // workaround for max health attribute, as all children of ender dragon entity are forced to use vanilla's dragon attribute values
        //getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(MAX_HEALTH_MODIFIER);
        //setHealth(getMaxHealth());
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
        cooldown = 800;
    }

    @Override
    public void tick() {
        if (getPhaseManager().getCurrentPhase().getPhase() == PhaseType.DYING) {
            setDeltaMovement(0, 0, 0);
        }
        super.tick();
        if (!level.isClientSide)
            manager.tick(this);
    }

    @Override
    public void onRemovedFromWorld() {
        if (!level.isClientSide)
            manager.end();
        super.onRemovedFromWorld();
    }

    @Override
    protected boolean reallyHurt(DamageSource source, float amount) {
        amount *= 0.35;
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
                    trySummonHelp(goldDragon.level);
                }
            } else if (charge) {
                if (entitylivingbase != null) {
                    getPhase(PhaseType.CHARGING_PLAYER).setTarget(entitylivingbase.position());
                    trySummonHelp(goldDragon.level);
                }
            }
        }

        private void trySummonHelp(World world) {
            if (goldDragon.cooldown == 0) {
                goldDragon.resetCooldown();
                List<? extends PlayerEntity> players = world.players();
                for (PlayerEntity player : players) {
                    if (player.isSpectator() || !player.isAlive()) {
                        continue;
                    }
                    double distance = Math.sqrt(goldDragon.distanceToSqr(player));
                    if (distance > 100) continue;
                    BlockPos pos = player.blockPosition();
                    int entitiesAround = world.getEntitiesOfClass(MobEntity.class, player.getBoundingBox().inflate(10)).size();
                    if (entitiesAround > 15) {
                        GunsRPG.log.debug("Aborting summoning dragon help at {}, there are too many mobs! ({})", player.getName(), entitiesAround);
                        continue;
                    }
                    spawnMob(pos, player, new ZombieGunnerEntity(world));
                    spawnMob(pos, player, new ZombieGunnerEntity(world));
                    spawnMob(pos, player, new RocketAngelEntity(world));
                }
            }
        }

        private void spawnMob(BlockPos pos, LivingEntity target, MobEntity mob) {
            BlockPos spawnPos = genRandomPos(pos, target.level);
            mob.setPos(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
            mob.setTarget(target);
            mob.finalizeSpawn((ServerWorld) target.level, target.level.getCurrentDifficultyAt(spawnPos), SpawnReason.REINFORCEMENT, null, null);
            target.level.addFreshEntity(mob);
        }

        private BlockPos genRandomPos(BlockPos pos, World world) {
            Random random = goldDragon.random;
            int x = pos.getX() + random.nextInt(15) - random.nextInt(15);
            int z = pos.getZ() + random.nextInt(15) - random.nextInt(15);
            int y = pos.getY() + 15;
            BlockPos.Mutable mutableBlockPos = new BlockPos.Mutable(x, y, z);
            while (world.isEmptyBlock(mutableBlockPos.below())) {
                mutableBlockPos.setY(mutableBlockPos.getY() - 1);
            }
            return mutableBlockPos.immutable();
        }
    }

    public static class DragonStatsManager {
        private static final Predicate<Entity> VALID_PLAYERS = EntityPredicates.ENTITY_STILL_ALIVE.and(EntityPredicates.withinDistance(0, 128, 0, 192));
        private final ServerWorld level;
        private final ServerBossInfo bossInfoServer;
        private int ticksSinceLastCheck;

        protected DragonStatsManager(ServerWorld _level, ITextComponent displayName) {
            level = _level;
            bossInfoServer = (ServerBossInfo) (new ServerBossInfo(displayName, BossInfo.Color.YELLOW, BossInfo.Overlay.PROGRESS).setCreateWorldFog(true).setDarkenScreen(true));
            bossInfoServer.setVisible(true);
        }

        public void tick(GoldDragonEntity dragon) {
            bossInfoServer.setPercent(dragon.getHealth() / dragon.getMaxHealth());
            if (++ticksSinceLastCheck >= 20) {
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
            for (ServerPlayerEntity player : level.getPlayers(VALID_PLAYERS)) {
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
