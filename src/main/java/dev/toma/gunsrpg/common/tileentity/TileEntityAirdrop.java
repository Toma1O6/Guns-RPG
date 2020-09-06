package dev.toma.gunsrpg.common.tileentity;

import dev.toma.gunsrpg.common.ModRegistry;
import dev.toma.gunsrpg.util.math.WeightedRandom;
import net.minecraft.item.ItemStack;

import java.util.*;
import java.util.function.Supplier;

public class TileEntityAirdrop extends IInventoryFactory {

    public static final WeightedRandom<LootCategory> CATEGORIES = new WeightedRandom<>(LootCategory::getChances, LootCategory.values());
    public static final WeightedRandom<LootRarity> AMMO_RARITY = new WeightedRandom<>(LootRarity::getAmmoWeight, LootRarity.values());
    public static final WeightedRandom<LootRarity> MEDS_RARITY = new WeightedRandom<>(LootRarity::getMedsWeight, LootRarity.values());
    public static final WeightedRandom<LootRarity> BOOST_RARITY = new WeightedRandom<>(LootRarity::getBoostWeight, LootRarity.values());
    public static final WeightedRandom<SlotCount> SLOT_COUNT_PICKER = new WeightedRandom<>(SlotCount::getWeight, SlotCount.array);
    private final Random random;

    public TileEntityAirdrop() {
        this.random = new Random();
    }

    @Override
    public String getName() {
        return "container.airdrop";
    }

    @Override
    public int getSizeInventory() {
        return 9;
    }

