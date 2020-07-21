package dev.toma.gunsrpg.debuffs;

import dev.toma.gunsrpg.common.capability.object.PlayerSkills;
import net.minecraft.entity.player.EntityPlayer;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class DebuffType<T extends Debuff> {

    protected String name;
    protected List<DamageResult> entityChances;
    protected List<Pair<Predicate<Integer>, Consumer<EntityPlayer>>> effectList;
    protected Supplier<T> factory;
    protected int timecap;
    protected Function<PlayerSkills, Integer> extraTime;

    protected DebuffType(TypeBuilder<T> builder) {
        this.name = builder.name;
        this.entityChances = builder.activationConditionList;
        this.factory = builder.factory;
        this.timecap = builder.tickCap;
        this.effectList = builder.effectList;
        this.extraTime = builder.extraTime;
    }

    public T createInstance() {
        return this.factory.get();
    }

    public String getName() {
        return name;
    }

    public static class TypeBuilder<T extends Debuff> {

        private String name;
        private final List<DamageResult> activationConditionList = new ArrayList<>();
        private final List<Pair<Predicate<Integer>, Consumer<EntityPlayer>>> effectList = new ArrayList<>();
        private final Supplier<T> factory;
        private int tickCap;
        private Function<PlayerSkills, Integer> extraTime;

        private TypeBuilder(Supplier<T> factory) {
            this.factory = factory;
        }

        public static <T extends Debuff> TypeBuilder<T> create(Supplier<T> factory) {
            return new TypeBuilder<>(factory);
        }

        public TypeBuilder<T> name(String name) {
            this.name = name;
            return this;
        }

        public TypeBuilder<T> cap(int ticks, Function<PlayerSkills, Integer> extraTime) {
            this.tickCap = ticks;
            this.extraTime = extraTime;
            return this;
        }

        public DamageResult.ResultBuilder<T> addHitData() {
            return new DamageResult.ResultBuilder<>(this);
        }

        public EffectBuilder<T> effectBuilder() {
            return new EffectBuilder<>(this);
        }

        public DebuffType<T> build() {
            DebuffType<T> debuffType = new DebuffType<>(this);
            DebuffTypes.TYPES.add(debuffType);
            return debuffType;
        }

        public List<DamageResult> conditions() {
            return this.activationConditionList;
        }
    }

    public static class EffectBuilder<T extends Debuff> {

        private final TypeBuilder<T> parent;
        private Predicate<Integer> condition;
        private Consumer<EntityPlayer> action;

        protected EffectBuilder(TypeBuilder<T> parent) {
            this.parent = parent;
        }

        public EffectBuilder<T> when(int value) {
            this.condition = i -> i == value;
            return this;
        }

        public EffectBuilder<T> when(int min, int max) {
            this.condition = i -> i >= min && i <= max;
            return this;
        }

        public EffectBuilder<T> when(Predicate<Integer> condition) {
            this.condition = condition;
            return this;
        }

        public EffectBuilder<T> executes(Consumer<EntityPlayer> action) {
            this.action = action;
            return this;
        }

        public TypeBuilder<T> pop() {
            this.parent.effectList.add(Pair.of(Objects.requireNonNull(condition), Objects.requireNonNull(action)));
            return this.parent;
        }
    }
}
