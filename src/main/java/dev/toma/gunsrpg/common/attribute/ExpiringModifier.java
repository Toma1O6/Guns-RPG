package dev.toma.gunsrpg.common.attribute;

import dev.toma.gunsrpg.api.common.attribute.IAttributeModifier;
import dev.toma.gunsrpg.api.common.attribute.IModifierOp;
import dev.toma.gunsrpg.api.common.attribute.IModifierSerializer;
import dev.toma.gunsrpg.api.common.attribute.ITickableModifier;
import dev.toma.gunsrpg.common.attribute.serialization.ModifierSerialization;

import java.util.UUID;

public class ExpiringModifier implements IAttributeModifier, ITickableModifier {

    private final UUID uuid;
    private final IModifierOp op;
    private final double value;
    private final int initialTime;
    private int time;

    public ExpiringModifier(String uuid, IModifierOp op, double value, int expiresIn) {
        this(UUID.fromString(uuid), op, value, expiresIn);
    }

    public ExpiringModifier(UUID uuid, IModifierOp op, double value, int expiresIn) {
        this.uuid = uuid;
        this.op = op;
        this.value = value;
        this.initialTime = expiresIn;
        this.time = expiresIn;
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

    @Override
    public String toString() {
        return "ExpiringModifier{" +
                "id=" + uuid +
                ", value=" + value +
                ", timeLeft=" + time +
                '}';
    }
}
