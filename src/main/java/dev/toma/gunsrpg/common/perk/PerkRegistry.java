package dev.toma.gunsrpg.common.perk;

import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public final class PerkRegistry {

    private static final PerkRegistry REGISTRY = new PerkRegistry();
    private final Map<ResourceLocation, Perk> perkMap = new HashMap<>();

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

    public int size() {
        return perkMap.size();
    }

    private PerkRegistry() {}
}
