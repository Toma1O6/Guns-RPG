package dev.toma.gunsrpg.common.skills.transaction;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.ITransaction;
import dev.toma.gunsrpg.api.common.ITransactionType;
import dev.toma.gunsrpg.api.common.data.ITransactionManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.IdentityHashMap;
import java.util.Map;

public class TransactionManager implements ITransactionManager {

    private static final Marker MARKER = MarkerManager.getMarker("Transactions");
    private final Map<ITransactionType<?>, ITransactionHandler<?>> handlerMap = new IdentityHashMap<>();

    @Override
    public <T> void registerHandler(ITransactionType<T> type, ITransactionPredicate<T> fundChecker, ITransactionConsumer<T> handler) {
        handlerMap.put(type, new ITransactionHandler<T>() {
            @Override
            public boolean hasFunds(ITransaction<T> transaction) {
                return fundChecker.hasFundsForTransaction(transaction);
            }

            @Override
            public void handle(ITransaction<T> transaction) {
                handler.processTransaction(transaction);
            }
        });
    }

    @Override
    public <T> boolean hasFunds(ITransaction<T> transaction) {
        ITransactionHandler<T> handler = getHandler(transaction.getType());
        return handler != null && handler.hasFunds(transaction);
    }

    @Override
    public <T> void handleTransaction(ITransaction<T> transaction) {
        ITransactionHandler<T> handler = getHandler(transaction.getType());
        if (handler != null) {
            handler.handle(transaction);
        } else {
            GunsRPG.log.fatal(MARKER, "Received invalid transaction [Transaction=(amount={}, data={})]", transaction.total(), transaction.getData());
        }
    }

    @SuppressWarnings("unchecked")
    private <T> ITransactionHandler<T> getHandler(ITransactionType<T> type) {
        return (ITransactionHandler<T>) handlerMap.get(type);
    }

    public interface ITransactionHandler<T> {

        boolean hasFunds(ITransaction<T> transaction);

        void handle(ITransaction<T> transaction);
    }
}
