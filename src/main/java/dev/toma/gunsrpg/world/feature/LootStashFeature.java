package dev.toma.gunsrpg.world.feature;

import dev.toma.gunsrpg.common.block.MilitaryCrateBlock;
import dev.toma.gunsrpg.common.tileentity.ILootGenerator;
import dev.toma.gunsrpg.util.locate.IterableLocator;
import dev.toma.gunsrpg.util.object.LazyLoader;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LootStashFeature extends Feature<NoFeatureConfig> {

    private final LazyLoader<Map<MilitaryCrateBlock.BiomeVariant, MilitaryCrateBlock>> MAP = new LazyLoader<>(() -> {
        IterableLocator<Block> locator = new IterableLocator<>(null);
        Stream<Block> blocks = locator.locateAll(ForgeRegistries.BLOCKS, block -> block instanceof MilitaryCrateBlock);
        return blocks.map(block -> (MilitaryCrateBlock) block).collect(Collectors.toMap(MilitaryCrateBlock::getVariant, Function.identity()));
    });

    public LootStashFeature() {
        super(NoFeatureConfig.CODEC);
    }

    @Override
    public boolean place(ISeedReader seedReader, ChunkGenerator generator, Random random, BlockPos pos, NoFeatureConfig config) {
        Block block = seedReader.getBlockState(pos).getBlock();
        if (!block.is(Tags.Blocks.STONE) && !block.is(Tags.Blocks.DIRT)) {
            return false;
        }
        Biome biome = seedReader.getBiome(pos);
        float temperature = biome.getTemperature(pos);
        MilitaryCrateBlock.BiomeVariant variant = MilitaryCrateBlock.BiomeVariant.ARTIC;
        for (MilitaryCrateBlock.BiomeVariant biomeVariant : MilitaryCrateBlock.BiomeVariant.BY_TEMPERATURE) {
            if (temperature > biomeVariant.getMinTemperature()) {
                variant = biomeVariant;
            }
        }
        MilitaryCrateBlock militaryCrateBlock = MAP.get().get(variant);
        seedReader.setBlock(pos, militaryCrateBlock.defaultBlockState(), 2);
        TileEntity tile = seedReader.getBlockEntity(pos);
        if (tile instanceof ILootGenerator) {
            ILootGenerator lootGenerator = (ILootGenerator) tile;
            lootGenerator.generateLoot();
        }
        return true;
    }
}
