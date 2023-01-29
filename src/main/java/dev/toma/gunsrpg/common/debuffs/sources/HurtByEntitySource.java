package dev.toma.gunsrpg.common.debuffs.sources;

import com.mojang.serialization.Codec;
import dev.toma.gunsrpg.common.debuffs.IDebuffContext;
import dev.toma.gunsrpg.common.init.ModDebuffSources;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.registry.Registry;

import java.util.Map;

public class HurtByEntitySource implements DebuffSource {

    public static final Codec<HurtByEntitySource> CODEC = Codec.unboundedMap(Registry.ENTITY_TYPE, Codec.FLOAT)
            .fieldOf("entityMap").xmap(HurtByEntitySource::new, t -> t.entityMap).codec();
    private final Map<EntityType<?>, Float> entityMap;

    public HurtByEntitySource(Map<EntityType<?>, Float> entityMap) {
        this.entityMap = entityMap;
    }

    public static boolean isValidEntitySource(DamageSource source) {
        return source instanceof EntityDamageSource && !((EntityDamageSource) source).isThorns() && source.getEntity() != null;
    }

    @Override
    public float getChance(IDebuffContext context) {
        DamageSource source = context.getSource();
        if (isValidEntitySource(source)) {
            Entity entity = source.getEntity();
            EntityType<?> type = entity.getType();
            return this.entityMap.getOrDefault(type, 0.0F);
        }
        return 0.0F;
    }

    @Override
    public DebuffSourceType<?> getType() {
        return ModDebuffSources.HURT_BY_ENTITY;
    }
}
