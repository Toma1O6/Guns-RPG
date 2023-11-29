package dev.toma.gunsrpg.common.attribute;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.attribute.IModifierOp;
import dev.toma.gunsrpg.api.common.attribute.IModifierOperator;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public final class AttributeOps {

    private static final Map<ResourceLocation, IModifierOp> OPERATION_MAP = new HashMap<>();

    public static final IModifierOp SUM = create("sum", OperationTarget.BEFORE_MULTIPLY, Double::sum);
    public static final IModifierOp SUB = create("sub", OperationTarget.BEFORE_MULTIPLY, (v1, v2) -> v1 - v2);
    public static final IModifierOp MUL = create("mul", OperationTarget.MULTIPLIER, (v1, v2) -> v1 * v2);
    public static final IModifierOp MULB = create("mulb", OperationTarget.MULTIPLIER, (v1, v2) -> v1 * (1.0F + v2));
    public static final IModifierOp SET = create("set", OperationTarget.BEFORE_MULTIPLY, (v1, v2) -> v2);

    public static IModifierOp register(IModifierOp op) {
        OPERATION_MAP.put(op.getId(), op);
        return op;
    }

    public static IModifierOp find(ResourceLocation id) {
        return OPERATION_MAP.get(id);
    }

    private static IModifierOp create(String id, OperationTarget target, IModifierOperator operator) {
        return create(id, target, operator, 0);
    }

    private static IModifierOp create(String id, OperationTarget target, IModifierOperator operator, int executionOrder) {
        IModifierOp op = new ModifierOperation(GunsRPG.makeResource(id), target, operator, executionOrder);
        register(op);
        return op;
    }

    private AttributeOps() {}
}
