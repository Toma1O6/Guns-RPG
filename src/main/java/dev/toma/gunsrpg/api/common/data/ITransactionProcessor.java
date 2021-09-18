package dev.toma.gunsrpg.api.common.data;

import dev.toma.gunsrpg.api.common.ITransaction;

public interface ITransactionProcessor {

    boolean hasFunds(ITransaction<?> transaction);

    void processTransaction(ITransaction<?> transaction);
}
