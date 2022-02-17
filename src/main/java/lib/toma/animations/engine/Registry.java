package lib.toma.animations.engine;

import lib.toma.animations.api.lifecycle.IRegistrationListener;
import lib.toma.animations.api.lifecycle.IRegistry;
import lib.toma.animations.api.lifecycle.IRegistryEntry;
import net.minecraft.util.ResourceLocation;

import java.util.*;

public class Registry<T extends IRegistryEntry> implements IRegistry<T> {

    private final Map<ResourceLocation, T> elementMap;
    private final List<IRegistrationListener<T>> registryListeners = new ArrayList<>();

    private Registry(RegistryBuilder<T> builder) {
        this.elementMap = builder.defaultMap;
        if (builder.vanillaListener != null) {
            addCallback(builder.vanillaListener);
        }
    }

    @Override
    public T getElement(ResourceLocation key) {
        return elementMap.get(key);
    }

    @Override
    public void addCallback(IRegistrationListener<T> listener) {
        registryListeners.add(Objects.requireNonNull(listener));
    }

    @Override
    public Collection<ResourceLocation> keys() {
        return elementMap.keySet();
    }

    @Override
    public Collection<T> values() {
        return elementMap.values();
    }

    public void load(boolean isDevMode) {
        registryListeners.forEach(listener -> register(listener, isDevMode));
    }

    private void register(IRegistrationListener<T> listener, boolean isDevMode) {
        for (T t : listener.getAll(isDevMode)) {
            register(t);
        }
    }

    private void register(T t) {
        ResourceLocation key = t.getKey();
        if (elementMap.put(key, t) != null) {
            throw new UnsupportedOperationException("Duplicate registry element: " + key);
        }
    }

    public static class RegistryBuilder<T extends IRegistryEntry> {

        private Map<ResourceLocation, T> defaultMap = new HashMap<>();
        private IRegistrationListener<T> vanillaListener;

        public RegistryBuilder<T> map(Map<ResourceLocation, T> elementMap) {
            this.defaultMap = elementMap;
            return this;
        }

        public RegistryBuilder<T> vanillaListener(IRegistrationListener<T> listener) {
            this.vanillaListener = listener;
            return this;
        }

        public IRegistry<T> buildRegistry() {
            return new Registry<>(this);
        }
    }
}
