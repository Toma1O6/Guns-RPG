package dev.toma.gunsrpg.common;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.attribute.IAttribute;
import dev.toma.gunsrpg.api.common.attribute.IAttributeId;
import dev.toma.gunsrpg.api.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.api.common.data.*;
import dev.toma.gunsrpg.common.attribute.Attribs;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.capability.PlayerDataProvider;
import dev.toma.gunsrpg.common.debuffs.IDebuffContext;
import dev.toma.gunsrpg.common.debuffs.IDebuffType;
import dev.toma.gunsrpg.common.entity.projectile.AbstractProjectile;
import dev.toma.gunsrpg.common.init.*;
import dev.toma.gunsrpg.common.item.HammerItem;
import dev.toma.gunsrpg.common.item.ICustomUseDuration;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.common.item.guns.setup.AbstractGun;
import dev.toma.gunsrpg.common.skills.AvengeMeFriendsSkill;
import dev.toma.gunsrpg.common.skills.SecondChanceSkill;
import dev.toma.gunsrpg.common.tileentity.DeathCrateTileEntity;
import dev.toma.gunsrpg.config.ModConfig;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.S2C_SendSkillDataPacket;
import dev.toma.gunsrpg.util.ModUtils;
import dev.toma.gunsrpg.util.SkillUtil;
import dev.toma.gunsrpg.util.properties.Properties;
import dev.toma.gunsrpg.world.LootStashes;
import dev.toma.gunsrpg.world.MobSpawnManager;
import dev.toma.gunsrpg.world.cap.WorldData;
import dev.toma.gunsrpg.world.cap.WorldDataProvider;
import dev.toma.gunsrpg.world.feature.ModConfiguredFeatures;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.IAngerable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.SlimeEntity;
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
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.common.world.MobSpawnInfoBuilder;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
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
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = GunsRPG.MODID)
public class CommonEventHandler {

