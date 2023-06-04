package dev.toma.gunsrpg.config.client;

public enum MouseInputType {

    HOLD,
    TOGGLE;

    public boolean isHold() {
        return this == HOLD;
    }
}
