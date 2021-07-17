package dev.toma.gunsrpg.common.tileentity;

import dev.toma.gunsrpg.common.init.GRPGItems;
import dev.toma.gunsrpg.common.init.GRPGTileEntities;
import dev.toma.gunsrpg.util.math.WeightedRandom;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import java.util.*;
import java.util.function.Supplier;

public class AirdropTileEntity extends InventoryTileEntity {

    public static final WeightedRandom<LootCategory> CATEGORIES = new WeightedRandom<>(LootCategory::getChances, LootCategory.values());
    public static final WeightedRandom<LootRarity> AMMO_RARITY = new WeightedRandom<>(LootRarity::getAmmoWeight, LootRarity.values());
    public static final WeightedRandom<LootRarity> MEDS_RARITY = new WeightedRandom<>(LootRarity::getMedsWeight, LootRarity.values());
    public static final WeightedRandom<LootRarity> BOOST_RARITY = new WeightedRandom<>(LootRarity::getBoostWeight, LootRarity.values());
    public static final WeightedRandom<SlotCount> SLOT_COUNT_PICKER = new WeightedRandom<>(SlotCount::getWeight, SlotCount.array);
    private final Random random;

    public AirdropTileEntity() {
        this(GRPGTileEntities.AIRDROP.get());
    }

    protected AirdropTileEntity(TileEntityType<? extends AirdropTileEntity> type) {
        super(type);
        this.random = new Random();
    }

    @Override
    public IItemHandlerModifiable createInventory() {
        return new ItemStackHandler(9);
    }

    public void generateLoot() {
        getInventory().ifPresent(handler -> {
            int slots = SLOT_COUNT_PICKER.getRandom().getSlots();
            LootStorage storage = LootStorage.instance();
            for (int i = 0; i < slots; i++) {
                LootCategory category = CATEGORIES.getRandom();
                LootRarity rarity = getRarityPicker(category).getRandom();
                handler.setStackInSlot(i, storage.getRandomItem(category, rarity, random));
            }
        });
    }

    private WeightedRandom<LootRarity> getRarityPicker(LootCategory category) {
        return category.rarityFromCategory.get();
    }

    private enum LootCategory {

        MEDICAL(() -> MEDS_RARITY, 30),
        AMMO(() -> AMMO_RARITY, 50),
        BOOST(() -> BOOST_RARITY, 20);

        private final Supplier<WeightedRandom<LootRarity>> rarityFromCategory;
        private final int chance;

        LootCategory(Supplier<WeightedRandom<LootRarity>> rarityFromCategory, int chance) {
            this.rarityFromCategory = rarityFromCategory;
            this.chance = chance;
        }

        public int getChances() {
            return chance;
        }
    }

    private enum LootRarity {

        COMMON(39, 33, 33),
        UNCOMMON(25, 25, 25),
        RARE(17, 16, 16),
        VERY_RARE(11, 11, 11),
        EPIC(6, 9, 9),
        LEGENDARY(2, 6, 6);

        private final int ammoWeight, medsWeight, boostWeight;

        LootRarity(int ammo, int meds, int boost) {
            this.ammoWeight = ammo;
            this.medsWeight = meds;
            this.boostWeight = boost;
        }

        public int getAmmoWeight() {
            return ammoWeight;
        }

        public int getMedsWeight() {
            return medsWeight;
        }

        public int getBoostWeight() {
            return boostWeight;
        }
    }

    public static final class LootStorage {

        private static final LootStorage INSTANCE = new LootStorage();
        private final Map<LootCategory, Map<LootRarity, List<Supplier<ItemStack>>>> completeLootPool = new HashMap<>();

        private LootStorage() {
            Map<LootRarity, List<Supplier<ItemStack>>> meds = new HashMap<>();
            meds.put(LootRarity.COMMON, this.getCommonMeds());
            meds.put(LootRarity.UNCOMMON, this.getUncommonMeds());
            meds.put(LootRarity.RARE, this.getRareMeds());
            meds.put(LootRarity.VERY_RARE, this.getVeryRareMeds());
            meds.put(LootRarity.EPIC, this.getEpicMeds());
            meds.put(LootRarity.LEGENDARY, this.getLegendaryMeds());
            completeLootPool.put(LootCategory.MEDICAL, meds);
            Map<LootRarity, List<Supplier<ItemStack>>> ammo = new HashMap<>();
            ammo.put(LootRarity.COMMON, this.getCommonAmmo());
            ammo.put(LootRarity.UNCOMMON, this.getUncommonAmmo());
            ammo.put(LootRarity.RARE, this.getRareAmmo());
            ammo.put(LootRarity.VERY_RARE, this.getVeryRareAmmo());
            ammo.put(LootRarity.EPIC, this.getEpicAmmo());
            ammo.put(LootRarity.LEGENDARY, this.getLegendaryAmmo());
            completeLootPool.put(LootCategory.AMMO, ammo);
            Map<LootRarity, List<Supplier<ItemStack>>> boosts = new HashMap<>();
            boosts.put(LootRarity.COMMON, this.getCommonBoosts());
            boosts.put(LootRarity.UNCOMMON, this.getUncommonBoosts());
            boosts.put(LootRarity.RARE, this.getRareBoosts());
            boosts.put(LootRarity.VERY_RARE, this.getVeryRareBoosts());
            boosts.put(LootRarity.EPIC, this.getEpicBoosts());
            boosts.put(LootRarity.LEGENDARY, this.getLegendaryBoosts());
            completeLootPool.put(LootCategory.BOOST, boosts);
        }

