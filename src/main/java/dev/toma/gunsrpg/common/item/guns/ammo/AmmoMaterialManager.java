package dev.toma.gunsrpg.common.item.guns.ammo;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.IAmmoMaterial;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public final class AmmoMaterialManager {

    private static final AmmoMaterialManager INSTANCE = new AmmoMaterialManager();
    private final Map<ResourceLocation, IAmmoMaterial> materialMap = new HashMap<>();

    public static AmmoMaterialManager get() {
        return INSTANCE;
    }

    public IAmmoMaterial parse(CompoundNBT nbt) {
        String stringMaterialID = nbt.getString("material");
        if (stringMaterialID.isEmpty())
            return null;
        ResourceLocation materialID = new ResourceLocation(stringMaterialID);
        return findMaterial(materialID);
    }

    public IAmmoMaterial findMaterial(ResourceLocation location) {
        return materialMap.get(location);
    }

    public IAmmoMaterial createMaterial(ResourceLocation materialID, int textColor, int levelReq) {
        return createMaterial(new SimpleMaterial(materialID, textColor, levelReq));
    }

    public IAmmoMaterial createMaterial(String name, int textColor, int levelReq) {
        return createMaterial(GunsRPG.makeResource(name), textColor, levelReq);
    }

    public IAmmoMaterial createMaterial(ResourceLocation materialID, int textColor, int levelReq, ReactiveMaterial.IImpactCallback callback) {
        return createMaterial(new ReactiveMaterial(materialID, textColor, levelReq, callback));
    }

    public IAmmoMaterial createMaterial(String name, int textColor, int levelReq, ReactiveMaterial.IImpactCallback callback) {
        return createMaterial(GunsRPG.makeResource(name), textColor, levelReq, callback);
    }

    public IAmmoMaterial createMaterial(IAmmoMaterial material) {
        materialMap.put(material.getMaterialID(), material);
        return material;
    }


}
