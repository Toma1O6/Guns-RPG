package dev.toma.gunsrpg.common.skills.core;

import dev.toma.gunsrpg.api.common.skill.ITransactionValidator;
import dev.toma.gunsrpg.api.common.skill.ISkillProperties;

public class SkillProperties implements ISkillProperties {

    private final int level;
    private final int price;
    private final ITransactionValidator validator;

    public SkillProperties(int level, int price, ITransactionValidator validator) {
        this.level = level;
        this.price = price;
        this.validator = validator;
    }

    @Override
    public int getRequiredLevel() {
        return level;
    }

    @Override
    public int getPrice() {
        return price;
    }

    @Override
    public ITransactionValidator getTransactionValidator() {
        return validator;
    }
}
