package dev.toma.gunsrpg.common.item.perk;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.perk.Perk;
import dev.toma.gunsrpg.common.perk.PerkRegistry;
import dev.toma.gunsrpg.common.perk.PerkType;
import dev.toma.gunsrpg.resource.perks.CrystalConfiguration;
import dev.toma.gunsrpg.resource.perks.PerkConfiguration;
import dev.toma.gunsrpg.util.ModUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.Constants;

import java.util.*;
import java.util.stream.Collectors;

public final class Crystal {

    private final int level;
    private final List<CrystalAttribute> attributes;

    public Crystal(int level, List<CrystalAttribute> attributes) {
        this.level = level;
        this.attributes = attributes;
    }

    public static Map<Perk, List<CrystalAttribute>> groupAttributes(List<CrystalAttribute> attributes) {
        Map<Perk, List<CrystalAttribute>> map = new HashMap<>();
        for (CrystalAttribute attribute : attributes) {
            Perk perk = attribute.getPerk();
            map.computeIfAbsent(perk, key -> new ArrayList<>()).add(attribute);
        }
        return map;
    }

    public static Crystal mergeAndLevelUp(Crystal crystal1, Crystal crystal2, int level) {
        List<CrystalAttribute> list = new ArrayList<>();
        list.addAll(crystal1.attributes);
        list.addAll(crystal2.attributes);
        Map<Perk, List<CrystalAttribute>> map = groupAttributes(list);
        List<CrystalAttribute> result = new ArrayList<>();
        for (Map.Entry<Perk, List<CrystalAttribute>> entry : map.entrySet()) {
            CrystalAttribute attribute = CrystalAttribute.flatten(entry.getKey(), entry.getValue());
            result.add(attribute);
        }
        List<CrystalAttribute> buffs = result.stream().filter(type -> type.getType() == PerkType.BUFF).collect(Collectors.toList());
        List<CrystalAttribute> debuffs = result.stream().filter(type -> type.getType() == PerkType.DEBUFF).collect(Collectors.toList());
        CrystalConfiguration.Storage storage = GunsRPG.getModLifecycle().getPerkManager().configLoader.getConfiguration().getCrystalConfig().getStorage();
        int buffLimit = storage.getBuffCapacity() == -1 ? Integer.MAX_VALUE : storage.getBuffCapacity();
        int debuffLimit = storage.getDebuffCapacity() == -1 ? Integer.MAX_VALUE : storage.getDebuffCapacity();
        Random random = new Random();
        if (buffs.size() > buffLimit) {
            ModUtils.clearRandomItems(buffs, random, buffs.size() - buffLimit);
        }
        if (debuffs.size() > debuffLimit) {
            ModUtils.clearRandomItems(debuffs, random, debuffs.size() - debuffLimit);
        }
        result.clear();
        result.addAll(buffs);
        result.addAll(debuffs);
        result.sort(compareAttributes());
        return new Crystal(level, result);
    }

    public static Crystal generate() {
        PerkConfiguration perkConfig = GunsRPG.getModLifecycle().getPerkManager().configLoader.getConfiguration();
        CrystalConfiguration crystalConfig = perkConfig.getCrystalConfig();
        CrystalConfiguration.Spawns spawns = crystalConfig.getSpawns();
        CrystalConfiguration.Spawn spawn = spawns.getRandomSpawn();
        CrystalConfiguration.Types types = spawns.getTypeRanges();
        return generate(spawn.getLevel(), types.getBuffCount(), types.getDebuffCount());
    }

    public static Crystal generate(int level, int buffs, int debuffs) {
        PerkRegistry registry = PerkRegistry.getRegistry();
        Set<CrystalAttribute> set = new HashSet<>();
        for (int i = 0; i < buffs; i++) {
            Perk perk = registry.getRandomPerk();
            set.add(new CrystalAttribute(perk, PerkType.BUFF, level));
        }
        for (int i = 0; i < debuffs; i++) {
            Perk perk = registry.getRandomPerk();
            set.add(new CrystalAttribute(perk, PerkType.DEBUFF, level));
        }
        return new Crystal(level, new ArrayList<>(set));
    }

    public static Crystal mergeAttributes(int level, Map<PerkType, List<CrystalAttribute>> map) {
        List<CrystalAttribute> list = new ArrayList<>();
        for (List<CrystalAttribute> attributes : map.values()) {
            list.addAll(attributes);
        }
        return new Crystal(level, list);
    }

    public int getLevel() {
        return level;
    }

    public List<CrystalAttribute> listAttributes() {
        return attributes;
    }

    public boolean hasAnyAttributes() {
        return attributes.size() > 0;
    }

    public EnumMap<PerkType, List<CrystalAttribute>> groupByType() {
        EnumMap<PerkType, List<CrystalAttribute>> map = new EnumMap<>(PerkType.class);
        for (PerkType type : PerkType.values()) {
            map.put(type, new ArrayList<>());
        }
        for (CrystalAttribute attribute : attributes) {
            PerkType type = attribute.getType();
            map.get(type).add(attribute);
        }
        return map;
    }

    public Crystal append(CrystalAttribute attribute) {
        Set<CrystalAttribute> set = new HashSet<>(attributes);
        set.remove(attribute);
        set.add(attribute);
        return new Crystal(level, new ArrayList<>(set));
    }

    public Crystal remove(CrystalAttribute attribute) {
        List<CrystalAttribute> copy = new ArrayList<>(attributes);
        copy.remove(attribute);
        return new Crystal(level, copy);
    }

    public ItemStack asItem(CrystalItem item) {
        ItemStack stack = new ItemStack(item);
        CompoundNBT nbt = new CompoundNBT();
        CompoundNBT crystalNbt = toNbt();
        nbt.put("crystal", crystalNbt);
        stack.setTag(nbt);
        return stack;
    }

    public CompoundNBT toNbt() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("level", level);
        ListNBT list = new ListNBT();
        attributes.forEach(attr -> list.add(attr.toNbt()));
        nbt.put("attributes", list);
        return nbt;
    }

    public static Crystal fromNbt(CompoundNBT nbt) {
        int level = Math.max(1, nbt.getInt("level"));
        ListNBT list = nbt.getList("attributes", Constants.NBT.TAG_COMPOUND);
        List<CrystalAttribute> collection = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            CompoundNBT attrNbt = list.getCompound(i);
            CrystalAttribute attribute = CrystalAttribute.fromNbt(attrNbt);
            if (attribute == null) continue;
            collection.add(attribute);
        }
        collection.sort(compareAttributes());
        return new Crystal(level, collection);
    }

    public static Comparator<CrystalAttribute> compareAttributes() {
        return Comparator.comparing(CrystalAttribute::getType, (o1, o2) -> o2.ordinal() - o1.ordinal()).thenComparing(CrystalAttribute::getValue).reversed();
    }
}
