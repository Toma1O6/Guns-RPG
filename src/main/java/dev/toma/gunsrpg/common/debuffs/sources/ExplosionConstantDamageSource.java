package dev.toma.gunsrpg.common.debuffs.sources;

import com.mojang.serialization.Codec;
import dev.toma.gunsrpg.common.debuffs.IDebuffContext;
import dev.toma.gunsrpg.common.init.ModDebuffSources;

public class ExplosionConstantDamageSource extends AbstractExplosionDamageSource {

    public static final Codec<ExplosionConstantDamageSource> CODEC = Codec.FLOAT.fieldOf("chance")
            .xmap(ExplosionConstantDamageSource::new, t -> t.chance).codec();
    private final float chance;

    public ExplosionConstantDamageSource(float chance) {
        this.chance = chance;
    }

    @Override
    public float getActualChance(IDebuffContext context) {
        return this.chance;
    }

    @Override
    public DebuffSourceType<?> getType() {
        return ModDebuffSources.EXPLOSION_DAMAGE_CONSTANT;
    }
}
