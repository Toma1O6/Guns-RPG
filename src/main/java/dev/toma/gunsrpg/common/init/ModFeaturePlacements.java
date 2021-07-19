package dev.toma.gunsrpg.common.init;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.config.ModConfig;
import dev.toma.gunsrpg.world.feature.ConfigurablePlacement;
import net.minecraft.world.gen.placement.IPlacementConfig;
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

    public static final RegistryObject<Placement<NoPlacementConfig>> CONFIGURABLE = register("configurable", () -> new ConfigurablePlacement(NoPlacementConfig.CODEC, ModConfig.worldConfig.amethyst));

    private static <CF extends IPlacementConfig> RegistryObject<Placement<CF>> register(String name, Supplier<Placement<CF>> supplier) {
        return TYPES.register(name, supplier);
    }

    public static void subscribe(IEventBus bus) {
        TYPES.register(bus);
    }
}
