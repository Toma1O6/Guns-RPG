package dev.toma.gunsrpg.world.feature;

import dev.toma.gunsrpg.common.init.ModBlocks;
import dev.toma.gunsrpg.common.init.ModFeaturePlacements;
import dev.toma.gunsrpg.common.init.ModFeatures;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ReplaceBlockConfig;
import net.minecraft.world.gen.placement.NoPlacementConfig;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Unsafe classloading. Blocks may be null if this class is loaded too soon
 */
public class ModConfiguredFeatures {

    private static final List<ConfiguredFeature<?, ?>> ORES = new ArrayList<>();

    public static final ConfiguredFeature<?, ?> ORE_AMETHYST = register(ModFeatures.AMETHYST_ORE.get().configured(new ReplaceBlockConfig(Blocks.STONE.defaultBlockState(), ModBlocks.AMETHYST_ORE.defaultBlockState())).decorated(ModFeaturePlacements.AMETHYST.get().configured(NoPlacementConfig.NONE)));
    public static final ConfiguredFeature<?, ?> BLACK_CRYSTAL = register(ModFeatures.BLACK_CRYSTAL.get().configured(new ReplaceBlockConfig(Blocks.STONE.defaultBlockState(), ModBlocks.BLACK_CRYSTAL_ORE.defaultBlockState())).decorated(ModFeaturePlacements.BLACK_CRYSTAL.get().configured(NoPlacementConfig.NONE)));
    public static final ConfiguredFeature<?, ?> BLUE_CRYSTAL = register(ModFeatures.BLUE_CRYSTAL.get().configured(new ReplaceBlockConfig(Blocks.STONE.defaultBlockState(), ModBlocks.BLUE_CRYSTAL_ORE.defaultBlockState())).decorated(ModFeaturePlacements.BLUE_CRYSTAL.get().configured(NoPlacementConfig.NONE)));
    public static final ConfiguredFeature<?, ?> GREEN_CRYSTAL = register(ModFeatures.GREEN_CRYSTAL.get().configured(new ReplaceBlockConfig(Blocks.STONE.defaultBlockState(), ModBlocks.GREEN_CRYSTAL_ORE.defaultBlockState())).decorated(ModFeaturePlacements.GREEN_CRYSTAL.get().configured(NoPlacementConfig.NONE)));
    public static final ConfiguredFeature<?, ?> RED_CRYSTAL = register(ModFeatures.RED_CRYSTAL.get().configured(new ReplaceBlockConfig(Blocks.STONE.defaultBlockState(), ModBlocks.RED_CRYSTAL_ORE.defaultBlockState())).decorated(ModFeaturePlacements.RED_CRYSTAL.get().configured(NoPlacementConfig.NONE)));
    public static final ConfiguredFeature<?, ?> WHITE_CRYSTAL = register(ModFeatures.WHITE_CRYSTAL.get().configured(new ReplaceBlockConfig(Blocks.STONE.defaultBlockState(), ModBlocks.WHITE_CRYSTAL_ORE.defaultBlockState())).decorated(ModFeaturePlacements.WHITE_CRYSTAL.get().configured(NoPlacementConfig.NONE)));
    public static final ConfiguredFeature<?, ?> YELLOW_CRYSTAL = register(ModFeatures.YELLOW_CRYSTAL.get().configured(new ReplaceBlockConfig(Blocks.STONE.defaultBlockState(), ModBlocks.YELLOW_CRYSTAL_ORE.defaultBlockState())).decorated(ModFeaturePlacements.YELLOW_CRYSTAL.get().configured(NoPlacementConfig.NONE)));
    public static final ConfiguredFeature<?, ?> BLACK_ORB = register(ModFeatures.BLACK_ORB.get().configured(new ReplaceBlockConfig(Blocks.STONE.defaultBlockState(), ModBlocks.BLACK_ORB_ORE.defaultBlockState())).decorated(ModFeaturePlacements.BLACK_ORB.get().configured(NoPlacementConfig.NONE)));
    public static final ConfiguredFeature<?, ?> BLUE_ORB = register(ModFeatures.BLUE_ORB.get().configured(new ReplaceBlockConfig(Blocks.STONE.defaultBlockState(), ModBlocks.BLUE_ORB_ORE.defaultBlockState())).decorated(ModFeaturePlacements.BLUE_ORB.get().configured(NoPlacementConfig.NONE)));
    public static final ConfiguredFeature<?, ?> GREEN_ORB = register(ModFeatures.GREEN_ORB.get().configured(new ReplaceBlockConfig(Blocks.STONE.defaultBlockState(), ModBlocks.GREEN_ORB_ORE.defaultBlockState())).decorated(ModFeaturePlacements.GREEN_ORB.get().configured(NoPlacementConfig.NONE)));
    public static final ConfiguredFeature<?, ?> RED_ORB = register(ModFeatures.RED_ORB.get().configured(new ReplaceBlockConfig(Blocks.STONE.defaultBlockState(), ModBlocks.RED_ORB_ORE.defaultBlockState())).decorated(ModFeaturePlacements.RED_ORB.get().configured(NoPlacementConfig.NONE)));
    public static final ConfiguredFeature<?, ?> WHITE_ORB = register(ModFeatures.WHITE_ORB.get().configured(new ReplaceBlockConfig(Blocks.STONE.defaultBlockState(), ModBlocks.WHITE_ORB_ORE.defaultBlockState())).decorated(ModFeaturePlacements.WHITE_ORB.get().configured(NoPlacementConfig.NONE)));
    public static final ConfiguredFeature<?, ?> YELLOW_ORB = register(ModFeatures.YELLOW_ORB.get().configured(new ReplaceBlockConfig(Blocks.STONE.defaultBlockState(), ModBlocks.YELLOW_ORB_ORE.defaultBlockState())).decorated(ModFeaturePlacements.YELLOW_ORB.get().configured(NoPlacementConfig.NONE)));

    public static Collection<ConfiguredFeature<?, ?>> getOres() {
        return ORES;
    }

    private static ConfiguredFeature<?, ?> register(ConfiguredFeature<?, ?> feature) {
        ORES.add(feature);
        return feature;
    }
}
