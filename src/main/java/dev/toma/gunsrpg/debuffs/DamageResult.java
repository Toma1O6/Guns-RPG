package dev.toma.gunsrpg.debuffs;

import net.minecraft.util.DamageSource;

import java.util.Objects;
import java.util.function.Predicate;

public class DamageResult {

    private Predicate<DamageSource> predicate;
    private float chance;

    private DamageResult(ResultBuilder<?> builder) {
        this.predicate = Objects.requireNonNull(builder.predicate);
        this.chance = Math.min(1.0F, Math.max(0.0F, builder.chance));
    }

    public float getChance() {
        return chance;
    }

    public boolean allows(DamageSource source) {
        return this.predicate.test(source);
    }

    public static class ResultBuilder<T extends Debuff> {

        private final DebuffType.TypeBuilder<T> parent;
        private Predicate<DamageSource> predicate;
        private float chance;

        protected ResultBuilder(DebuffType.TypeBuilder<T> parent) {
            this.parent = parent;
        }

        public ResultBuilder<T> condition(Predicate<DamageSource> predicate) {
            this.predicate = predicate;
            return this;
        }

        public ResultBuilder<T> chance(float chance) {
            this.chance = chance;
            return this;
        }

        public DebuffType.TypeBuilder<T> pop() {
            this.parent.conditions().add(new DamageResult(this));
            return this.parent;
        }
    }
}
