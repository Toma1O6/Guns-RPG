package dev.toma.gunsrpg.common.item.guns.reload;

import dev.toma.gunsrpg.api.common.IAmmoMaterial;
import dev.toma.gunsrpg.api.common.IReloadManager;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoType;
import dev.toma.gunsrpg.util.locate.ammo.ItemLocator;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.LazyOptional;

public final class ReloadManagers {

    private static final IReloadManager FULL = new FullReloadManager();

    public static IReloadManager fullMagLoading() {
        return FULL;
    }

    public static IReloadManager singleBulletLoading(int preparationTicks, PlayerEntity player, GunItem item, ItemStack stack, ResourceLocation bulletLoadPath) {
        AmmoType type = item.getAmmoType();
        IAmmoMaterial material = item.getMaterialFromNBT(stack);
        LazyOptional<IPlayerData> optional = PlayerData.get(player);
        int maxAmmo = optional.isPresent() ? item.getMaxAmmo(optional.orElse(null).getAttributes()) : 0;
        int actAmmo = item.getAmmo(stack);
        int remAmmo = maxAmmo - actAmmo;
        int invAmmo = player.isCreative() ? Integer.MAX_VALUE : ItemLocator.countItems(player.inventory, ItemLocator.typeAndMaterial(type, material));
        int target = Math.min(remAmmo, invAmmo);
        return new StagedReloadManager(preparationTicks, target, bulletLoadPath);
    }

    public static IReloadManager either(boolean constraint, IReloadManager valid, IReloadManager invalid) {
        return constraint ? valid : invalid;
    }

    public static IReloadManager clip(ResourceLocation clipReloadAnimation) {
        return new ClipReloadManager((item, player) -> clipReloadAnimation);
    }

    private ReloadManagers() {}
}
