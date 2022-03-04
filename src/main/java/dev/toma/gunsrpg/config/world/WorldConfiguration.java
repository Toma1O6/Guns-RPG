package dev.toma.gunsrpg.config.world;

import dev.toma.configuration.api.IConfigWriter;
import dev.toma.configuration.api.IObjectSpec;
import dev.toma.configuration.api.NumberDisplayType;
import dev.toma.configuration.api.type.BooleanType;
import dev.toma.configuration.api.type.EnumType;
import dev.toma.configuration.api.type.IntType;
import dev.toma.configuration.api.type.ObjectType;

public class WorldConfiguration extends ObjectType {

    public final BooleanType createCrateOnPlayerDeath;
    public final BooleanType disableMobSpawners;
    public final IntType bloodMoonMobAgroRange;
    public final IntType rocketAngelSpawnChance;
    public final IntType zombieGunnerSpawn;
    public final IntType explosiveSkeletonSpawn;
    public final IntType bloodmoonCycle;
    public final IntType airdropFrequency;
    public final EnumType<SleepRestriction> sleepRestriction;
    public final SimpleOreGenConfig amethyst;
    public final SimpleOreGenConfig blackCrystal;
    public final SimpleOreGenConfig blueCrystal;
    public final SimpleOreGenConfig greenCrystal;
    public final SimpleOreGenConfig redCrystal;
    public final SimpleOreGenConfig whiteCrystal;
    public final SimpleOreGenConfig yellowCrystal;
    public final SimpleOreGenConfig blackOrb;
    public final SimpleOreGenConfig blueOrb;
    public final SimpleOreGenConfig greenOrb;
    public final SimpleOreGenConfig redOrb;
    public final SimpleOreGenConfig whiteOrb;
    public final SimpleOreGenConfig yellowOrb;

    public WorldConfiguration(IObjectSpec spec) {
        super(spec);
        IConfigWriter writer = spec.getWriter();
        createCrateOnPlayerDeath = writer.writeBoolean("Player death crates", false, "Allow death crate spawning on player death", "Doesn't spawn if keepInventory gamerule is set to true!");
        disableMobSpawners = writer.writeBoolean("Disable mob spawners", true, "Disables mob spawning from spawners", "This prevents xp farming");
        bloodMoonMobAgroRange = writer.writeBoundedInt("Bloodmoon aggro range", 40, 1, 64, "Defines at which range will mobs aggro on you during bloodmoon").setDisplay(NumberDisplayType.SLIDER);
        rocketAngelSpawnChance = writer.writeBoundedInt("Rocket angel spawn chance", 2, 0, 16).setDisplay(NumberDisplayType.SLIDER);
        zombieGunnerSpawn = writer.writeBoundedInt("ZombieGunner chance", 8, 0, 60, "Spawn chance for zombie gunner entity");
        explosiveSkeletonSpawn = writer.writeBoundedInt("ExplosiveSkeletion chance", 5, 0, 60, "Spawn chance for explosive skeleton entity");
        bloodmoonCycle = writer.writeBoundedInt("Bloodmoon cycle", 7, -1, 999, "Defines bloodmoon cycle", "Set to -1 to disable");
        airdropFrequency = writer.writeBoundedInt("Airdrop frequency", 3, -1, 999, "Defines airdrop spawn frequency [days]", "Set to -1 to disable");
        sleepRestriction = writer.writeEnum("Restrict sleep", SleepRestriction.ALWAYS, "Defines when players will be able to sleep");
        amethyst = writer.writeObject(sp -> new SimpleOreGenConfig(sp, 4, 1, 16), "Amethyst ore", "Configure amethyst spawning");
        blackCrystal = writer.writeObject(sp -> new SimpleOreGenConfig(sp, 2, 1, 32), "Black crystal", "Configure black crystal spawning");
        blueCrystal = writer.writeObject(sp -> new SimpleOreGenConfig(sp, 2, 1, 32), "Blue crystal", "Configure blue crystal spawning");
        greenCrystal = writer.writeObject(sp -> new SimpleOreGenConfig(sp, 2, 1, 32), "Green crystal", "Configure green crystal spawning");
        redCrystal = writer.writeObject(sp -> new SimpleOreGenConfig(sp, 2, 1, 32), "Red crystal", "Configure red crystal spawning");
        whiteCrystal = writer.writeObject(sp -> new SimpleOreGenConfig(sp, 2, 1, 32), "White crystal", "Configure white crystal spawning");
        yellowCrystal = writer.writeObject(sp -> new SimpleOreGenConfig(sp, 2, 1, 32), "Yellow crystal", "Configure yellow crystal spawning");
        blackOrb = writer.writeObject(sp -> new SimpleOreGenConfig(sp, 2, 1, 32), "Black orb", "Configure black orb spawning");
        blueOrb = writer.writeObject(sp -> new SimpleOreGenConfig(sp, 2, 1, 32), "Blue orb", "Configure blue orb spawning");
        greenOrb = writer.writeObject(sp -> new SimpleOreGenConfig(sp, 2, 1, 32), "Green orb", "Configure green orb spawning");
        redOrb = writer.writeObject(sp -> new SimpleOreGenConfig(sp, 2, 1, 32), "Red orb", "Configure red orb spawning");
        whiteOrb = writer.writeObject(sp -> new SimpleOreGenConfig(sp, 2, 1, 32), "White orb", "Configure white orb spawning");
        yellowOrb = writer.writeObject(sp -> new SimpleOreGenConfig(sp, 2, 1, 32), "Yellow orb", "Configure yellow orb spawning");
    }
}
