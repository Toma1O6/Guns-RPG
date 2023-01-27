package dev.toma.gunsrpg.integration.questing.quest;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.data.IReputationProvider;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.integration.questing.Tiered;
import dev.toma.questing.common.party.Party;
import dev.toma.questing.common.quest.instance.AbstractQuest;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

import java.util.UUID;

public class MayorQuest extends AbstractQuest implements Tiered, MayorGivenQuest {

    private final MayorQuestProvider provider;
    private final int tier;
    private UUID mayorUUID;

    public MayorQuest(MayorQuestProvider provider, QuestData questData, int tier) {
        super(questData);
        this.provider = provider;
        this.tier = tier;
    }

    public MayorQuest(MayorQuestProvider provider) {
        this.provider = provider;
        this.tier = 0;
    }

    @Override
    public UUID getMayorId() {
        return this.mayorUUID;
    }

    @Override
    public void setMayorId(UUID mayorId) {
        this.mayorUUID = mayorId;
    }

    @Override
    public MayorQuestProvider getProvider() {
        return provider;
    }

    @Override
    public int getTier() {
        return tier;
    }

    @Override
    protected void onCompleted(World level) {
        Party party = this.getParty();
        UUID owner = party.getOwner();
        party.forEachOnlineMemberExcept(null, level, player -> {
            float base = GunsRPG.config.quest.reputationGainPerTier;
            addReputation(player, this.tier * base, player.getUUID().equals(owner), this.mayorUUID);
        });
    }

    @Override
    protected void onFailed(World level) {
        Party party = this.getParty();
        UUID owner = party.getOwner();
        party.forEachOnlineMemberExcept(null, level, player -> {
            float value = -GunsRPG.config.quest.reputationLostPerTier;
            addReputation(player, this.tier * value, player.getUUID().equals(owner), this.mayorUUID);
        });
    }

    private static void addReputation(PlayerEntity player, float reputation, boolean isOwner, UUID mayorUUID) {
        PlayerData.get(player).ifPresent(data -> {
            IReputationProvider provider = data.getReputation();
            float actualReputation = reputation;
            if (!isOwner) {
                actualReputation *= GunsRPG.config.quest.otherPartyMemberReputationMultiplier;
            }
            float rp = provider.getReputation(mayorUUID);
            provider.setReputation(mayorUUID, rp + actualReputation);
        });
    }
}
