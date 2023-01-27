package dev.toma.gunsrpg.integration.questing.quest;

public interface PartyRestrictedQuest {

    default int maxPartySize() {
        return 1;
    }
}
