package dev.toma.gunsrpg.common.attribute;

import dev.toma.gunsrpg.common.attribute.serialization.IModifierSerializer;
import dev.toma.gunsrpg.common.attribute.serialization.ModifierSerialization;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;

import java.util.*;
import java.util.function.BiConsumer;

public class Attribute implements IAttribute {

    private final IAttributeId id;
    private final Map<UUID, IAttributeModifier> modifierMap = new HashMap<>();
    private final Set<ITickableModifier> temporaryModifiers = new HashSet<>();
    private final Set<IAttributeListener> listeners = new HashSet<>();
    private final double baseValue;
    private boolean requireUpdate;
    private double value;
    private double modifier;

    public Attribute(IAttributeId id) {
        this.id = id;
        this.baseValue = id.getBaseValue();
        this.value = getBaseValue();
        this.requireUpdate = true;
    }

    @Override
    public double getModifier() {
        return (modifier = calcModifier());
    }

    @Override
    public double value() {
        if (requireUpdate) {
            requireUpdate = false;
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
    public void markChanged() {
        requireUpdate = true;
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
    public void addModifier(IAttributeModifier modifier) {
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
}
