package dev.toma.gunsrpg.common.item.guns.ammo;

import dev.toma.gunsrpg.api.common.IAmmoMaterial;
import dev.toma.gunsrpg.common.entity.projectile.*;

public final class AmmoMaterials {

    // Default weapon materials
    public static final IAmmoMaterial WOOD = AmmoMaterialManager.get().createMaterial("wood", 0xB54800, 1);
    public static final IAmmoMaterial STONE = AmmoMaterialManager.get().createMaterial("stone", 0x565656, 2);
    public static final IAmmoMaterial IRON = AmmoMaterialManager.get().createMaterial("iron", 0xAFAFAF, 3);
    public static final IAmmoMaterial LAPIS = AmmoMaterialManager.get().createMaterial("lapis", 0x3700FF, 3, 0xDD);
    public static final IAmmoMaterial GOLD = AmmoMaterialManager.get().createMaterial("gold", 0xF2C100, 4);
    public static final IAmmoMaterial REDSTONE = AmmoMaterialManager.get().createMaterial("redstone", 0xB70300, 4, 0xFF << 16);
    public static final IAmmoMaterial EMERALD = AmmoMaterialManager.get().createMaterial("emerald", 0x41C409, 5, 0xCC << 8);
    public static final IAmmoMaterial QUARTZ = AmmoMaterialManager.get().createMaterial("quartz", 0xCCCCCC, 5);
    public static final IAmmoMaterial DIAMOND = AmmoMaterialManager.get().createMaterial("diamond", 0x4CC4EF, 6);
    public static final IAmmoMaterial AMETHYST = AmmoMaterialManager.get().createMaterial("amethyst", 0xC100BB, 7);
    public static final IAmmoMaterial NETHERITE = AmmoMaterialManager.get().createMaterial("netherite", 0x8F6365, 8);

    // grenade launcher
    public static final IAmmoMaterial GRENADE = AmmoMaterialManager.get().createReactiveMaterial("grenade", 0xFFFF00, 1, ExplosiveReaction.EXPLOSION);
    public static final IAmmoMaterial TEAR_GAS = AmmoMaterialManager.get().createReactiveMaterial("tear_gas", 0x74E074, 2, EffectSpreadReaction.TEAR_GAS);
    public static final IAmmoMaterial STICKY = AmmoMaterialManager.get().createReactiveMaterial("sticky", 0xFFFFFF, 4, MultipartReaction.multi(ExplosiveReaction.EXPLOSION, PropertyTriggerReaction.STICKY));
    public static final IAmmoMaterial HE_GRENADE = AmmoMaterialManager.get().createReactiveMaterial("he_grenade", 0xAFAFAF, 6, ExplosiveReaction.HE_EXPLOSION);
    public static final IAmmoMaterial IMPACT = AmmoMaterialManager.get().createReactiveMaterial("impact_grenade", 0xFF0000, 7, MultipartReaction.multi(ExplosiveReaction.EXPLOSION, PropertyTriggerReaction.IMPACT));

    // rocket launcher
    public static final IAmmoMaterial ROCKET = AmmoMaterialManager.get().createReactiveMaterial("rocket", 0xFF0000, 1, ExplosiveReaction.EXPLOSION);
    public static final IAmmoMaterial TOXIN = AmmoMaterialManager.get().createReactiveMaterial("toxin", 0x8400C1, 2, MultipartReaction.multi(ExplosiveReaction.EXPLOSION, EffectSpreadReaction.TOXIN));
    public static final IAmmoMaterial DEMOLITION = AmmoMaterialManager.get().createReactiveMaterial("demolition", 0x565656, 4, ExplosiveReaction.DESTRUCTIVE_EXPLOSION);
    public static final IAmmoMaterial NAPALM = AmmoMaterialManager.get().createReactiveMaterial("napalm", 0xFF6A00, 6, MultipartReaction.multi(ExplosiveReaction.EXPLOSION, NapalmReaction.NAPALM));
    public static final IAmmoMaterial HE_ROCKET = AmmoMaterialManager.get().createReactiveMaterial("he_rocket", 0x298FAD, 7, ExplosiveReaction.HE_EXPLOSION);

    private AmmoMaterials() {}
}
