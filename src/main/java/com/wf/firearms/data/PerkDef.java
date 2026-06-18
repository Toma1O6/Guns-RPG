package com.wf.firearms.data;

import com.google.gson.JsonObject;

public final class PerkDef {
    private final String id;
    private final String attribute;
    private final double scaling;
    private final double buffBound;
    private final double debuffBound;
    private final boolean invertCalculation;

    public PerkDef(String id, JsonObject root) {
        this.id = id;
        this.attribute = root.has("attribute") ? root.get("attribute").getAsString() : "";
        this.scaling = root.has("scaling") ? root.get("scaling").getAsDouble() : 0;
        this.invertCalculation = root.has("invertCalculation") && root.get("invertCalculation").getAsBoolean();
        if (root.has("bounds")) {
            JsonObject b = root.getAsJsonObject("bounds");
            this.buffBound = b.has("buff") ? b.get("buff").getAsDouble() : 0;
            this.debuffBound = b.has("debuff") ? b.get("debuff").getAsDouble() : 0;
        } else {
            this.buffBound = 0;
            this.debuffBound = 0;
        }
    }

    public String getId() {
        return id;
    }

    public String getAttribute() {
        return attribute;
    }

    public double getScaling() {
        return scaling;
    }

    public double getBuffBound() {
        return buffBound;
    }

    public double getDebuffBound() {
        return debuffBound;
    }

    public boolean isInvertCalculation() {
        return invertCalculation;
    }
}