        public static LootStorage instance() {
            return INSTANCE;
        }

        @SafeVarargs
        private static <V> List<V> listOf(V... values) {
            List<V> list = new ArrayList<>();
            Collections.addAll(list, values);
            return list;
        }

        public ItemStack getRandomItem(LootCategory category, LootRarity rarity, Random rand) {
            List<Supplier<ItemStack>> list = completeLootPool.get(category).get(rarity);
            int idx = rand.nextInt(list.size());
            return list.get(idx).get();
        }

        private List<Supplier<ItemStack>> getCommonMeds() {
            return listOf(
                    () -> new ItemStack(GRPGItems.BANDAGE),
                    () -> new ItemStack(GRPGItems.ANTIDOTUM_PILLS),
                    () -> new ItemStack(GRPGItems.BANDAGE, 2)
            );
        }

        private List<Supplier<ItemStack>> getUncommonMeds() {
            return listOf(
                    () -> new ItemStack(GRPGItems.PLASTER_CAST),
                    () -> new ItemStack(GRPGItems.VACCINE)
            );
        }

        private List<Supplier<ItemStack>> getRareMeds() {
            return listOf(
                    () -> new ItemStack(GRPGItems.ANTIDOTUM_PILLS, 2),
                    () -> new ItemStack(GRPGItems.BANDAGE, 3)
            );
        }

        private List<Supplier<ItemStack>> getVeryRareMeds() {
            return listOf(
                    () -> new ItemStack(GRPGItems.PLASTER_CAST, 2),
                    () -> new ItemStack(GRPGItems.VACCINE, 2)
            );
        }

        private List<Supplier<ItemStack>> getEpicMeds() {
            return listOf(
                    () -> new ItemStack(GRPGItems.BANDAGE, 4),
                    () -> new ItemStack(GRPGItems.ANTIDOTUM_PILLS, 3)
            );
        }

        private List<Supplier<ItemStack>> getLegendaryMeds() {
            return listOf(
                    () -> new ItemStack(GRPGItems.BANDAGE, 5),
                    () -> new ItemStack(GRPGItems.PLASTER_CAST, 3)
            );
        }

        private List<Supplier<ItemStack>> getCommonAmmo() {
            return listOf(
                    () -> new ItemStack(GRPGItems.WOODEN_AMMO_9MM, 25),
                    () -> new ItemStack(GRPGItems.STONE_AMMO_9MM, 25),
                    () -> new ItemStack(GRPGItems.IRON_AMMO_9MM, 25),
                    () -> new ItemStack(GRPGItems.WOODEN_AMMO_9MM, 50),
                    () -> new ItemStack(GRPGItems.STONE_AMMO_9MM, 50),
                    () -> new ItemStack(GRPGItems.WOODEN_AMMO_45ACP, 25),
                    () -> new ItemStack(GRPGItems.STONE_AMMO_45ACP, 25),
                    () -> new ItemStack(GRPGItems.IRON_AMMO_45ACP, 25),
                    () -> new ItemStack(GRPGItems.WOODEN_AMMO_45ACP, 50),
                    () -> new ItemStack(GRPGItems.WOODEN_AMMO_556MM, 25),
                    () -> new ItemStack(GRPGItems.STONE_AMMO_556MM, 25),
                    () -> new ItemStack(GRPGItems.WOODEN_AMMO_762MM, 25),
                    () -> new ItemStack(GRPGItems.WOODEN_AMMO_12G, 25),
                    () -> new ItemStack(GRPGItems.WOODEN_AMMO_CROSSBOW_BOLT, 25),
                    () -> new ItemStack(GRPGItems.STONE_AMMO_CROSSBOW_BOLT, 25)
            );
        }

