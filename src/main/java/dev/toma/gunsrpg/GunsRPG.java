package dev.toma.gunsrpg;

import com.mojang.brigadier.CommandDispatcher;
import dev.toma.gunsrpg.client.ModKeybinds;
import dev.toma.gunsrpg.client.gui.GuiAirdrop;
import dev.toma.gunsrpg.client.gui.GuiBlastFurnace;
import dev.toma.gunsrpg.client.gui.GuiDeathCrate;
import dev.toma.gunsrpg.client.gui.GuiSmithingTable;
import dev.toma.gunsrpg.client.gui.skills.SkillTreePlacement;
import dev.toma.gunsrpg.client.render.*;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.capability.PlayerDataStorage;
import dev.toma.gunsrpg.common.command.CommandGRPG;
import dev.toma.gunsrpg.common.entity.*;
import dev.toma.gunsrpg.common.init.*;
import dev.toma.gunsrpg.common.item.guns.ammo.ItemAmmo;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.util.recipes.BlastFurnaceRecipe;
import dev.toma.gunsrpg.util.recipes.SmithingTableRecipes;
import dev.toma.gunsrpg.world.MobSpawnManager;
import dev.toma.gunsrpg.world.cap.WorldDataCap;
import dev.toma.gunsrpg.world.cap.WorldDataFactory;
import dev.toma.gunsrpg.world.cap.WorldDataStorage;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.command.CommandSource;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.ParallelDispatchEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

@Mod(GunsRPG.MODID)
public class GunsRPG {

    public static final String MODID = "gunsrpg";
    public static Logger log = LogManager.getLogger();
    public static Map<Item, Item> oreToChunkMap = new HashMap<>();

    public GunsRPG() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        // deferred registries
        GRPGEntityTypes.subscribe(eventBus);
        GRPGEffects.subscribe(eventBus);
        GRPGTileEntities.subscribe(eventBus);
        GRPGContainers.subscribe(eventBus);
        // lifecycle events
        eventBus.addListener(this::clientSetup);
        eventBus.addListener(this::commonSetup);
        eventBus.addListener(this::parallelEvent);
        // other events
        MinecraftForge.EVENT_BUS.addListener(this::registerCommands);
    }

    private void clientSetup(FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(AirdropEntity.class, RenderAirdrop::new);
        RenderingRegistry.registerEntityRenderingHandler(ExplosiveSkeletonEntity.class, RenderExplosiveSkeleton::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityExplosiveArrow.class, RenderExplosiveArrow::new);
        RenderingRegistry.registerEntityRenderingHandler(ZombieGunnerEntity.class, RenderZombieGunner::new);
        RenderingRegistry.registerEntityRenderingHandler(BloodmoonGolemEntity.class, RenderBloodmoonGolem::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityCrossbowBolt.class, CrossbowBoltRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(GrenadeEntity.class, RenderGrenade::new);
        RenderingRegistry.registerEntityRenderingHandler(RocketAngelEntity.class, RenderRocketAngel::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityGoldDragon.class, RenderGoldenDragon::new);
        ModKeybinds.registerKeybinds();
        MinecraftForge.EVENT_BUS.register(new ModKeybinds());
        //((IReloadableResourceManager) Minecraft.getInstance().getResourceManager()).registerReloadListener(ClientSideManager.SCRIPT_LOADER);
    }

    private void parallelEvent(ParallelDispatchEvent event) {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            event.enqueueWork(this::screenSetup);
        }
    }

    private void screenSetup() {
        ScreenManager.register(GRPGContainers.AIRDROP.get(), GuiAirdrop::new);
        ScreenManager.register(GRPGContainers.BLAST_FURNACE.get(), GuiBlastFurnace::new);
        ScreenManager.register(GRPGContainers.DEATH_CRATE.get(), GuiDeathCrate::new);
        ScreenManager.register(GRPGContainers.SMITHING_TABLE.get(), GuiSmithingTable::new);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        NetworkManager.init();
        CapabilityManager.INSTANCE.register(PlayerData.class, new PlayerDataStorage(), PlayerDataFactory::new);
        CapabilityManager.INSTANCE.register(WorldDataCap.class, new WorldDataStorage(), WorldDataFactory::new);
        SkillTreePlacement.generatePlacement();
        SmithingTableRecipes.register();
        ItemAmmo.init();
        initOresToChunks();
        GameRegistry.addSmelting(GRPGItems.IRON_ORE_CHUNK, new ItemStack(Items.IRON_INGOT), 0.7F);
        GameRegistry.addSmelting(GRPGItems.GOLD_ORE_CHUNK, new ItemStack(Items.GOLD_INGOT), 1.0F);
        LootTableList.register(makeResource("inject/dungeon_inject"));
        MobSpawnManager.instance().initialize();
        BlastFurnaceRecipe.init();
    }

    private void registerCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSource> dispatcher = event.getDispatcher();
        CommandGRPG.register(dispatcher);
    }

    public static ResourceLocation makeResource(String path) {
        return new ResourceLocation(MODID, path);
    }

    private static void initOresToChunks() {
        oreToChunkMap.put(Blocks.IRON_ORE.asItem(), GRPGItems.IRON_ORE_CHUNK);
        oreToChunkMap.put(Blocks.GOLD_ORE.asItem(), GRPGItems.GOLD_ORE_CHUNK);
    }
}
