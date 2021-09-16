package dev.toma.gunsrpg.common.debuffs;

import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.common.capability.object.PlayerSkills;
import dev.toma.gunsrpg.util.function.ToFloatFunction;
import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.ToIntFunction;

public class StagedDebuffType<D extends IStagedDebuff> extends DebuffType<D> {

    private final ToIntFunction<PlayerSkills> progress;
    private final ToFloatFunction<IDebuffContext> resist;
    private final ToFloatFunction<IDebuffContext>[] applyConditions;
    private final LinkedStage head;

    @SuppressWarnings("unchecked")
    private StagedDebuffType(StagedBuilder<D> builder) {
        super(builder);
        this.progress = builder.progress;
        this.resist = builder.resistChance;
        this.applyConditions = builder.conditions.toArray(new ToFloatFunction[0]);
        this.head = builder.head;
    }

    @Override
    public D onTrigger(IDebuffContext context, Random random) {
        float resist = this.resist.applyAsFloat(context);
        if (resist > 0 && random.nextFloat() < resist)
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

    public int getProgressionTarget(IPlayerData data) {
        return progress.applyAsInt(data.getSkills());
    }

    public LinkedStage firstStage() {
        return head;
    }

    @Override
    public int getFlags() {
        return TriggerFlags.HURT.getValue();
    }

    public static StagedDebuffType<IStagedDebuff> createPoisonType() {
        return new StagedBuilder<>()

                .build();
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

        private ToIntFunction<PlayerSkills> progress;
        private ToFloatFunction<IDebuffContext> resistChance;
        private List<ToFloatFunction<IDebuffContext>> conditions = new ArrayList<>();
        private LinkedStage head;
        private LinkedStage last;
        private IFactory<D> factory;
        private BooleanSupplier disabledStatus; // for configs

        public StagedBuilder<D> progressTimeFunc(ToIntFunction<PlayerSkills> progress) {
            this.progress = progress;
            return this;
        }

        public StagedBuilder<D> resistance(ToFloatFunction<IDebuffContext> resistChance) {
            this.resistChance = resistChance;
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
