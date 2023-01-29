package dev.toma.gunsrpg.common.init;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.debuffs.sources.WeaponDamageSource;
import dev.toma.gunsrpg.common.debuffs.sources.*;

public final class ModDebuffSources {

    public static final DebuffSourceType<HurtByEntitySource> HURT_BY_ENTITY = new DebuffSourceType<>(GunsRPG.makeResource("hurt_by_entity"), HurtByEntitySource.CODEC);
    public static final DebuffSourceType<HurtBySlimeSource> HURT_BY_SLIME = new DebuffSourceType<>(GunsRPG.makeResource("hurt_by_slime"), HurtBySlimeSource.CODEC);
    public static final DebuffSourceType<EntityMeleeDamageSource> ENTITY_MELEE_DAMAGE = new DebuffSourceType<>(GunsRPG.makeResource("entity_melee_damage"), EntityMeleeDamageSource.CODEC);
    public static final DebuffSourceType<ExplosionDamageSource> EXPLOSION_DAMAGE = new DebuffSourceType<>(GunsRPG.makeResource("explosion_damage"), ExplosionDamageSource.CODEC);
    public static final DebuffSourceType<ExplosionConstantDamageSource> EXPLOSION_DAMAGE_CONSTANT = new DebuffSourceType<>(GunsRPG.makeResource("explosion_damage_constant"), ExplosionConstantDamageSource.CODEC);
    public static final DebuffSourceType<FallDamageSource> FALL_DAMAGE = new DebuffSourceType<>(GunsRPG.makeResource("fall_damage"), FallDamageSource.CODEC);
    public static final DebuffSourceType<FallConstantDamageSource> FALL_DAMAGE_CONSTANT = new DebuffSourceType<>(GunsRPG.makeResource("fall_damage_constant"), FallConstantDamageSource.CODEC);
    public static final DebuffSourceType<WeaponDamageSource> WEAPON_DAMAGE = new DebuffSourceType<>(GunsRPG.makeResource("weapon_damage"), WeaponDamageSource.CODEC);

    public static void register() {
        register(HURT_BY_ENTITY);
        register(HURT_BY_SLIME);
        register(ENTITY_MELEE_DAMAGE);
        register(EXPLOSION_DAMAGE);
        register(EXPLOSION_DAMAGE_CONSTANT);
        register(FALL_DAMAGE);
        register(FALL_DAMAGE_CONSTANT);
        register(WEAPON_DAMAGE);
    }

    private static void register(DebuffSourceType<?> type) {
        ModRegistries.DEBUFF_SOURCE_TYPES.register(type);
    }
}
