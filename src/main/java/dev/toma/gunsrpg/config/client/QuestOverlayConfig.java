package dev.toma.gunsrpg.config.client;

import dev.toma.configuration.api.IConfigWriter;
import dev.toma.configuration.api.IObjectSpec;
import dev.toma.configuration.api.type.BooleanType;
import dev.toma.configuration.api.type.IntType;
import dev.toma.configuration.api.type.ObjectType;

public final class QuestOverlayConfig extends ObjectType {

    private final BooleanType rightAligned;
    private final IntType heightOffset;

    public QuestOverlayConfig(IObjectSpec spec) {
        super(spec);

        IConfigWriter writer = spec.getWriter();
        rightAligned = writer.writeBoolean("Right alignment", true, "Decides whether the overlay is going to be aligned to the right or not");
        heightOffset = writer.writeBoundedInt("Height offset", 60, 0, Short.MAX_VALUE, "Height offset of quest overlay");
    }

    public boolean isRightAligned() {
        return rightAligned.get();
    }

    public int getOffsetY() {
        return heightOffset.get();
    }
}
