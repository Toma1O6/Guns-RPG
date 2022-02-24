package dev.toma.gunsrpg.common.attribute;

import dev.toma.gunsrpg.GunsRPG;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public final class AttributeOps {

    private static final Map<ResourceLocation, IModifierOp> OPERATION_MAP = new HashMap<>();

    public static final IModifierOp SUM = create("sum", OperationTarget.DIRECT_VALUE, Double::sum, 0);
    public static final IModifierOp SUB = create("sub", OperationTarget.DIRECT_VALUE, (v1, v2) -> v1 - v2, 0);
    public static final IModifierOp MUL = create("mul", OperationTarget.MULTIPLIER, (v1, v2) -> v1 * v2, 1);
    public static final IModifierOp MULB = create("mulb", OperationTarget.MULTIPLIER, (v1, v2) -> v1 * (1.0F + v2), 1);

    public static IModifierOp register(IModifierOp op) {
        OPERATION_MAP.put(op.getId(), op);
        return op;
    }

    public static IModifierOp find(ResourceLocation id) {
        return OPERATION_MAP.get(id);
    }

    private static IModifierOp create(String id, OperationTarget target, IModifierOperator operator, int executionOrder) {
        IModifierOp op = new ModifierOperation(GunsRPG.makeResource(id), target, operator, executionOrder);
        register(op);
        return op;
    }

    private AttributeOps() {}
}
