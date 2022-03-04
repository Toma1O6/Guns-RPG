package dev.toma.gunsrpg.world.feature;

import com.mojang.serialization.Codec;
import dev.toma.gunsrpg.api.common.IGeneratorConfig;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.placement.NoPlacementConfig;
import net.minecraft.world.gen.placement.SimplePlacement;

import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ConfigurablePlacement extends SimplePlacement<NoPlacementConfig> {

    final Supplier<IGeneratorConfig> cfgSupplier;

    public ConfigurablePlacement(Codec<NoPlacementConfig> codec, Supplier<IGeneratorConfig> cfgSupplier) {
        super(codec);
        this.cfgSupplier = cfgSupplier;
    }

    @Override
    protected Stream<BlockPos> place(Random random, NoPlacementConfig noPlacementConfig, BlockPos pos) {
        IGeneratorConfig config = cfgSupplier.get();
        int attempts = config.getSpawnAttempts();
        return IntStream.range(0, attempts).mapToObj(value -> {
            int configMin = config.getMinHeight();
            int configMax = config.getMaxHeight();
            int delta = configMax - configMin;
            int x = pos.getX() + random.nextInt(16);
            int y = configMin + random.nextInt(delta);
            int z = pos.getZ() + random.nextInt(16);
            return new BlockPos(x, y, z);
        });
    }
}
