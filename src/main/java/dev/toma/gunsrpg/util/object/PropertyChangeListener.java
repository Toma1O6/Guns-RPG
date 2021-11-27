package dev.toma.gunsrpg.util.object;

public final class PropertyChangeListener<T, C> {

    private final IPropertyGetter<T, C> propertyGetter;
    private final IChangeCallback<T, C> callback;
    private final IPropertyComparator<T> comparator;
    private T lastPropState;

    public PropertyChangeListener(IPropertyGetter<T, C> propertyGetter, IChangeCallback<T, C> callback) {
        this(propertyGetter, callback, PropertyChangeListener::defaultCompare);
    }

    public PropertyChangeListener(IPropertyGetter<T, C> propertyGetter, IChangeCallback<T, C> callback, IPropertyComparator<T> comparator) {
        this.propertyGetter = propertyGetter;
        this.callback = callback;
        this.comparator = comparator;
    }

    public void refresh(C context) {
        T prop = propertyGetter.getProperty(context);
        if (!comparator.isSame(lastPropState, prop)) {
            lastPropState = prop;
            callback.onPropertyChanged(prop, context);
        }
    }

    public void scheduleUpdate() {
        lastPropState = null;
    }

    private static <T> boolean defaultCompare(T p1, T p2) {
        boolean nonnull = p1 != null && p2 != null;
        return nonnull && p1.equals(p2);
    }

    @FunctionalInterface
    public interface IPropertyGetter<T, C> {
        T getProperty(C context);
    }

    @FunctionalInterface
    public interface IChangeCallback<T, C> {
        void onPropertyChanged(T value, C context);
    }

    @FunctionalInterface
    public interface IPropertyComparator<T> {
        boolean isSame(T prop1, T prop2);
    }
}
