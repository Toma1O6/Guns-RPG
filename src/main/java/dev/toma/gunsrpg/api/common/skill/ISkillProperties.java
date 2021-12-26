package dev.toma.gunsrpg.api.common.skill;

public interface ISkillProperties {

    int getRequiredLevel();

    int getPrice();

    ITransactionValidator getTransactionValidator();
}
