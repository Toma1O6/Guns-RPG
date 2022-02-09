package dev.toma.gunsrpg.common.attribute;

import dev.toma.gunsrpg.common.attribute.serialization.IModifierSerializer;
import dev.toma.gunsrpg.common.attribute.serialization.ModifierSerialization;

import java.util.UUID;

public class ExpiringModifier implements IAttributeModifier, ITickableModifier {

    private final UUID uuid;
    private final IModifierOp op;
    private final double value;
    private final int initialTime;
    private int time;

    public ExpiringModifier(String uuid, IModifierOp op, double value, int expiryTime) {
        this(UUID.fromString(uuid), op, value, expiryTime);
    }

    public ExpiringModifier(UUID uuid, IModifierOp op, double value, int expiryTime) {
        this.uuid = uuid;
        this.op = op;
        this.value = value;
        this.initialTime = expiryTime;
        this.time = expiryTime;
    }

    @Override
    public UUID getUid() {
        return uuid;
    }

    @Override
    public IModifierOp getOperation() {
        return op;
    }

    @Override
    public double getModifierValue() {
        return value;
    }

    @Override
    public IModifierSerializer<?> getSerializer() {
        return ModifierSerialization.EXPIRING;
    }

    @Override
    public IAttributeModifier instance() {
        return new ExpiringModifier(uuid, op, value, initialTime);
    }

    @Override
    public void tick() {
        --time;
    }

    @Override
    public boolean shouldRemove() {
        return time <= 0;
    }

    public int getTimeLeft() {
        return time;
    }

    public int getInitialTime() {
        return initialTime;
    }

    public void setTimeLeft(int time) {
        this.time = time;
    }
}
