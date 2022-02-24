package dev.toma.gunsrpg.common.attribute;

import dev.toma.gunsrpg.common.attribute.serialization.IModifierSerializer;
import dev.toma.gunsrpg.common.attribute.serialization.ModifierSerialization;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class Attribute implements IAttribute {

    private final IAttributeId id;
    private final Map<UUID, IAttributeModifier> modifierMap = new HashMap<>();
    private final Set<ITickableModifier> temporaryModifiers = new HashSet<>();
    private final Set<IAttributeListener> listeners = new HashSet<>();
    private final double baseValue;
    private boolean requireUpdate;
    private double value;

    public Attribute(IAttributeId id) {
        this.id = id;
        this.baseValue = id.getBaseValue();
        this.value = getBaseValue();
        this.requireUpdate = true;
    }

    @Override
    public double getModifiedValue(double value) {
        Comparator<IAttributeModifier> executionOrderSorter = Comparator.comparing(IAttributeModifier::getOperation, Comparator.comparingInt(IModifierOp::getPriority));
        List<IAttributeModifier> sumTargets = getModifiersByType(OperationTarget.DIRECT_VALUE, executionOrderSorter);
        for (IAttributeModifier modifier : sumTargets) {
            double modifierValue = modifier.getModifierValue();
            IModifierOp operation = modifier.getOperation();
            value = operation.combine(value, modifierValue);
        }
        List<IAttributeModifier> mulTargets = getModifiersByType(OperationTarget.MULTIPLIER, executionOrderSorter);
        double multiplier = 1.0;
        for (IAttributeModifier modifier : mulTargets) {
            double modifierValue = modifier.getModifierValue();
            IModifierOp operation = modifier.getOperation();
            multiplier = operation.combine(multiplier, modifierValue);
        }
        return value * multiplier;
    }

    @Override
    public double value() {
        if (requireUpdate) {
            requireUpdate = false;
            this.value = getModifiedValue(baseValue);
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
    public void markChanged() {
        requireUpdate = true;
    }

    @Override
    public void tickAttributes() {
        Iterator<ITickableModifier> iterator = temporaryModifiers.iterator();
        while (iterator.hasNext()) {
            ITickableModifier modifier = iterator.next();
            if (modifier.shouldRemove()) {
                iterator.remove();
                removeModifier(modifier);
                notifyListenerChange(modifier, IAttributeListener::onModifierRemoved);
                markChanged();
                continue;
            }
            modifier.tick();
        }
    }

    @Override
    public void addModifier(IAttributeModifier modifier) {
        IAttributeModifier instance = modifier.instance();
        IAttributeModifier oldModifier = modifierMap.put(modifier.getUid(), instance);
        if (oldModifier instanceof ITickableModifier) {
            temporaryModifiers.remove(oldModifier);
        }
        if (instance instanceof ITickableModifier) {
            temporaryModifiers.add((ITickableModifier) instance);
        }
        notifyListenerChange(instance, IAttributeListener::onModifierAdded);
        markChanged();
    }

    @Override
    public void removeModifier(IAttributeModifier modifier) {
        removeModifierById(modifier.getUid());
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

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        ListNBT modifiers = new ListNBT();
        for (IAttributeModifier modifier : modifierMap.values()) {
            modifiers.add(this.serializeModifier(modifier));
        }
        nbt.put("modifiers", modifiers);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        modifierMap.clear();
        temporaryModifiers.clear();
        ListNBT modifiers = nbt.getList("modifiers", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < modifiers.size(); i++) {
            CompoundNBT data = modifiers.getCompound(i);
            addModifier(this.deserializeModifier(data));
        }
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

    @SuppressWarnings("unchecked")
    private <M extends IAttributeModifier> CompoundNBT serializeModifier(M modifier) {
        IModifierSerializer<M> serializer = (IModifierSerializer<M>) modifier.getSerializer();
        CompoundNBT data = new CompoundNBT();
        data.putString("serializer", serializer.getSerializerUid().toString());
        serializer.toNbtStructure(modifier, data);
        return data;
    }

    private <M extends IAttributeModifier> M deserializeModifier(CompoundNBT data) {
        ResourceLocation type = new ResourceLocation(data.getString("serializer"));
        IModifierSerializer<M> serializer = ModifierSerialization.getSerializer(type);
        return serializer.fromNbtStructure(data);
    }

    private List<IAttributeModifier> getModifiersByType(OperationTarget target, Comparator<IAttributeModifier> sorter) {
        return modifierMap.values().stream().filter(modifier -> modifier.getOperation().getOperationTarget() == target).sorted(sorter).collect(Collectors.toList());
    }
}
