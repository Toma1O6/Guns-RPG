package dev.toma.gunsrpg.common.init;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.IGeneratorConfig;
import dev.toma.gunsrpg.config.ModConfig;
import dev.toma.gunsrpg.world.feature.ConfigurablePlacement;
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

    public static final RegistryObject<Placement<NoPlacementConfig>> AMETHYST = register("amethyst", () -> ModConfig.worldConfig.amethyst);
    public static final RegistryObject<Placement<NoPlacementConfig>> BLACK_CRYSTAL = register("black_crystal", () -> ModConfig.worldConfig.blackCrystal);
    public static final RegistryObject<Placement<NoPlacementConfig>> BLUE_CRYSTAL = register("blue_crystal", () -> ModConfig.worldConfig.blueCrystal);
    public static final RegistryObject<Placement<NoPlacementConfig>> GREEN_CRYSTAL = register("green_crystal", () -> ModConfig.worldConfig.greenCrystal);
    public static final RegistryObject<Placement<NoPlacementConfig>> RED_CRYSTAL = register("red_crystal", () -> ModConfig.worldConfig.redCrystal);
    public static final RegistryObject<Placement<NoPlacementConfig>> WHITE_CRYSTAL = register("white_crystal", () -> ModConfig.worldConfig.whiteCrystal);
    public static final RegistryObject<Placement<NoPlacementConfig>> YELLOW_CRYSTAL = register("yellow_crystal", () -> ModConfig.worldConfig.yellowCrystal);
    public static final RegistryObject<Placement<NoPlacementConfig>> BLACK_ORB = register("black_orb", () -> ModConfig.worldConfig.blackOrb);
    public static final RegistryObject<Placement<NoPlacementConfig>> BLUE_ORB = register("blue_orb", () -> ModConfig.worldConfig.blueOrb);
    public static final RegistryObject<Placement<NoPlacementConfig>> GREEN_ORB = register("green_orb", () -> ModConfig.worldConfig.greenOrb);
    public static final RegistryObject<Placement<NoPlacementConfig>> RED_ORB = register("red_orb", () -> ModConfig.worldConfig.redOrb);
    public static final RegistryObject<Placement<NoPlacementConfig>> WHITE_ORB = register("white_orb", () -> ModConfig.worldConfig.whiteOrb);
    public static final RegistryObject<Placement<NoPlacementConfig>> YELLOW_ORB = register("yellow_orb", () -> ModConfig.worldConfig.yellowOrb);

    private static RegistryObject<Placement<NoPlacementConfig>> register(String name, Supplier<IGeneratorConfig> configSupplier) {
        return TYPES.register(name, () -> new ConfigurablePlacement(NoPlacementConfig.CODEC, configSupplier));
    }

    public static void subscribe(IEventBus bus) {
        TYPES.register(bus);
    }
}
