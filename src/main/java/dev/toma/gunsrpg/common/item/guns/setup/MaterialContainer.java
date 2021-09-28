package dev.toma.gunsrpg.common.item.guns.setup;

import dev.toma.gunsrpg.api.common.IAmmoMaterial;

import java.util.Map;
import java.util.Set;

public final class MaterialContainer {

    private final Map<IAmmoMaterial, IMaterialStat> damageModifiers;

    public MaterialContainer(MaterialContainerBuilder builder) {
        this.damageModifiers = builder.modifierMap;
    }

    public int getAdditionalDamage(IAmmoMaterial material) {
        IMaterialStat stat = damageModifiers.get(material);
        return stat != null ? stat.value() : 0;
    }

    public int getRequiredLevel(IAmmoMaterial material) {
        IMaterialStat stat = damageModifiers.get(material);
        if (stat == null) return 0;
        return stat.requiredLevel();
    }

    public boolean canUse(IAmmoMaterial material, int weaponLevel) {
        IMaterialStat stat = damageModifiers.get(material);
        if (stat == null) return false;
        return stat.requiredLevel() <= weaponLevel;
    }

    public Set<IAmmoMaterial> getCompatible() {
        return damageModifiers.keySet();
    }
}
