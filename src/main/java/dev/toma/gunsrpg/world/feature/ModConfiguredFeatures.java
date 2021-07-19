package dev.toma.gunsrpg.world.feature;

import dev.toma.gunsrpg.common.init.ModBlocks;
import dev.toma.gunsrpg.common.init.ModFeaturePlacements;
import dev.toma.gunsrpg.common.init.ModFeatures;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ReplaceBlockConfig;
import net.minecraft.world.gen.placement.NoPlacementConfig;

/**
 * Unsafe classloading. Blocks may be null if this class is loaded too soon
 */
public class ModConfiguredFeatures {

    public static final ConfiguredFeature<?, ?> ORE_AMETHYST = ModFeatures.AMETHYST_ORE.get().configured(new ReplaceBlockConfig(Blocks.STONE.defaultBlockState(), ModBlocks.AMETHYST_ORE.defaultBlockState())).decorated(ModFeaturePlacements.CONFIGURABLE.get().configured(NoPlacementConfig.NONE));
}
