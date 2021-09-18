package dev.toma.gunsrpg.common.item.guns.reload;

import dev.toma.gunsrpg.api.common.IAmmoMaterial;
import dev.toma.gunsrpg.api.common.IReloadManager;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoType;
import dev.toma.gunsrpg.util.AmmoLocator;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public final class ReloadManagers {

    private static final IReloadManager FULL = new FullReloadManager();

    public static IReloadManager fullMagLoading() {
        return FULL;
    }

    public static IReloadManager singleBulletLoading(int preparationTicks, PlayerEntity player, GunItem item, ItemStack stack, ResourceLocation bulletLoadPath) {
        AmmoLocator locator = new AmmoLocator();
        AmmoType type = item.getAmmoType();
        IAmmoMaterial material = item.getMaterialFromNBT(stack);
        int maxAmmo = item.getMaxAmmo(player);
        int actAmmo = item.getAmmo(stack);
        int remAmmo = maxAmmo - actAmmo;
        int invAmmo = player.isCreative() ? Integer.MAX_VALUE : locator.count(player.inventory, AmmoLocator.ISearchConstraint.typeAndMaterial(type, material));
        int target = Math.min(remAmmo, invAmmo);
        return new StagedReloadManager(preparationTicks, target, bulletLoadPath);
    }

    public static IReloadManager either(boolean constraint, IReloadManager valid, IReloadManager invalid) {
        return constraint ? valid : invalid;
    }

    private ReloadManagers() {}
}
