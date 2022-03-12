package dev.toma.gunsrpg.common.init;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.world.feature.LootStashFeature;
import net.minecraft.world.gen.feature.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public final class ModFeatures {
    private ModFeatures() {}

    private static final DeferredRegister<Feature<?>> TYPES = DeferredRegister.create(ForgeRegistries.FEATURES, GunsRPG.MODID);

    public static final RegistryObject<Feature<ReplaceBlockConfig>> AMETHYST_ORE = register("amethyst_ore", () -> new ReplaceBlockFeature(ReplaceBlockConfig.CODEC));
    public static final RegistryObject<Feature<ReplaceBlockConfig>> BLACK_CRYSTAL = register("black_crystal", () -> new ReplaceBlockFeature(ReplaceBlockConfig.CODEC));
    public static final RegistryObject<Feature<ReplaceBlockConfig>> BLUE_CRYSTAL = register("blue_crystal", () -> new ReplaceBlockFeature(ReplaceBlockConfig.CODEC));
    public static final RegistryObject<Feature<ReplaceBlockConfig>> GREEN_CRYSTAL = register("green_crystal", () -> new ReplaceBlockFeature(ReplaceBlockConfig.CODEC));
    public static final RegistryObject<Feature<ReplaceBlockConfig>> RED_CRYSTAL = register("red_crystal", () -> new ReplaceBlockFeature(ReplaceBlockConfig.CODEC));
    public static final RegistryObject<Feature<ReplaceBlockConfig>> WHITE_CRYSTAL = register("white_crystal", () -> new ReplaceBlockFeature(ReplaceBlockConfig.CODEC));
    public static final RegistryObject<Feature<ReplaceBlockConfig>> YELLOW_CRYSTAL = register("yellow_crystal", () -> new ReplaceBlockFeature(ReplaceBlockConfig.CODEC));
    public static final RegistryObject<Feature<ReplaceBlockConfig>> BLACK_ORB = register("black_orb", () -> new ReplaceBlockFeature(ReplaceBlockConfig.CODEC));
    public static final RegistryObject<Feature<ReplaceBlockConfig>> BLUE_ORB = register("blue_orb", () -> new ReplaceBlockFeature(ReplaceBlockConfig.CODEC));
    public static final RegistryObject<Feature<ReplaceBlockConfig>> GREEN_ORB = register("green_orb", () -> new ReplaceBlockFeature(ReplaceBlockConfig.CODEC));
    public static final RegistryObject<Feature<ReplaceBlockConfig>> RED_ORB = register("red_orb", () -> new ReplaceBlockFeature(ReplaceBlockConfig.CODEC));
    public static final RegistryObject<Feature<ReplaceBlockConfig>> WHITE_ORB = register("white_orb", () -> new ReplaceBlockFeature(ReplaceBlockConfig.CODEC));
    public static final RegistryObject<Feature<ReplaceBlockConfig>> YELLOW_ORB = register("yellow_orb", () -> new ReplaceBlockFeature(ReplaceBlockConfig.CODEC));
    public static final RegistryObject<Feature<NoFeatureConfig>> LOOT_STASH = register("loot_stash", LootStashFeature::new);

    private static <FC extends IFeatureConfig> RegistryObject<Feature<FC>> register(String name, Supplier<Feature<FC>> supplier) {
        return TYPES.register(name, supplier);
    }

    public static void subscribe(IEventBus bus) {
        TYPES.register(bus);
    }
}
