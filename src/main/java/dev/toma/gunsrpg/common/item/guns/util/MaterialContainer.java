package dev.toma.gunsrpg.common.item.guns.util;

import dev.toma.gunsrpg.common.item.guns.ammo.IAmmoMaterial;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

public final class MaterialContainer {

    private final Map<IAmmoMaterial, Modifier> damageModifiers = new IdentityHashMap<>();

    public MaterialContainer() {}

    public MaterialContainer add(IAmmoMaterial material, int value) {
        return add(material, value, material.defaultLevelRequirement());
    }

    public MaterialContainer add(IAmmoMaterial material, int value, int levelRequirement) {
        damageModifiers.put(material, new Modifier(value, levelRequirement));
        return this;
    }

    public int getAdditionalDamage(IAmmoMaterial material) {
        Modifier modifier = damageModifiers.get(material);
        return modifier != null ? modifier.bonus : 0;
    }

    public int getRequiredLevel(IAmmoMaterial material) {
        Modifier modifier = damageModifiers.get(material);
        if (modifier == null) return 0;
        return modifier.levelReq;
    }

    public boolean canUse(IAmmoMaterial material, int weaponLevel) {
        Modifier modifier = damageModifiers.get(material);
        if (modifier == null) return false;
        return modifier.levelReq <= weaponLevel;
    }

    public Set<IAmmoMaterial> getCompatible() {
        return damageModifiers.keySet();
    }

    private static class Modifier {
        private final int bonus;
        private final int levelReq;

        private Modifier(int bonus, int levelReq) {
            this.bonus = bonus;
            this.levelReq = levelReq;
        }
    }
}