    public void generateLoot() {
        int slots = SLOT_COUNT_PICKER.getRandom().getSlots();
        LootStorage storage = LootStorage.instance();
        for(int i = 0; i < slots; i++) {
            LootCategory category = CATEGORIES.getRandom();
            LootRarity rarity = getRarityPicker(category).getRandom();
            setInventorySlotContents(i, storage.getRandomItem(category, rarity, random));
        }
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

        private final Map<LootCategory, Map<LootRarity, List<Supplier<ItemStack>>>> completeLootPool = new HashMap<>();
        private static final LootStorage INSTANCE = new LootStorage();

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

        public ItemStack getRandomItem(LootCategory category, LootRarity rarity, Random rand) {
            List<Supplier<ItemStack>> list = completeLootPool.get(category).get(rarity);
            int idx = rand.nextInt(list.size());
            return list.get(idx).get();
        }

        private List<Supplier<ItemStack>> getCommonMeds() {
            return listOf(
                    () -> new ItemStack(ModRegistry.GRPGItems.BANDAGE),
                    () -> new ItemStack(ModRegistry.GRPGItems.ANTIDOTUM_PILLS),
                    () -> new ItemStack(ModRegistry.GRPGItems.BANDAGE, 2)
            );
        }

        private List<Supplier<ItemStack>> getUncommonMeds() {
            return listOf(
                    () -> new ItemStack(ModRegistry.GRPGItems.PLASTER_CAST),
                    () -> new ItemStack(ModRegistry.GRPGItems.VACCINE)
            );
        }

        private List<Supplier<ItemStack>> getRareMeds() {
            return listOf(
                    () -> new ItemStack(ModRegistry.GRPGItems.ANTIDOTUM_PILLS, 2),
                    () -> new ItemStack(ModRegistry.GRPGItems.BANDAGE, 3)
            );
        }

        private List<Supplier<ItemStack>> getVeryRareMeds() {
            return listOf(
                    () -> new ItemStack(ModRegistry.GRPGItems.PLASTER_CAST, 2),
                    () -> new ItemStack(ModRegistry.GRPGItems.VACCINE, 2)
            );
        }

        private List<Supplier<ItemStack>> getEpicMeds() {
            return listOf(
                    () -> new ItemStack(ModRegistry.GRPGItems.BANDAGE, 4),
                    () -> new ItemStack(ModRegistry.GRPGItems.ANTIDOTUM_PILLS, 3)
            );
        }

        private List<Supplier<ItemStack>> getLegendaryMeds() {
            return listOf(
                    () -> new ItemStack(ModRegistry.GRPGItems.BANDAGE, 5),
                    () -> new ItemStack(ModRegistry.GRPGItems.PLASTER_CAST, 3)
            );
        }

        private List<Supplier<ItemStack>> getCommonAmmo() {
            return listOf(
                    () -> new ItemStack(ModRegistry.GRPGItems.WOODEN_AMMO_9MM, 25),
                    () -> new ItemStack(ModRegistry.GRPGItems.STONE_AMMO_9MM, 25),
                    () -> new ItemStack(ModRegistry.GRPGItems.IRON_AMMO_9MM, 25),
                    () -> new ItemStack(ModRegistry.GRPGItems.WOODEN_AMMO_9MM, 50),
                    () -> new ItemStack(ModRegistry.GRPGItems.STONE_AMMO_9MM, 50),
                    () -> new ItemStack(ModRegistry.GRPGItems.WOODEN_AMMO_45ACP, 25),
                    () -> new ItemStack(ModRegistry.GRPGItems.STONE_AMMO_45ACP, 25),
                    () -> new ItemStack(ModRegistry.GRPGItems.IRON_AMMO_45ACP, 25),
                    () -> new ItemStack(ModRegistry.GRPGItems.WOODEN_AMMO_45ACP, 50),
                    () -> new ItemStack(ModRegistry.GRPGItems.WOODEN_AMMO_556MM, 25),
                    () -> new ItemStack(ModRegistry.GRPGItems.STONE_AMMO_556MM, 25),
                    () -> new ItemStack(ModRegistry.GRPGItems.WOODEN_AMMO_762MM, 25),
                    () -> new ItemStack(ModRegistry.GRPGItems.WOODEN_AMMO_12G, 25),
                    () -> new ItemStack(ModRegistry.GRPGItems.WOODEN_AMMO_CROSSBOW_BOLT, 25),
                    () -> new ItemStack(ModRegistry.GRPGItems.STONE_AMMO_CROSSBOW_BOLT, 25)
            );
        }

        private List<Supplier<ItemStack>> getUncommonAmmo() {
            return listOf(
                    () -> new ItemStack(ModRegistry.GRPGItems.GOLD_AMMO_9MM, 25),
                    () -> new ItemStack(ModRegistry.GRPGItems.DIAMOND_AMMO_9MM, 25),
                    () -> new ItemStack(ModRegistry.GRPGItems.IRON_AMMO_9MM, 50),
                    () -> new ItemStack(ModRegistry.GRPGItems.GOLD_AMMO_45ACP, 25),
                    () -> new ItemStack(ModRegistry.GRPGItems.DIAMOND_AMMO_45ACP, 25),
                    () -> new ItemStack(ModRegistry.GRPGItems.STONE_AMMO_45ACP, 50),
                    () -> new ItemStack(ModRegistry.GRPGItems.IRON_AMMO_556MM, 25),
                    () -> new ItemStack(ModRegistry.GRPGItems.GOLD_AMMO_556MM, 25),
                    () -> new ItemStack(ModRegistry.GRPGItems.WOODEN_AMMO_556MM, 50),
                    () -> new ItemStack(ModRegistry.GRPGItems.STONE_AMMO_556MM, 50),
                    () -> new ItemStack(ModRegistry.GRPGItems.STONE_AMMO_762MM, 25),
                    () -> new ItemStack(ModRegistry.GRPGItems.IRON_AMMO_762MM, 25),
                    () -> new ItemStack(ModRegistry.GRPGItems.WOODEN_AMMO_762MM, 50),
                    () -> new ItemStack(ModRegistry.GRPGItems.STONE_AMMO_12G, 25),
                    () -> new ItemStack(ModRegistry.GRPGItems.IRON_AMMO_12G, 25),
                    () -> new ItemStack(ModRegistry.GRPGItems.WOODEN_AMMO_12G, 50),
                    () -> new ItemStack(ModRegistry.GRPGItems.WOODEN_AMMO_CROSSBOW_BOLT, 50),
                    () -> new ItemStack(ModRegistry.GRPGItems.STONE_AMMO_CROSSBOW_BOLT, 50),
                    () -> new ItemStack(ModRegistry.GRPGItems.IRON_AMMO_CROSSBOW_BOLT, 25)
            );
        }

        private List<Supplier<ItemStack>> getRareAmmo() {
            return listOf(
                    () -> new ItemStack(ModRegistry.GRPGItems.EMERALD_AMMO_9MM, 25),
                    () -> new ItemStack(ModRegistry.GRPGItems.AMETHYST_AMMO_9MM, 25),
                    () -> new ItemStack(ModRegistry.GRPGItems.GOLD_AMMO_9MM, 50),
                    () -> new ItemStack(ModRegistry.GRPGItems.DIAMOND_AMMO_9MM, 50),
                    () -> new ItemStack(ModRegistry.GRPGItems.EMERALD_AMMO_45ACP, 25),
                    () -> new ItemStack(ModRegistry.GRPGItems.IRON_AMMO_45ACP, 50),
                    () -> new ItemStack(ModRegistry.GRPGItems.GOLD_AMMO_45ACP, 50),
                    () -> new ItemStack(ModRegistry.GRPGItems.DIAMOND_AMMO_556MM, 25),
                    () -> new ItemStack(ModRegistry.GRPGItems.IRON_AMMO_556MM, 50),
                    () -> new ItemStack(ModRegistry.GRPGItems.GOLD_AMMO_762MM, 25),
                    () -> new ItemStack(ModRegistry.GRPGItems.STONE_AMMO_762MM, 50),
                    () -> new ItemStack(ModRegistry.GRPGItems.GOLD_AMMO_12G, 25),
                    () -> new ItemStack(ModRegistry.GRPGItems.STONE_AMMO_12G, 50),
                    () -> new ItemStack(ModRegistry.GRPGItems.GOLD_AMMO_CROSSBOW_BOLT, 25),
                    () -> new ItemStack(ModRegistry.GRPGItems.DIAMOND_AMMO_CROSSBOW_BOLT, 25),
                    () -> new ItemStack(ModRegistry.GRPGItems.IRON_AMMO_CROSSBOW_BOLT, 50)
            );
        }

        private List<Supplier<ItemStack>> getVeryRareAmmo() {
            return listOf(
                    () -> new ItemStack(ModRegistry.GRPGItems.EMERALD_AMMO_9MM, 50),
                    () -> new ItemStack(ModRegistry.GRPGItems.AMETHYST_AMMO_9MM, 50),
                    () -> new ItemStack(ModRegistry.GRPGItems.AMETHYST_AMMO_45ACP, 25),
                    () -> new ItemStack(ModRegistry.GRPGItems.DIAMOND_AMMO_45ACP, 50),
                    () -> new ItemStack(ModRegistry.GRPGItems.EMERALD_AMMO_556MM, 25),
                    () -> new ItemStack(ModRegistry.GRPGItems.GOLD_AMMO_556MM, 50),
                    () -> new ItemStack(ModRegistry.GRPGItems.DIAMOND_AMMO_762MM, 25),
                    () -> new ItemStack(ModRegistry.GRPGItems.EMERALD_AMMO_762MM, 25),
                    () -> new ItemStack(ModRegistry.GRPGItems.IRON_AMMO_762MM, 50),
                    () -> new ItemStack(ModRegistry.GRPGItems.DIAMOND_AMMO_12G, 25),
                    () -> new ItemStack(ModRegistry.GRPGItems.IRON_AMMO_12G, 50),
                    () -> new ItemStack(ModRegistry.GRPGItems.EMERALD_AMMO_CROSSBOW_BOLT, 25),
                    () -> new ItemStack(ModRegistry.GRPGItems.AMETHYST_AMMO_CROSSBOW_BOLT, 25),
                    () -> new ItemStack(ModRegistry.GRPGItems.GOLD_AMMO_CROSSBOW_BOLT, 50)
            );
        }

        private List<Supplier<ItemStack>> getEpicAmmo() {
            return listOf(
                    () -> new ItemStack(ModRegistry.GRPGItems.EMERALD_AMMO_45ACP, 50),
                    () -> new ItemStack(ModRegistry.GRPGItems.AMETHYST_AMMO_45ACP, 50),
                    () -> new ItemStack(ModRegistry.GRPGItems.AMETHYST_AMMO_556MM, 25),
                    () -> new ItemStack(ModRegistry.GRPGItems.DIAMOND_AMMO_556MM, 50),
                    () -> new ItemStack(ModRegistry.GRPGItems.EMERALD_AMMO_556MM, 50),
                    () -> new ItemStack(ModRegistry.GRPGItems.AMETHYST_AMMO_762MM, 25),
                    () -> new ItemStack(ModRegistry.GRPGItems.GOLD_AMMO_762MM, 50),
                    () -> new ItemStack(ModRegistry.GRPGItems.DIAMOND_AMMO_762MM, 50),
                    () -> new ItemStack(ModRegistry.GRPGItems.EMERALD_AMMO_12G, 25),
                    () -> new ItemStack(ModRegistry.GRPGItems.AMETHYST_AMMO_12G, 25),
                    () -> new ItemStack(ModRegistry.GRPGItems.GOLD_AMMO_12G, 50),
                    () -> new ItemStack(ModRegistry.GRPGItems.DIAMOND_AMMO_12G, 50),
                    () -> new ItemStack(ModRegistry.GRPGItems.DIAMOND_AMMO_CROSSBOW_BOLT, 50),
                    () -> new ItemStack(ModRegistry.GRPGItems.EMERALD_AMMO_CROSSBOW_BOLT, 50),
                    () -> new ItemStack(ModRegistry.GRPGItems.AMETHYST_AMMO_CROSSBOW_BOLT, 50)
            );
        }

        private List<Supplier<ItemStack>> getLegendaryAmmo() {
            return listOf(
                    () -> new ItemStack(ModRegistry.GRPGItems.AMETHYST_AMMO_556MM, 50),
                    () -> new ItemStack(ModRegistry.GRPGItems.EMERALD_AMMO_762MM, 50),
                    () -> new ItemStack(ModRegistry.GRPGItems.AMETHYST_AMMO_762MM, 50),
                    () -> new ItemStack(ModRegistry.GRPGItems.EMERALD_AMMO_12G, 50),
                    () -> new ItemStack(ModRegistry.GRPGItems.AMETHYST_AMMO_12G, 50)
            );
        }

        private List<Supplier<ItemStack>> getCommonBoosts() {
            return listOf(
                    () -> new ItemStack(ModRegistry.GRPGItems.ANALGETICS),
                    () -> new ItemStack(ModRegistry.GRPGItems.ANALGETICS, 2)
            );
        }

        private List<Supplier<ItemStack>> getUncommonBoosts() {
            return listOf(
                    () -> new ItemStack(ModRegistry.GRPGItems.STEREOIDS),
                    () -> new ItemStack(ModRegistry.GRPGItems.ADRENALINE)
            );
        }

        private List<Supplier<ItemStack>> getRareBoosts() {
            return listOf(
                    () -> new ItemStack(ModRegistry.GRPGItems.ANALGETICS, 3),
                    () -> new ItemStack(ModRegistry.GRPGItems.PAINKILLERS)
            );
        }

        private List<Supplier<ItemStack>> getVeryRareBoosts() {
            return listOf(
                    () -> new ItemStack(ModRegistry.GRPGItems.STEREOIDS, 2),
                    () -> new ItemStack(ModRegistry.GRPGItems.ADRENALINE, 2)
            );
        }

        private List<Supplier<ItemStack>> getEpicBoosts() {
            return listOf(
                    () -> new ItemStack(ModRegistry.GRPGItems.ANALGETICS, 4),
                    () -> new ItemStack(ModRegistry.GRPGItems.PAINKILLERS, 2)
            );
        }

        private List<Supplier<ItemStack>> getLegendaryBoosts() {
            return listOf(
                    () -> new ItemStack(ModRegistry.GRPGItems.PAINKILLERS, 3),
                    () -> new ItemStack(ModRegistry.GRPGItems.MORPHINE)
            );
        }

        @SafeVarargs
        private static <V> List<V> listOf(V... values) {
            List<V> list = new ArrayList<>();
            Collections.addAll(list, values);
            return list;
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
