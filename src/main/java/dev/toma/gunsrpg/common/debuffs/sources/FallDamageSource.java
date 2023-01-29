package dev.toma.gunsrpg.common.debuffs.sources;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.toma.gunsrpg.common.attribute.Attribs;
import dev.toma.gunsrpg.common.debuffs.IDebuffContext;
import dev.toma.gunsrpg.common.init.ModDebuffSources;

public class FallDamageSource extends AbstractFallDamageSource {

    public static final Codec<FallDamageSource> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.FLOAT.fieldOf("damageDownscale").forGetter(t -> t.damageDownscale),
            Codec.FLOAT.fieldOf("multiplier").forGetter(t -> t.multiplier)
    ).apply(instance, FallDamageSource::new));
    private final float damageDownscale;
    private final float multiplier;

    public FallDamageSource(float damageDownscale, float multiplier) {
        this.damageDownscale = damageDownscale;
        this.multiplier = multiplier;
    }

    @Override
    public float getActualChance(IDebuffContext context) {
        float amount = context.getReceivedDamage();
        float f = amount / this.damageDownscale;
        float resist = context.getData().getAttributes().getAttribute(Attribs.FALL_RESISTANCE).floatValue();
        return f * this.multiplier * (Math.max(0.0F, 1.0F - resist));
    }

    @Override
    public DebuffSourceType<?> getType() {
        return ModDebuffSources.FALL_DAMAGE;
    }
}
