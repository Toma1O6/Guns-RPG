package dev.toma.gunsrpg.common.skills.transaction;

import dev.toma.gunsrpg.api.common.ITransaction;
import dev.toma.gunsrpg.api.common.ITransactionType;
import dev.toma.gunsrpg.common.skills.core.SkillType;

public final class TransactionTypes {

    public static final ITransactionType<SkillType<?>> SKILLPOINT_TRANSACTION = create(SkillPointTransaction::create);
    public static final ITransactionType<WeaponPointTransaction.IWeaponData> WEAPON_POINT_TRANSACTION = create((data, total) -> new WeaponPointTransaction(data));

    public static <T> ITransactionType<T> create(ITransactionFactory<T> factory) {
        return new SimpleTransactionType<>(factory);
    }

    @SuppressWarnings("unchecked")
    public static <R> ITransactionType<R> cast(ITransactionType<?> type) {
        return (ITransactionType<R>) type;
    }

    private static class SimpleTransactionType<T> implements ITransactionType<T> {

        private final ITransactionFactory<T> factory;

        private SimpleTransactionType(ITransactionFactory<T> factory) {
            this.factory = factory;
        }

        @Override
        public ITransaction<T> newTransaction(T data, int total) {
            return factory.createTransaction(data, total);
        }
    }

    public interface ITransactionFactory<T> {
        ITransaction<T> createTransaction(T data, int total);
    }
}
