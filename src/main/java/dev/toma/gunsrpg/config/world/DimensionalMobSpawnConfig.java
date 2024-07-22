package dev.toma.gunsrpg.config.world;

import dev.toma.configuration.config.Configurable;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.storage.IWorldInfo;

public class DimensionalMobSpawnConfig {

    @Configurable
    @Configurable.Range(min = 0, max = 96)
    @Configurable.Comment("Overworld spawn chance")
    public int overworld;

    @Configurable
    @Configurable.Range(min = 0, max = 96)
    @Configurable.Comment("Nether spawn chance")
    public int nether;

    @Configurable
    @Configurable.Range(min = 0, max = 96)
    @Configurable.Comment("End spawn chance")
    public int end;

    @Configurable
    @Configurable.Range(min = 0)
    @Configurable.Comment("Defines since which day this mob can start spawning. Applies only for overworld dimension")
    public int spawnStartDay;

    public DimensionalMobSpawnConfig(int overworld, int nether, int end, int spawnStartDay) {
        this.overworld = overworld;
        this.nether = nether;
        this.end = end;
        this.spawnStartDay = spawnStartDay;
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

    public boolean canSpawn(IWorldInfo info, Biome.Category category) {
        if (this.spawnStartDay == 0 || category == Biome.Category.NETHER || category == Biome.Category.THEEND) {
            return true;
        }
        long gametime = info.getGameTime();
        long day = gametime % 24_000L;
        return day >= this.spawnStartDay;
    }
}
