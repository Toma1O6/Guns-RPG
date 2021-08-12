package lib.toma.animations;

import lib.toma.animations.pipeline.AnimationStage;
import lib.toma.animations.pipeline.frame.IKeyframe;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class AnimationUtils {

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
