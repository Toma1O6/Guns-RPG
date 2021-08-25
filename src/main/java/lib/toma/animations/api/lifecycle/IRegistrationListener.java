package lib.toma.animations.api.lifecycle;

/**
 * Interface responsible for "collecting" registry entries
 * for registration. Gets around issues related to class loading.
 *
 * @param <T> Object type
 */
@FunctionalInterface
public interface IRegistrationListener<T extends IRegistryEntry> {

    /**
     * Retrieves array of all entries for registration
     * @return Array of all entries for registration
     */
    T[] getAll();
}
