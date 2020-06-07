package dev.toma.gunsrpg.common;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.animation.AnimationManager;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.capability.PlayerDataManager;
import dev.toma.gunsrpg.common.capability.object.DebuffData;
import dev.toma.gunsrpg.common.entity.EntityExplosiveSkeleton;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.config.GRPGConfig;
import dev.toma.gunsrpg.debuffs.DebuffTypes;
import dev.toma.gunsrpg.util.object.EntitySpawnManager;
import dev.toma.gunsrpg.util.object.ShootingManager;
import dev.toma.gunsrpg.world.BloodmoonEntitySpawnEntryList;
import dev.toma.gunsrpg.world.cap.WorldCapProvider;
import dev.toma.gunsrpg.world.cap.WorldDataCap;
import dev.toma.gunsrpg.world.cap.WorldDataFactory;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.*;

@Mod.EventBusSubscriber(modid = GunsRPG.MODID)
public class CommonEventHandler {

    @SubscribeEvent
    public static void playSoundAtEntity(PlaySoundAtEntityEvent event) {
        if(event.getSound() == SoundEvents.ITEM_ARMOR_EQUIP_GENERIC) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void loadChunk(ChunkEvent.Load event) {
        Map<BlockPos, TileEntity> map = event.getChunk().getTileEntityMap();
        List<BlockPos> scheduledRemovalList = new ArrayList<>();
        for(Map.Entry<BlockPos, TileEntity> entityEntry : map.entrySet()) {
            if(entityEntry.getValue() instanceof TileEntityMobSpawner) {
                scheduledRemovalList.add(entityEntry.getKey());
            }
        }
        for(BlockPos pos : scheduledRemovalList) {
            event.getWorld().destroyBlock(pos, false);
        }
    }

    @SubscribeEvent
    public static void onCapAttachP(AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        if(entity instanceof EntityPlayer) {
            event.addCapability(GunsRPG.makeResource("playerdata"), new PlayerDataManager((EntityPlayer) entity));
        }
    }

    @SubscribeEvent
    public static void onCapAttachW(AttachCapabilitiesEvent<World> event) {
        event.addCapability(GunsRPG.makeResource("worldcap"), new WorldCapProvider());
    }

    @SubscribeEvent
    public static void onLogIn(PlayerEvent.PlayerLoggedInEvent event) {
        PlayerData data = PlayerDataFactory.get(event.player);
        data.sync();
        data.handleLogin();
    }

    @SubscribeEvent
    public static void clonePlayer(net.minecraftforge.event.entity.player.PlayerEvent.Clone event) {
        PlayerData old = PlayerDataFactory.get(event.getOriginal());
        PlayerData newData = PlayerDataFactory.get(event.getEntityPlayer());
        if(event.getOriginal().isDead) {
            DebuffData debuffData = old.getDebuffData();
            debuffData.getDebuffs()[0] = DebuffTypes.POISON.createInstance();
            debuffData.getDebuffs()[1] = DebuffTypes.INFECTION.createInstance();
            debuffData.getDebuffs()[2] = DebuffTypes.BROKEN_BONE.createInstance();
            debuffData.getDebuffs()[3] = DebuffTypes.BLEED.createInstance();
        }
        PlayerDataFactory.get(event.getEntityPlayer()).deserializeNBT(old.serializeNBT());
    }

    @SubscribeEvent
    public static void onArrowImpact(ProjectileImpactEvent.Arrow event) {
        EntityArrow arrow = event.getArrow();
        World world = arrow.world;
        if(!world.isRemote) {
            RayTraceResult result = event.getRayTraceResult();
            if(result != null && result.typeOfHit == RayTraceResult.Type.BLOCK && arrow.shootingEntity instanceof EntityExplosiveSkeleton) {
                arrow.setDead();
                EnumFacing facing = result.sideHit;
                BlockPos pos = result.getBlockPos().offset(facing);
                switch (facing) {
                    case NORTH: case SOUTH: {
                        for(int y = -1; y < 2; y++) {
                            for(int x = -1; x < 2; x++) {
                                BlockPos p = new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ());
                                if(world.getBlockState(p).getBlock() == Blocks.BEDROCK) continue;
                                world.destroyBlock(p, false);
                            }
                        }
                        break;
                    }
                    case WEST: case EAST: {
                        for(int y = -1; y < 2; y++) {
                            for(int z = -1; z < 2; z++) {
                                BlockPos p = new BlockPos(pos.getX(), pos.getY() + y, pos.getZ() + z);
                                if(world.getBlockState(p).getBlock() == Blocks.BEDROCK) continue;
                                world.destroyBlock(p, false);
                            }
                        }
                        break;
                    }
                    case UP: case DOWN: {
                        for(int x = -1; x < 2; x++) {
                            for(int z = -1; z < 2; z++) {
                                BlockPos p = new BlockPos(pos.getX() + x, pos.getY(), pos.getZ() + z);
                                if(world.getBlockState(p).getBlock() == Blocks.BEDROCK) continue;
                                world.destroyBlock(p, false);
                            }
                        }
                        break;
                    }
                }
                BlockPos newPos = pos.offset(facing.getOpposite());
                for(EnumFacing f : EnumFacing.values()) {
                    BlockPos p = newPos.offset(f);
                    if(world.getBlockState(p).getBlock() == Blocks.BEDROCK) continue;
                    world.destroyBlock(p, false);
                }
                if(world.getBlockState(newPos).getBlock() == Blocks.BEDROCK) return;
                world.destroyBlock(newPos, false);
                IBlockState state = world.getBlockState(pos);
                world.createExplosion(arrow, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 2.0F, true);
            }
        }
    }