    public static final Random random = new Random();
    private static final UUID MOVEMENT_SPEED = UUID.fromString("785168CE-6979-421A-ADE7-98461F04D1A0");
    private static final float SKILLBOOK_DROP_CHANCE = 0.02F;
    private static final float PERKBOOK_DROP_CHANCE = 0.07F;

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void loadBiomes(BiomeLoadingEvent event) {
        Biome.Category category = event.getCategory();
        BiomeGenerationSettingsBuilder builder = event.getGeneration();
        MobSpawnInfoBuilder mobSpawnBuilder = event.getSpawns();
        if (category != Biome.Category.OCEAN && category != Biome.Category.RIVER) {
            mobSpawnBuilder.addSpawn(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(ModEntities.ZOMBIE_GUNNER.get(), ModConfig.worldConfig.zombieGunnerSpawn.get(), 1, 2));
            mobSpawnBuilder.addSpawn(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(ModEntities.EXPLOSIVE_SKELETON.get(), ModConfig.worldConfig.explosiveSkeletonSpawn.get(), 1, 2));
            mobSpawnBuilder.addSpawn(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(ModEntities.ZOMBIE_KNIGHT.get(), ModConfig.worldConfig.zombieKnightSpawn.get(), 1, 2));
            mobSpawnBuilder.addMobCharge(ModEntities.ZOMBIE_GUNNER.get(), 0.7, 0.15);
            mobSpawnBuilder.addMobCharge(ModEntities.EXPLOSIVE_SKELETON.get(), 0.7, 0.15);
            mobSpawnBuilder.addMobCharge(ModEntities.ZOMBIE_KNIGHT.get(), 1.0, 0.12);
        }
        if (category != Biome.Category.NETHER && category != Biome.Category.THEEND) {
            if (category != Biome.Category.OCEAN && category != Biome.Category.RIVER) {
                builder.addFeature(GenerationStage.Decoration.UNDERGROUND_STRUCTURES, ModConfiguredFeatures.LOOT_STASH);
            }
            for (ConfiguredFeature<?, ?> feature : ModConfiguredFeatures.getOres()) {
                builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, feature);
            }
        } else {
            mobSpawnBuilder.addMobCharge(ModEntities.ROCKET_ANGEL.get(), 0.7, 0.15);
            mobSpawnBuilder.addSpawn(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(ModEntities.ROCKET_ANGEL.get(), 2, 1, 1));
        }
    }

    @SubscribeEvent
    public static void playSoundAtEntity(PlaySoundAtEntityEvent event) {
        if (event.getSound() == SoundEvents.ARMOR_EQUIP_GENERIC) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void attachEntityCapabilities(AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        if (entity instanceof PlayerEntity) {
            event.addCapability(GunsRPG.makeResource("playerdata"), new PlayerDataProvider((PlayerEntity) entity));
        }
    }

    @SubscribeEvent
    public static void attachLevelCapabilities(AttachCapabilitiesEvent<World> event) {
        event.addCapability(GunsRPG.makeResource("worldcap"), new WorldDataProvider(event.getObject()));
    }

    @SubscribeEvent
    public static void onLogIn(PlayerEvent.PlayerLoggedInEvent event) {
        PlayerEntity player = event.getPlayer();
        ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
        NetworkManager.sendClientPacket(serverPlayer, new S2C_SendSkillDataPacket(ModRegistries.SKILLS.getValues()));
        PlayerData.get(player).ifPresent(data -> {
            data.sync(DataFlags.WILDCARD);
            data.getProgressData().onLogIn();
            ModList list = ModList.get();
            Optional<? extends ModContainer> optional = list.getModContainerById(GunsRPG.MODID);
            optional.ifPresent(container -> {
                VersionChecker.CheckResult result = VersionChecker.getResult(container.getModInfo());
                switch (result.status) {
                    case OUTDATED:
                    case BETA_OUTDATED:
                        StringTextComponent textComponent = new StringTextComponent(getMessageLogo() + TextFormatting.GREEN + " Your mod is " + TextFormatting.DARK_RED + "outdated" + TextFormatting.GREEN + ". Click " + TextFormatting.BOLD + "HERE" + TextFormatting.RESET + TextFormatting.GREEN + " to get latest update");
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

    @SubscribeEvent
    public static void getDestructionSpeed(net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed event) {
        PlayerEntity player = event.getPlayer();
        ItemStack stack = player.getMainHandItem();
        PlayerData.get(player).ifPresent(data -> {
            IAttributeProvider provider = data.getAttributes();
            if (stack.getItem() instanceof AxeItem) {
                editMiningSpeed(event, provider, Attribs.WOODCUTTING_SPEED);
            } else if (stack.getItem() instanceof PickaxeItem) {
                editMiningSpeed(event, provider, Attribs.MINING_SPEED);
            } else if (stack.getItem() instanceof ShovelItem) {
                editMiningSpeed(event, provider, Attribs.DIGGING_SPEED);
            }
        });
    }

    @SubscribeEvent
    public static void finishEating(LivingEntityUseItemEvent.Finish event) {
        LivingEntity living = event.getEntityLiving();
        if (living.level.isClientSide) return;
        if (living instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) living;
            PlayerData.get(player).ifPresent(data -> {
                IDebuffs debuffs = data.getDebuffControl();
                ItemStack usedItem = event.getItem();
                Random random = player.getRandom();
                if (usedItem.getItem() == Items.ROTTEN_FLESH) {
                    if (!debuffs.hasDebuff(Debuffs.INFECTION) && random.nextFloat() < 0.3F) {
                        debuffs.toggle(Debuffs.INFECTION);
                    }
                } else if (usedItem.getItem() == Items.SPIDER_EYE) {
                    if (!debuffs.hasDebuff(Debuffs.POISON) && random.nextFloat() < 0.4F) {
                        debuffs.toggle(Debuffs.POISON);
                    }
                }
            });
        }
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
                if (nutrition >= ModConfig.skillConfig.getWellFedMinNutrition()) {
                    PlayerData.get(player).ifPresent(data -> {
                        ISkillProvider provider = data.getSkillProvider();
                        if (provider.hasSkill(Skills.WELL_FED_I)) {
                            SkillUtil.getTopHierarchySkill(Skills.WELL_FED_I, provider).applyEffects(player, data);
                        }
                    });
                }
            }
        }
    }

    @SubscribeEvent
    public static void breakBlock(BlockEvent.BreakEvent event) {
        PlayerEntity player = event.getPlayer();
        ItemStack stack = player.getMainHandItem();
        World world = (World) event.getWorld();
        BlockState blockState = event.getState();
        if (stack.getItem() instanceof HammerItem) {
            HammerItem hammer = (HammerItem) stack.getItem();
            Direction facing = ModUtils.getFacing(player);
            for (BlockPos pos : hammer.gatherBlocks(event.getPos(), facing)) {
                BlockState state = world.getBlockState(pos);
                if (hammer.canHarvestBlock(stack, state) && state.getDestroySpeed(world, pos) >= 0.0F) {
                    world.destroyBlock(pos, true, player);
                    stack.mineBlock(world, state, pos, player);
                    if (stack.getDamageValue() == stack.getMaxDamage()) break;
                }
            }
        } else if (blockState.getMaterial() == Material.GLASS && stack.isEmpty() && !world.isClientSide) {
            PlayerData.get(player).ifPresent(data -> {
                IDebuffs debuffs = data.getDebuffControl();
                if (!debuffs.hasDebuff(Debuffs.BLEED) && world.random.nextFloat() < 0.5F) {
                    debuffs.toggle(Debuffs.BLEED);
                }
            });
        }
    }

    @SubscribeEvent
    public static void damageEntity(LivingDamageEvent event) {
        DamageSource source = event.getSource();
        if (source.getDirectEntity() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) source.getDirectEntity();
            PlayerData.get(player).ifPresent(data -> {
                IAttributeProvider provider = data.getAttributes();
                float health = event.getEntityLiving().getMaxHealth();
                boolean instantKill = false;
                if (health < 100.0F) {
                    float f = random.nextFloat();
                    boolean b = f < provider.getAttributeValue(Attribs.INSTANT_KILL);
                    if (b) {
                        event.setAmount(event.getEntityLiving().getHealth());
                        instantKill = true;
                    }
                }
                if (!instantKill && player.getMainHandItem().getItem() instanceof TieredItem) {
                    float amount = event.getAmount() + provider.getAttribute(Attribs.MELEE_DAMAGE).floatValue();
                    event.setAmount(amount);
                }
            });
        } else if (source.getDirectEntity() instanceof AbstractArrowEntity && source.getEntity() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) source.getEntity();
            PlayerData.get(player).ifPresent(data -> {
                IAttributeProvider attributeProvider = data.getAttributes();
                double amount = event.getAmount();
                amount *= attributeProvider.getAttributeValue(Attribs.BOW_DAMAGE);
                event.setAmount((float) amount);
            });
        }
    }

    @SubscribeEvent
    public static void onPotionAdded(PotionEvent.PotionAddedEvent event) {
        LivingEntity entity = event.getEntityLiving();
        EffectInstance effectInstance = event.getPotionEffect();
        if (entity instanceof PlayerEntity && effectInstance.getDuration() > 30) {
            PlayerData.get((PlayerEntity) entity).ifPresent(iPlayerData -> {
                IAttributeProvider provider = iPlayerData.getAttributes();
                IAttribute attribute = provider.getAttribute(Attribs.POTION_EFFECT);
                int value = effectInstance.duration;
                effectInstance.duration = (int) (value * attribute.value());
            });
        }
    }

    @SubscribeEvent
    public static void changeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        PlayerEntity player = event.getPlayer();
        PlayerData.get(player).ifPresent(data -> data.sync(DataFlags.WILDCARD));
    }

    @SubscribeEvent
    public static void clonePlayer(net.minecraftforge.event.entity.player.PlayerEvent.Clone event) {
        LazyOptional<IPlayerData> oldDataOptional = PlayerData.get(event.getOriginal());
        LazyOptional<IPlayerData> freshDataOptional = PlayerData.get(event.getPlayer());
        int flag = DataFlags.WILDCARD;
        oldDataOptional.ifPresent(oldData -> freshDataOptional.ifPresent(freshData -> freshData.fromNbt(oldData.toNbt(flag), flag)));
    }

    @SubscribeEvent
    public static void onEntityDamaged(LivingHurtEvent event) {
        if (event.getEntity() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntity();
            PlayerData.get(player).ifPresent(data -> {
                float amount = event.getAmount();
                DamageSource source = event.getSource();
                IAttributeProvider provider = data.getAttributes();
                // all damage
                amount *= provider.getAttribute(Attribs.DAMAGE_TAKEN).floatValue();
                // fall damage
                if (source == DamageSource.FALL) {
                    amount *= provider.getAttribute(Attribs.FALL_DAMAGE).floatValue();
                }
                // debuff triggers
                if (amount > 1.0F) {
                    IDebuffs debuffs = data.getDebuffControl();
                    IDebuffContext context = IDebuffContext.of(source, player, data, amount);
                    debuffs.trigger(IDebuffType.TriggerFlags.HURT, context);
                }
                event.setAmount(amount);
            });
        }
    }

    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event) {
        boolean isWeapon = event.getSource() instanceof GunDamageSource;
        if (isWeapon) {
            Entity source = ((GunDamageSource) event.getSource()).getSrc();
            if (source instanceof PlayerEntity) {
                PlayerData.get((PlayerEntity) source).ifPresent(data -> data.getProgressData().onEnemyKilled(event.getEntity(), ((GunDamageSource) event.getSource()).getStacc()));
            }
        } else {
            Entity source = event.getSource().getEntity();
            if (source instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) source;
                PlayerData.get(player).ifPresent(data -> data.getProgressData().onEnemyKilled(event.getEntity(), player.getMainHandItem()));
            }
        }
        if (event.getEntity() instanceof IMob && !(event.getEntity() instanceof SlimeEntity)) {
            if (!event.getEntity().level.isClientSide) {
                Entity entity = event.getEntity();
                Item item = null;
                float chance = random.nextFloat();
                float skillChance = SKILLBOOK_DROP_CHANCE;
                float perkChance = PERKBOOK_DROP_CHANCE;
                if (event.getSource().getEntity() instanceof PlayerEntity) {
                    PlayerEntity player = (PlayerEntity) event.getSource().getEntity();
                    IPlayerData data = PlayerData.getUnsafe(player);
                    IAttributeProvider provider = data.getAttributes();
                    skillChance *= provider.getAttribute(Attribs.SKILL_BOOK_CHANCE).floatValue();
                    perkChance *= provider.getAttribute(Attribs.PERK_BOOK_CHANCE).floatValue();
                }
                perkChance += skillChance;
                if (chance < skillChance) {
                    item = ModItems.SKILLPOINT_BOOK;
                } else if (chance < perkChance) {
                    item = ModItems.PERKPOINT_BOOK;
                }
                if (item != null) {
                    entity.level.addFreshEntity(new ItemEntity(entity.level, entity.getX(), entity.getY(), entity.getZ(), new ItemStack(item)));
                }
            }
        }
        if (event.getEntity() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntity();
            PlayerData.get(player).ifPresent(data -> {
                ISkillProvider provider = data.getSkillProvider();
                if (provider.hasSkill(Skills.SECOND_CHANCE_I)) {
                    SecondChanceSkill secondChanceSkill = SkillUtil.getTopHierarchySkill(Skills.SECOND_CHANCE_I, provider);
                    if (secondChanceSkill.canApply(player)) {
                        event.setCanceled(true);
                        secondChanceSkill.setOnCooldown();
                        secondChanceSkill.onUse(player);
                        data.sync(DataFlags.SKILLS);
                    }
                }
                if (!event.isCanceled()) {
                    data.getDebuffControl().clearActive();
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
                    AvengeMeFriendsSkill avengeSkill = provider.getSkill(Skills.AVENGE_ME_FRIENDS);
                    if (avengeSkill != null) {
                        avengeSkill.applyEffects(player);
                    }
                }
            });
        }
    }

    @SubscribeEvent
    public static void respawnPlayer(PlayerEvent.PlayerRespawnEvent event) {
        PlayerEntity player = event.getPlayer();
        PlayerData.get(player).ifPresent(data -> {
            ISkillProvider provider = data.getSkillProvider();
            if (provider.hasSkill(Skills.WAR_MACHINE)) {
                provider.getSkill(Skills.WAR_MACHINE).onPurchase(player);
            }
            if (!event.isEndConquered()) {
                data.getDebuffControl().trigger(IDebuffType.TriggerFlags.RESPAWN, IDebuffContext.of(DamageSource.GENERIC, player, data, 0.0F));
            }
            data.sync(DataFlags.WILDCARD);
        });
    }

    @SubscribeEvent
    public static void getLootingLevel(LootingLevelEvent event) {
        DamageSource damageSource = event.getDamageSource();
        if (damageSource instanceof GunDamageSource) {
            GunDamageSource source = (GunDamageSource) damageSource;
            Entity projectile = source.getDirectEntity();
            if (projectile instanceof AbstractProjectile) {
                AbstractProjectile abstractProjectile = (AbstractProjectile) projectile;
                int lootLevel = abstractProjectile.getProperty(Properties.LOOT_LEVEL);
                event.setLootingLevel(lootLevel);
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
                updateMovementSpeedAttribute(player, data.getAttributes());
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

    @SubscribeEvent
    public static void tickServer(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) return;
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        ServerWorld world = server.getLevel(World.OVERWORLD);
        LootStashes.tick(world);
    }

    @SubscribeEvent
    public static void rightClickInteract(PlayerInteractEvent.RightClickBlock event) {
        cancelIfPlayerHoldsGun(event);
    }

    @SubscribeEvent
    public static void rightClickEntityInteract(PlayerInteractEvent.EntityInteract event) {
        cancelIfPlayerHoldsGun(event);
    }

    @SubscribeEvent
    public static void rightClickEntitySpecificInteract(PlayerInteractEvent.EntityInteractSpecific event) {
        cancelIfPlayerHoldsGun(event);
    }

    @SubscribeEvent
    public static void leftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        cancelIfPlayerHoldsGun(event);
    }

    @SubscribeEvent
    public static void onStartUsingItem(LivingEntityUseItemEvent.Start event) {
        ItemStack stack = event.getItem();
        LivingEntity entity = event.getEntityLiving();
        if (stack.getItem() instanceof ICustomUseDuration && entity instanceof PlayerEntity) {
            ICustomUseDuration useDuration = (ICustomUseDuration) stack.getItem();
            PlayerEntity player = (PlayerEntity) entity;
            int defaultDuration = event.getDuration();
            event.setDuration(useDuration.getUseDuration(defaultDuration, stack, player));
        }
    }

    private static void cancelIfPlayerHoldsGun(PlayerInteractEvent event) {
        if (event.getPlayer().getMainHandItem().getItem() instanceof AbstractGun) {
            event.setCanceled(true);
        }
    }

    private static void editMiningSpeed(PlayerEvent.BreakSpeed event, IAttributeProvider provider, IAttributeId attributeId) {
        float baseSpeed = event.getOriginalSpeed();
        // values are >1.0 when breaking tool compatible material
        if (baseSpeed > 1.0F) {
            float multiplier = provider.getAttribute(attributeId).floatValue();
            float newSpeed = baseSpeed * multiplier;
            event.setNewSpeed(newSpeed);
        }
    }

    private static String getMessageLogo() {
        return TextFormatting.BLUE + "[" + TextFormatting.YELLOW + "GunsRPG" + TextFormatting.BLUE + "]" + TextFormatting.RESET;
    }

    private static void updateMovementSpeedAttribute(PlayerEntity player, IAttributeProvider provider) {
        ModifiableAttributeInstance instance = player.getAttribute(Attributes.MOVEMENT_SPEED);
        AttributeModifier modifier = instance.getModifier(MOVEMENT_SPEED);
        double modifierValue = modifier == null ? 0.0 : modifier.getAmount();
        double requiredValue = 1.0F - provider.getAttributeValue(Attribs.MOVEMENT_SPEED);
        if (modifierValue != requiredValue) {
            AttributeModifier newModifier = new AttributeModifier(MOVEMENT_SPEED, "attribute", requiredValue, AttributeModifier.Operation.MULTIPLY_TOTAL);
            instance.removeModifier(MOVEMENT_SPEED);
            instance.addTransientModifier(newModifier);
        }
    }
}
