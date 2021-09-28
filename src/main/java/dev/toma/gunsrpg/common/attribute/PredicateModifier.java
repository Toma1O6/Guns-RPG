package dev.toma.gunsrpg.common.attribute;

import net.minecraft.entity.player.PlayerEntity;

import java.util.UUID;
import java.util.function.Predicate;

public class PredicateModifier extends AttributeModifier implements ITickableModifier {

    private final PlayerEntity player;
    private final Predicate<PlayerEntity> condition;
    private boolean wasInvalid;

    PredicateModifier(UUID uuid, IModifierOp op, double value, PlayerEntity player, Predicate<PlayerEntity> condition) {
        super(uuid, op, value);
        this.player = player;
        this.condition = condition;
    }

    PredicateModifier(String uuid, IModifierOp op, double value, PlayerEntity player, Predicate<PlayerEntity> condition) {
        this(UUID.fromString(uuid), op, value, player, condition);
    }

    @Override
    public void tick() {
        if (wasInvalid)
            return;
        if (!condition.test(player)) {
            wasInvalid = true;
        }
    }

    @Override
    public boolean shouldRemove() {
        return wasInvalid;
    }
}
