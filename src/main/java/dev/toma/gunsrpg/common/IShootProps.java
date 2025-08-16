package dev.toma.gunsrpg.common;

import dev.toma.gunsrpg.util.properties.PropertyContext;
import net.minecraft.util.math.vector.Vector2f;

public interface IShootProps {

    Vector2f weaponAngle();

    float getInaccuracy();

    default float getDamageMultiplier() {
        return 1.0F;
    }

    default PropertyContext getExtraData() {
        return PropertyContext.empty();
    }
}
