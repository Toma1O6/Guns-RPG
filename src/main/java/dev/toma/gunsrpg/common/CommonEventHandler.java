package dev.toma.gunsrpg.common;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.capability.IPlayerData;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.capability.PlayerDataProvider;
import dev.toma.gunsrpg.common.capability.object.PlayerSkills;
import dev.toma.gunsrpg.common.debuffs.DamageContext;
import dev.toma.gunsrpg.common.debuffs.Debuff;
import dev.toma.gunsrpg.common.entity.CrossbowBoltEntity;
import dev.toma.gunsrpg.common.entity.ExplosiveArrowEntity;
import dev.toma.gunsrpg.common.init.*;
import dev.toma.gunsrpg.common.item.HammerItem;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.common.skills.LightHunterSkill;
import dev.toma.gunsrpg.common.skills.SecondChanceSkill;
import dev.toma.gunsrpg.common.skills.WellFedSkill;
import dev.toma.gunsrpg.common.tileentity.DeathCrateTileEntity;
import dev.toma.gunsrpg.config.ModConfig;
import dev.toma.gunsrpg.util.ModUtils;
import dev.toma.gunsrpg.util.SkillUtil;
import dev.toma.gunsrpg.world.MobSpawnManager;
import dev.toma.gunsrpg.world.cap.IWorldData;
import dev.toma.gunsrpg.world.cap.WorldData;
import dev.toma.gunsrpg.world.cap.WorldDataProvider;
import dev.toma.gunsrpg.world.feature.ModConfiguredFeatures;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.IAngerable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.*;
import net.minecraft.loot.ConstantRange;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.RandomValueRange;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.world.Explosion;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.gen.GenerationStage;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.common.world.MobSpawnInfoBuilder;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.VersionChecker;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Mod.EventBusSubscriber(modid = GunsRPG.MODID)
public class CommonEventHandler {

