package dev.toma.gunsrpg.common.item.guns.util;

import dev.toma.gunsrpg.api.common.data.ISkillProvider;
import dev.toma.gunsrpg.common.item.guns.setup.AbstractGun;
import dev.toma.gunsrpg.sided.ClientSideManager;
import dev.toma.gunsrpg.util.function.FloatSupplier;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Predicate;

public final class ScopeDataRegistry {

    public static final FloatSupplier ZOOM_2_5 = () -> ClientSideManager.config.optics.scope25x;
    public static final FloatSupplier ZOOM_3_0 = () -> ClientSideManager.config.optics.scope30x;
    public static final FloatSupplier ZOOM_3_5 = () -> ClientSideManager.config.optics.scope35x;
    public static final FloatSupplier ZOOM_4_0 = () -> ClientSideManager.config.optics.scope40x;
    public static final FloatSupplier ZOOM_6_0 = () -> ClientSideManager.config.optics.scope60x;

    private static final ScopeDataRegistry REGISTRY = new ScopeDataRegistry();
    private final Map<AbstractGun, Entry> data = new IdentityHashMap<>();

    public static ScopeDataRegistry getRegistry() {
        return REGISTRY;
    }

    public void register(AbstractGun gun, float scopeFov, FloatSupplier sensitivityMultiplier) {
        register(gun, scopeFov, sensitivityMultiplier, skills -> true);
    }

    public void register(AbstractGun gun, float scopeFov, FloatSupplier sensitivityMultiplier, Predicate<ISkillProvider> condition) {
        data.put(gun, new Entry(scopeFov, sensitivityMultiplier, condition));
    }

    public Entry getRegistryEntry(AbstractGun gun) {
        Entry entry = data.get(gun);
        return entry != null ? entry : Entry.NO_DATA;
    }

    public static class Entry {

        public static final Entry NO_DATA = new Entry(0.0F, () -> 0.0F, skills -> false);
        private final float fov;
        private final FloatSupplier sensitivityProvider;
        private final Predicate<ISkillProvider> skillCondition;

        private Entry(float fov, FloatSupplier sensitivityProvider, Predicate<ISkillProvider> skillCondition) {
            this.fov = fov;
            this.sensitivityProvider = sensitivityProvider;
            this.skillCondition = skillCondition;
        }

        public float getFov() {
            return fov;
        }

        public float getSensitivityMultiplier() {
            return sensitivityProvider.getFloat();
        }

        public boolean isApplicable(ISkillProvider provider) {
            return skillCondition.test(provider);
        }
    }
}
