package dev.toma.gunsrpg.config.world;

import dev.toma.configuration.api.IConfigWriter;
import dev.toma.configuration.api.IObjectSpec;
import dev.toma.configuration.api.NumberDisplayType;
import dev.toma.configuration.api.type.*;

import java.text.DecimalFormat;

public class WorldConfiguration extends ObjectType {

    public final BooleanType createCrateOnPlayerDeath;
    public final BooleanType disableMobSpawners;
    public final IntType bloodMoonMobAgroRange;
    public final IntType rocketAngelSpawnChance;
    public final IntType zombieGunnerSpawn;
    public final IntType explosiveSkeletonSpawn;
    public final IntType zombieKnightSpawn;
    public final IntType bloodmoonCycle;
    public final IntType airdropFrequency;
    public final EnumType<SleepRestriction> sleepRestriction;
    public final DoubleType lootStashChance;
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
        zombieGunnerSpawn = writer.writeBoundedInt("Zombie Gunner chance", 8, 0, 60, "Spawn chance for zombie gunner entity");
        explosiveSkeletonSpawn = writer.writeBoundedInt("Explosive Skeleton chance", 5, 0, 60, "Spawn chance for explosive skeleton entity");
        zombieKnightSpawn = writer.writeBoundedInt("Zombie Knight chance", 1, 0, 60, "Spawn chance for zombie knight entity");
        bloodmoonCycle = writer.writeBoundedInt("Bloodmoon cycle", 7, -1, 999, "Defines bloodmoon cycle", "Set to -1 to disable");
        airdropFrequency = writer.writeBoundedInt("Airdrop frequency", 3, -1, 999, "Defines airdrop spawn frequency [days]", "Set to -1 to disable");
        sleepRestriction = writer.writeEnum("Restrict sleep", SleepRestriction.ALWAYS, "Defines when players will be able to sleep");
        lootStashChance = writer.writeBoundedDouble("Loot stash chance", 0.005, 0.0, 0.1, "Chance of loot stash spawn per chunk").setDisplay(NumberDisplayType.TEXT_FIELD_SLIDER).setFormatting(new DecimalFormat("0.0###"));
        amethyst = writer.writeObject(sp -> new SimpleOreGenConfig(sp, 3, 1, 16), "Amethyst ore", "Configure amethyst spawning");
        blackCrystal = writer.writeObject(sp -> new SimpleOreGenConfig(sp, 3, 1, 56), "Black crystal", "Configure black crystal spawning");
        blueCrystal = writer.writeObject(sp -> new SimpleOreGenConfig(sp, 3, 1, 56), "Blue crystal", "Configure blue crystal spawning");
        greenCrystal = writer.writeObject(sp -> new SimpleOreGenConfig(sp, 3, 1, 56), "Green crystal", "Configure green crystal spawning");
        redCrystal = writer.writeObject(sp -> new SimpleOreGenConfig(sp, 3, 1, 56), "Red crystal", "Configure red crystal spawning");
        whiteCrystal = writer.writeObject(sp -> new SimpleOreGenConfig(sp, 3, 1, 56), "White crystal", "Configure white crystal spawning");
        yellowCrystal = writer.writeObject(sp -> new SimpleOreGenConfig(sp, 3, 1, 56), "Yellow crystal", "Configure yellow crystal spawning");
        blackOrb = writer.writeObject(sp -> new SimpleOreGenConfig(sp, 4, 1, 56), "Black orb", "Configure black orb spawning");
        blueOrb = writer.writeObject(sp -> new SimpleOreGenConfig(sp, 4, 1, 56), "Blue orb", "Configure blue orb spawning");
        greenOrb = writer.writeObject(sp -> new SimpleOreGenConfig(sp, 4, 1, 56), "Green orb", "Configure green orb spawning");
        redOrb = writer.writeObject(sp -> new SimpleOreGenConfig(sp, 4, 1, 56), "Red orb", "Configure red orb spawning");
        whiteOrb = writer.writeObject(sp -> new SimpleOreGenConfig(sp, 4, 1, 56), "White orb", "Configure white orb spawning");
        yellowOrb = writer.writeObject(sp -> new SimpleOreGenConfig(sp, 4, 1, 56), "Yellow orb", "Configure yellow orb spawning");
    }
}
