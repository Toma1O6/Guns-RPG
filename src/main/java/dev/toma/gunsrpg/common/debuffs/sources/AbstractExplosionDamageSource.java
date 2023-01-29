package dev.toma.gunsrpg.common.debuffs.sources;

import dev.toma.gunsrpg.common.debuffs.IDebuffContext;

public abstract class AbstractExplosionDamageSource extends AbstractDamageSource {

    @Override
    public boolean isValid(IDebuffContext context) {
        return context.getSource().isExplosion();
    }
}
