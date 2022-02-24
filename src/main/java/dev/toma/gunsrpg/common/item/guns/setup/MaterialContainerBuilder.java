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

    /**
     * Adds new material stats into internal ammo mappings.
     * @param material The ammo material
     * @param value Added damage value
     * @param levelPredicate Required level for use
     * @return Current builder instance
     */
    public MaterialContainerBuilder define(IAmmoMaterial material, int value, int levelPredicate) {
        if (modifierMap.put(material, IMaterialStat.of(value, levelPredicate)) != null) {
            throw new IllegalStateException("Duplicate material definition: " + material);
        }
        return this;
    }

    /**
     * Adds new material stats into internal ammo mappings.
     * @param material The ammo material
     * @param value Added damage value
     * @return Current builder instance
     */
    public MaterialContainerBuilder define(IAmmoMaterial material, int value) {
        return define(material, value, material.defaultLevelRequirement());
    }

    public MaterialContainerBuilder define(IAmmoMaterial material) {
        return define(material, 0);
    }

    public WeaponBuilder build() {
        return builder.materialContainer(new MaterialContainer(this));
    }
}
