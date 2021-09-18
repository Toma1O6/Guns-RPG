package dev.toma.gunsrpg.api.common.data;

import dev.toma.gunsrpg.api.common.ITransaction;
import dev.toma.gunsrpg.api.common.ITransactionType;

public interface ITransactionManager {

    <T> void registerHandler(ITransactionType<T> type, ITransactionPredicate<T> predicate, ITransactionConsumer<T> handler);

    <T> boolean hasFunds(ITransaction<T> transaction);

    <T> void handleTransaction(ITransaction<T> transaction);

    interface ITransactionPredicate<T> {
        boolean hasFundsForTransaction(ITransaction<T> transaction);
    }

    interface ITransactionConsumer<T> {
        void processTransaction(ITransaction<T> transaction);
    }
}
