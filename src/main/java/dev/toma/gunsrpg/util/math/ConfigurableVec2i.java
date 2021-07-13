package dev.toma.gunsrpg.util.math;

import dev.toma.configuration.api.IConfigWriter;
import dev.toma.configuration.api.IObjectSpec;
import dev.toma.configuration.api.type.IntType;
import dev.toma.configuration.api.type.ObjectType;

public class ConfigurableVec2i extends ObjectType {

    private final IntType x;
    private final IntType y;

    public ConfigurableVec2i(IObjectSpec spec, int _x, int _y) {
        super(spec);
        IConfigWriter writer = spec.getWriter();
        x = writer.writeInt("x", _x, "X coordinate of two dimensional vector");
        y = writer.writeInt("y", _y, "Y coordinate of two dimensional vector");
    }

    public int getX() {
        return x.get();
    }

    public int getY() {
        return y.get();
    }
}
