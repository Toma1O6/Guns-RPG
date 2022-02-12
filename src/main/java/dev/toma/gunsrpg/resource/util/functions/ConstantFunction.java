package dev.toma.gunsrpg.resource.util.functions;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class ConstantFunction implements IFunction {

    private static final ConstantFunction FALSE = new ConstantFunction(false);
    private static final ConstantFunction TRUE = new ConstantFunction(true);
    private final boolean constant;

    public ConstantFunction(boolean constant) {
        this.constant = constant;
    }

    public static IFunction constant(boolean constant) {
        return pickConstant(constant);
    }

    @Override
    public boolean canApplyFor(int input) {
        return constant;
    }

    private static ConstantFunction pickConstant(boolean constant) {
        return constant ? TRUE : FALSE;
    }

    static final class Adapter implements IFunctionAdapter<ConstantFunction> {

        private final boolean constant;

        Adapter(boolean constant) {
            this.constant = constant;
        }

        @Override
        public ConstantFunction resolveFromJson(JsonObject object) throws JsonParseException {
            return pickConstant(constant);
        }
    }
}
