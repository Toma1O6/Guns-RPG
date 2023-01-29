package dev.toma.gunsrpg.common.debuffs.sources;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.toma.gunsrpg.common.debuffs.IDebuffContext;
import dev.toma.gunsrpg.common.init.ModDebuffSources;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.util.DamageSource;

public class HurtBySlimeSource implements DebuffSource {

    public static final Codec<HurtBySlimeSource> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.FLOAT.fieldOf("baseChance").forGetter(t -> t.baseChance),
            Codec.FLOAT.fieldOf("sizeIncrements").forGetter(t -> t.sizeIncrements)
    ).apply(instance, HurtBySlimeSource::new));
    private final float baseChance;
    private final float sizeIncrements;

    public HurtBySlimeSource(float baseChance, float sizeIncrements) {
        this.baseChance = baseChance;
        this.sizeIncrements = sizeIncrements;
    }

    @Override
    public float getChance(IDebuffContext context) {
        DamageSource source = context.getSource();
        if (HurtByEntitySource.isValidEntitySource(source)) {
            Entity entity = source.getDirectEntity();
            EntityType<?> type = entity.getType();
            if (type == EntityType.SLIME) {
                int size = ((SlimeEntity) entity).getSize();
                return this.baseChance + size * this.sizeIncrements;
            }
        }
        return 0.0F;
    }

    @Override
    public DebuffSourceType<?> getType() {
        return ModDebuffSources.HURT_BY_SLIME;
    }
}
