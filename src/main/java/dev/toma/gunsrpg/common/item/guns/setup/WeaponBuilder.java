package dev.toma.gunsrpg.common.item.guns.setup;

import dev.toma.gunsrpg.api.common.IWeaponConfig;
import dev.toma.gunsrpg.common.PlayerShootProperties;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoType;
import dev.toma.gunsrpg.common.item.guns.util.Firemode;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Objects;
import java.util.function.BiFunction;

public final class WeaponBuilder {

    private WeaponCategory category;
    private IWeaponConfig config;
    private MaterialContainer container;
    private AmmoType ammoType;
    private BiFunction<PlayerEntity, Firemode, Firemode> firemodeSelector = (player, firemode) -> firemode;

    /* SET methods */

    public WeaponBuilder category(WeaponCategory category) {
        this.category = category;
        return this;
    }

    public WeaponBuilder caliber(AmmoType type) {
        this.ammoType = type;
        return this;
    }

    public WeaponBuilder config(IWeaponConfig config) {
        this.config = config;
        return this;
    }

    public WeaponBuilder firemodeSelector(BiFunction<PlayerEntity, Firemode, Firemode> firemodeSelector) {
        this.firemodeSelector = Objects.requireNonNull(firemodeSelector);
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

    public AmmoType getAmmoType() {
        return ammoType;
    }

    public BiFunction<PlayerEntity, Firemode, Firemode> getFiremodeSelector() {
        return firemodeSelector;
    }

    /* Validation */

    public void validate() {
        Objects.requireNonNull(category, "Weapon category is undefined!");
        if (ammoType == null)
            ammoType = category.getDefaultAmmoType();
    }
}
