package dev.toma.gunsrpg.common.skills.core;

import dev.toma.gunsrpg.api.common.skill.IDataResolver;
import dev.toma.gunsrpg.api.common.skill.ITransactionValidatorFactory;

public class PlayerLevelTransactionValidatorFactory implements ITransactionValidatorFactory<PlayerLevelTransactionValidator, Object> {

    @Override
    public PlayerLevelTransactionValidator createFor(Object data) {
        return new PlayerLevelTransactionValidator();
    }

    @Override
    public boolean isDataMatch(PlayerLevelTransactionValidator handler, Object data) {
        return true;
    }

    @Override
    public IDataResolver<Object> resolver() {
        return json -> null;
    }
}
