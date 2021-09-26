package lib.toma.animations;

import lib.toma.animations.api.*;
import lib.toma.animations.api.lifecycle.Registries;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.function.Function;

public class AnimationUtils {

    public static <A extends IAnimation> A createAnimation(ResourceLocation providerPath, Function<IKeyframeProvider, A> creatorFunction) {
        AnimationEngine engine = AnimationEngine.get();
        IAnimationLoader loader = engine.loader();
        IKeyframeProvider provider = loader.getProvider(providerPath);
        return creatorFunction.apply(provider);
    }

    public static void encodeAnimationType(AnimationType<?> type, PacketBuffer buffer) {
        if (!type.hasCreator())
            throw new IllegalArgumentException(String.format("Animation type (%s) doesn't support raw animation creation!", type.getKey()));
        buffer.writeResourceLocation(type.getKey());
    }

    @SuppressWarnings("unchecked")
    public static <A extends IAnimation> AnimationType<A> decodeAnimationType(PacketBuffer buffer) {
        return (AnimationType<A>) Registries.ANIMATION_TYPES.getElement(buffer.readResourceLocation());
    }

    /**
     * Returns rotation-vector pair from supplied quaternion.
     * Rotation is in degrees
     * @param quaternion Quaternion element
     * @return rotation-vector pair
     */
    public static Pair<Float, Vector3f> getVectorWithRotation(Quaternion quaternion) {
        double value = Math.acos(quaternion.r());
        double deg = Math.toDegrees(value * 2f);
        double rad = deg * ((float) Math.PI / 180.0F);
        float f = (float) Math.sin((rad / 2.0F));
        float x = f == 0 ? 0.0F : quaternion.i() / f;
        float y = f == 0 ? 0.0F : quaternion.j() / f;
        float z = f == 0 ? 0.0F : quaternion.k() / f;
        return Pair.of((float) deg, new Vector3f(x, y, z));
    }

    public static <K, V> V safeRet(Map<K, V> map, K key, V fallback) {
        V v = map.get(key);
        return v != null ? v : Objects.requireNonNull(fallback, "Fallback value cannot be null");
    }

    public static TreeMap<AnimationStage, IKeyframe[]> createSortedMap() {
        return new TreeMap<>(AnimationStage::compareTo);
    }
}
