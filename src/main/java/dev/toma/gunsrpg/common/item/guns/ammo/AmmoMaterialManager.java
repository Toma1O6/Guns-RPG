package dev.toma.gunsrpg.common.item.guns.ammo;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.IAmmoMaterial;
import dev.toma.gunsrpg.common.entity.projectile.IReaction;
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

    public IAmmoMaterial createMaterial(ResourceLocation materialID, int textColor, int levelReq, Integer tracer) {
        return createMaterial(new SimpleMaterial(materialID, textColor, levelReq, tracer));
    }

    public IAmmoMaterial createMaterial(String name, int textColor, int levelReq, Integer tracer) {
        return createMaterial(GunsRPG.makeResource(name), textColor, levelReq, tracer);
    }

    public IAmmoMaterial createMaterial(ResourceLocation materialID, int textColor, int levelReq) {
        return createMaterial(materialID, textColor, levelReq, null);
    }

    public IAmmoMaterial createMaterial(String name, int textColor, int levelReq) {
        return createMaterial(name, textColor, levelReq, null);
    }

    public IAmmoMaterial createReactiveMaterial(ResourceLocation materialID, int textColor, int levelReq, IReaction reaction) {
        return createMaterial(new ReactiveMaterial(materialID, textColor, levelReq, reaction));
    }

    public IAmmoMaterial createReactiveMaterial(String name, int textColor, int levelReq, IReaction reaction) {
        return createReactiveMaterial(GunsRPG.makeResource(name), textColor, levelReq, reaction);
    }

    public IAmmoMaterial createMaterial(IAmmoMaterial material) {
        materialMap.put(material.getMaterialID(), material);
        return material;
    }


}
