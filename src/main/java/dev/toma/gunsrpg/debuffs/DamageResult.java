package dev.toma.gunsrpg.debuffs;

import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.capability.object.PlayerSkills;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public class DamageResult {

    private final Predicate<DamageSource> predicate;
    private final Function<PlayerSkills, Float> chance;

    private DamageResult(ResultBuilder builder) {
        this.predicate = Objects.requireNonNull(builder.predicate);
        this.chance = builder.chance;
    }

    public float getChance(EntityPlayer player) {
        return chance.apply(PlayerDataFactory.get(player).getSkills());
    }

    public boolean allows(DamageSource source) {
        return this.predicate.test(source);
    }

    public static class ResultBuilder {

        private final DebuffType.TypeBuilder parent;
        private Predicate<DamageSource> predicate;
        private Function<PlayerSkills, Float> chance;

        protected ResultBuilder(DebuffType.TypeBuilder parent) {
            this.parent = parent;
        }

        public ResultBuilder condition(Predicate<DamageSource> predicate) {
            this.predicate = predicate;
            return this;
        }

        public ResultBuilder chance(float chance) {
            this.chance = skills -> chance;
            return this;
        }

        public ResultBuilder chance(Function<PlayerSkills, Float> chance) {
            this.chance = chance;
            return this;
        }

        public DebuffType.TypeBuilder pop() {
            this.parent.conditions().add(new DamageResult(this));
            return this.parent;
        }
    }
}
