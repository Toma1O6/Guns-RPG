package dev.toma.gunsrpg.common.debuffs.sources;

import com.mojang.serialization.Codec;
import dev.toma.gunsrpg.common.debuffs.IDebuffContext;
import dev.toma.gunsrpg.common.init.ModDebuffSources;

public class WeaponDamageSource extends AbstractDamageSource {

    public static final Codec<WeaponDamageSource> CODEC = Codec.FLOAT.fieldOf("chance")
            .xmap(WeaponDamageSource::new, t -> t.chance).codec();
    private final float chance;

    public WeaponDamageSource(float chance) {
        this.chance = chance;
    }

    @Override
    public boolean isValid(IDebuffContext context) {
        return context.getSource() instanceof dev.toma.gunsrpg.world.WeaponDamageSource;
    }

    @Override
    public float getActualChance(IDebuffContext context) {
        return chance;
    }

    @Override
    public DebuffSourceType<?> getType() {
        return ModDebuffSources.WEAPON_DAMAGE;
    }
}
