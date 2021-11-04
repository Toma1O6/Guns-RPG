package dev.toma.gunsrpg.common.item.guns.setup;

import dev.toma.gunsrpg.api.common.IAmmoMaterial;

import java.util.Map;
import java.util.Set;

public final class MaterialContainer {

    private final Map<IAmmoMaterial, IMaterialStat> stats;

    public MaterialContainer(MaterialContainerBuilder builder) {
        this.stats = builder.modifierMap;
    }

    public IMaterialStat getStat(IAmmoMaterial material) {
        return stats.get(material);
    }

    public int getAdditionalDamage(IAmmoMaterial material) {
        IMaterialStat stat = getStat(material);
        return stat != null ? stat.value() : 0;
    }

    public int getRequiredLevel(IAmmoMaterial material) {
        IMaterialStat stat = getStat(material);
        if (stat == null) return 0;
        return stat.requiredLevel();
    }

    public Set<IAmmoMaterial> getCompatible() {
        return stats.keySet();
    }
}
