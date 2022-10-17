package dev.toma.gunsrpg.config.world;

import dev.toma.configuration.api.IConfigWriter;
import dev.toma.configuration.api.IObjectSpec;
import dev.toma.configuration.api.type.IntType;
import dev.toma.configuration.api.type.ObjectType;
import net.minecraft.world.biome.Biome;

public class DimensionalMobSpawnConfig extends ObjectType {

    private final IntType overworld;
    private final IntType nether;
    private final IntType end;

    public DimensionalMobSpawnConfig(IObjectSpec spec, int overworld, int nether, int end) {
        super(spec);
        IConfigWriter writer = spec.getWriter();
        this.overworld = writer.writeBoundedInt("Overworld", overworld, 0, 96);
        this.nether = writer.writeBoundedInt("Nether", nether, 0, 96);
        this.end = writer.writeBoundedInt("End", end, 0, 96);
    }

    public int overworldChance() {
        return this.overworld.get();
    }

    public int netherChance() {
        return this.nether.get();
    }

    public int endChance() {
        return this.end.get();
    }

    public int choiceFromBiomeCategory(Biome.Category cat) {
        return cat == Biome.Category.NETHER ? netherChance() : cat == Biome.Category.THEEND ? endChance() : overworldChance();
    }
}
