package dev.toma.gunsrpg.common.item.perk;

import java.util.Collection;

public final class Crystal {

    private final Collection<CrystalAttribute> attributes;

    public Crystal(Collection<CrystalAttribute> attributes) {
        this.attributes = attributes;
    }

    public Collection<CrystalAttribute> listAttributes() {
        return attributes;
    }
}
