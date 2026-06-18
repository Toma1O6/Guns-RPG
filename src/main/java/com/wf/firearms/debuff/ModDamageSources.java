package com.wf.firearms.debuff;

import com.wf.firearms.GunsRpg;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public final class ModDamageSources {
    public static final ResourceKey<DamageType> BLEEDING =
            ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(GunsRpg.MOD_ID, "bleeding"));
    public static final ResourceKey<DamageType> FRACTURE =
            ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(GunsRpg.MOD_ID, "fracture"));
    public static final ResourceKey<DamageType> POISON =
            ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(GunsRpg.MOD_ID, "poison"));
    public static final ResourceKey<DamageType> INFECTION =
            ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(GunsRpg.MOD_ID, "infection"));

    private ModDamageSources() {}

    public static DamageSource bleeding(Level level) {
        return new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(BLEEDING));
    }

    public static DamageSource fracture(Level level) {
        return new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(FRACTURE));
    }

    public static DamageSource poison(Level level) {
        return new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(POISON));
    }

    public static DamageSource infection(Level level) {
        return new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(INFECTION));
    }

    public static DamageSource forDebuff(Level level, DebuffType type) {
        return switch (type) {
            case BLEED -> bleeding(level);
            case FRACTURE -> fracture(level);
            case POISON -> poison(level);
            case INFECTION -> infection(level);
        };
    }

    public static boolean isDebuffDamage(net.minecraft.world.damagesource.DamageSource source) {
        return source.is(BLEEDING) || source.is(FRACTURE) || source.is(POISON) || source.is(INFECTION);
    }
}
