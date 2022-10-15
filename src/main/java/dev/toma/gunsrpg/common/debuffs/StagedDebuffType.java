package dev.toma.gunsrpg.common.debuffs;

import dev.toma.gunsrpg.api.common.attribute.IAttribute;
import dev.toma.gunsrpg.api.common.attribute.IAttributeId;
import dev.toma.gunsrpg.api.common.attribute.IAttributeModifier;
import dev.toma.gunsrpg.api.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.common.attribute.ExpiringModifier;
import dev.toma.gunsrpg.util.function.ToFloatFunction;
import net.minecraft.entity.player.PlayerEntity;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public class StagedDebuffType<D extends IStagedDebuff> extends DebuffType<D> {

    private final IAttributeId delay;
    private final IAttributeId resistance;
    private final IAttributeId blockingAttribute;
    private final ToFloatFunction<IDebuffContext>[] applyConditions;
    private final LinkedStage head;

    @SuppressWarnings("unchecked")
    private StagedDebuffType(StagedBuilder<D> builder) {
        super(builder);
        this.delay = builder.delayAttribute;
        this.resistance = builder.resistanceAttribute;
        this.blockingAttribute = builder.blockingAttribute;
        this.applyConditions = builder.conditions.toArray(new ToFloatFunction[0]);
        this.head = builder.head;
    }

    @Override
    public D onTrigger(IDebuffContext context, Random random, @Nullable Object data) {
        IAttributeProvider provider = context.getData().getAttributes();
        float resist = provider.getAttribute(resistance).floatValue();
        if (this.isTemporarilyDisabled(provider) || (resist > 0 && random.nextFloat() < resist))
            return null; // blocked by resistance
        for (ToFloatFunction<IDebuffContext> function : applyConditions) {
            float chance = function.applyAsFloat(context);
            if (chance <= 0)
                continue; // can't get debuff from this context
            if (random.nextFloat() <= chance) {
                return createRaw();
            }
        }
        return null;
    }

    public static float getBuffedProgress(IAttributeProvider provider, IAttributeId blockingAttribute) {
        IAttribute attribute = provider.getAttribute(blockingAttribute);
        List<IAttributeModifier> list = attribute.listModifiers();
        if (list.isEmpty()) {
            return 0.0F;
        }
        IAttributeModifier modifier = list.get(0);
        if (modifier instanceof ExpiringModifier) {
            ExpiringModifier expiringModifier = (ExpiringModifier) modifier;
            int total = expiringModifier.getInitialTime();
            int left = expiringModifier.getTimeLeft();
            return (float) left / total;
        }
        return 0.0F;
    }

    public int getDelay(IPlayerData data) {
        IAttributeProvider provider = data.getAttributes();
        return provider.getAttribute(delay).intValue();
    }

    public boolean isTemporarilyDisabled(IAttributeProvider provider) {
        return provider.getAttribute(blockingAttribute).intValue() > 0;
    }

    public IAttributeId getBlockingAttribute() {
        return blockingAttribute;
    }

    public LinkedStage firstStage() {
        return head;
    }

    @Override
    public int getFlags() {
        return TriggerFlags.HURT.getValue();
    }

    public static class LinkedStage {

        private LinkedStage next;
        private final int limit;
        private final Consumer<PlayerEntity> stageEvent;

        public LinkedStage(int limit, Consumer<PlayerEntity> stageEvent) {
            this.limit = limit;
            this.stageEvent = stageEvent;
        }

        public int getLimit() {
            return limit;
        }

        public LinkedStage getNext() {
            return next;
        }

        public Consumer<PlayerEntity> getStageEvent() {
            return stageEvent;
        }

        private void link(LinkedStage stage) {
            next = stage;
        }
    }

    public static class StagedBuilder<D extends IStagedDebuff> implements IDebuffBuilder<D> {

        private final List<ToFloatFunction<IDebuffContext>> conditions = new ArrayList<>();
        private IAttributeId delayAttribute;
        private IAttributeId resistanceAttribute;
        private IAttributeId blockingAttribute;
        private LinkedStage head;
        private LinkedStage last;
        private IFactory<D> factory;
        private BooleanSupplier disabledStatus; // for configs

        public StagedBuilder<D> delay(IAttributeId attributeId) {
            this.delayAttribute = attributeId;
            return this;
        }

        public StagedBuilder<D> resistance(IAttributeId attributeId) {
            this.resistanceAttribute = attributeId;
            return this;
        }

        public StagedBuilder<D> blockingAttribute(IAttributeId attributeId) {
            this.blockingAttribute = attributeId;
            return this;
        }

        public StagedBuilder<D> addApplyConstraint(ToFloatFunction<IDebuffContext> constraint) {
            this.conditions.add(constraint);
            return this;
        }

        public StagedBuilder<D> stage(int limit, Consumer<PlayerEntity> stageEvent) {
            LinkedStage stage = new LinkedStage(limit, stageEvent);
            if (head == null)
                head = stage;
            if (last == null) {
                last = head;
            } else {
                last.link(stage);
                last = stage;
            }
            return this;
        }

        public StagedBuilder<D> factory(IFactory<D> factory) {
            this.factory = factory;
            return this;
        }

        public StagedBuilder<D> disableOn(BooleanSupplier statusSupplier) {
            disabledStatus = statusSupplier;
            return this;
        }

        public StagedDebuffType<D> build() {
            return new StagedDebuffType<>(this);
        }

        @Override
        public IFactory<D> getFactory() {
            return factory;
        }

        @Override
        public BooleanSupplier disabledStatusSupplier() {
            return disabledStatus;
        }
    }
}
