package dev.toma.gunsrpg.common.attribute;

import java.util.UUID;

public final class TemporaryModifiers {

    public static final ITemporaryModifierFactory MORPHINE_WEAPON_DAMAGE = factory(UUID.fromString("DB60A6B1-B1FE-4ECC-8D25-CDABBC7DB5EB"), AttributeOps.MUL, 1.2, 600);

    private static ITemporaryModifierFactory factory(UUID uid, IModifierOp op, double value, int ticks) {
        return () -> new TemporaryModifier(uid, op, value, ticks);
    }

    private TemporaryModifiers() {}
}
