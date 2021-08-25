package lib.toma.animations.api.lifecycle;

import net.minecraft.util.ResourceLocation;

import java.util.Collection;

/**
 * Registry containing mappings {@link ResourceLocation} -> {@link T}
 * @param <T> Type of object stored in this registry
 */
public interface IRegistry<T extends IRegistryEntry> {

    /**
     * Used to retrieve element stored under specific key
     * @param key Object key
     * @return Element stored under specified key or {@code null} when no such element exists
     */
    T getElement(ResourceLocation key);

    /**
     * Adds registry callback which is then used to retrieve all entries and register then when needed.
     * Registration is run when {@link lib.toma.animations.AnimationEngine#startEngine(boolean)} is called,
     * so <b> you must register your callbacks before animation engine start. </b>
     *
     * @param listener Registration callback
     */
    void addCallback(IRegistrationListener<T> listener);

    /**
     * Retrieves all existing keys in this registry
     * @return All existing keys in this registry
     */
    Collection<ResourceLocation> keys();

    /**
     * Retrieves all values contained in this registry
     * @return All values contained in this registry
     */
    Collection<T> values();
}
