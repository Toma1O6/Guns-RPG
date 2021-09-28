package dev.toma.gunsrpg.common.item.guns.setup;

import dev.toma.gunsrpg.api.common.IAmmoMaterial;

import java.util.IdentityHashMap;
import java.util.Map;

public class MaterialContainerBuilder {

    private final WeaponBuilder builder;
    final Map<IAmmoMaterial, IMaterialStat> modifierMap = new IdentityHashMap<>();

    public MaterialContainerBuilder(WeaponBuilder builder) {
        this.builder = builder;
    }

    public MaterialContainerBuilder define(IAmmoMaterial material, int value, int levelPredicate) {
        if (modifierMap.put(material, IMaterialStat.of(value, levelPredicate)) != null) {
            throw new IllegalStateException("Duplicate material definition: " + material);
        }
        return this;
    }

    public MaterialContainerBuilder define(IAmmoMaterial material, int value) {
        return define(material, value, material.defaultLevelRequirement());
    }

    public WeaponBuilder build() {
        return builder.materialContainer(new MaterialContainer(this));
    }
}
