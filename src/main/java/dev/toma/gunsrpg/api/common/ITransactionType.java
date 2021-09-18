package dev.toma.gunsrpg.api.common;

public interface ITransactionType<T> {

    ITransaction<T> newTransaction(T data, int total);
}
