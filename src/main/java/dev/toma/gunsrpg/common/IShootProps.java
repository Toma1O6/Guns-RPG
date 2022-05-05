package dev.toma.gunsrpg.common;

import dev.toma.gunsrpg.util.properties.PropertyContext;

public interface IShootProps {

    float getInaccuracy();

    default float getDamageMultiplier() {
        return 1.0F;
    }

    default PropertyContext getExtraData() {
        return PropertyContext.empty();
    }
}
