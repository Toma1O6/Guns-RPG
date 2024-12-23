package dev.toma.gunsrpg.config;

import dev.toma.configuration.config.Configurable;
import dev.toma.gunsrpg.common.quests.mayor.ReputationStatus;
import dev.toma.gunsrpg.util.Interval;
import dev.toma.gunsrpg.util.helper.ReputationHelper;

public final class QuestConfig {

    @Configurable
    @Configurable.Range(min = 1, max = 16)
    @Configurable.Synchronized
    public int maxPartySize = 5;

    @Configurable
    @Configurable.Comment("Will allow sending invites to players who are already in group with other players")
    @Configurable.Synchronized
    public boolean allowInvitePlayersInGroup = true;

    @Configurable
    @Configurable.DecimalRange(min = 1.0, max = 100.0)
    @Configurable.Comment("Additional kill multiplier for kill tasks for quests. Applied based on your group size")
    public float defaultKillMemberMultiplier = 1.5F;

    @Configurable
    @Configurable.Comment("Will increase mob spawn waves for bigger questing groups")
    public boolean adjustSpawnWavesForGroup = true;

    @Configurable
    @Configurable.DecimalRange(min = 1.0, max = 32.0)
    public float waveScalingMultiplier = 1.5F;

    @Configurable
    @Configurable.Comment("Will add additional Resistance potion effect to spawned mobs for bigger questing groups")
    public boolean buffSpawnedMobsForGroup = true;

    @Configurable
    @Configurable.DecimalRange(min = 0.0F, max = ReputationHelper.MAX_REPUTATION)
    @Configurable.Comment({"Amount of reputation awarded when quest is completed", "Value is multiplied by quest tier"})
    @Configurable.Gui.NumberFormat("0.0###")
    public float reputationAwardPerTier = 0.2F;

    @Configurable
    @Configurable.DecimalRange(min = 0.0F, max = ReputationHelper.MAX_REPUTATION)
    @Configurable.Comment({"Amount of reputation lost when quest is failed", "Value is multiplied by quest tier"})
    @Configurable.Gui.NumberFormat("0.0###")
    public float reputationLossPerTier = 0.3F;

    @Configurable
    @Configurable.Range(min = 6000L) // 5 minutes
    @Configurable.Synchronized
    @Configurable.Comment("Quest refresh interval for mayor [ticks]")
    public long questRefreshInterval = Interval.hours(1).valueIn(Interval.Unit.TICK);

    @Configurable
    @Configurable.Synchronized
    @Configurable.Comment("Will block all possible interaction types inside quest areas")
    public boolean disableQuestAreaInteractions = true;

    @Configurable
    @Configurable.Comment("Enables various rewards given by mayor for reaching certain reputation levels")
    public boolean mayorRewardsForReputation = true;

    @Configurable
    @Configurable.Comment("At which reputation status weapon book is awarded")
    public ReputationStatus weaponBookRewardStatus = ReputationStatus.FRIENDLY;

    @Configurable
    @Configurable.Comment("At which reputation status egg shards are awarded")
    public ReputationStatus eggShardRewardStatus = ReputationStatus.HERO;
}
