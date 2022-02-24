package dev.toma.gunsrpg.common.attribute;

import net.minecraft.util.ResourceLocation;

public class ModifierOperation implements IModifierOp {

    private final ResourceLocation id;
    private final OperationTarget operationTarget;
    private final IModifierOperator operator;
    private final int executionOrder;

    public ModifierOperation(ResourceLocation id, OperationTarget operationTarget, IModifierOperator operator, int executionOrder) {
        this.id = id;
        this.operationTarget = operationTarget;
        this.operator = operator;
        this.executionOrder = executionOrder;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public OperationTarget getOperationTarget() {
        return operationTarget;
    }

    @Override
    public double combine(double baseValue, double modifierValue) {
        return operator.combine(baseValue, modifierValue);
    }

    @Override
    public int getPriority() {
        return executionOrder;
    }
}
