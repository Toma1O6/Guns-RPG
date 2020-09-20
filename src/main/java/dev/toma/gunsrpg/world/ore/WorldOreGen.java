package dev.toma.gunsrpg.world.ore;

import dev.toma.gunsrpg.common.init.GRPGBlocks;
import dev.toma.gunsrpg.config.GRPGConfig;
import dev.toma.gunsrpg.config.world.WorldConfiguration;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class WorldOreGen implements IWorldGenerator {

    private final SingleOreGen amethyst;

    public WorldOreGen() {
        this.amethyst = new SingleOreGen(GRPGBlocks.AMETHYST_ORE.getDefaultState());
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        WorldConfiguration wcfg = GRPGConfig.worldConfig;
        gen(amethyst, world, random, chunkX, chunkZ, wcfg.amethyst.spawns, wcfg.amethyst.minHeight, wcfg.amethyst.maxHeight);
    }

    private void gen(WorldGenerator gen, World world, Random random, int chunkX, int chunkZ, int chance, int from, int to) {
        if(to < from) throw new IllegalArgumentException("Invalid ore gen settings! Max height cannot be smaller than Min height! Check your ore gen configs");
        int diff = to - from + 1;
        for(int i = 0; i < chance; i++) {
            int x = (chunkX << 4) + random.nextInt(15) + 1;
            int y = from + random.nextInt(diff);
            int z = (chunkZ << 4) + random.nextInt(15) + 1;
            gen.generate(world, random, new BlockPos(x, y, z));
        }
    }
}
