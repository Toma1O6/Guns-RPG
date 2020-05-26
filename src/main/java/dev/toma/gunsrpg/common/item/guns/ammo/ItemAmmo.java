package dev.toma.gunsrpg.common.item.guns.ammo;

import dev.toma.gunsrpg.common.item.GRPGItem;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.common.skilltree.Ability;
import dev.toma.gunsrpg.util.ModUtils;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static dev.toma.gunsrpg.common.ModRegistry.GRPGItems.*;

public class ItemAmmo extends GRPGItem implements IAmmoProvider {

    public static Map<GunItem, ItemAmmo[]> GUN_TO_ITEM_MAP = new HashMap<>();
    private final AmmoType ammoType;
    private final AmmoMaterial material;
    private final Ability.Type type;
    private final Supplier<GunItem> item;

    public ItemAmmo(String name, AmmoType ammoType, AmmoMaterial material, Ability.Type type, Supplier<GunItem> item) {
        super(name);
        this.material = material;
        this.ammoType = ammoType;
        this.type = type;
        this.item = item;
    }

    @Override
    public Ability.Type getRequiredProperty() {
        return type;
    }

    @Override
    public AmmoType getAmmoType() {
        return ammoType;
    }

    @Override
    public AmmoMaterial getMaterial() {
        return material;
    }

    public static ItemAmmo getAmmoFor(GunItem item, ItemStack stack) {
        AmmoMaterial material = item.getMaterialFromNBT(stack);
        if(material == null) return null;
        AmmoType type = item.getAmmoType();
        for(ItemAmmo ia : GUN_TO_ITEM_MAP.get(item)) {
            if(ia.getAmmoType() == type && ia.getMaterial() == material) {
                return ia;
            }
        }
        return null;
    }

    public static void init() {
        List<ItemAmmo> pistol = ModUtils.newList(WOODEN_AMMO_9MM, STONE_AMMO_9MM, IRON_AMMO_9MM, GOLD_AMMO_9MM, DIAMOND_AMMO_9MM, EMERALD_AMMO_9MM, AMETHYST_AMMO_9MM);
        List<ItemAmmo> smg = ModUtils.newList(WOODEN_AMMO_45ACP, STONE_AMMO_45ACP, IRON_AMMO_45ACP, GOLD_AMMO_45ACP, DIAMOND_AMMO_45ACP, EMERALD_AMMO_45ACP, AMETHYST_AMMO_45ACP);
        List<ItemAmmo> ar = ModUtils.newList(WOODEN_AMMO_556MM, STONE_AMMO_556MM, IRON_AMMO_556MM, GOLD_AMMO_556MM, DIAMOND_AMMO_556MM, EMERALD_AMMO_556MM, AMETHYST_AMMO_556MM);
        List<ItemAmmo> sr = ModUtils.newList(WOODEN_AMMO_762MM, STONE_AMMO_762MM, IRON_AMMO_762MM, GOLD_AMMO_762MM, DIAMOND_AMMO_762MM, EMERALD_AMMO_762MM, AMETHYST_AMMO_762MM);
        List<ItemAmmo> sg = ModUtils.newList(WOODEN_AMMO_12G, STONE_AMMO_12G, IRON_AMMO_12G, GOLD_AMMO_12G, DIAMOND_AMMO_12G, EMERALD_AMMO_12G, AMETHYST_AMMO_12G);
        GUN_TO_ITEM_MAP.put(PISTOL, pistol.toArray(new ItemAmmo[0]));
        GUN_TO_ITEM_MAP.put(SMG, smg.toArray(new ItemAmmo[0]));
        GUN_TO_ITEM_MAP.put(ASSAULT_RIFLE, ar.toArray(new ItemAmmo[0]));
        GUN_TO_ITEM_MAP.put(SNIPER_RIFLE, sr.toArray(new ItemAmmo[0]));
        GUN_TO_ITEM_MAP.put(SHOTGUN, sg.toArray(new ItemAmmo[0]));
    }

    private static void put(GunItem item, ItemAmmo ammo) {
        ItemAmmo[] itemAmmo = GUN_TO_ITEM_MAP.computeIfAbsent(item, k -> new ItemAmmo[AmmoMaterial.values().length]);
        int id = ModUtils.getLastIndexOfArray(itemAmmo);
        itemAmmo[id] = ammo;
    }
}