    @SubscribeEvent
    public static void onEntityDamaged(LivingHurtEvent event) {
        if(event.getAmount() > 0 && event.getEntity() instanceof EntityPlayer) {
            PlayerData data = PlayerDataFactory.get((EntityPlayer) event.getEntity());
            data.getDebuffData().forEachDebuff(b -> b.onHurt(event.getSource(), (EntityPlayer) event.getEntity()));
        }
    }

    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event) {
        if(event.getEntity() instanceof IMob) {
            Entity src = event.getSource().getTrueSource();
            if(src instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) src;
                GunItem item = null;
                if(player.getHeldItemMainhand().getItem() instanceof GunItem) {
                    PlayerDataFactory.get(player).getSkillData().kill(event.getEntity(), (GunItem) player.getHeldItemMainhand().getItem());
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if(event.phase == TickEvent.Phase.END) {
            EntityPlayer player = event.player;
            World world = player.world;
            PlayerDataFactory.get(player).tick();
            if(!world.isRemote) {
                if(world.getWorldTime() % 200 == 0) {
                    for(int i = 0; i < world.loadedTileEntityList.size(); i++) {
                        TileEntity te = world.loadedTileEntityList.get(i);
                        if(te instanceof TileEntityMobSpawner) {
                            world.destroyBlock(te.getPos(), false);
                        }
                    }
                }
            } else {
                AnimationManager.tick();
            }
            ShootingManager.updateAllShooting(world);
        }
    }

    private static Random random = new Random();
    public static Map<Class<? extends Entity>, EntitySpawnManager> HEALTH_MAP = new HashMap<>();

    @SubscribeEvent
    public static void entityJoinWorld(EntityJoinWorldEvent event) {
        if(event.getEntity() instanceof IMob || event.getEntity() instanceof EntityWolf) {
            EntityLivingBase entity = (EntityLivingBase) event.getEntity();
            World world = event.getWorld();
            boolean bloodmoon = WorldDataFactory.isBloodMoon(world);
            if(bloodmoon) {
                EntityLivingBase replacement = BloodmoonEntitySpawnEntryList.createEntity(entity, world);
                if(replacement != null) {
                    event.setCanceled(true);
                    world.spawnEntity(replacement);
                    return;
                }
            }
            int modifier = random.nextDouble() <= 0.2 ? 3 : random.nextDouble() <= 0.5 ? 2 : 1;
            EntitySpawnManager manager = HEALTH_MAP.get(entity.getClass());
            if(manager == null) return;
            if(manager.hasCustomAction()) {
                manager.runAction(entity);
            }
            double value = manager.getHealthBase() * modifier;
            entity.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(value);
            if(!entity.world.isRemote) entity.setHealth((float) value);
            if(bloodmoon) {
                IAttributeInstance instance = entity.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
                if(instance == null) return;
                double v = instance.getAttributeValue();
                instance.setBaseValue(v >= GRPGConfig.world.bloodMoonMobAgroRange ? v : GRPGConfig.world.bloodMoonMobAgroRange);
                EntityPlayer player = findNearestPlayer(entity, world);
                if(player != null) entity.setRevengeTarget(player);
            }
        }
    }

    @SubscribeEvent
    public static void worldTick(TickEvent.WorldTickEvent event) {
        WorldDataCap cap = WorldDataFactory.get(event.world);
        if(cap == null) return;
        WorldDataFactory.get(event.world).tick(event.world);
    }

    @SubscribeEvent
    public static void trySleepInBed(PlayerSleepInBedEvent event) {
        EntityPlayer player = event.getEntityPlayer();
        BlockPos pos = event.getPos();
        if(WorldDataFactory.isBloodMoon(player.world) && !player.world.provider.isDaytime()) {
            player.setSpawnPoint(pos.up(), true);
            event.setResult(EntityPlayer.SleepResult.NOT_SAFE);
        }
    }

    @SubscribeEvent
    public static void leftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        if(event.getEntityPlayer().getHeldItemMainhand().getItem() instanceof GunItem) {
            event.setCanceled(true);
        }
    }

    private static EntityPlayer findNearestPlayer(Entity entity, World world) {
        double lastDist = Double.MAX_VALUE;
        EntityPlayer p = null;
        for(int i = 0; i < world.playerEntities.size(); i++) {
            EntityPlayer player = world.playerEntities.get(i);
            double d = entity.getDistanceSq(player);
            if(d < lastDist) {
                lastDist = d;
                p = player;
            }
        }
        return p;
    }

    private static <K, V> V getMapNonnull(Map<K, V> map, K key, V defValue) {
        V v = map.get(key);
        return v != null ? v : defValue;
    }
}
