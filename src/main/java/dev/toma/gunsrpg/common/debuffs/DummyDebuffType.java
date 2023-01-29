package dev.toma.gunsrpg.common.debuffs;

import dev.toma.gunsrpg.api.common.data.IDebuffs;
import dev.toma.gunsrpg.api.common.data.IPlayerData;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Random;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

public final class DummyDebuffType<D extends IStagedDebuff> extends DebuffType<D> {

    private final Supplier<DataDrivenDebuffType<?>> linkedDebuff;

    private DummyDebuffType(DummyBuilder<D> builder) {
        super(builder);
        this.linkedDebuff = builder.linkedDebuff;
    }

    @Override
    public D onTrigger(IDebuffContext context, Random random, @Nullable Object data) {
        IPlayerData playerData = context.getData();
        IDebuffs debuffs = playerData.getDebuffControl();
        DataDrivenDebuffType<?> linked = this.linkedDebuff.get();
        if (debuffs.hasDebuff(linked)) {
            return null;
        }
        if (data instanceof DataDrivenDebuffType<?>) {
            DataDrivenDebuffType<?> type = (DataDrivenDebuffType<?>) data;
            if (type == linked) {
                return this.createRaw();
            }
        }
        return null;
    }

    @Override
    public int getFlags() {
        return TriggerFlags.HEAL.getValue();
    }

    @Override
    public boolean isToggleable() {
        return false;
    }

    public DataDrivenDebuffType<?> getLinkedType() {
        return this.linkedDebuff.get();
    }

    public static final class DummyBuilder<D extends IStagedDebuff> implements IDebuffBuilder<D> {

        private Supplier<DataDrivenDebuffType<?>> linkedDebuff;
        private IFactory<D> factory;

        public DummyBuilder<D> factory(IFactory<D> factory) {
            this.factory = factory;
            return this;
        }

        public DummyBuilder<D> linkedTo(Supplier<DataDrivenDebuffType<?>> typeSupplier) {
            this.linkedDebuff = typeSupplier;
            return this;
        }

        public DummyDebuffType<D> build() {
            Objects.requireNonNull(factory);
            Objects.requireNonNull(linkedDebuff);
            return new DummyDebuffType<>(this);
        }

        @Override
        public IFactory<D> getFactory() {
            return factory;
        }

        @Override
        public BooleanSupplier disabledStatusSupplier() {
            return () -> false;
        }
    }
}
