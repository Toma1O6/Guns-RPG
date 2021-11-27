package dev.toma.gunsrpg.common.item.guns.util;

import dev.toma.gunsrpg.common.item.guns.setup.AbstractGun;

import javax.annotation.Nullable;
import java.util.IdentityHashMap;
import java.util.Map;

public final class ScopeDataRegistry {

    private static final ScopeDataRegistry REGISTRY = new ScopeDataRegistry();
    private final Map<AbstractGun, Entry> data = new IdentityHashMap<>();

    public static ScopeDataRegistry getRegistry() {
        return REGISTRY;
    }

    public void register(AbstractGun gun, float scopeFov, float sensitivityMultiplier) {
        data.put(gun, new Entry(scopeFov, sensitivityMultiplier));
    }

    @Nullable
    public Entry getRegistryEntry(AbstractGun gun) {
        Entry entry = data.get(gun);
        if (entry == null) {
            throw new IllegalStateException(String.format("Attempted to get scope data for gun (%s), which doesn't contain custom data configuration", gun.getRegistryName().toString()));
        }
        return entry;
    }

    public static class Entry {

        private final float fov, sensMultiplier;

        private Entry(float fov, float sensMultiplier) {
            this.fov = fov;
            this.sensMultiplier = sensMultiplier;
        }

        public float getFov() {
            return fov;
        }

        public float getSensitivityMultiplier() {
            return sensMultiplier;
        }
    }
}
