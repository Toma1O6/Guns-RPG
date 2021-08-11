package dev.toma.gunsrpg.common.item.guns.ammo;

import dev.toma.gunsrpg.ModTabs;
import dev.toma.gunsrpg.common.item.BaseItem;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static dev.toma.gunsrpg.common.init.ModItems.*;

public class AmmoItem extends BaseItem implements IAmmoProvider {

    public static Map<GunItem, AmmoItem[]> GUN_TO_ITEM_MAP = new HashMap<>();
    private final AmmoType ammoType;
    private final AmmoMaterial material;

    public AmmoItem(String name, AmmoType ammoType, AmmoMaterial material) {
        super(name, new Properties().tab(ModTabs.ITEM_TAB));
        this.material = material;
        this.ammoType = ammoType;
    }

    public static AmmoItem getAmmoFor(GunItem item, ItemStack stack) {
        AmmoMaterial material = item.getMaterialFromNBT(stack);
        if (material == null) return null;
        AmmoType type = item.getAmmoType();
        for (AmmoItem ia : GUN_TO_ITEM_MAP.get(item)) {
            if (ia.getAmmoType() == type && ia.getMaterial() == material) {
                return ia;
            }
        }
        return null;
    }

    public static void init() {
        List<AmmoItem> pistol = Arrays.asList(WOODEN_AMMO_9MM, STONE_AMMO_9MM, IRON_AMMO_9MM, GOLD_AMMO_9MM, DIAMOND_AMMO_9MM, EMERALD_AMMO_9MM, AMETHYST_AMMO_9MM);
        List<AmmoItem> smg = Arrays.asList(WOODEN_AMMO_45ACP, STONE_AMMO_45ACP, IRON_AMMO_45ACP, GOLD_AMMO_45ACP, DIAMOND_AMMO_45ACP, EMERALD_AMMO_45ACP, AMETHYST_AMMO_45ACP);
        List<AmmoItem> ar = Arrays.asList(WOODEN_AMMO_556MM, STONE_AMMO_556MM, IRON_AMMO_556MM, GOLD_AMMO_556MM, DIAMOND_AMMO_556MM, EMERALD_AMMO_556MM, AMETHYST_AMMO_556MM);
        List<AmmoItem> sr = Arrays.asList(WOODEN_AMMO_762MM, STONE_AMMO_762MM, IRON_AMMO_762MM, GOLD_AMMO_762MM, DIAMOND_AMMO_762MM, EMERALD_AMMO_762MM, AMETHYST_AMMO_762MM);
        List<AmmoItem> sg = Arrays.asList(WOODEN_AMMO_12G, STONE_AMMO_12G, IRON_AMMO_12G, GOLD_AMMO_12G, DIAMOND_AMMO_12G, EMERALD_AMMO_12G, AMETHYST_AMMO_12G);
        List<AmmoItem> cb = Arrays.asList(WOODEN_AMMO_CROSSBOW_BOLT, STONE_AMMO_CROSSBOW_BOLT, IRON_AMMO_CROSSBOW_BOLT, GOLD_AMMO_CROSSBOW_BOLT, DIAMOND_AMMO_CROSSBOW_BOLT, EMERALD_AMMO_CROSSBOW_BOLT, AMETHYST_AMMO_CROSSBOW_BOLT);
        GUN_TO_ITEM_MAP.put(M1911, pistol.toArray(new AmmoItem[0]));
        GUN_TO_ITEM_MAP.put(UMP45, smg.toArray(new AmmoItem[0]));
        GUN_TO_ITEM_MAP.put(SKS, ar.toArray(new AmmoItem[0]));
        GUN_TO_ITEM_MAP.put(KAR98K, sr.toArray(new AmmoItem[0]));
        GUN_TO_ITEM_MAP.put(S1897, sg.toArray(new AmmoItem[0]));
        GUN_TO_ITEM_MAP.put(WOODEN_CROSSBOW, cb.toArray(new AmmoItem[0]));
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
