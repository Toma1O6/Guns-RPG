package dev.toma.gunsrpg.integration.questing.engine;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.questing.common.component.reward.instance.Reward;
import dev.toma.questing.common.engine.QuestEngine;
import dev.toma.questing.common.party.Party;
import dev.toma.questing.common.quest.instance.Quest;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TutorialQuestEngine implements QuestEngine {

    public static final ResourceLocation IDENTIFIER = GunsRPG.makeResource("tutorial");

    @Override
    public boolean canStartQuest(Party party, Quest quest, World world) {
        return false;
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
        return null;
    }

    @Override
    public ResourceLocation getIndentifier() {
        return IDENTIFIER;
    }

    @Override
    public Map<UUID, List<Quest>> getQuestsByParty() {
        return null;
    }

    @Override
    public Map<UUID, List<Reward>> getClaimableRewards() {
        return null;
    }

    @Override
    public void storeRewards(Map<UUID, Reward> map) {

    }

    @Override
    public void onLoaded() {

    }
}
