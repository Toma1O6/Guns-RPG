package lib.toma.animations;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.Collection;

public final class EasingRegistry {

    private static final Marker MARKER = MarkerManager.getMarker("EasingRegistry");
    private static final EasingRegistry REGISTRY = new EasingRegistry();
    private final Int2ObjectMap<IEasing> mapById = new Int2ObjectOpenHashMap<>();
    private byte id;

    public static EasingRegistry getRegistry() {
        return REGISTRY;
    }

    public <E extends IEasing> E register(E easing, byte assignedId) {
        mapById.put(assignedId, easing);
        if (assignedId != id) {
            AnimationEngine.logger.warn(MARKER, "Got unexpected easing ID. Expected {}, got {}.", id, assignedId);
        }
        ++id;
        return easing;
    }

    public <E extends IEasing> E register(E easing) {
        easing.setEasingId(id);
        return register(easing, id);
    }

    public IEasing getEasing(byte id) {
        IEasing easing = mapById.get(id);
        return easing != null ? easing : AnimationUtils.DEFAULT_EASE_FUNC;
    }

    public Collection<IEasing> getRegisteredEasings() {
        return mapById.values();
    }

    private EasingRegistry() {}
}
