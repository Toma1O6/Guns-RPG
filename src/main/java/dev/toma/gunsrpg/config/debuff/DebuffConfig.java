package dev.toma.gunsrpg.config.debuff;

import dev.toma.configuration.api.IConfigWriter;
import dev.toma.configuration.api.IObjectSpec;
import dev.toma.configuration.api.type.BooleanType;
import dev.toma.configuration.api.type.ObjectType;

public class DebuffConfig extends ObjectType {

    private final BooleanType disablePoison;
    private final BooleanType disableInfection;
    private final BooleanType disableFractures;
    private final BooleanType disableBleeding;
    private final BooleanType disableRespawnDebuff;

    public DebuffConfig(IObjectSpec spec) {
        super(spec);
        IConfigWriter writer = spec.getWriter();
        disablePoison = writer.writeBoolean("Disable poisoning", false);
        disableInfection = writer.writeBoolean("Disable infections", false);
        disableFractures = writer.writeBoolean("Disable fractures", false);
        disableBleeding = writer.writeBoolean("Disable bleeds", false);
        disableRespawnDebuff = writer.writeBoolean("Disable respawn health debuff", false);
    }

    public boolean disablePoison() {
        return disablePoison.get();
    }

    public boolean disableInfection() {
        return disableInfection.get();
    }

    public boolean disableFractures() {
        return disableFractures.get();
    }

    public boolean disableBleeding() {
        return disableBleeding.get();
    }

    public boolean disableRespawnDebuff() {
        return disableRespawnDebuff.get();
    }
}
