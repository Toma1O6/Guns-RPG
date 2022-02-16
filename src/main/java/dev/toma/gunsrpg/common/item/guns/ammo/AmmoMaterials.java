package dev.toma.gunsrpg.common.item.guns.ammo;

import dev.toma.gunsrpg.api.common.IAmmoMaterial;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public final class AmmoMaterials {

    // Default weapon materials
    public static final IAmmoMaterial WOOD = AmmoMaterialManager.get().createMaterial("wood", 0xB54800, 0);
    public static final IAmmoMaterial STONE = AmmoMaterialManager.get().createMaterial("stone", 0x565656, 1);
    public static final IAmmoMaterial IRON = AmmoMaterialManager.get().createMaterial("iron", 0xAFAFAF, 2);
    public static final IAmmoMaterial LAPIS = AmmoMaterialManager.get().createMaterial("lapis", 0x3700FF, 2);
    public static final IAmmoMaterial GOLD = AmmoMaterialManager.get().createMaterial("gold", 0xF2C100, 3);
    public static final IAmmoMaterial REDSTONE = AmmoMaterialManager.get().createMaterial("redstone", 0xB70300, 3);
    public static final IAmmoMaterial EMERALD = AmmoMaterialManager.get().createMaterial("emerald", 0x41C409, 4);
    public static final IAmmoMaterial QUARTZ = AmmoMaterialManager.get().createMaterial("quartz", 0xCCCCCC, 4);
    public static final IAmmoMaterial DIAMOND = AmmoMaterialManager.get().createMaterial("diamond", 0x4CC4EF, 5);
    public static final IAmmoMaterial AMETHYST = AmmoMaterialManager.get().createMaterial("amethyst", 0xC100BB, 6);
    public static final IAmmoMaterial NETHERITE = AmmoMaterialManager.get().createMaterial("netherite", 0x8F6365, 7);

    // grenade launcher
    public static final IAmmoMaterial GRENADE = AmmoMaterialManager.get().createMaterial("grenade", 0xFFFF00, 0);
    public static final IAmmoMaterial HE_GRENADE = AmmoMaterialManager.get().createMaterial("he_grenade", 0xAFAFAF, 5);
    public static final IAmmoMaterial IMPACT = AmmoMaterialManager.get().createMaterial("impact_grenade", 0xFF0000, 6,
            (projectile, world, impactPos, impactedOn) -> explode(world, impactPos, 4.0F));
    public static final IAmmoMaterial STICKY = AmmoMaterialManager.get().createMaterial("sticky", 0xFFFFFF, 3, AmmoMaterials::stick);
    public static final IAmmoMaterial TEAR_GAS = AmmoMaterialManager.get().createMaterial("tear_gas", 0x74E074, 1,
            (projectile, world, impactPos, impactedOn) -> effect(world, impactPos));

    // rocket launcher
    public static final IAmmoMaterial ROCKET = AmmoMaterialManager.get().createMaterial("rocket", 0xFF0000, 0,
            (projectile, world, impactPos, impactedOn) -> explode(world, impactPos, 6.0F));
    public static final IAmmoMaterial HE_ROCKET = AmmoMaterialManager.get().createMaterial("he_rocket", 0x298FAD, 6,
            (projectile, world, impactPos, impactedOn) -> explode(world, impactPos, 8.0F));
    public static final IAmmoMaterial DEMOLITION = AmmoMaterialManager.get().createMaterial("demolition", 0x565656, 3,
            (projectile, world, impactPos, impactedOn) -> explode(world, impactPos, 8.0F, Explosion.Mode.DESTROY));
    public static final IAmmoMaterial NAPALM = AmmoMaterialManager.get().createMaterial("napalm", 0xFF6A00, 5,
            (projectile, world, impactPos, impactedOn) -> effect(world, impactPos));
    public static final IAmmoMaterial TOXIN = AmmoMaterialManager.get().createMaterial("toxin", 0x8400C1, 1,
            (projectile, world, impactPos, impactedOn) -> effect(world, impactPos));


    private AmmoMaterials() {}

    private static void explode(World world, Vector3d impact, float power, Explosion.Mode mode) {
        if (!world.isClientSide) {
            world.explode(null, impact.x, impact.y + 1, impact.z, power, mode);
        }
    }

    private static void explode(World world, Vector3d impact, float power) {
        explode(world, impact, power, Explosion.Mode.NONE);
    }

    private static void stick(ProjectileEntity projectile, World world, Vector3d impact, Entity victim) {
        // TODO projectile.stick(impact);
    }

    private static void effect(World world, Vector3d impact) {
        // TODO spawn effect
    }
}
