package dev.toma.gunsrpg.common.perk;

import dev.toma.gunsrpg.util.ModUtils;
import net.minecraft.util.ResourceLocation;

import java.util.*;

public final class PerkRegistry {

    private static final PerkRegistry REGISTRY = new PerkRegistry();
    private final Random random = new Random();
    private final Map<ResourceLocation, Perk> perkMap = new HashMap<>();
    private List<Perk> perkList;

    public static PerkRegistry getRegistry() {
        return REGISTRY;
    }

    public Perk getPerkById(ResourceLocation id) {
        return perkMap.get(id);
    }

    public void dropRegistry() {
        perkMap.clear();
    }

    public void register(ResourceLocation id, Perk perk) {
        perk.setId(id);
        perkMap.put(id, perk);
    }

    public void finishRegistering() {
        perkList = new ArrayList<>(perkMap.values());
    }

    public Perk getRandomPerk() {
        return ModUtils.getRandomListElement(perkList, random);
    }

    public int size() {
        return perkMap.size();
    }

    public Set<ResourceLocation> getPerkIds() {
        return perkMap.keySet();
    }

    public Set<Perk> getPerks() {
        return new HashSet<>(perkMap.values());
    }

    private PerkRegistry() {}
}
