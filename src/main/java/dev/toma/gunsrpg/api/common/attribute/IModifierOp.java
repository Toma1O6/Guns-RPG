package dev.toma.gunsrpg.api.common.attribute;

import dev.toma.gunsrpg.common.attribute.OperationTarget;
import net.minecraft.util.ResourceLocation;

public interface IModifierOp {

    ResourceLocation getId();

    OperationTarget getOperationTarget();

    double combine(double baseValue, double modifierValue);

    int getPriority();
}
