package dev.toma.gunsrpg.resource.util.functions;

import java.util.HashMap;
import java.util.Map;

public final class Functions {

    private static final Functions REGISTRY = new Functions();
    private final Map<String, IFunctionAdapter<?>> functionAdapterMap = new HashMap<>();

    public static Functions getRegistry() {
        return REGISTRY;
    }

    public void register(String function, IFunctionAdapter<?> adapter) {
        functionAdapterMap.put(function, adapter);
    }

    @SuppressWarnings("unchecked")
    public <F extends IFunction> IFunctionAdapter<F> getAdapter(String function) {
        return (IFunctionAdapter<F>) functionAdapterMap.get(function);
    }

    private Functions() {
        register("false", new ConstantFunction.Adapter(false));
        register("true", new ConstantFunction.Adapter(true));
        register("each", new ComparatorFunction.Adapter(ComparatorFunction.EACH));
        register("equal", new ComparatorFunction.Adapter(ComparatorFunction.EQUAL));
        register("smaller", new ComparatorFunction.Adapter(ComparatorFunction.SMALLER));
        register("smallerOrEqual", new ComparatorFunction.Adapter(ComparatorFunction.SMALLER_EQUAL));
        register("bigger", new ComparatorFunction.Adapter(ComparatorFunction.BIGGER));
        register("biggerOrEqual", new ComparatorFunction.Adapter(ComparatorFunction.BIGGER_EQUAL));
        register("betweenInclusive", new RangedFunction.Adapter(RangedFunction.BETWEEN_INCLUSIVE));
        register("betweenExclusive", new RangedFunction.Adapter(RangedFunction.BETWEEN_EXCLUSIVE));
        register("oneOf", new ListFunction.Adapter());
    }
}
