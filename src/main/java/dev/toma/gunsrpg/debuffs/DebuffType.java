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

public class DebuffType {

    protected String name;
    protected List<DamageResult> entityChances;
    protected List<Pair<Predicate<Integer>, Consumer<EntityPlayer>>> effectList;
    protected Supplier<? extends Debuff> factory;
    protected int timecap;
    protected Function<PlayerSkills, Integer> extraTime;
    protected Function<PlayerSkills, Float> resistance;

    protected DebuffType(TypeBuilder builder) {
        this.name = builder.name;
        this.entityChances = builder.activationConditionList;
        this.factory = builder.factory;
        this.timecap = builder.tickCap;
        this.effectList = builder.effectList;
        this.extraTime = builder.extraTime;
        this.resistance = builder.resistance;
    }

    public Debuff createInstance() {
        return this.factory.get();
    }

    public String getName() {
        return name;
    }

    public static class TypeBuilder {

        private String name;
        private final List<DamageResult> activationConditionList = new ArrayList<>();
        private final List<Pair<Predicate<Integer>, Consumer<EntityPlayer>>> effectList = new ArrayList<>();
        private final Supplier<? extends Debuff> factory;
        private int tickCap;
        private Function<PlayerSkills, Integer> extraTime;
        private Function<PlayerSkills, Float> resistance;

        private TypeBuilder(Supplier<? extends Debuff> factory) {
            this.factory = factory;
        }

        public static TypeBuilder create(Supplier<? extends Debuff> factory) {
            return new TypeBuilder(factory);
        }

        public TypeBuilder name(String name) {
            this.name = name;
            return this;
        }

        public TypeBuilder cap(int ticks, Function<PlayerSkills, Integer> extraTime) {
            this.tickCap = ticks;
            this.extraTime = extraTime;
            return this;
        }

        public TypeBuilder resistance(Function<PlayerSkills, Float> resistance) {
            this.resistance = resistance;
            return this;
        }

        public DamageResult.ResultBuilder addHitData() {
            return new DamageResult.ResultBuilder(this);
        }

        public EffectBuilder effectBuilder() {
            return new EffectBuilder(this);
        }

        public DebuffType build() {
            DebuffType debuffType = new DebuffType(this);
            DebuffTypes.TYPES.add(debuffType);
            return debuffType;
        }

        public List<DamageResult> conditions() {
            return this.activationConditionList;
        }
    }

    public static class EffectBuilder {

        private final TypeBuilder parent;
        private Predicate<Integer> condition;
        private Consumer<EntityPlayer> action;

        protected EffectBuilder(TypeBuilder parent) {
            this.parent = parent;
        }

        public EffectBuilder when(int value) {
            this.condition = i -> i == value;
            return this;
        }

        public EffectBuilder when(int min, int max) {
            this.condition = i -> i >= min && i <= max;
            return this;
        }

        public EffectBuilder when(Predicate<Integer> condition) {
            this.condition = condition;
            return this;
        }

        public EffectBuilder executes(Consumer<EntityPlayer> action) {
            this.action = action;
            return this;
        }

        public TypeBuilder pop() {
            this.parent.effectList.add(Pair.of(Objects.requireNonNull(condition), Objects.requireNonNull(action)));
            return this.parent;
        }
    }
}
