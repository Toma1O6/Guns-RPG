package dev.toma.gunsrpg.config.util;

import dev.toma.configuration.api.INameable;

public enum ScopeRenderer implements INameable {

    TEXTURE,
    IN_MODEL;

    @Override
    public String getUnformattedName() {
        return name();
    }

    @Override
    public String getFormattedName() {
        return name().toLowerCase().replaceAll("_", "");
    }
}
