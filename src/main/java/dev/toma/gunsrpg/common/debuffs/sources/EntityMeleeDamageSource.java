package dev.toma.gunsrpg.common.debuffs.sources;

import com.mojang.serialization.Codec;
import dev.toma.gunsrpg.common.debuffs.IDebuffContext;
import dev.toma.gunsrpg.common.init.ModDebuffSources;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;

public class EntityMeleeDamageSource extends AbstractDamageSource {

    public static final Codec<EntityMeleeDamageSource> CODEC = Codec.FLOAT.fieldOf("chance")
            .xmap(EntityMeleeDamageSource::new, t -> t.chance).codec();
    private final float chance;

    public EntityMeleeDamageSource(float chance) {
        this.chance = chance;
    }

    @Override
    public boolean isValid(IDebuffContext context) {
        DamageSource source = context.getSource();
        return source instanceof EntityDamageSource && !source.isExplosion() && !source.isProjectile();
    }

    @Override
    public float getActualChance(IDebuffContext context) {
        return this.chance;
    }

    @Override
    public DebuffSourceType<?> getType() {
        return ModDebuffSources.ENTITY_MELEE_DAMAGE;
    }
}
