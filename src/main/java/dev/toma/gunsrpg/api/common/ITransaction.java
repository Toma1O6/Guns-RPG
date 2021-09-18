package dev.toma.gunsrpg.api.common;

public interface ITransaction<T> {

    int total();

    T getData();

    ITransactionType<T> getType();
}
