package dev.toma.gunsrpg.common;

public interface IShootProps {

    int getFirerate();

    float getInaccuracy();

    default float getDamageMultiplier() {
        return 1.0F;
    }
}
