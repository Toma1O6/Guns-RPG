package lib.toma.animations.api.lifecycle;

import net.minecraft.util.ResourceLocation;

/**
 * Common API for all registry entries, makes sure all entries have their own key
 */
public interface IRegistryEntry {

    /**
     * Returns key assigned to this registry entry
     * @return Key assigned to this registry entry
     */
    ResourceLocation getKey();
}
