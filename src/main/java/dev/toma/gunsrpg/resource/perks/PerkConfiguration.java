package dev.toma.gunsrpg.resource.perks;

public final class PerkConfiguration {

    private final CrystalConfiguration crystals;
    private final FusionConfiguration fusion;
    private final PurificationConfiguration purification;

    public PerkConfiguration(CrystalConfiguration crystals, FusionConfiguration fusion, PurificationConfiguration purification) {
        this.crystals = crystals;
        this.fusion = fusion;
        this.purification = purification;
    }

    public CrystalConfiguration getCrystalConfig() {
        return crystals;
    }

    public FusionConfiguration getFusionConfig() {
        return fusion;
    }

    public PurificationConfiguration getPurificationConfig() {
        return purification;
    }
}
