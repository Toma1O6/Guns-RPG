package dev.toma.gunsrpg.common.item.guns.util;

import dev.toma.gunsrpg.api.common.data.ISkillProvider;
import dev.toma.gunsrpg.common.item.guns.setup.AbstractGun;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Predicate;

public final class ScopeDataRegistry {

    private static final ScopeDataRegistry REGISTRY = new ScopeDataRegistry();
    private final Map<AbstractGun, Entry> data = new IdentityHashMap<>();

    public static ScopeDataRegistry getRegistry() {
        return REGISTRY;
    }

    public void register(AbstractGun gun, float scopeFov, float sensitivityMultiplier) {
        register(gun, scopeFov, sensitivityMultiplier, skills -> true);
    }

    public void register(AbstractGun gun, float scopeFov, float sensitivityMultiplier, Predicate<ISkillProvider> condition) {
        data.put(gun, new Entry(scopeFov, sensitivityMultiplier, condition));
    }

    public Entry getRegistryEntry(AbstractGun gun) {
        Entry entry = data.get(gun);
        return entry != null ? entry : Entry.NO_DATA;
    }

    public static class Entry {

        public static final Entry NO_DATA = new Entry(0.0F, 0.0F, skills -> false);
        private final float fov, sensMultiplier;
        private final Predicate<ISkillProvider> skillCondition;

        private Entry(float fov, float sensMultiplier, Predicate<ISkillProvider> skillCondition) {
            this.fov = fov;
            this.sensMultiplier = sensMultiplier;
            this.skillCondition = skillCondition;
        }

        public float getFov() {
            return fov;
        }

        public float getSensitivityMultiplier() {
            return sensMultiplier;
        }

        public boolean isApplicable(ISkillProvider provider) {
            return skillCondition.test(provider);
        }
    }
}
