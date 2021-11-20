package dev.toma.gunsrpg.common.attribute;

import dev.toma.gunsrpg.common.attribute.serialization.IModifierSeralizer;
import dev.toma.gunsrpg.common.attribute.serialization.ModifierSerialization;

import java.util.UUID;

public class TemporaryModifier extends AttributeModifier implements ITickableModifier {

    private int ticksLeft;

    public TemporaryModifier(String uid, IModifierOp op, double value, int ticks) {
        this(UUID.fromString(uid), op, value, ticks);
    }

    public TemporaryModifier(UUID uid, IModifierOp op, double value, int ticks) {
        super(uid, op, value);
        this.ticksLeft = ticks;
    }

    @Override
    public void tick() {
        --ticksLeft;
    }

    @Override
    public boolean shouldRemove() {
        return ticksLeft <= 0;
    }

    @Override
    public IModifierSeralizer<?> getSerizalizer() {
        return ModifierSerialization.TEMPORARY;
    }

    public int getTicksLeft() {
        return ticksLeft;
    }
}
