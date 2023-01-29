package dev.toma.gunsrpg.common.debuffs.sources;

import dev.toma.gunsrpg.common.debuffs.IDebuffContext;

public abstract class AbstractDamageSource implements DebuffSource {

    public abstract boolean isValid(IDebuffContext context);

    public abstract float getActualChance(IDebuffContext context);

    @Override
    public float getChance(IDebuffContext context) {
        if (this.isValid(context)) {
            return this.getActualChance(context);
        }
        return 0;
    }
}
