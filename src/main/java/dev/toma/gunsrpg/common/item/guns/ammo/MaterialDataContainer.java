package dev.toma.gunsrpg.common.item.guns.ammo;

import dev.toma.gunsrpg.api.common.IAmmoMaterial;
import dev.toma.gunsrpg.util.ModUtils;

import java.util.HashMap;
import java.util.Map;

public class MaterialDataContainer implements IMaterialDataContainer {

    private final Map<IAmmoMaterial, IMaterialData> dataMap = new HashMap<>();

    @Override
    public void register(IAmmoMaterial material, IMaterialData data) {
        dataMap.put(material, data);
    }

    @Override
    public IMaterialData getMaterialData(IAmmoMaterial material) {
        return ModUtils.getNonnullFromMap(dataMap, material, MaterialData.EMPTY);
    }
}
