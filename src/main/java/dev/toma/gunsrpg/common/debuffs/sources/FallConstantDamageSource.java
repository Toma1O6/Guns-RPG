package dev.toma.gunsrpg.common.debuffs.sources;

import com.mojang.serialization.Codec;
import dev.toma.gunsrpg.common.debuffs.IDebuffContext;
import dev.toma.gunsrpg.common.init.ModDebuffSources;

public class FallConstantDamageSource extends AbstractFallDamageSource {

    public static final Codec<FallConstantDamageSource> CODEC = Codec.FLOAT.fieldOf("chance")
            .xmap(FallConstantDamageSource::new, t -> t.chance).codec();
    private final float chance;

    public FallConstantDamageSource(float chance) {
        this.chance = chance;
    }

    @Override
    public float getActualChance(IDebuffContext context) {
        return this.chance;
    }

    @Override
    public DebuffSourceType<?> getType() {
        return ModDebuffSources.FALL_DAMAGE_CONSTANT;
    }
}
