package dev.toma.gunsrpg.api.common.skill;

public interface ITransactionValidatorFactory<T extends ITransactionValidator, D> {

    T createFor(D data);

    boolean isDataMatch(T handler, D data);

    IDataResolver<D> resolver();
}
