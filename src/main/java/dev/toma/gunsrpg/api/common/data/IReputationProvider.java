package dev.toma.gunsrpg.api.common.data;

import java.util.UUID;

public interface IReputationProvider {

    float getReputation(UUID uuid);

    void setReputation(UUID uuid, float reputation);

    default void addReputation(UUID uuid, float addReputationValue) {
        float rep = this.getReputation(uuid);
        this.setReputation(uuid, rep + addReputationValue);
    }

    default void removeReputation(UUID uuid, float removeReputationValue) {
        this.addReputation(uuid, -removeReputationValue);
    }
}
