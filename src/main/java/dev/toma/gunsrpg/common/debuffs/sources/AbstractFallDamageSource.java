package dev.toma.gunsrpg.common.debuffs.sources;

import dev.toma.gunsrpg.common.debuffs.IDebuffContext;
import net.minecraft.util.DamageSource;

public abstract class AbstractFallDamageSource extends AbstractDamageSource {

    @Override
    public boolean isValid(IDebuffContext context) {
        return context.getSource() == DamageSource.FALL;
    }
}
