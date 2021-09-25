package dev.toma.gunsrpg.common.attribute;

import dev.toma.gunsrpg.GunsRPG;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public final class AttributeOps {

    private static final Map<ResourceLocation, IModifierOp> OPERATION_MAP = new HashMap<>();

    public static final IModifierOp SUM = create("sum", Double::sum, 0);
    public static final IModifierOp MUL = create("mul", (v1, v2) -> v1 * v2, 1);

    public static IModifierOp register(IModifierOp op) {
        OPERATION_MAP.put(op.getId(), op);
        return op;
    }

    public static  IModifierOp find(ResourceLocation id) {
        return OPERATION_MAP.get(id);
    }

    private static IModifierOp create(String name, IOperator op, int priority) {
        ResourceLocation id = GunsRPG.makeResource(name);
        return register(new IModifierOp() {
            @Override
            public ResourceLocation getId() {
                return id;
            }

            @Override
            public double combine(double baseValue, double modifierValue) {
                return op.combine(baseValue, modifierValue);
            }

            @Override
            public int getPriority() {
                return priority;
            }
        });
    }

    public interface IOperator {
        double combine(double base, double modValue);
    }

    private AttributeOps() {}
}
