package dev.toma.gunsrpg.common.init;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.IGeneratorConfig;
import dev.toma.gunsrpg.world.feature.ConfigurablePlacement;
import dev.toma.gunsrpg.world.feature.UnderTopLayerPlacement;
import net.minecraft.world.gen.placement.NoPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public final class ModFeaturePlacements {
    private ModFeaturePlacements() {}

    private static final DeferredRegister<Placement<?>> TYPES = DeferredRegister.create(ForgeRegistries.DECORATORS, GunsRPG.MODID);

    public static final RegistryObject<Placement<NoPlacementConfig>> AMETHYST = register("amethyst", () -> GunsRPG.config.world.generationConfig.amethyst);
    public static final RegistryObject<Placement<NoPlacementConfig>> BLACK_CRYSTAL = register("black_crystal", () -> GunsRPG.config.world.generationConfig.blackCrystal);
    public static final RegistryObject<Placement<NoPlacementConfig>> BLUE_CRYSTAL = register("blue_crystal", () -> GunsRPG.config.world.generationConfig.blueCrystal);
    public static final RegistryObject<Placement<NoPlacementConfig>> GREEN_CRYSTAL = register("green_crystal", () -> GunsRPG.config.world.generationConfig.greenCrystal);
    public static final RegistryObject<Placement<NoPlacementConfig>> RED_CRYSTAL = register("red_crystal", () -> GunsRPG.config.world.generationConfig.redCrystal);
    public static final RegistryObject<Placement<NoPlacementConfig>> WHITE_CRYSTAL = register("white_crystal", () -> GunsRPG.config.world.generationConfig.whiteCrystal);
    public static final RegistryObject<Placement<NoPlacementConfig>> YELLOW_CRYSTAL = register("yellow_crystal", () -> GunsRPG.config.world.generationConfig.yellowCrystal);
    public static final RegistryObject<Placement<NoPlacementConfig>> BLACK_ORB = register("black_orb", () -> GunsRPG.config.world.generationConfig.blackOrb);
    public static final RegistryObject<Placement<NoPlacementConfig>> BLUE_ORB = register("blue_orb", () -> GunsRPG.config.world.generationConfig.blueOrb);
    public static final RegistryObject<Placement<NoPlacementConfig>> GREEN_ORB = register("green_orb", () -> GunsRPG.config.world.generationConfig.greenOrb);
    public static final RegistryObject<Placement<NoPlacementConfig>> RED_ORB = register("red_orb", () -> GunsRPG.config.world.generationConfig.redOrb);
    public static final RegistryObject<Placement<NoPlacementConfig>> WHITE_ORB = register("white_orb", () -> GunsRPG.config.world.generationConfig.whiteOrb);
    public static final RegistryObject<Placement<NoPlacementConfig>> YELLOW_ORB = register("yellow_orb", () -> GunsRPG.config.world.generationConfig.yellowOrb);
    public static final RegistryObject<Placement<NoPlacementConfig>> LOOT_STASH = registerPlacement("loot_stash", () -> new UnderTopLayerPlacement(GunsRPG.config.world.lootStashChance));

    private static RegistryObject<Placement<NoPlacementConfig>> registerPlacement(String name, Supplier<Placement<NoPlacementConfig>> supplier) {
        return TYPES.register(name, supplier);
    }

    private static RegistryObject<Placement<NoPlacementConfig>> register(String name, Supplier<IGeneratorConfig> configSupplier) {
        return TYPES.register(name, () -> new ConfigurablePlacement(NoPlacementConfig.CODEC, configSupplier));
    }

    public static void subscribe(IEventBus bus) {
        TYPES.register(bus);
    }
}
