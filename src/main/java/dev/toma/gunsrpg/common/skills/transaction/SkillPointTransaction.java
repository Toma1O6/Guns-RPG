package dev.toma.gunsrpg.common.skills.transaction;

import dev.toma.gunsrpg.api.common.ITransaction;
import dev.toma.gunsrpg.api.common.ITransactionType;
import dev.toma.gunsrpg.common.skills.core.SkillType;

public class SkillPointTransaction implements ITransaction<SkillType<?>> {

    private final SkillType<?> type;

    public static SkillPointTransaction create(SkillType<?> type, int total) {
        return new SkillPointTransaction(type);
    }

    public SkillPointTransaction(SkillType<?> type) {
        this.type = type;
    }

    @Override
    public int total() {
        return type.price;
    }

    @Override
    public SkillType<?> getData() {
        return type;
    }

    @Override
    public ITransactionType<SkillType<?>> getType() {
        return TransactionTypes.SKILLPOINT_TRANSACTION;
    }
}
