package dev.toma.gunsrpg.common.attribute;

import java.util.*;
import java.util.function.BiConsumer;

public class Attribute implements IAttribute {

    private final IAttributeId id;
    private final Map<UUID, IAttributeModifier> modifierMap = new HashMap<>();
    private final Set<ITickableModifier> temporaryModifiers = new HashSet<>();
    private final Set<IAttributeListener> listeners = new HashSet<>();
    private double baseValue;
    private boolean changed;
    private double value;
    private double modifier;

    public Attribute(IAttributeId id) {
        this.id = id;
        this.baseValue = id.getBaseValue();
        this.value = getBaseValue();
    }

    @Override
    public double getModifier() {
        return modifier;
    }

    @Override
    public double value() {
        if (changed) {
            changed = false;
            modifier = calcModifier();
            value = baseValue * modifier;
            notifyListenerChange(value, IAttributeListener::onValueChanged);
        }
        return value;
    }

    @Override
    public int intValue() {
        return (int) Math.round(value());
    }

    @Override
    public float floatValue() {
        return (float) value();
    }

    @Override
    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public double getBaseValue() {
        return baseValue;
    }

    @Override
    public void setBaseValue(double value) {
        this.baseValue = value;
    }

    @Override
    public void markChanged() {
        changed = true;
    }

    @Override
    public void tickAttributes() {
        Iterator<ITickableModifier> iterator = temporaryModifiers.iterator();
        while (iterator.hasNext()) {
            ITickableModifier modifier = iterator.next();
            if (modifier.shouldRemove()) {
                notifyListenerChange(modifier, IAttributeListener::onModifierRemoved);
                iterator.remove();
                markChanged();
                continue;
            }
            modifier.tick();
        }
    }

    @Override
    public void addModifier(IModifierProvider provider) {
        IAttributeModifier modifier = provider.getModifier();
        IAttributeModifier oldModifier = modifierMap.put(modifier.getUid(), modifier);
        if (oldModifier instanceof ITickableModifier) {
            temporaryModifiers.remove(oldModifier);
        }
        if (modifier instanceof ITickableModifier) {
            temporaryModifiers.add((ITickableModifier) modifier);
        }
        notifyListenerChange(modifier, IAttributeListener::onModifierAdded);
        markChanged();
    }

    @Override
    public void removeModifier(IModifierProvider provider) {
        removeModifierById(provider.getModifier().getUid());
    }

    @Override
    public void removeModifierById(UUID modifierId) {
        IAttributeModifier modifier = modifierMap.remove(modifierId);
        if (modifier instanceof ITickableModifier) {
            temporaryModifiers.remove(modifier);
        }
        markChanged();
    }

    @Override
    public void removeAllModifiers() {
        modifierMap.clear();
        markChanged();
    }

    @Override
    public IAttributeModifier getModifier(IModifierProvider provider) {
        return modifierMap.get(provider.getModifier().getUid());
    }

    @Override
    public Collection<IAttributeModifier> listModifiers() {
        List<IAttributeModifier> list = new ArrayList<>(modifierMap.values());
        list.sort(Comparator.comparingInt(mod -> mod.getOperation().getPriority()));
        return list;
    }

    @Override
    public void addAttributeListener(IAttributeListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(IAttributeListener listener) {
        listeners.remove(listener);
    }

    @Override
    public IAttributeId getId() {
        return id;
    }

    private <P> void notifyListenerChange(P parameter, BiConsumer<IAttributeListener, P> notifyEvent) {
        listeners.forEach(listener -> notifyEvent.accept(listener, parameter));
    }

    private double calcModifier() {
        Collection<IAttributeModifier> modifiers = listModifiers();
        double value = 1.0;
        for (IAttributeModifier modifier : modifiers) {
            IModifierOp op = modifier.getOperation();
            double modValue = modifier.getModifierValue();
            value = op.combine(value, modValue);
        }
        return value;
    }
}
