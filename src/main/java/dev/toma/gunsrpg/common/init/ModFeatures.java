package dev.toma.gunsrpg.common.init;

import dev.toma.gunsrpg.GunsRPG;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.ReplaceBlockConfig;
import net.minecraft.world.gen.feature.ReplaceBlockFeature;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public final class ModFeatures {
    private ModFeatures() {}

    private static final DeferredRegister<Feature<?>> TYPES = DeferredRegister.create(ForgeRegistries.FEATURES, GunsRPG.MODID);

    public static final RegistryObject<Feature<ReplaceBlockConfig>> AMETHYST_ORE = register("amethyst_ore", () -> new ReplaceBlockFeature(ReplaceBlockConfig.CODEC));

    private static <FC extends IFeatureConfig> RegistryObject<Feature<FC>> register(String name, Supplier<Feature<FC>> supplier) {
        return TYPES.register(name, supplier);
    }

    public static void subscribe(IEventBus bus) {
        TYPES.register(bus);
    }
}
