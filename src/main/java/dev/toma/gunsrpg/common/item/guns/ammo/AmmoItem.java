package dev.toma.gunsrpg.common.item.guns.ammo;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.ModTabs;
import dev.toma.gunsrpg.common.item.BaseItem;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;
import java.util.stream.Collectors;

public class AmmoItem extends BaseItem implements IAmmoProvider {

    public static Map<GunItem, IAmmoProvider[]> GUN_TO_ITEM_MAP = new IdentityHashMap<>();
    private final AmmoType ammoType;
    private final AmmoMaterial material;

    public AmmoItem(String name, AmmoType ammoType, AmmoMaterial material) {
        super(name, new Properties().tab(ModTabs.ITEM_TAB));
        this.material = material;
        this.ammoType = ammoType;
    }

    public static IAmmoProvider getAmmoFor(GunItem item, ItemStack stack) {
        AmmoMaterial material = item.getMaterialFromNBT(stack);
        if (material == null) return null;
        AmmoType type = item.getAmmoType();
        for (IAmmoProvider ia : GUN_TO_ITEM_MAP.get(item)) {
            if (ia.getAmmoType() == type && ia.getMaterial() == material) {
                return ia;
            }
        }
        return null;
    }

    public static void init() {
        GunsRPG.log.debug("Making weapon -> ammo mappings");
        long time = System.currentTimeMillis();
        Collection<Item> items = ForgeRegistries.ITEMS.getValues();
        List<GunItem> weapons = items.stream().filter(it -> it instanceof GunItem).map(it -> (GunItem) it).collect(Collectors.toList());
        List<IAmmoProvider> ammoProviders = items.stream().filter(it -> it instanceof IAmmoProvider).map(it -> (IAmmoProvider) it).collect(Collectors.toList());
        for (GunItem weapon : weapons) {
            AmmoType type = weapon.getAmmoType();
            Set<AmmoMaterial> materials = weapon.getCompatibleMaterials();
            IAmmoProvider[] providers = ammoProviders.stream()
                    .filter(provider -> provider.getAmmoType() == type && materials.contains(provider.getMaterial()))
                    .toArray(IAmmoProvider[]::new);
            GUN_TO_ITEM_MAP.put(weapon, providers);
        }
        long len = System.currentTimeMillis() - time;
        GunsRPG.log.debug("Weapon -> ammo mappings finished, took {}ms", len);
    }

    @Override
    public AmmoType getAmmoType() {
        return ammoType;
    }

    @Override
    public AmmoMaterial getMaterial() {
        return material;
    }
}
