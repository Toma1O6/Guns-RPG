package lib.toma.animations.api;

import com.google.gson.stream.JsonReader;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

/**
 * Animation loader is responsible for serialization, deserialization and management of {@link IKeyframeProvider}s.
 *
 * @author Toma
 */
public interface IAnimationLoader {

    /**
     * Returns {@link IKeyframeProvider} which is under 'path' key.
     *
     * @param path Keyframe provider path
     * @return Loaded provider or empty provider when no such provider exists
     */
    IKeyframeProvider getProvider(ResourceLocation path);

    /**
     * Get Keyframe provider loader reference
     * @return Keyframe provider loader
     */
    ILoader getResourceReader();

    /**
     * Serializes provided keyframe provider to compact string (all whitespaces are removed)
     * @param provider Provider to serialize
     * @return Compact string in JSON structure
     */
    String serializeResource(IKeyframeProvider provider);

    /**
     * Adds loading listener. Provided listener is called every time
     * mod animations are reloaded
     * @param listener Load listener
     */
    void addLoadingListener(ILoadingFinished listener);

    /**
     * Animation load callback
     */
    @FunctionalInterface
    interface ILoadingFinished {

        /**
         * Animation load callback
         * @param providerMap Key-Provider mappings loaded from asset directory
         * @param resourceLoader Serializer
         */
        void onLoaded(Map<ResourceLocation, IKeyframeProvider> providerMap, ILoader resourceLoader);
    }

    /**
     * Keyframe provider serializer
     */
    @FunctionalInterface
    interface ILoader {

        /**
         * Tries to parse input from supplied reader into new keyframe provider instance
         * @param reader Json reader
         * @return Parsed keyframe provider
         */
        IKeyframeProvider loadResource(JsonReader reader);
    }
}
