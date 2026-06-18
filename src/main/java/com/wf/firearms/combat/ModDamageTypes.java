package com.wf.firearms.combat;

import com.wf.firearms.GunsRpg;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public final class ModDamageTypes {
    public static final ResourceKey<DamageType> BULLET = ResourceKey.create(
            Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(GunsRpg.MOD_ID, "bullet"));

    private ModDamageTypes() {}

    public static DamageSource bullet(Level level, Entity shooter) {
        return new DamageSource(level.registryAccess()
                .registryOrThrow(Registries.DAMAGE_TYPE)
                .getHolderOrThrow(BULLET), shooter);
    }

    public static boolean isBullet(DamageSource source) {
        return "gunsrpg.bullet".equals(source.type().msgId());
    }
}
