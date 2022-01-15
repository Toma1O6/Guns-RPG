package dev.toma.gunsrpg.resource.gunner;

public class GunnerLoadouts {

    private final GunnerGlobalProperties properties;
    private final GunnerLoadoutInstance[] instances;

    public GunnerLoadouts(GunnerGlobalProperties properties, GunnerLoadoutInstance[] instances) {
        this.properties = properties;
        this.instances = instances;
    }

    public GunnerGlobalProperties getGlobalProperties() {
        return properties;
    }

    public GunnerLoadoutInstance[] getLoadouts() {
        return instances;
    }
}
