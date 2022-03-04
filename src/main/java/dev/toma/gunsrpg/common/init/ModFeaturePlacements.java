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

    private static RegistryObject<Placement<NoPlacementConfig>> register(String name, Supplier<IGeneratorConfig> configSupplier) {
        return TYPES.register(name, () -> new ConfigurablePlacement(NoPlacementConfig.CODEC, configSupplier));
    }

    public static void subscribe(IEventBus bus) {
        TYPES.register(bus);
    }
}
