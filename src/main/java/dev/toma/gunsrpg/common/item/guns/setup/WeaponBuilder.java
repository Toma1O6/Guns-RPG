package dev.toma.gunsrpg.common.item.guns.setup;

import dev.toma.gunsrpg.api.common.IWeaponConfig;
import dev.toma.gunsrpg.common.item.guns.ammo.IMaterialDataContainer;

public final class WeaponBuilder {

    private WeaponCategory category;
    private IWeaponConfig config;
    private MaterialContainer container;

    /* SET methods */

    public WeaponBuilder category(WeaponCategory category) {
        this.category = category;
        return this;
    }

    public WeaponBuilder config(IWeaponConfig config) {
        this.config = config;
        return this;
    }

    public MaterialContainerBuilder ammo() {
        return new MaterialContainerBuilder(this);
    }

    public MaterialContainerBuilder ammo(WeaponCategory category) {
        this.category(category);
        return ammo();
    }

    /* INTERNAL SET methods */

    WeaponBuilder materialContainer(MaterialContainer container) {
        this.container = container;
        return this;
    }

    /* GET methods */

    public WeaponCategory getWeaponCategory() {
        return category;
    }

    public IWeaponConfig getConfig() {
        return config;
    }

    public MaterialContainer getMaterialContainer() {
        return container;
    }

    /* Validation */

    public void validate() {

    }
}