        private List<Supplier<ItemStack>> getUncommonAmmo() {
            return listOf(
                    () -> new ItemStack(GRPGItems.GOLD_AMMO_9MM, 25),
                    () -> new ItemStack(GRPGItems.DIAMOND_AMMO_9MM, 25),
                    () -> new ItemStack(GRPGItems.IRON_AMMO_9MM, 50),
                    () -> new ItemStack(GRPGItems.GOLD_AMMO_45ACP, 25),
                    () -> new ItemStack(GRPGItems.DIAMOND_AMMO_45ACP, 25),
                    () -> new ItemStack(GRPGItems.STONE_AMMO_45ACP, 50),
                    () -> new ItemStack(GRPGItems.IRON_AMMO_556MM, 25),
                    () -> new ItemStack(GRPGItems.GOLD_AMMO_556MM, 25),
                    () -> new ItemStack(GRPGItems.WOODEN_AMMO_556MM, 50),
                    () -> new ItemStack(GRPGItems.STONE_AMMO_556MM, 50),
                    () -> new ItemStack(GRPGItems.STONE_AMMO_762MM, 25),
                    () -> new ItemStack(GRPGItems.IRON_AMMO_762MM, 25),
                    () -> new ItemStack(GRPGItems.WOODEN_AMMO_762MM, 50),
                    () -> new ItemStack(GRPGItems.STONE_AMMO_12G, 25),
                    () -> new ItemStack(GRPGItems.IRON_AMMO_12G, 25),
                    () -> new ItemStack(GRPGItems.WOODEN_AMMO_12G, 50),
                    () -> new ItemStack(GRPGItems.WOODEN_AMMO_CROSSBOW_BOLT, 50),
                    () -> new ItemStack(GRPGItems.STONE_AMMO_CROSSBOW_BOLT, 50),
                    () -> new ItemStack(GRPGItems.IRON_AMMO_CROSSBOW_BOLT, 25)
            );
        }

        private List<Supplier<ItemStack>> getRareAmmo() {
            return listOf(
                    () -> new ItemStack(GRPGItems.EMERALD_AMMO_9MM, 25),
                    () -> new ItemStack(GRPGItems.AMETHYST_AMMO_9MM, 25),
                    () -> new ItemStack(GRPGItems.GOLD_AMMO_9MM, 50),
                    () -> new ItemStack(GRPGItems.DIAMOND_AMMO_9MM, 50),
                    () -> new ItemStack(GRPGItems.EMERALD_AMMO_45ACP, 25),
                    () -> new ItemStack(GRPGItems.IRON_AMMO_45ACP, 50),
                    () -> new ItemStack(GRPGItems.GOLD_AMMO_45ACP, 50),
                    () -> new ItemStack(GRPGItems.DIAMOND_AMMO_556MM, 25),
                    () -> new ItemStack(GRPGItems.IRON_AMMO_556MM, 50),
                    () -> new ItemStack(GRPGItems.GOLD_AMMO_762MM, 25),
                    () -> new ItemStack(GRPGItems.STONE_AMMO_762MM, 50),
                    () -> new ItemStack(GRPGItems.GOLD_AMMO_12G, 25),
                    () -> new ItemStack(GRPGItems.STONE_AMMO_12G, 50),
                    () -> new ItemStack(GRPGItems.GOLD_AMMO_CROSSBOW_BOLT, 25),
                    () -> new ItemStack(GRPGItems.DIAMOND_AMMO_CROSSBOW_BOLT, 25),
                    () -> new ItemStack(GRPGItems.IRON_AMMO_CROSSBOW_BOLT, 50)
            );
        }

        private List<Supplier<ItemStack>> getVeryRareAmmo() {
            return listOf(
                    () -> new ItemStack(GRPGItems.EMERALD_AMMO_9MM, 50),
                    () -> new ItemStack(GRPGItems.AMETHYST_AMMO_9MM, 50),
                    () -> new ItemStack(GRPGItems.AMETHYST_AMMO_45ACP, 25),
                    () -> new ItemStack(GRPGItems.DIAMOND_AMMO_45ACP, 50),
                    () -> new ItemStack(GRPGItems.EMERALD_AMMO_556MM, 25),
                    () -> new ItemStack(GRPGItems.GOLD_AMMO_556MM, 50),
                    () -> new ItemStack(GRPGItems.DIAMOND_AMMO_762MM, 25),
                    () -> new ItemStack(GRPGItems.EMERALD_AMMO_762MM, 25),
                    () -> new ItemStack(GRPGItems.IRON_AMMO_762MM, 50),
                    () -> new ItemStack(GRPGItems.DIAMOND_AMMO_12G, 25),
                    () -> new ItemStack(GRPGItems.IRON_AMMO_12G, 50),
                    () -> new ItemStack(GRPGItems.EMERALD_AMMO_CROSSBOW_BOLT, 25),
                    () -> new ItemStack(GRPGItems.AMETHYST_AMMO_CROSSBOW_BOLT, 25),
                    () -> new ItemStack(GRPGItems.GOLD_AMMO_CROSSBOW_BOLT, 50)
            );
        }

