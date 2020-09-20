package dev.toma.gunsrpg.common.debuffs;

import net.minecraft.util.DamageSource;

public class DamageContext {

    final DamageSource source;
    final float amount;

    private DamageContext(DamageSource source, float amount) {
        this.source = source;
        this.amount = amount;
    }

    public static DamageContext getContext(DamageSource source, float amount) {
        return new DamageContext(source, amount);
    }

    public DamageSource getSource() {
        return source;
    }

    public float getAmount() {
        return amount;
    }
}
