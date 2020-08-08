package dev.toma.gunsrpg.common.entity;

import dev.toma.gunsrpg.GunsRPG;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.dragon.phase.PhaseList;
import net.minecraft.entity.boss.dragon.phase.PhaseManager;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class EntityGoldDragon extends EntityDragon {

    private final BossInfoServer bossInfoServer = (BossInfoServer) (new BossInfoServer(getDisplayName(), BossInfo.Color.YELLOW, BossInfo.Overlay.PROGRESS).setCreateFog(true).setDarkenSky(true));
    private int cooldown;

    public EntityGoldDragon(World world) {
        super(world);
        phaseManager = new MyPhaseManager(this);
    }

    @Override
    public boolean attackEntityFromPart(MultiPartEntityPart dragonPart, DamageSource source, float damage) {
        damage = this.phaseManager.getCurrentPhase().getAdjustedDamage(dragonPart, source, damage);
        if (damage < 0.01F) {
            return false;
        } else {
            float f = this.getHealth();
            this.attackDragonFrom(source, damage);
            if (this.getHealth() <= 0.0F && !this.phaseManager.getCurrentPhase().getIsStationary()) {
                this.setHealth(1.0F);
                this.phaseManager.setPhase(PhaseList.DYING);
            }
            return true;
        }
    }

    @Override
    public void addTrackingPlayer(EntityPlayerMP player) {
        bossInfoServer.addPlayer(player);
    }

    @Override
    public void removeTrackingPlayer(EntityPlayerMP player) {
        bossInfoServer.removePlayer(player);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (!world.isRemote) {
            if (this.getHealth() < this.getMaxHealth() && ticksExisted % 12 == 0) {
                heal(1);
            }
            bossInfoServer.setPercent(this.getHealth() / this.getMaxHealth());
            if (cooldown > 0) cooldown--;
        }
    }

    public void resetCooldown() {
        cooldown = 800;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(1000F);
    }

    @Override
    protected boolean attackDragonFrom(DamageSource source, float amount) {
        amount *= 0.35;
        boolean result = super.attackDragonFrom(source, amount);
        hurtResistantTime = 0;
        return result;
    }

    @Override
    public int initPathPoints() {
        if (this.pathPoints[0] == null) {
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
                int j1 = Math.max(this.world.getSeaLevel() + 30, this.world.getTopSolidOrLiquidBlock(new BlockPos(l, 0, i1)).getY() + j);
                this.pathPoints[i] = new PathPoint((int) posX + l, j1, (int) posZ + i1);
            }
            this.neighbors[0] = 6146;
            this.neighbors[1] = 8197;
            this.neighbors[2] = 8202;
            this.neighbors[3] = 16404;
            this.neighbors[4] = 32808;
            this.neighbors[5] = 32848;
            this.neighbors[6] = 65696;
            this.neighbors[7] = 131392;
            this.neighbors[8] = 131712;
            this.neighbors[9] = 263424;
            this.neighbors[10] = 526848;
            this.neighbors[11] = 525313;
            this.neighbors[12] = 1581057;
            this.neighbors[13] = 3166214;
            this.neighbors[14] = 2138120;
            this.neighbors[15] = 6373424;
            this.neighbors[16] = 4358208;
            this.neighbors[17] = 12910976;
            this.neighbors[18] = 9044480;
            this.neighbors[19] = 9706496;
            this.neighbors[20] = 15216640;
            this.neighbors[21] = 13688832;
            this.neighbors[22] = 11763712;
            this.neighbors[23] = 8257536;
        }

        return this.getNearestPpIdx(this.posX, this.posY, this.posZ);
    }

    static class MyPhaseManager extends PhaseManager {

        private final EntityGoldDragon goldDragon;

        public MyPhaseManager(EntityGoldDragon dragon) {
            super(dragon);
            this.goldDragon = dragon;
        }

        @Override
        public void setPhase(PhaseList<?> phaseIn) {
            boolean strafe = false;
            boolean charge = false;
            if (goldDragon == null) {
                super.setPhase(phaseIn);
                return;
            }
            EntityLivingBase entitylivingbase = goldDragon.world.getNearestAttackablePlayer(this.goldDragon, 150.0D, 60.0D);
            if (phaseIn == PhaseList.LANDING_APPROACH) {
                if (entitylivingbase == null) {
                    phaseIn = PhaseList.HOLDING_PATTERN;
                } else if (goldDragon.rand.nextBoolean()) {
                    phaseIn = PhaseList.STRAFE_PLAYER;
                    strafe = true;
                } else {
                    phaseIn = PhaseList.CHARGING_PLAYER;
                    charge = true;
                }
            }
            super.setPhase(phaseIn);
            if (strafe) {
                if (entitylivingbase != null) {
                    getPhase(PhaseList.STRAFE_PLAYER).setTarget(entitylivingbase);
                    trySummonHelp(goldDragon.world);
                }
            } else if (charge) {
                if (entitylivingbase != null) {
                    getPhase(PhaseList.CHARGING_PLAYER).setTarget(entitylivingbase.getPositionVector());
                    trySummonHelp(goldDragon.world);
                }
            }
        }

        private void trySummonHelp(World world) {
            if (goldDragon.cooldown == 0) {
                goldDragon.resetCooldown();
                List<EntityPlayer> players = world.playerEntities;
                for (EntityPlayer player : players) {
                    if (player.isCreative() || player.isSpectator() || !player.isEntityAlive()) {
                        continue;
                    }
                    double distance = Math.sqrt(goldDragon.getDistanceSq(player));
                    if (distance > 100) continue;
                    BlockPos pos = player.getPosition();
                    int entitiesAround = world.getEntitiesWithinAABB(EntityMob.class, player.getEntityBoundingBox().grow(10)).size();
                    if (entitiesAround > 15) {
                        GunsRPG.log.debug("Aborting summoning dragon help at {}, there are too many mobs! ({})", player.getName(), entitiesAround);
                        continue;
                    }
                    spawnMob(pos, player, new EntityZombieGunner(world));
                    spawnMob(pos, player, new EntityZombieGunner(world));
                    spawnMob(pos, player, new EntityRocketAngel(world));
                }
            }
        }

        private void spawnMob(BlockPos pos, EntityLivingBase target, EntityMob mob) {
            BlockPos spawnPos = genRandomPos(pos, target.world);
            mob.setPositionAndUpdate(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
            mob.setRevengeTarget(target);
            mob.onInitialSpawn(target.world.getDifficultyForLocation(spawnPos), null);
            target.world.spawnEntity(mob);
        }

        private BlockPos genRandomPos(BlockPos pos, World world) {
            Random random = goldDragon.rand;
            int x = pos.getX() + random.nextInt(15) - random.nextInt(15);
            int z = pos.getZ() + random.nextInt(15) - random.nextInt(15);
            int y = pos.getY() + 15;
            BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos(x, y, z);
            while (world.isAirBlock(mutableBlockPos.down())) {
                mutableBlockPos.setY(mutableBlockPos.getY() - 1);
            }
            return mutableBlockPos.toImmutable();
        }
    }
}
