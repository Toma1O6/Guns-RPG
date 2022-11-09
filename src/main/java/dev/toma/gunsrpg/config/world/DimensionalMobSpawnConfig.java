package dev.toma.gunsrpg.config.world;

import dev.toma.configuration.config.Configurable;
import net.minecraft.world.biome.Biome;

public class DimensionalMobSpawnConfig {

    @Configurable
    @Configurable.Range(min = 0, max = 96)
    @Configurable.Comment("Overworld spawn chance")
    public final int overworld;

    @Configurable
    @Configurable.Range(min = 0, max = 96)
    @Configurable.Comment("Nether spawn chance")
    public final int nether;

    @Configurable
    @Configurable.Range(min = 0, max = 96)
    @Configurable.Comment("End spawn chance")
    public final int end;

    public DimensionalMobSpawnConfig(int overworld, int nether, int end) {
        this.overworld = overworld;
        this.nether = nether;
        this.end = end;
    }

    public int overworldChance() {
        return this.overworld;
    }

    public int netherChance() {
        return this.nether;
    }

    public int endChance() {
        return this.end;
    }

    public int choiceFromBiomeCategory(Biome.Category cat) {
        return cat == Biome.Category.NETHER ? netherChance() : cat == Biome.Category.THEEND ? endChance() : overworldChance();
    }
}
