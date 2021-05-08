package dev.toma.gunsrpg.common.debuffs;

import com.google.common.base.Preconditions;
import dev.toma.gunsrpg.common.capability.object.PlayerSkills;
import dev.toma.gunsrpg.util.function.ToFloatBiFunction;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;

public final class DebuffType extends IForgeRegistryEntry.Impl<DebuffType> {

    private final ToIntFunction<PlayerSkills> debuffProgressTime;
    private final ToFloatBiFunction<PlayerSkills, DamageSource> resistChance;
    private final List<ToFloatBiFunction<PlayerSkills, DamageContext>> conditions;
    private final Stage[] debuffStages;
    private final Supplier<Debuff> factory;
    private final BooleanSupplier isBlacklisted;

    public DebuffType(Builder builder) {
        this.debuffProgressTime = builder.debuffProgressTime;
        this.resistChance = builder.resistChance;
        this.conditions = builder.conditions;
        List<Stage> stages = builder.stages;
        stages.sort(Comparator.comparingInt(s -> s.lastVal));
        this.debuffStages = stages.toArray(new Stage[0]);
        this.factory = builder.factory;
        this.isBlacklisted = builder.isBlacklisted;
    }

    public boolean isBlacklisted() {
        return isBlacklisted.getAsBoolean();
    }

    public void tickStage(int idx, EntityPlayer player) {
        this.debuffStages[idx].effect.accept(player);
    }

    public Debuff create() {
        return factory.get();
    }

    public int getProgressDelay(PlayerSkills skills) {
        return debuffProgressTime.applyAsInt(skills);
    }

    public float getResistChance(PlayerSkills skills, DamageSource source) {
        return resistChance.applyAsFloat(skills, source);
    }

    public List<ToFloatBiFunction<PlayerSkills, DamageContext>> getConditions() {
        return conditions;
    }

    protected int getAppropriateStage(int debuffProgressPct) {
        for(int i = 0; i < debuffStages.length; i++) {
            if(debuffProgressPct <= debuffStages[i].lastVal) {
                return i;
            }
        }
        throw new IllegalStateException("Couldn't get appropriate debuff stage. \"Someone\" messed up setup");
    }

    static class Stage {
        final int lastVal;
        final Consumer<EntityPlayer> effect;

        public Stage(int max, Consumer<EntityPlayer> effect) {
            this.lastVal = max;
            this.effect = effect;
        }
    }

    public static class Builder {

        private ToIntFunction<PlayerSkills> debuffProgressTime;
        private ToFloatBiFunction<PlayerSkills, DamageSource> resistChance;
        private final List<ToFloatBiFunction<PlayerSkills, DamageContext>> conditions;
        private final List<Stage> stages = new ArrayList<>();
        private Supplier<Debuff> factory;
        private BooleanSupplier isBlacklisted = () -> false;

        private Builder() {
            conditions = new ArrayList<>();
        }

        public static Builder create() {
            return new Builder();
        }

        public Builder progress(ToIntFunction<PlayerSkills> debuffProgressTime) {
            this.debuffProgressTime = debuffProgressTime;
            return this;
        }

        public Builder resist(ToFloatBiFunction<PlayerSkills, DamageSource> resistChance) {
            this.resistChance = resistChance;
            return this;
        }

        public Builder condition(ToFloatBiFunction<PlayerSkills, DamageContext> condition) {
            this.conditions.add(Objects.requireNonNull(condition, "Condition must be non-null"));
            return this;
        }

        public Builder addStage(int maxPct, Consumer<EntityPlayer> effect) {
            this.stages.add(new Stage(maxPct, effect));
            return this;
        }

        public Builder factory(Supplier<Debuff> factory) {
            this.factory = factory;
            return this;
        }

        public Builder blacklistOn(BooleanSupplier supplier) {
            this.isBlacklisted = supplier;
            return this;
        }

        public DebuffType build() {
            Preconditions.checkNotNull(isBlacklisted, "Undefined blacklist supplier");
            Preconditions.checkNotNull(debuffProgressTime, "Undefined progress timer");
            Preconditions.checkNotNull(resistChance, "Undefined resist chance");
            Preconditions.checkState(!conditions.isEmpty(), "Condition list is empty");
            Preconditions.checkState(!stages.isEmpty(), "Stages are empty");
            Preconditions.checkNotNull(factory, "Instance factory is null");
            return new DebuffType(this);
        }
    }
}
