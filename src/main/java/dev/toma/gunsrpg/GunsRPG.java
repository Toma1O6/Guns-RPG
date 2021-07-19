package dev.toma.gunsrpg;

import com.mojang.brigadier.CommandDispatcher;
import dev.toma.gunsrpg.asm.Hooks;
import dev.toma.gunsrpg.client.gui.skills.SkillTreePlacement;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.capability.PlayerDataStorage;
import dev.toma.gunsrpg.common.command.CommandGRPG;
import dev.toma.gunsrpg.common.init.*;
import dev.toma.gunsrpg.common.item.guns.ammo.ItemAmmo;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.sided.ClientSideManager;
import dev.toma.gunsrpg.util.recipes.BlastFurnaceRecipe;
import dev.toma.gunsrpg.util.recipes.SmithingTableRecipes;
import dev.toma.gunsrpg.world.MobSpawnManager;
import dev.toma.gunsrpg.world.cap.WorldDataCap;
import dev.toma.gunsrpg.world.cap.WorldDataFactory;
import dev.toma.gunsrpg.world.cap.WorldDataStorage;
import net.minecraft.command.CommandSource;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(GunsRPG.MODID)
public class GunsRPG {

    public static final String MODID = "gunsrpg";
    public static Logger log = LogManager.getLogger();

    public GunsRPG() {
        System.out.println();
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        // deferred registries
        GRPGEntityTypes.subscribe(eventBus);
        GRPGEffects.subscribe(eventBus);
        GRPGTileEntities.subscribe(eventBus);
        GRPGContainers.subscribe(eventBus);
        // lifecycle events
        eventBus.addListener(this::clientSetup);
        eventBus.addListener(this::commonSetup);
        // other events
        MinecraftForge.EVENT_BUS.addListener(this::registerCommands);

        ModTags.init();
    }

    public static ResourceLocation makeResource(String path) {
        return new ResourceLocation(MODID, path);
    }

    private void clientSetup(FMLClientSetupEvent event) {
        ClientSideManager.instance().clientSetup(event);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        NetworkManager.init();
        CapabilityManager.INSTANCE.register(PlayerData.class, new PlayerDataStorage(), PlayerDataFactory::new);
        CapabilityManager.INSTANCE.register(WorldDataCap.class, new WorldDataStorage(), WorldDataFactory::new);
        SkillTreePlacement.generatePlacement();
        SmithingTableRecipes.register();
        ItemAmmo.init();
        Hooks.initOre2ChunkMap();
        MobSpawnManager.instance().initialize();
        BlastFurnaceRecipe.init();
    }

    private void registerCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSource> dispatcher = event.getDispatcher();
        CommandGRPG.register(dispatcher);
    }
}
