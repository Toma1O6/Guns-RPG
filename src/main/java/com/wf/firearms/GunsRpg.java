package com.wf.firearms;

import com.wf.firearms.command.GunsRpgCommands;
import com.wf.firearms.combat.AmmoStatsRegistry;
import com.wf.firearms.combat.FirearmRegistry;
import com.wf.firearms.registry.ModBlockEntities;
import com.wf.firearms.registry.ModBlocks;
import com.wf.firearms.registry.ModEntities;
import com.wf.firearms.registry.ModItems;
import com.wf.firearms.registry.ModMenuTypes;
import com.wf.firearms.registry.ModRecipeSerializers;
import com.wf.firearms.registry.ModCreativeTabs;
import com.wf.firearms.registry.ModGunEnchantments;
import com.wf.firearms.registry.ModRecipeTypes;
import com.wf.firearms.compat.tacz.TaczBridge;
import com.wf.firearms.compat.tacz.TaczCombatBridge;
import com.wf.firearms.compat.tacz.TaczGunsrpgAmmoBridge;
import com.wf.firearms.compat.tacz.TaczPerkBridge;
import com.wf.firearms.compat.tacz.TaczWeaponUseBridge;
import com.wf.firearms.config.CombatConfig;
import com.wf.firearms.config.TaczBackendConfig;
import com.wf.firearms.config.WeaponCaliberOverrides;
import com.wf.firearms.config.AirdropConfig;
import com.wf.firearms.config.BloodmoonConfig;
import com.wf.firearms.config.GunnerLoadoutConfig;
import com.wf.firearms.config.MobSpawnConfig;
import com.wf.firearms.config.WeaponLevelConfig;
import com.wf.firearms.config.GunshotAlertConfig;
import com.wf.firearms.data.LevelingStrategy;
import com.wf.firearms.data.PlayerFirearmsData;
import com.wf.firearms.data.SkillDatabase;
import com.wf.firearms.data.WeaponMapping;
import com.wf.firearms.debuff.DebuffConfig;
import com.wf.firearms.debuff.DebuffCureRegistry;
import com.wf.firearms.gameplay.ProgressionService;
import com.wf.firearms.gameplay.StarterKitService;
import com.wf.firearms.network.ModNetwork;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(GunsRpg.MOD_ID)
public class GunsRpg {
    public static final String MOD_ID = "gunsrpg";
    public static final Logger LOGGER = LogManager.getLogger();

    public GunsRpg() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModBlocks.BLOCKS.register(modBus);
        ModBlocks.ITEMS.register(modBus);
        ModItems.ITEMS.register(modBus);
        ModGunEnchantments.ENCHANTMENTS.register(modBus);
        ModCreativeTabs.TABS.register(modBus);
        ModEntities.ENTITIES.register(modBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modBus);
        ModMenuTypes.MENUS.register(modBus);
        ModRecipeTypes.RECIPE_TYPES.register(modBus);
        ModRecipeSerializers.SERIALIZERS.register(modBus);
        modBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
        ModNetwork.register();
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            SkillDatabase.reload();
            DebuffConfig.reload();
            DebuffCureRegistry.reload();
            TaczBackendConfig.reload();
            WeaponCaliberOverrides.reload();
            AirdropConfig.reload();
            BloodmoonConfig.reload();
            WeaponLevelConfig.reload();
            GunnerLoadoutConfig.reload();
            MobSpawnConfig.reload();
            GunshotAlertConfig.reload();
            CombatConfig.reload();
            WeaponMapping.reload();
            TaczBridge.registerKillListener();
            TaczGunsrpgAmmoBridge.registerReloadListener();
            TaczCombatBridge.registerHurtListener();
            TaczWeaponUseBridge.registerListeners();
            TaczPerkBridge.registerListeners();
            AmmoStatsRegistry.reload();
            com.wf.firearms.grenade.GrenadeMaterialRegistry.reload();
            FirearmRegistry.init();
            StarterKitService.reload();
            LevelingStrategy leveling = LevelingStrategy.load();
            PlayerFirearmsData.setLevelingStrategy(leveling);
            ProgressionService.setLeveling(leveling);
        });
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        GunsRpgCommands.register(event.getDispatcher());
    }
}
