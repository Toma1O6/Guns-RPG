package dev.toma.gunsrpg.common.item.guns.ammo;

import dev.toma.gunsrpg.api.common.IAmmoMaterial;

public interface IMaterialDataContainer {

    void register(IAmmoMaterial material, IMaterialData data);

    IMaterialData getMaterialData(IAmmoMaterial material);
}
