package dev.toma.gunsrpg.common.skills.core;

import com.google.gson.JsonElement;
import dev.toma.gunsrpg.api.common.skill.IDataResolver;
import dev.toma.gunsrpg.api.common.skill.ITransactionValidator;
import dev.toma.gunsrpg.api.common.skill.ITransactionValidatorFactory;
import net.minecraft.util.ResourceLocation;

import java.util.*;

public final class TransactionValidatorRegistry {

    private static final Map<ResourceLocation, ITransactionValidatorFactory<?, ?>> TYPE_FACTORY_MAP = new HashMap<>();
    private static final Map<ITransactionValidatorFactory<?, ?>, Set<ITransactionValidator>> TYPE_VALIDATORS = new IdentityHashMap<>();

    @SuppressWarnings("unchecked")
    public static <F extends ITransactionValidatorFactory<?, ?>> F getValidatorFactory(ResourceLocation location) {
        return (F) TYPE_FACTORY_MAP.get(location);
    }

    @SuppressWarnings("unchecked")
    public static <T extends ITransactionValidator> Set<T> getRegisteredValidators(ITransactionValidatorFactory<T, ?> factory) {
        return (Set<T>) TYPE_VALIDATORS.computeIfAbsent(factory, key -> new HashSet<>());
    }

    public static <T extends ITransactionValidator, D> T getTransactionValidator(ITransactionValidatorFactory<T, D> factory, JsonElement jsonData) {
        Set<T> set = getRegisteredValidators(factory);
        IDataResolver<D> dataResolver = factory.resolver();
        T handler = null;
        D data = dataResolver.resolve(jsonData);
        for (T t : set) {
            if (factory.isDataMatch(t, data)) {
                handler = t;
                break;
            }
        }
        if (handler == null) {
            handler = factory.createFor(data);
            set.add(handler);
        }
        return handler;
    }

    public static void registerValidatorFactory(ResourceLocation key, ITransactionValidatorFactory<?, ?> factory) {
        TYPE_FACTORY_MAP.put(key, factory);
    }

    public static Set<ResourceLocation> getRegisteredValidatorTypes() {
        return TYPE_FACTORY_MAP.keySet();
    }

    static {
        registerValidatorFactory(PlayerLevelTransactionValidator.ID, new PlayerLevelTransactionValidatorFactory());
        registerValidatorFactory(WeaponTransactionValidator.ID, new WeaponLevelTransactionValidatorFactory());
    }
}
