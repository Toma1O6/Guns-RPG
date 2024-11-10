package dev.toma.gunsrpg;

import com.mojang.brigadier.CommandDispatcher;
import dev.toma.configuration.Configuration;
import dev.toma.configuration.config.format.ConfigFormats;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.api.common.data.IQuestingData;
import dev.toma.gunsrpg.api.common.data.IWorldData;
import dev.toma.gunsrpg.common.LootStashDetectorHandler;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.capability.PlayerDataStorage;
import dev.toma.gunsrpg.common.command.GunsrpgCommand;
import dev.toma.gunsrpg.common.init.*;
import dev.toma.gunsrpg.config.GunsrpgConfig;
import dev.toma.gunsrpg.config.world.DimensionalMobSpawnConfig;
import dev.toma.gunsrpg.config.world.MobConfig;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.sided.ClientSideManager;
import dev.toma.gunsrpg.util.Lifecycle;
import dev.toma.gunsrpg.world.MobSpawnManager;
import dev.toma.gunsrpg.world.cap.NbtCapabilityStorage;
import dev.toma.gunsrpg.world.cap.QuestingData;
import dev.toma.gunsrpg.world.cap.WorldData;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.storage.IWorldInfo;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

@Mod(GunsRPG.MODID)
public class GunsRPG {

    public static final String MODID = "gunsrpg";
    public static Logger log = LogManager.getLogger("Guns-RPG");
    private static final Lifecycle modLifecycle = new Lifecycle();

    public static GunsrpgConfig config;

    public GunsRPG() {
        config = Configuration.registerConfig(GunsrpgConfig.class, ConfigFormats.yaml()).getConfigInstance();
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        // deferred registries
        ModEntities.subscribe(eventBus);
        ModBlockEntities.subscribe(eventBus);
        ModContainers.subscribe(eventBus);
        ModFeatures.subscribe(eventBus);
        ModFeaturePlacements.subscribe(eventBus);
        ModRecipeSerializers.subscribe(eventBus);
        ModPotions.subscribe(eventBus);
        ModEffects.subscribe(eventBus);
        // lifecycle events
        eventBus.addListener(this::clientSetup);
        eventBus.addListener(this::commonSetup);
        // other events
        MinecraftForge.EVENT_BUS.addListener(this::registerCommands);
        MinecraftForge.EVENT_BUS.register(new LootStashDetectorHandler());

        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> ClientSideManager.instance().clientInit());

        modLifecycle.modInit();
    }

    public static Lifecycle getModLifecycle() {
        return modLifecycle;
    }

    public static ResourceLocation makeResource(String path) {
        return new ResourceLocation(MODID, path);
    }

    private void clientSetup(FMLClientSetupEvent event) {
        ClientSideManager.instance().clientSetup(event);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        NetworkManager.init();
        CapabilityManager.INSTANCE.register(IPlayerData.class, new PlayerDataStorage(), PlayerData::new);
        CapabilityManager.INSTANCE.register(IWorldData.class, new NbtCapabilityStorage<>(), WorldData::new);
        CapabilityManager.INSTANCE.register(IQuestingData.class, new NbtCapabilityStorage<>(), QuestingData::new);
        modLifecycle.commonInit();
        MobSpawnManager.instance().initialize();

        event.enqueueWork(this::registerMobSpawnPlacements);
    }

    private void registerMobSpawnPlacements() {
        MobConfig mobCfg = config.world.mobConfig;
        EntitySpawnPlacementRegistry.register(ModEntities.ZOMBIE_GUNNER.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, this.createSpawnRule(() -> mobCfg.zombieGunnerSpawn));
        EntitySpawnPlacementRegistry.register(ModEntities.EXPLOSIVE_SKELETON.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, this.createSpawnRule(() -> mobCfg.grenadierSpawn));
        EntitySpawnPlacementRegistry.register(ModEntities.ZOMBIE_KNIGHT.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, this.createSpawnRule(() -> mobCfg.zombieKnightSpawn));
        EntitySpawnPlacementRegistry.register(ModEntities.ROCKET_ANGEL.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, this.createSpawnRule(() -> mobCfg.rocketAngelSpawn));
        EntitySpawnPlacementRegistry.register(ModEntities.ZOMBIE_NIGHTMARE.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, this.createSpawnRule(() -> mobCfg.zombieNightmareSpawn));
    }

    private <T extends MonsterEntity> EntitySpawnPlacementRegistry.IPlacementPredicate<T> createSpawnRule(Supplier<DimensionalMobSpawnConfig> configProvider) {
        return (entityType, iServerWorld, spawnReason, blockPos, random) -> {
            Biome biome = iServerWorld.getBiome(blockPos);
            Biome.Category category = biome.getBiomeCategory();
            IWorldInfo info = iServerWorld.getLevelData();
            DimensionalMobSpawnConfig spawnConfig = configProvider.get();
            return spawnConfig.canSpawn(info, category) && MonsterEntity.checkMonsterSpawnRules(entityType, iServerWorld, spawnReason, blockPos, random);
        };
    }

    private void registerCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSource> dispatcher = event.getDispatcher();
        GunsrpgCommand.registerCommandTree(dispatcher);
    }
}