        private List<Supplier<ItemStack>> getEpicAmmo() {
            return listOf(
                    () -> new ItemStack(GRPGItems.EMERALD_AMMO_45ACP, 50),
                    () -> new ItemStack(GRPGItems.AMETHYST_AMMO_45ACP, 50),
                    () -> new ItemStack(GRPGItems.AMETHYST_AMMO_556MM, 25),
                    () -> new ItemStack(GRPGItems.DIAMOND_AMMO_556MM, 50),
                    () -> new ItemStack(GRPGItems.EMERALD_AMMO_556MM, 50),
                    () -> new ItemStack(GRPGItems.AMETHYST_AMMO_762MM, 25),
                    () -> new ItemStack(GRPGItems.GOLD_AMMO_762MM, 50),
                    () -> new ItemStack(GRPGItems.DIAMOND_AMMO_762MM, 50),
                    () -> new ItemStack(GRPGItems.EMERALD_AMMO_12G, 25),
                    () -> new ItemStack(GRPGItems.AMETHYST_AMMO_12G, 25),
                    () -> new ItemStack(GRPGItems.GOLD_AMMO_12G, 50),
                    () -> new ItemStack(GRPGItems.DIAMOND_AMMO_12G, 50),
                    () -> new ItemStack(GRPGItems.DIAMOND_AMMO_CROSSBOW_BOLT, 50),
                    () -> new ItemStack(GRPGItems.EMERALD_AMMO_CROSSBOW_BOLT, 50),
                    () -> new ItemStack(GRPGItems.AMETHYST_AMMO_CROSSBOW_BOLT, 50)
            );
        }

        private List<Supplier<ItemStack>> getLegendaryAmmo() {
            return listOf(
                    () -> new ItemStack(GRPGItems.AMETHYST_AMMO_556MM, 50),
                    () -> new ItemStack(GRPGItems.EMERALD_AMMO_762MM, 50),
                    () -> new ItemStack(GRPGItems.AMETHYST_AMMO_762MM, 50),
                    () -> new ItemStack(GRPGItems.EMERALD_AMMO_12G, 50),
                    () -> new ItemStack(GRPGItems.AMETHYST_AMMO_12G, 50)
            );
        }

        private List<Supplier<ItemStack>> getCommonBoosts() {
            return listOf(
                    () -> new ItemStack(GRPGItems.ANALGETICS),
                    () -> new ItemStack(GRPGItems.ANALGETICS, 2)
            );
        }

        private List<Supplier<ItemStack>> getUncommonBoosts() {
            return listOf(
                    () -> new ItemStack(GRPGItems.STEREOIDS),
                    () -> new ItemStack(GRPGItems.ADRENALINE)
            );
        }

        private List<Supplier<ItemStack>> getRareBoosts() {
            return listOf(
                    () -> new ItemStack(GRPGItems.ANALGETICS, 3),
                    () -> new ItemStack(GRPGItems.PAINKILLERS)
            );
        }

        private List<Supplier<ItemStack>> getVeryRareBoosts() {
            return listOf(
                    () -> new ItemStack(GRPGItems.STEREOIDS, 2),
                    () -> new ItemStack(GRPGItems.ADRENALINE, 2)
            );
        }

        private List<Supplier<ItemStack>> getEpicBoosts() {
            return listOf(
                    () -> new ItemStack(GRPGItems.ANALGETICS, 4),
                    () -> new ItemStack(GRPGItems.PAINKILLERS, 2)
            );
        }

        private List<Supplier<ItemStack>> getLegendaryBoosts() {
            return listOf(
                    () -> new ItemStack(GRPGItems.PAINKILLERS, 3),
                    () -> new ItemStack(GRPGItems.MORPHINE)
            );
        }
    }

    private static class SlotCount {
        private static final SlotCount[] array = {new SlotCount(32, 5), new SlotCount(26, 6), new SlotCount(18, 7), new SlotCount(15, 8), new SlotCount(6, 9)};
        private final int weight;
        private final int slots;

        private SlotCount(int weight, int slots) {
            this.weight = weight;
            this.slots = slots;
        }

        public int getWeight() {
            return weight;
        }

        public int getSlots() {
            return slots;
        }
    }
}
