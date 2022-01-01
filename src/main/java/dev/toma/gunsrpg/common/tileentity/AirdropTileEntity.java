package dev.toma.gunsrpg.common.tileentity;

import dev.toma.gunsrpg.common.init.ModBlockEntities;
import dev.toma.gunsrpg.common.init.ModItems;
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
        this(ModBlockEntities.AIRDROP.get());
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
                    () -> new ItemStack(ModItems.BANDAGE),
                    () -> new ItemStack(ModItems.ANTIDOTUM_PILLS)
            );
        }

        private List<Supplier<ItemStack>> getUncommonMeds() {
            return listOf(
                    () -> new ItemStack(ModItems.PLASTER_CAST),
                    () -> new ItemStack(ModItems.VACCINE)
            );
        }

        private List<Supplier<ItemStack>> getRareMeds() {
            return listOf(
                    () -> new ItemStack(ModItems.ANTIDOTUM_PILLS, 2),
                    () -> new ItemStack(ModItems.BANDAGE, 2)
            );
        }

        private List<Supplier<ItemStack>> getVeryRareMeds() {
            return listOf(
                    () -> new ItemStack(ModItems.PLASTER_CAST, 2),
                    () -> new ItemStack(ModItems.VACCINE, 2)
            );
        }

        private List<Supplier<ItemStack>> getEpicMeds() {
            return listOf(
                    () -> new ItemStack(ModItems.BANDAGE, 4),
                    () -> new ItemStack(ModItems.ANTIDOTUM_PILLS, 2)
            );
        }

        private List<Supplier<ItemStack>> getLegendaryMeds() {
            return listOf(
                    () -> new ItemStack(ModItems.BANDAGE, 4),
                    () -> new ItemStack(ModItems.PLASTER_CAST, 3)
            );
        }

        private List<Supplier<ItemStack>> getCommonAmmo() {
            return listOf(
                    () -> new ItemStack(ModItems.WOODEN_9MM, 25),
                    () -> new ItemStack(ModItems.STONE_9MM, 25),
                    () -> new ItemStack(ModItems.IRON_9MM, 25),
                    () -> new ItemStack(ModItems.WOODEN_9MM, 50),
                    () -> new ItemStack(ModItems.STONE_9MM, 50),
                    () -> new ItemStack(ModItems.WOODEN_45ACP, 25),
                    () -> new ItemStack(ModItems.STONE_45ACP, 25),
                    () -> new ItemStack(ModItems.IRON_45ACP, 25),
                    () -> new ItemStack(ModItems.WOODEN_45ACP, 50),
                    () -> new ItemStack(ModItems.WOODEN_556MM, 25),
                    () -> new ItemStack(ModItems.STONE_556MM, 25),
                    () -> new ItemStack(ModItems.WOODEN_762MM, 25),
                    () -> new ItemStack(ModItems.WOODEN_12G, 25),
                    () -> new ItemStack(ModItems.WOODEN_BOLT, 25),
                    () -> new ItemStack(ModItems.STONE_BOLT, 25)
            );
        }

        private List<Supplier<ItemStack>> getUncommonAmmo() {
            return listOf(
                    () -> new ItemStack(ModItems.GOLD_9MM, 25),
                    () -> new ItemStack(ModItems.DIAMOND_9MM, 25),
                    () -> new ItemStack(ModItems.IRON_9MM, 50),
                    () -> new ItemStack(ModItems.GOLD_45ACP, 25),
                    () -> new ItemStack(ModItems.DIAMOND_45ACP, 25),
                    () -> new ItemStack(ModItems.STONE_45ACP, 50),
                    () -> new ItemStack(ModItems.IRON_556MM, 25),
                    () -> new ItemStack(ModItems.GOLD_556MM, 25),
                    () -> new ItemStack(ModItems.WOODEN_556MM, 50),
                    () -> new ItemStack(ModItems.STONE_556MM, 50),
                    () -> new ItemStack(ModItems.STONE_762MM, 25),
                    () -> new ItemStack(ModItems.IRON_762MM, 25),
                    () -> new ItemStack(ModItems.WOODEN_762MM, 50),
                    () -> new ItemStack(ModItems.STONE_12G, 25),
                    () -> new ItemStack(ModItems.IRON_12G, 25),
                    () -> new ItemStack(ModItems.WOODEN_12G, 50),
                    () -> new ItemStack(ModItems.WOODEN_BOLT, 50),
                    () -> new ItemStack(ModItems.STONE_BOLT, 50),
                    () -> new ItemStack(ModItems.IRON_BOLT, 25)
            );
        }

        private List<Supplier<ItemStack>> getRareAmmo() {
            return listOf(
                    () -> new ItemStack(ModItems.EMERALD_9MM, 25),
                    () -> new ItemStack(ModItems.AMETHYST_9MM, 25),
                    () -> new ItemStack(ModItems.GOLD_9MM, 50),
                    () -> new ItemStack(ModItems.DIAMOND_9MM, 50),
                    () -> new ItemStack(ModItems.EMERALD_45ACP, 25),
                    () -> new ItemStack(ModItems.IRON_45ACP, 50),
                    () -> new ItemStack(ModItems.GOLD_45ACP, 50),
                    () -> new ItemStack(ModItems.DIAMOND_556MM, 25),
                    () -> new ItemStack(ModItems.IRON_556MM, 50),
                    () -> new ItemStack(ModItems.GOLD_762MM, 25),
                    () -> new ItemStack(ModItems.STONE_762MM, 50),
                    () -> new ItemStack(ModItems.GOLD_12G, 25),
                    () -> new ItemStack(ModItems.STONE_12G, 50),
                    () -> new ItemStack(ModItems.GOLD_BOLT, 25),
                    () -> new ItemStack(ModItems.DIAMOND_BOLT, 25),
                    () -> new ItemStack(ModItems.IRON_BOLT, 50)
            );
        }

        private List<Supplier<ItemStack>> getVeryRareAmmo() {
            return listOf(
                    () -> new ItemStack(ModItems.EMERALD_9MM, 50),
                    () -> new ItemStack(ModItems.AMETHYST_9MM, 50),
                    () -> new ItemStack(ModItems.AMETHYST_45ACP, 25),
                    () -> new ItemStack(ModItems.DIAMOND_45ACP, 50),
                    () -> new ItemStack(ModItems.EMERALD_556MM, 25),
                    () -> new ItemStack(ModItems.GOLD_556MM, 50),
                    () -> new ItemStack(ModItems.DIAMOND_762MM, 25),
                    () -> new ItemStack(ModItems.EMERALD_762MM, 25),
                    () -> new ItemStack(ModItems.IRON_762MM, 50),
                    () -> new ItemStack(ModItems.DIAMOND_12G, 25),
                    () -> new ItemStack(ModItems.IRON_12G, 50),
                    () -> new ItemStack(ModItems.EMERALD_BOLT, 25),
                    () -> new ItemStack(ModItems.AMETHYST_BOLT, 25),
                    () -> new ItemStack(ModItems.GOLD_BOLT, 50)
            );
        }

        private List<Supplier<ItemStack>> getEpicAmmo() {
            return listOf(
                    () -> new ItemStack(ModItems.EMERALD_45ACP, 50),
                    () -> new ItemStack(ModItems.AMETHYST_45ACP, 50),
                    () -> new ItemStack(ModItems.AMETHYST_556MM, 25),
                    () -> new ItemStack(ModItems.DIAMOND_556MM, 50),
                    () -> new ItemStack(ModItems.EMERALD_556MM, 50),
                    () -> new ItemStack(ModItems.AMETHYST_762MM, 25),
                    () -> new ItemStack(ModItems.GOLD_762MM, 50),
                    () -> new ItemStack(ModItems.DIAMOND_762MM, 50),
                    () -> new ItemStack(ModItems.EMERALD_12G, 25),
                    () -> new ItemStack(ModItems.AMETHYST_12G, 25),
                    () -> new ItemStack(ModItems.GOLD_12G, 50),
                    () -> new ItemStack(ModItems.DIAMOND_12G, 50),
                    () -> new ItemStack(ModItems.DIAMOND_BOLT, 50),
                    () -> new ItemStack(ModItems.EMERALD_BOLT, 50),
                    () -> new ItemStack(ModItems.AMETHYST_BOLT, 50)
            );
        }

        private List<Supplier<ItemStack>> getLegendaryAmmo() {
            return listOf(
                    () -> new ItemStack(ModItems.AMETHYST_556MM, 50),
                    () -> new ItemStack(ModItems.EMERALD_762MM, 50),
                    () -> new ItemStack(ModItems.AMETHYST_762MM, 50),
                    () -> new ItemStack(ModItems.EMERALD_12G, 50),
                    () -> new ItemStack(ModItems.AMETHYST_12G, 50)
            );
        }

        private List<Supplier<ItemStack>> getCommonBoosts() {
            return listOf(
                    () -> new ItemStack(ModItems.ANALGETICS),
                    () -> new ItemStack(ModItems.ANALGETICS, 2)
            );
        }

        private List<Supplier<ItemStack>> getUncommonBoosts() {
            return listOf(
                    () -> new ItemStack(ModItems.STEREOIDS),
                    () -> new ItemStack(ModItems.ADRENALINE)
            );
        }

        private List<Supplier<ItemStack>> getRareBoosts() {
            return listOf(
                    () -> new ItemStack(ModItems.ANALGETICS, 3),
                    () -> new ItemStack(ModItems.PAINKILLERS)
            );
        }

        private List<Supplier<ItemStack>> getVeryRareBoosts() {
            return listOf(
                    () -> new ItemStack(ModItems.STEREOIDS, 2),
                    () -> new ItemStack(ModItems.ADRENALINE, 2)
            );
        }

        private List<Supplier<ItemStack>> getEpicBoosts() {
            return listOf(
                    () -> new ItemStack(ModItems.ANALGETICS, 4),
                    () -> new ItemStack(ModItems.PAINKILLERS, 2)
            );
        }

        private List<Supplier<ItemStack>> getLegendaryBoosts() {
            return listOf(
                    () -> new ItemStack(ModItems.PAINKILLERS, 3),
                    () -> new ItemStack(ModItems.MORPHINE)
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
