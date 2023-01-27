package dev.toma.gunsrpg.integration.questing.engine;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.integration.questing.quest.PartyRestrictedQuest;
import dev.toma.gunsrpg.util.Interval;
import dev.toma.questing.common.component.reward.instance.Reward;
import dev.toma.questing.common.engine.QuestEngine;
import dev.toma.questing.common.party.Party;
import dev.toma.questing.common.quest.instance.Quest;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.*;

public class MayorQuestsEngine implements QuestEngine {

    public static final ResourceLocation IDENTIFIER = GunsRPG.makeResource("mayor_quests");
    private final Map<UUID, List<Quest>> quests = new HashMap<>();
    private final Map<UUID, Long> party2LastRefreshMap = new HashMap<>();

    @Override
    public boolean canStartQuest(Party party, Quest quest, World world) {
        int partySize = party.getMembers().size();
        List<Quest> active = this.quests.getOrDefault(party.getOwner(), Collections.emptyList());
        return active.isEmpty() && (!(quest instanceof PartyRestrictedQuest) || partySize <= ((PartyRestrictedQuest) quest).maxPartySize());
    }

    @Override
    public boolean shouldShowInQuestUI() {
        return false;
    }

    @Override
    public boolean canClaimRewardsViaUI() {
        return false;
    }

    @Override
    public List<Quest> generateQuests(Party party, World world) {
        return Collections.emptyList();
    }

    @Override
    public ResourceLocation getIndentifier() {
        return IDENTIFIER;
    }

    @Override
    public Map<UUID, List<Quest>> getQuestsByParty() {
        return this.quests;
    }

    @Override
    public Map<UUID, List<Reward>> getClaimableRewards() {
        return Collections.emptyMap();
    }

    @Override
    public void storeRewards(Map<UUID, Reward> map) {
    }

    @Override
    public void onLoaded() {
    }

    public boolean shouldRefreshQuests(Party party, long time) {
        long lastRefresh = party2LastRefreshMap.getOrDefault(party.getOwner(), Long.MIN_VALUE);
        long timeDiff = Math.abs(time - lastRefresh);
        long refresh;
        try {
            refresh = Interval.parse(GunsRPG.config.quest.questRefreshTimer).getTicks();
        } catch (IllegalArgumentException e) {
            refresh = 20L * 60L * 60L;
        }
        return timeDiff > refresh;
    }

    public void generateQuests(Party party, float reputation, UUID mayorId) {

    }
}