    public static final Random random = new Random();

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void loadBiomes(BiomeLoadingEvent event) {
        Biome.Category category = event.getCategory();
        BiomeGenerationSettingsBuilder builder = event.getGeneration();
        MobSpawnInfoBuilder mobSpawnBuilder = event.getSpawns();
        if (category != Biome.Category.OCEAN && category != Biome.Category.RIVER) {
            mobSpawnBuilder.addSpawn(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(ModEntities.ZOMBIE_GUNNER.get(), ModConfig.worldConfig.zombieGunnerSpawn.get(), 1, 3));
            mobSpawnBuilder.addSpawn(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(ModEntities.EXPLOSIVE_SKELETON.get(), ModConfig.worldConfig.explosiveSkeletonSpawn.get(), 1, 3));
        }
        if (category != Biome.Category.NETHER && category != Biome.Category.THEEND) {
            builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, ModConfiguredFeatures.ORE_AMETHYST);
        }
    }

    @SubscribeEvent
    public static void playSoundAtEntity(PlaySoundAtEntityEvent event) {
        if (event.getSound() == SoundEvents.ARMOR_EQUIP_GENERIC) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onCapAttachP(AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        if (entity instanceof PlayerEntity) {
            event.addCapability(GunsRPG.makeResource("playerdata"), new PlayerDataProvider((PlayerEntity) entity));
        }
    }

    @SubscribeEvent
    public static void onCapAttachW(AttachCapabilitiesEvent<World> event) {
        event.addCapability(GunsRPG.makeResource("worldcap"), new WorldDataProvider());
    }

    @SubscribeEvent
    public static void onLogIn(PlayerEvent.PlayerLoggedInEvent event) {
        PlayerEntity player = event.getPlayer();
        PlayerData.get(player).ifPresent(data -> {
            data.sync();
            data.handleLogin();
            ModList list = ModList.get();
            Optional<? extends ModContainer> optional = list.getModContainerById(GunsRPG.MODID);
            optional.ifPresent(container -> {
                VersionChecker.CheckResult result = VersionChecker.getResult(container.getModInfo());
                switch (result.status) {
                    case OUTDATED:
                    case BETA_OUTDATED:
                        StringTextComponent textComponent = new StringTextComponent(getMessageLogo() + TextFormatting.GREEN + " Your mod is " + TextFormatting.DARK_RED + "outdated" + TextFormatting.GREEN + ". Click " + TextFormatting.BOLD + "HERE" + TextFormatting.RESET.toString() + TextFormatting.GREEN + " to get latest update");
                        String url = "https://www.curseforge.com/minecraft/mc-mods/guns-rpg";
                        textComponent.getStyle().withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
                        player.sendMessage(textComponent, Util.NIL_UUID);
                        break;
                    case UP_TO_DATE:
                        player.sendMessage(new StringTextComponent(getMessageLogo() + TextFormatting.GREEN + " Your mod is up to date, enjoy!"), Util.NIL_UUID);
                        break;
                }
            });
        });
    }

    private static String getMessageLogo() {
        return TextFormatting.BLUE + "[" + TextFormatting.YELLOW + "GunsRPG" + TextFormatting.BLUE + "]" + TextFormatting.RESET;
    }

    @SubscribeEvent
    public static void getDestructionSpeed(net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed event) {
        PlayerEntity player = event.getPlayer();
        ItemStack stack = player.getMainHandItem();
        PlayerData.get(player).ifPresent(data -> {
            PlayerSkills skills = data.getSkills();
            if (stack.getItem() instanceof AxeItem && skills.hasSkill(Skills.SHARP_AXE_I)) {
                float f = event.getOriginalSpeed();
                float f1 = skills.axeMiningSpeed * 2;
                if (f > 1.0F) {
                    float f2 = f * (1.0F + f1);
                    event.setNewSpeed(f2);
                }
            } else if (stack.getItem() instanceof PickaxeItem && skills.hasSkill(Skills.HEAVY_PICKAXE_I)) {
                float f = event.getOriginalSpeed();
                float f1 = skills.pickaxeMiningSpeed * 2;
                if (f > 1.0F) {
                    float f2 = f * (1.0F + f1);
                    event.setNewSpeed(f2);
                }
            } else if (stack.getItem() instanceof ShovelItem && skills.hasSkill(Skills.GRAVE_DIGGER_I)) {
                float f = event.getOriginalSpeed();
                float f1 = skills.shovelMiningSpeed * 2;
                if (f > 1.0F) {
                    float f2 = f * (1.0F + f1);
                    event.setNewSpeed(f2);
                }
            }
        });
    }

    @SubscribeEvent
    public static void disableShield(PlayerInteractEvent.RightClickItem event) {
        if (event.getHand() == Hand.OFF_HAND) {
            PlayerEntity player = event.getPlayer();
            ItemStack mainHand = player.getMainHandItem();
            if (mainHand.getItem() instanceof GunItem) {
                event.setCancellationResult(ActionResultType.FAIL);
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onEat(LivingEntityUseItemEvent.Finish event) {
        if (event.getEntityLiving() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            ItemStack stack = event.getItem();
            Item item = stack.getItem();
            Food food = item.getFoodProperties();
            if (food != null) {
                int nutrition = food.getNutrition();
                PlayerData.get(player).ifPresent(data -> {
                    PlayerSkills skills = data.getSkills();
                    WellFedSkill skill = skills.getSkill(Skills.WELL_FED_I);
                    if (nutrition >= ModConfig.skillConfig.getWellFedMinNutrition() && skill != null) {
                        SkillUtil.getBestSkillFromOverrides(skill, player).applyEffects(player);
                    }
                });
            }
        }
    }

    @SubscribeEvent
    public static void breakBlock(BlockEvent.BreakEvent event) {
        PlayerEntity player = event.getPlayer();
        ItemStack stack = player.getMainHandItem();
        World world = (World) event.getWorld();
        if (stack.getItem() instanceof HammerItem) {
            HammerItem hammer = (HammerItem) stack.getItem();
            Direction facing = ModUtils.getFacing(player);
            for (BlockPos pos : hammer.gatherBlocks(event.getPos(), facing)) {
                BlockState state = world.getBlockState(pos);
                if (hammer.canHarvestBlock(stack, state)) {
                    world.destroyBlock(pos, true, player);
                    stack.mineBlock(world, state, pos, player);
                    if (stack.getDamageValue() == stack.getMaxDamage()) break;
                }
            }
        }
    }

    @SubscribeEvent
    public static void damageEntity(LivingDamageEvent event) {
        DamageSource source = event.getSource();
        if (source.getDirectEntity() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) source.getDirectEntity();
            PlayerData.get(player).ifPresent(data -> {
                PlayerSkills skills = data.getSkills();
                float health = event.getEntityLiving().getMaxHealth();
                boolean instantKill = false;
                if (health < 100.0F) {
                    float f = random.nextFloat();
                    boolean b = f < skills.instantKillChance;
                    if (b) {
                        event.setAmount(event.getEntityLiving().getHealth());
                        instantKill = true;
                    }
                }
                if (!instantKill) {
                    event.setAmount(event.getAmount() + skills.extraDamage);
                }
            });
        } else if ((source.getDirectEntity() instanceof AbstractArrowEntity || source.getDirectEntity() instanceof CrossbowBoltEntity) && source.getEntity() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) source.getEntity();
            LightHunterSkill skill = PlayerData.getSkill(player, Skills.LIGHT_HUNTER);
            if (skill != null && skill.apply(player)) {
                event.setAmount(event.getAmount() * 1.2F);
            }
        }
    }

    @SubscribeEvent
    public static void clonePlayer(net.minecraftforge.event.entity.player.PlayerEvent.Clone event) {
        LazyOptional<IPlayerData> oldDataOptional = PlayerData.get(event.getOriginal());
        LazyOptional<IPlayerData> freshDataOptional = PlayerData.get(event.getPlayer());
        oldDataOptional.ifPresent(oldData -> freshDataOptional.ifPresent(freshData -> freshData.deserializeNBT(oldData.serializeNBT())));
    }

    @SubscribeEvent
    public static void onArrowImpact(ProjectileImpactEvent.Arrow event) {
        AbstractArrowEntity arrow = event.getArrow();
        World world = arrow.level;
        if (!world.isClientSide) {
            RayTraceResult rayTraceResult = event.getRayTraceResult();
            if (rayTraceResult != null && rayTraceResult.getType() == RayTraceResult.Type.BLOCK && arrow instanceof ExplosiveArrowEntity) {
                BlockRayTraceResult result = (BlockRayTraceResult) event.getRayTraceResult();
                arrow.remove();
                Direction facing = result.getDirection();
                BlockPos pos = result.getBlockPos().relative(facing);
                int pw = ((ExplosiveArrowEntity) arrow).blastSize;
                switch (facing) {
                    case NORTH:
                    case SOUTH: {
                        for (int y = -1; y < 2; y++) {
                            for (int x = -1; x < 2; x++) {
                                BlockPos p = new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ());
                                if (world.getBlockState(p).getBlock() == Blocks.BEDROCK) continue;
                                world.destroyBlock(p, false);
                            }
                        }
                        break;
                    }
                    case WEST:
                    case EAST: {
                        for (int y = -1; y < 2; y++) {
                            for (int z = -1; z < 2; z++) {
                                BlockPos p = new BlockPos(pos.getX(), pos.getY() + y, pos.getZ() + z);
                                if (world.getBlockState(p).getBlock() == Blocks.BEDROCK) continue;
                                world.destroyBlock(p, false);
                            }
                        }
                        break;
                    }
                    case UP:
                    case DOWN: {
                        for (int x = -1; x < 2; x++) {
                            for (int z = -1; z < 2; z++) {
                                BlockPos p = new BlockPos(pos.getX() + x, pos.getY(), pos.getZ() + z);
                                if (world.getBlockState(p).getBlock() == Blocks.BEDROCK) continue;
                                world.destroyBlock(p, false);
                            }
                        }
                        break;
                    }
                }
                BlockPos newPos = pos.relative(facing.getOpposite());
                for (Direction f : Direction.values()) {
                    BlockPos p = newPos.relative(f);
                    if (world.getBlockState(p).getBlock() == Blocks.BEDROCK) continue;
                    world.destroyBlock(p, false);
                }
                if (world.getBlockState(newPos).getBlock() == Blocks.BEDROCK) return;
                world.destroyBlock(newPos, false);
                world.explode(arrow, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, pw, Explosion.Mode.DESTROY);
            }
        }
    }

    @SubscribeEvent
    public static void onEntityDamaged(LivingHurtEvent event) {
        if (event.getAmount() >= 0.2F && event.getEntity() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntity();
            PlayerData.get(player).ifPresent(data -> data.getDebuffData().onPlayerAttackedFrom(DamageContext.getContext(event.getSource(), event.getAmount()), player));
        }
    }

    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event) {
        boolean flag = event.getSource() instanceof GunDamageSource;
        if (flag) {
            Entity source = ((GunDamageSource) event.getSource()).getSrc();
            if (source instanceof PlayerEntity) {
                PlayerData.get((PlayerEntity) source).ifPresent(data -> data.getSkills().onKillEntity(event.getEntity(), ((GunDamageSource) event.getSource()).getStacc()));
            }
        } else {
            Entity source = event.getSource().getDirectEntity();
            if (source instanceof PlayerEntity) {
                PlayerData.get((PlayerEntity) source).ifPresent(data -> data.getSkills().onKillEntity(event.getEntity(), ItemStack.EMPTY));
            }
        }
        if (event.getEntity() instanceof IMob) {
            if (!event.getEntity().level.isClientSide && random.nextFloat() <= 0.016) {
                Entity entity = event.getEntity();
                entity.level.addFreshEntity(new ItemEntity(entity.level, entity.getX(), entity.getY(), entity.getZ(), new ItemStack(ModItems.SKILLPOINT_BOOK)));
            }
        }
        if (event.getEntity() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntity();
            PlayerData.get(player).ifPresent(data -> {
                PlayerSkills skills = data.getSkills();
                SecondChanceSkill secondChanceSkill = skills.getSkill(Skills.SECOND_CHANCE_I);
                if (secondChanceSkill != null) {
                    secondChanceSkill = SkillUtil.getBestSkillFromOverrides(secondChanceSkill, player);
                    if (secondChanceSkill.apply(player)) {
                        event.setCanceled(true);
                        secondChanceSkill.setOnCooldown();
                        secondChanceSkill.onUse(player);
                        data.sync();
                    }
                }
                if (!event.isCanceled()) {
                    for (Debuff debuff : data.getDebuffData().getDebuffs()) {
                        if (debuff != null && !debuff.isInvalid())
                            debuff.invalidate();
                    }
                    if (ModConfig.worldConfig.createCrateOnPlayerDeath.get() && !player.level.getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) {
                        BlockPos pos = player.blockPosition();
                        World world = player.level;
                        while (!world.getBlockState(pos).getBlock().getCollisionShape(world.getBlockState(pos), world, pos, ISelectionContext.empty()).isEmpty() && pos.getY() < 255) {
                            pos = pos.above();
                        }
                        world.setBlock(pos, ModBlocks.DEATH_CRATE.defaultBlockState(), 3);
                        TileEntity tileEntity = world.getBlockEntity(pos);
                        if (tileEntity instanceof DeathCrateTileEntity) {
                            ((DeathCrateTileEntity) tileEntity).fillInventory(player);
                        }
                    }
                    if (skills.hasSkill(Skills.AVENGE_ME_FRIENDS) && !player.level.isClientSide) {
                        List<PlayerEntity> players = player.level.getEntitiesOfClass(PlayerEntity.class, VoxelShapes.block().bounds().move(player.blockPosition()).inflate(30));
                        players.forEach(p -> {
                            p.addEffect(new EffectInstance(Effects.ABSORPTION, 400, 2));
                            p.addEffect(new EffectInstance(Effects.REGENERATION, 500, 1));
                            p.level.playSound(p, p.getX(), p.getY(), p.getZ(), ModSounds.USE_AVENGE_ME_FRIENDS, SoundCategory.MASTER, 1.0F, 1.0F);
                        });
                    }
                }
            });
        }
    }

    @SubscribeEvent
    public static void respawnPlayer(PlayerEvent.PlayerRespawnEvent event) {
        PlayerEntity player = event.getPlayer();
        PlayerData.get(player).ifPresent(data -> {
            PlayerSkills skills = data.getSkills();
            if (skills.hasSkill(Skills.WAR_MACHINE)) {
                skills.getSkill(Skills.WAR_MACHINE).onPurchase(player);
            }
            if (!event.isEndConquered()) {
                data.setOnCooldown();
            }
            data.sync();
        });
    }

    @SubscribeEvent
    public static void getLootingLevel(LootingLevelEvent event) {
        if (event.getDamageSource() instanceof GunDamageSource) {
            GunDamageSource src = (GunDamageSource) event.getDamageSource();
            ItemStack stack = src.getStacc();
            Entity shooter = src.getSrc();
            if (stack.getItem() == ModItems.CROSSBOW && shooter instanceof PlayerEntity && PlayerData.hasActiveSkill((PlayerEntity) shooter, Skills.CROSSBOW_HUNTER)) {
                event.setLootingLevel(3);
            }
        }
    }

    @SubscribeEvent
    public static void checkEntitySpawn(LivingSpawnEvent.CheckSpawn event) {
        if (event.isSpawner()) {
            if (ModConfig.worldConfig.disableMobSpawners.get())
                event.setResult(Event.Result.DENY);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            PlayerEntity player = event.player;
            PlayerData.get(player).ifPresent(data -> {
                data.tick();
                player.abilities.setWalkingSpeed(data.getSkills().getMovementSpeed());
            });
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void entityJoinWorld(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof MonsterEntity || event.getEntity() instanceof IAngerable) {
            LivingEntity entity = (LivingEntity) event.getEntity();
            World world = event.getWorld();
            boolean bloodmoon = WorldData.isBloodMoon(world);
            MobSpawnManager.instance().processSpawn(entity, world, bloodmoon, event);
        }
    }

    @SubscribeEvent
    public static void worldTick(TickEvent.WorldTickEvent event) {
        IWorldData cap = WorldData.get(event.world);
        if (cap == null) return;
        cap.tick(event.world);
    }

    @SubscribeEvent
    public static void trySleepInBed(PlayerSleepInBedEvent event) {
        PlayerEntity player = event.getPlayer();
        BlockPos pos = event.getPos();
        if (!player.level.isDay()) {
            if (player instanceof ServerPlayerEntity) {
                ServerPlayerEntity serverplayer = (ServerPlayerEntity) player;
                serverplayer.setRespawnPosition(serverplayer.level.dimension(), pos.above(), serverplayer.yRot, true, false);
            }
            PlayerEntity.SleepResult result = ModConfig.worldConfig.sleepRestriction.get().getResult(player.level);
            if (result != null)
                event.setResult(result);
        }
    }

    @SubscribeEvent
    public static void leftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        if (event.getEntityLiving().getMainHandItem().getItem() instanceof GunItem) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void loadLootTables(LootTableLoadEvent event) {
        String loottable = event.getName().toString();
        if (loottable.equals("minecraft:chests/abandoned_mineshaft") || loottable.equals("minecraft:chests/desert_pyramid") || loottable.equals("minecraft:chests/simple_dungeon") || loottable.equals("minecraft:chests/village_blacksmith")) {
            event.getTable().addPool(LootPool.lootPool().add(ItemLootEntry
                    .lootTableItem(ModItems.SKILLPOINT_BOOK)
                    .apply(SetCount.setCount(new RandomValueRange(1, 3))))
                    .setRolls(new ConstantRange(1))
                    .bonusRolls(0.0F, 0.0F)
                    .build()
            );
        }
    }
}
