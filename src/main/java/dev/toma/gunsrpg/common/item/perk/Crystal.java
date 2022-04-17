package dev.toma.gunsrpg.common.item.perk;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.perk.Perk;
import dev.toma.gunsrpg.common.perk.PerkRegistry;
import dev.toma.gunsrpg.common.perk.PerkType;
import dev.toma.gunsrpg.resource.perks.CrystalConfiguration;
import dev.toma.gunsrpg.resource.perks.PerkConfiguration;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.Constants;

import java.util.*;

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
        return new Crystal(level, result);
    }

    public static Crystal generate() {
        PerkConfiguration perkConfig = GunsRPG.getModLifecycle().getPerkManager().configLoader.getConfiguration();
        CrystalConfiguration crystalConfig = perkConfig.getCrystalConfig();
        CrystalConfiguration.Spawns spawns = crystalConfig.getSpawns();
        CrystalConfiguration.Spawn spawn = spawns.getRandomSpawn();
        CrystalConfiguration.Types types = spawns.getTypeRanges();
        int crystalLevel = spawn.getLevel();
        PerkRegistry registry = PerkRegistry.getRegistry();
        Set<CrystalAttribute> attributeCollection = new HashSet<>();
        for (int i = 0; i < types.getBuffCount(); i++) {
            Perk perk = registry.getRandomPerk();
            attributeCollection.add(new CrystalAttribute(perk, PerkType.BUFF, crystalLevel));
        }
        for (int i = 0; i < types.getDebuffCount(); i++) {
            Perk perk = registry.getRandomPerk();
            attributeCollection.add(new CrystalAttribute(perk, PerkType.DEBUFF, crystalLevel));
        }
        return new Crystal(crystalLevel, new ArrayList<>(attributeCollection));
    }

    public int getLevel() {
        return level;
    }

    public List<CrystalAttribute> listAttributes() {
        return attributes;
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
            collection.add(CrystalAttribute.fromNbt(attrNbt));
        }
        Comparator<CrystalAttribute> comp = Comparator.comparing(CrystalAttribute::getType, (o1, o2) -> o2.ordinal() - o1.ordinal()).thenComparing(CrystalAttribute::getValue).reversed();
        collection.sort(comp);
        return new Crystal(level, collection);
    }
}
