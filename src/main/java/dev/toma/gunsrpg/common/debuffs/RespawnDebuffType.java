package dev.toma.gunsrpg.common.debuffs;

import java.util.Random;
import java.util.function.BooleanSupplier;

public class RespawnDebuffType<D extends IDebuff> extends DebuffType<D> {

    private final int debuffDuration;

    private RespawnDebuffType(RespawnDebuffBuilder<D> builder) {
        super(builder);
        this.debuffDuration = builder.debuffDuration;
    }

    public int getDebuffDuration() {
        return debuffDuration;
    }

    @Override
    public int getFlags() {
        return TriggerFlags.RESPAWN.getValue();
    }

    @Override
    public D onTrigger(IDebuffContext context, Random random) {
        return createRaw();
    }

    public static class RespawnDebuffBuilder<D extends IDebuff> implements IDebuffBuilder<D> {

        private int debuffDuration;
        private IFactory<D> factory;
        private BooleanSupplier stateSupplier;

        public RespawnDebuffBuilder<D> duration(int ticks) {
            this.debuffDuration = ticks;
            return this;
        }

        public RespawnDebuffBuilder<D> factory(IFactory<D> factory) {
            this.factory = factory;
            return this;
        }

        public RespawnDebuffBuilder<D> disableOn(BooleanSupplier supplier) {
            this.stateSupplier = supplier;
            return this;
        }

        public RespawnDebuffType<D> build() {
            return new RespawnDebuffType<>(this);
        }

        @Override
        public IFactory<D> getFactory() {
            return factory;
        }

        @Override
        public BooleanSupplier disabledStatusSupplier() {
            return stateSupplier;
        }
    }
}
