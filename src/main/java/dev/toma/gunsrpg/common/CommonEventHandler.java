package dev.toma.gunsrpg.common;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.capability.PlayerDataManager;
import dev.toma.gunsrpg.common.capability.object.PlayerSkills;
import dev.toma.gunsrpg.common.debuffs.DamageContext;
import dev.toma.gunsrpg.common.debuffs.Debuff;
import dev.toma.gunsrpg.common.entity.EntityCrossbowBolt;
import dev.toma.gunsrpg.common.entity.EntityExplosiveArrow;
import dev.toma.gunsrpg.common.init.GRPGBlocks;
import dev.toma.gunsrpg.common.init.GRPGItems;
import dev.toma.gunsrpg.common.init.GRPGSounds;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.item.ItemHammer;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.common.skills.AdrenalineRushSkill;
import dev.toma.gunsrpg.common.skills.LightHunterSkill;
import dev.toma.gunsrpg.common.skills.SecondChanceSkill;
import dev.toma.gunsrpg.common.tileentity.TileEntityDeathCrate;
import dev.toma.gunsrpg.config.GRPGConfig;
import dev.toma.gunsrpg.event.EntityEquippedItemEvent;
import dev.toma.gunsrpg.util.ModUtils;
import dev.toma.gunsrpg.util.SkillUtil;
import dev.toma.gunsrpg.util.object.Pair;
import dev.toma.gunsrpg.util.object.ShootingManager;
import dev.toma.gunsrpg.world.MobSpawnManager;
import dev.toma.gunsrpg.world.cap.WorldCapProvider;
import dev.toma.gunsrpg.world.cap.WorldDataCap;
import dev.toma.gunsrpg.world.cap.WorldDataFactory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockOre;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.*;

@Mod.EventBusSubscriber(modid = GunsRPG.MODID)
public class CommonEventHandler {

    public static final Random random = new Random();

