package dev.toma.gunsrpg.config.world;

import dev.toma.configuration.api.*;
import dev.toma.configuration.api.type.*;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.capability.object.PlayerPerkProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class WorldConfiguration extends ObjectType {

    public final BooleanType createCrateOnPlayerDeath;
    public final BooleanType disableMobSpawners;
    public final IntType bloodMoonMobAgroRange;
    public final DoubleType shootingMobAggroRange;
    public final IntType bloodmoonCycle;
    public final IntType airdropFrequency;
    public final IntType crystalStationUseCooldown;
    public final EnumType<SleepRestriction> sleepRestriction;
    public final DoubleType lootStashChance;
    public final DoubleType anotherAirdropSpawnChance;
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
    public final MayorHouseGeneratorConfig mayorHouseGenCfg;
    private final CollectionType<StringType> skullCrusherIgnoredMobs;
    public final DimensionalMobSpawnConfig zombieGunnerSpawn;
    public final DimensionalMobSpawnConfig grenadierSpawn;
    public final DimensionalMobSpawnConfig zombieKnightSpawn;
    public final DimensionalMobSpawnConfig rocketAngelSpawn;
    public final DimensionalMobSpawnConfig zombieNightmareSpawn;

    private boolean reloadEntities = true;
    private final Set<EntityType<?>> instantKillBlacklist = new HashSet<>();

    public WorldConfiguration(IObjectSpec spec) {
        super(spec);
        IConfigWriter writer = spec.getWriter();
        createCrateOnPlayerDeath = writer.writeBoolean("Player death crates", false, "Allow death crate spawning on player death", "Doesn't spawn if keepInventory gamerule is set to true!");
        disableMobSpawners = writer.writeBoolean("Disable mob spawners", true, "Disables mob spawning from spawners", "This prevents xp farming");
        bloodMoonMobAgroRange = writer.writeBoundedInt("Bloodmoon aggro range", 40, 1, 64, "Defines at which range will mobs aggro on you during bloodmoon").setDisplay(NumberDisplayType.SLIDER);
        shootingMobAggroRange = writer.writeBoundedDouble("Gunshot mob aggro range", 28.0, 0.0, 256.0, "Defines at which range will mobs aggro on you after shooting").setFormatting(new DecimalFormat("0.0#")).setDisplay(NumberDisplayType.TEXT_FIELD_SLIDER);
        zombieGunnerSpawn = writer.writeObject(sp -> new DimensionalMobSpawnConfig(sp, 25, 25, 1), "Zombie Gunner spawning");
        grenadierSpawn = writer.writeObject(sp -> new DimensionalMobSpawnConfig(sp, 16, 20, 1),"Grenadier spawning");
        zombieKnightSpawn = writer.writeObject(sp -> new DimensionalMobSpawnConfig(sp, 8, 12, 1), "Zombie Knight spawning");
        rocketAngelSpawn = writer.writeObject(sp -> new DimensionalMobSpawnConfig(sp, 2, 2, 0), "Rocket Angel spawning");
        zombieNightmareSpawn = writer.writeObject(sp -> new DimensionalMobSpawnConfig(sp, 1, 0, 0), "Zombie Nightmare spawning");
        bloodmoonCycle = writer.writeBoundedInt("Bloodmoon cycle", 7, -1, 999, "Defines bloodmoon cycle", "Set to -1 to disable");
        airdropFrequency = writer.writeBoundedInt("Airdrop frequency", 3, -1, 999, "Defines airdrop spawn frequency [days]", "Set to -1 to disable");
        crystalStationUseCooldown = writer.writeBoundedInt("Crystal station cooldown", PlayerPerkProvider.USE_COOLDOWN.getTicks(), 0, Integer.MAX_VALUE);
        sleepRestriction = writer.writeEnum("Restrict sleep", SleepRestriction.ALWAYS, "Defines when players will be able to sleep");
        anotherAirdropSpawnChance = writer.writeBoundedDouble("Next airdrop spawn chance", 0.005, 0.0, 1.0);
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
        mayorHouseGenCfg = writer.writeObject(MayorHouseGeneratorConfig::new, "Mayor house generation", "Configure spawn weights of mayor house feature");
        IRestriction<String> restriction = Restrictions.restrictStringByPattern(Pattern.compile("[a-z0-9_.-]+:[a-z0-9/._-]+"), false);
        // really ugly solution
        skullCrusherIgnoredMobs = writer.writeApplyList("Skull crusher ignore list", () -> {
            StringType type = new StringType("", "namespace:path", restriction);
            type.addListener(this::onMobListReload);
            return type;
        }, list -> {
            StringType bloodmoon = new StringType("", "gunsrpg:bloodmoon_golem", restriction);
            bloodmoon.addListener(this::onMobListReload);
            list.add(bloodmoon);
        }, "List of mobs ignored by skull crusher skill");
        skullCrusherIgnoredMobs.addListener(this::onMobListReload);
    }

    public boolean isInstantKillAllowed(EntityType<?> type) {
        if (reloadEntities) {
            this.reloadEntities = false;
            instantKillBlacklist.clear();
            skullCrusherIgnoredMobs.get().stream().map(AbstractConfigType::get).forEach(id -> {
                ResourceLocation location = new ResourceLocation(id);
                if (!ForgeRegistries.ENTITIES.containsKey(location)) {
                    GunsRPG.log.warn("Found unknown entity in config under property: \"skullCrusherIgnoreMobs\". No such entity exists with ID {}", id);
                } else {
                    EntityType<?> entity = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(id));
                    instantKillBlacklist.add(entity);
                }
            });
        }
        return !instantKillBlacklist.contains(type);
    }

    private void onMobListReload(Object obj) {
        this.reloadEntities = true;
    }
}