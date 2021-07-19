package dev.toma.gunsrpg.world;

import dev.toma.gunsrpg.common.init.ModBlocks;
import dev.toma.gunsrpg.config.ModConfig;
import dev.toma.gunsrpg.config.world.SimpleOreGenConfig;
import net.minecraft.block.BlockState;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;

/**
 * Unsafe classloading. Blocks may be null if this class is loaded too soon
 */
public class ModConfiguredFeatures {

    public static final ConfiguredFeature<?, ?> ORE_AMETHYST = register(ModBlocks.AMETHYST_ORE.defaultBlockState(), ModConfig.worldConfig.amethyst);

    private static ConfiguredFeature<?, ?> register(BlockState state, SimpleOreGenConfig config) {
        return Feature.ORE
                .configured(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, state, config.getSpawns()))
                .decorated(Placement.RANGE
                        .configured(new TopSolidRangeConfig(config.getMinHeight(), 0, config.getMaxHeight()))
                )
                .squared()
                .count(config.getSpawns());
    }
}
