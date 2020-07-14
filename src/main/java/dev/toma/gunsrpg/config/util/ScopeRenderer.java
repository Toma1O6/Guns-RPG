package dev.toma.gunsrpg.config.util;

public enum ScopeRenderer {

    TEXTURE,
    IN_MODEL;

    public boolean isTextureOverlay() {
        return this == TEXTURE;
    }
}