    @SubscribeEvent
    public static void entityEquipItem(EntityEquippedItemEvent event) {
        if(event.getEntity() instanceof EntityPlayer) {
            if(event.getSlot() == EntityEquipmentSlot.MAINHAND) {
                EntityPlayer player = (EntityPlayer) event.getEntity();
                IAttributeInstance attributeInstance = player.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_SPEED);
                attributeInstance.removeModifier(AdrenalineRushSkill.SKILL_MODIFIER);
                AdrenalineRushSkill adrenalineRushSkill = PlayerDataFactory.getSkill(player, Skills.ADRENALINE_RUSH_I);
                if(adrenalineRushSkill != null && adrenalineRushSkill.apply(player)) {
                    adrenalineRushSkill = SkillUtil.getBestSkillFromOverrides(adrenalineRushSkill, player);
                    AttributeModifier attributeModifier = adrenalineRushSkill.getAttackSpeedBoost();
                    attributeInstance.applyModifier(attributeModifier);
                }
            } else if(event.getSlot() == EntityEquipmentSlot.CHEST) {
                ItemStack stack = event.getStack();
                if(stack.getItem() == Items.ELYTRA) {
                    stack.setCount(0);
                }
            }
        }
    }

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
        ForgeVersion.CheckResult result = ForgeVersion.getResult(Loader.instance().activeModContainer());
        switch (result.status) {
            case OUTDATED: case BETA_OUTDATED: {
                TextComponentString textComponent = new TextComponentString(getMessageLogo() + TextFormatting.GREEN + " Your mod is " + TextFormatting.DARK_RED + "outdated" + TextFormatting.GREEN + ". Click " + TextFormatting.BOLD + "HERE" + TextFormatting.RESET.toString() + TextFormatting.GREEN + " to get latest update");
                String url = "https://www.curseforge.com/minecraft/mc-mods/guns-rpg";
                textComponent.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
                event.player.sendMessage(textComponent);
                break;
            }
            case UP_TO_DATE:
                event.player.sendMessage(new TextComponentString(getMessageLogo() + TextFormatting.GREEN + " Your mod is up to date, enjoy!"));
                break;
        }
    }

    private static String getMessageLogo() {
        return TextFormatting.BLUE + "[" + TextFormatting.YELLOW + "GunsRPG" + TextFormatting.BLUE + "]" + TextFormatting.RESET;
    }

    @SubscribeEvent
    public static void getDestructionSpeed(net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed event) {
        EntityPlayer player = event.getEntityPlayer();
        ItemStack stack = player.getHeldItemMainhand();
        PlayerSkills skills = PlayerDataFactory.get(player).getSkills();
        if(stack.getItem() instanceof ItemAxe && skills.hasSkill(Skills.SHARP_AXE_I)) {
            float f = event.getOriginalSpeed();
            float f1 = skills.axeMiningSpeed * 2;
            if(f > 1.0F) {
                float f2 = f * (1.0F + f1);
                event.setNewSpeed(f2);
            }
        } else if(stack.getItem() instanceof ItemPickaxe && skills.hasSkill(Skills.HEAVY_PICKAXE_I)) {
            float f = event.getOriginalSpeed();
            float f1 = skills.pickaxeMiningSpeed * 2;
            if(f > 1.0F) {
                float f2 = f * (1.0F + f1);
                event.setNewSpeed(f2);
            }
        } else if(stack.getItem() instanceof ItemSpade && skills.hasSkill(Skills.GRAVE_DIGGER_I)) {
            float f = event.getOriginalSpeed();
            float f1 = skills.shovelMiningSpeed * 2;
            if(f > 1.0F) {
                float f2 = f * (1.0F + f1);
                event.setNewSpeed(f2);
            }
        }
    }

    @SubscribeEvent
    public static void disableShield(PlayerInteractEvent.RightClickItem event) {
        if(event.getHand() == EnumHand.OFF_HAND) {
            EntityPlayer player = event.getEntityPlayer();
            ItemStack mainHand = player.getHeldItemMainhand();
            if(mainHand.getItem() instanceof GunItem) {
                event.setCancellationResult(EnumActionResult.FAIL);
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onEat(LivingEntityUseItemEvent.Finish event) {
        if(event.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            ItemStack stack = event.getItem();
            if(stack.getItem() instanceof ItemFood) {
                float value = ((ItemFood) stack.getItem()).getHealAmount(stack);
                PlayerSkills skills = PlayerDataFactory.get(player).getSkills();
                if(value >= 14 && skills.hasSkill(Skills.WELL_FED_I)) {
                    SkillUtil.getBestSkillFromOverrides(skills.getSkill(Skills.WELL_FED_I), player).applyEffects(player);
                }
            }
        }
    }

    @SubscribeEvent
    public static void breakBlock(BlockEvent.BreakEvent event) {
        EntityPlayer player = event.getPlayer();
        ItemStack stack = player.getHeldItemMainhand();
        World world = event.getWorld();
        if(stack.getItem() instanceof ItemHammer) {
            ItemHammer hammer = (ItemHammer) stack.getItem();
            EnumFacing facing = ModUtils.getFacing(player);
            for(BlockPos pos : hammer.gatherBlocks(event.getPos(), facing)) {
                IBlockState state = world.getBlockState(pos);
                if(hammer.canHarvestBlock(state)) {
                    state.getBlock().harvestBlock(world, player, pos, state, world.getTileEntity(pos), stack);
                    world.destroyBlock(pos, false);
                    stack.damageItem(1, player);
                    if(stack.getItemDamage() == stack.getMaxDamage()) break;
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void harvestBlock(BlockEvent.HarvestDropsEvent event) {
        EntityPlayer player = event.getHarvester();
        if(player == null) return;
        PlayerSkills skills = PlayerDataFactory.get(player).getSkills();
        if(event.getState().getBlock() instanceof BlockLog && skills.hasSkill(Skills.LUMBERJACK_I)) {
            Block block = event.getState().getBlock();
            for(IRecipe recipe : ForgeRegistries.RECIPES) {
                List<Ingredient> ingredients = recipe.getIngredients();
                if(ingredients.size() == 1) {
                    Ingredient ingredient = ingredients.get(0);
                    if(ingredient.apply(new ItemStack(block, 1, block.damageDropped(event.getState())))) {
                        ItemStack result = recipe.getRecipeOutput().copy();
                        result.setCount(1);
                        Pair<Float, Float> chances = SkillUtil.getBestSkillFromOverrides(skills.getSkill(Skills.LUMBERJACK_I), player).getDropChances();
                        if(random.nextFloat() < chances.getLeft()) {
                            event.getDrops().add(result);
                        }
                        if(random.nextFloat() < chances.getRight()) {
                            event.getDrops().add(new ItemStack(Items.STICK, 2));
                        }
                        break;
                    }
                }
            }
        } else if(event.getState().getBlock() instanceof BlockOre) {
            Block block = event.getState().getBlock();
            if(skills.hasSkill(Skills.MOTHER_LODE_I)) {
                Pair<Float, Float> chances = SkillUtil.getBestSkillFromOverrides(skills.getSkill(Skills.MOTHER_LODE_I), player).getDropChances();
                float x3 = chances.getRight();
                float x2 = chances.getLeft();
                if(random.nextFloat() < x3) {
                    replaceOres(event.getDrops(), 3);
                } else if(random.nextFloat() < x2) {
                    replaceOres(event.getDrops(), 2);
                } else replaceOres(event.getDrops(), 1);
            } else replaceOres(event.getDrops(), 1);
        }
    }

    private static void replaceOres(List<ItemStack> drops, int multiplier) {
        Iterator<ItemStack> iterator = drops.iterator();
        List<ItemStack> pending = new ArrayList<>();
        while (iterator.hasNext()) {
            ItemStack stack = iterator.next();
            Item replacement = GunsRPG.oreToChunkMap.get(stack.getItem());
            if(replacement != null) {
                ItemStack replaceStack = new ItemStack(replacement, Math.min(64, stack.getCount() * multiplier));
                pending.add(replaceStack);
                iterator.remove();
            } else {
                stack.setCount(Math.min(64, stack.getCount() * multiplier));
            }
        }
        drops.addAll(pending);
    }

    @SubscribeEvent
    public static void damageEntity(LivingDamageEvent event) {
        DamageSource source = event.getSource();
        if(source.getImmediateSource() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) source.getImmediateSource();
            PlayerSkills skills = PlayerDataFactory.get(player).getSkills();
            float health = event.getEntityLiving().getMaxHealth();
            boolean instantKill = false;
            if(health < 100.0F) {
                float f = random.nextFloat();
                boolean b = f < skills.instantKillChance;
                if(b) {
                    event.setAmount(event.getEntityLiving().getHealth());
                    instantKill = true;
                }
            }
            if(!instantKill) {
                event.setAmount(event.getAmount() + skills.extraDamage);
            }
        } else if((source.getImmediateSource() instanceof EntityArrow || source.getImmediateSource() instanceof EntityCrossbowBolt) && source.getTrueSource() instanceof EntityPlayer) {
            LightHunterSkill skill = PlayerDataFactory.getSkill((EntityPlayer) source.getTrueSource(), Skills.LIGHT_HUNTER);
            if(skill != null && skill.apply((EntityPlayer) source.getTrueSource())) {
                event.setAmount(event.getAmount() * 1.2F);
            }
        }
    }

    @SubscribeEvent
    public static void clonePlayer(net.minecraftforge.event.entity.player.PlayerEvent.Clone event) {
        PlayerData old = PlayerDataFactory.get(event.getOriginal());
        PlayerData newData = PlayerDataFactory.get(event.getEntityPlayer());
        PlayerDataFactory.get(event.getEntityPlayer()).deserializeNBT(old.serializeNBT());
    }

    @SubscribeEvent
    public static void onArrowImpact(ProjectileImpactEvent.Arrow event) {
        EntityArrow arrow = event.getArrow();
        World world = arrow.world;
        if(!world.isRemote) {
            RayTraceResult result = event.getRayTraceResult();
            if(result != null && result.typeOfHit == RayTraceResult.Type.BLOCK && arrow instanceof EntityExplosiveArrow) {
                arrow.setDead();
                EnumFacing facing = result.sideHit;
                BlockPos pos = result.getBlockPos().offset(facing);
                int pw = ((EntityExplosiveArrow) arrow).blastSize;
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
                world.createExplosion(arrow, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, pw, true);
            }
        }
    }

    @SubscribeEvent
    public static void onEntityDamaged(LivingHurtEvent event) {
        if(event.getAmount() >= 0.2F && event.getEntity() instanceof EntityPlayer) {
            PlayerData data = PlayerDataFactory.get((EntityPlayer) event.getEntity());
            data.getDebuffData().onPlayerAttackedFrom(DamageContext.getContext(event.getSource(), event.getAmount()), (EntityPlayer) event.getEntity());
        }
    }

    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event) {
        boolean flag = event.getSource() instanceof GunDamageSource;
        if(flag) {
            Entity source = ((GunDamageSource) event.getSource()).getSrc();
            if(source instanceof EntityPlayer) {
                PlayerDataFactory.get((EntityPlayer) source).getSkills().onKillEntity(event.getEntity(), ((GunDamageSource) event.getSource()).getStacc());
            }
        } else {
            Entity source = event.getSource().getTrueSource();
            if(source instanceof EntityPlayer) {
                PlayerDataFactory.get((EntityPlayer) source).getSkills().onKillEntity(event.getEntity(), ItemStack.EMPTY);
            }
        }
        if(event.getEntity() instanceof IMob) {
            if(!event.getEntity().world.isRemote && random.nextFloat() <= 0.016) {
                Entity entity = event.getEntity();
                entity.world.spawnEntity(new EntityItem(entity.world, entity.posX, entity.posY, entity.posZ, new ItemStack(GRPGItems.SKILLPOINT_BOOK)));
            }
        }
        if(event.getEntity() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntity();
            PlayerData data = PlayerDataFactory.get(player);
            PlayerSkills skills = data.getSkills();
            SecondChanceSkill secondChanceSkill = skills.getSkill(Skills.SECOND_CHANCE_I);
            if(secondChanceSkill != null) {
                secondChanceSkill = SkillUtil.getBestSkillFromOverrides(secondChanceSkill, player);
                if(secondChanceSkill.apply(player)) {
                    event.setCanceled(true);
                    secondChanceSkill.setOnCooldown();
                    secondChanceSkill.onUse(player);
                    PlayerDataFactory.get(player).sync();
                }
            }
            if(!event.isCanceled()) {
                for (Debuff debuff : data.getDebuffData().getDebuffs()) {
                    if(debuff != null && !debuff.isInvalid())
                        debuff.invalidate();
                }
                if(GRPGConfig.worldConfig.createCrateOnPlayerDeath && !player.world.getGameRules().getBoolean("keepInventory")) {
                    BlockPos pos = player.getPosition();
                    World world = player.world;
                    while (!world.getBlockState(pos).getBlock().isReplaceable(world, pos) && pos.getY() < 255) {
                        pos = pos.up();
                    }
                    world.setBlockState(pos, GRPGBlocks.DEATH_CRATE.getDefaultState());
                    TileEntity tileEntity = world.getTileEntity(pos);
                    if(tileEntity instanceof TileEntityDeathCrate) {
                        ((TileEntityDeathCrate) tileEntity).fillInventory(player);
                    }
                }
                if(skills.hasSkill(Skills.AVENGE_ME_FRIENDS) && !player.world.isRemote) {
                    List<EntityPlayer> players = player.world.getEntitiesWithinAABB(EntityPlayer.class, Block.FULL_BLOCK_AABB.offset(player.getPosition()).grow(30));
                    players.forEach(p -> {
                        p.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 400, 2));
                        p.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 500, 1));
                        p.world.playSound(p, p.posX, p.posY, p.posZ, GRPGSounds.USE_AVENGE_ME_FRIENDS, SoundCategory.MASTER, 1.0F, 1.0F);
                    });
                }
            }
        }
    }

    @SubscribeEvent
    public static void respawnPlayer(PlayerEvent.PlayerRespawnEvent event) {
        EntityPlayer player = event.player;
        PlayerSkills skills = PlayerDataFactory.get(player).getSkills();
        if(skills.hasSkill(Skills.WAR_MACHINE)) {
            skills.getSkill(Skills.WAR_MACHINE).onPurchase(player);
        }
        if(!event.isEndConquered()) {
            PlayerData data = PlayerDataFactory.get(player);
            data.setOnCooldown();
        }
        PlayerDataFactory.get(player).sync();
    }

    @SubscribeEvent
    public static void getLootingLevel(LootingLevelEvent event) {
        if(event.getDamageSource() instanceof GunDamageSource) {
            GunDamageSource src = (GunDamageSource) event.getDamageSource();
            ItemStack stack = src.getStacc();
            Entity shooter = src.getSrc();
            if(stack.getItem() == GRPGItems.CROSSBOW && shooter instanceof EntityPlayer && PlayerDataFactory.hasActiveSkill((EntityPlayer) shooter, Skills.CROSSBOW_HUNTER)) {
                event.setLootingLevel(3);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if(event.phase == TickEvent.Phase.END) {
            EntityPlayer player = event.player;
            World world = player.world;
            PlayerData data = PlayerDataFactory.get(player);
            data.tick();
            player.capabilities.walkSpeed = data.getSkills().getMovementSpeed();
            if(!world.isRemote) {
                if(world.getWorldTime() % 200 == 0) {
                    for(int i = 0; i < world.loadedTileEntityList.size(); i++) {
                        TileEntity te = world.loadedTileEntityList.get(i);
                        if(te instanceof TileEntityMobSpawner) {
                            world.destroyBlock(te.getPos(), false);
                        }
                    }
                }
            }
            ShootingManager.updateAllShooting(world);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void entityJoinWorld(EntityJoinWorldEvent event) {
        if(event.getEntity() instanceof IMob || event.getEntity() instanceof EntityWolf) {
            EntityLivingBase entity = (EntityLivingBase) event.getEntity();
            World world = event.getWorld();
            boolean bloodmoon = WorldDataFactory.isBloodMoon(world);
            MobSpawnManager.instance().processSpawn(entity, world, bloodmoon, event);
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
        if(!player.world.provider.isDaytime()) {
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

    @SubscribeEvent
    public static void loadLootTables(LootTableLoadEvent event) {
        String loottable = event.getName().toString();
        if(loottable.equals("minecraft:chests/abandoned_mineshaft") || loottable.equals("minecraft:chests/desert_pyramid") || loottable.equals("minecraft:chests/simple_dungeon") || loottable.equals("minecraft:chests/village_blacksmith")) {
            LootEntryItem entryItem = new LootEntryItem(
                    GRPGItems.SKILLPOINT_BOOK,
                    3,
                    0,
                    new LootFunction[] {new SetCount(new LootCondition[0], new RandomValueRange(1, 3))},
                    new LootCondition[0],
                    "skill_book"
                    );
            LootPool pool = new LootPool(new LootEntry[] {entryItem}, new LootCondition[0], new RandomValueRange(1), new RandomValueRange(0), "dungeon_inject_pool");
            event.getTable().addPool(pool);
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
