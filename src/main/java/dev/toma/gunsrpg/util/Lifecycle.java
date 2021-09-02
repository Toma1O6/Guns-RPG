package dev.toma.gunsrpg.util;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.init.ModItems;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoType;
import dev.toma.gunsrpg.api.common.IAmmoMaterial;
import dev.toma.gunsrpg.api.common.IAmmoProvider;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public final class Lifecycle {

    private static final Marker MARKER = MarkerManager.getMarker("Lifecycle");

    private final Map<GunItem, IAmmoProvider[]> weaponProviderMap = new IdentityHashMap<>();
    private final Map<Item, Item> ore2ChunkMap = new IdentityHashMap<>(2);

    public void commonInit() {
        initWeaponProviderMap();
        initOreToChunkMap();
    }

    @Nullable
    public Item getOreDropReplacement(Item src) {
        return ore2ChunkMap.get(src);
    }

    @Nullable
    public IAmmoProvider getAmmoForWeapon(GunItem item, ItemStack stack) {
        IAmmoMaterial material = item.getMaterialFromNBT(stack);
        if (material == null) return null;
        AmmoType ammoType = item.getAmmoType();
        AmmoLocator.ISearchConstraint constraint = AmmoLocator.ISearchConstraint.typeAndMaterial(ammoType, material);
        for (IAmmoProvider provider : getAllCompatibleAmmoProviders(item)) {
            if (constraint.isValidItem(provider)) {
                return provider;
            }
        }
        return null;
    }

    public IAmmoProvider[] getAllCompatibleAmmoProviders(GunItem gunItem) {
        return weaponProviderMap.get(gunItem);
    }

    private void initWeaponProviderMap() {
        GunsRPG.log.debug(MARKER, "Making weapon -> ammo mappings");
        long startTime = System.currentTimeMillis();
        Collection<Item> items = ForgeRegistries.ITEMS.getValues();
        List<GunItem> weapons = items.stream().filter(it -> it instanceof GunItem).map(it -> (GunItem) it).collect(Collectors.toList());
        List<IAmmoProvider> ammoProviders = items.stream().filter(it -> it instanceof IAmmoProvider).map(it -> (IAmmoProvider) it).collect(Collectors.toList());
        for (GunItem weapon : weapons) {
            AmmoType type = weapon.getAmmoType();
            Set<IAmmoMaterial> materials = weapon.getCompatibleMaterials();
            IAmmoProvider[] providers = ammoProviders.stream()
                    .filter(provider -> provider.getAmmoType() == type && materials.contains(provider.getMaterial()))
                    .toArray(IAmmoProvider[]::new);
            weaponProviderMap.put(weapon, providers);
        }
        long len = System.currentTimeMillis() - startTime;
        GunsRPG.log.debug(MARKER, "Weapon -> ammo mappings finished, took {}ms", len);
    }

    private void initOreToChunkMap() {
        ore2ChunkMap.put(Blocks.IRON_ORE.asItem(), ModItems.IRON_ORE_CHUNK);
        ore2ChunkMap.put(Blocks.GOLD_ORE.asItem(), ModItems.GOLD_ORE_CHUNK);
    }
}
