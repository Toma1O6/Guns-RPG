package dev.toma.gunsrpg.util.recipes.smithing;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;

public final class ConditionType<C extends ICraftingCondition> {

    private final ResourceLocation id;
    private final IConditionSerializer<C> serializer;

    public ConditionType(ResourceLocation id, IConditionSerializer<C> serializer) {
        this.id = id;
        this.serializer = serializer;
    }

    public ResourceLocation getId() {
        return id;
    }

    public IConditionSerializer<C> getSerializer() {
        return serializer;
    }

    @SuppressWarnings("unchecked")
    public static <C extends ICraftingCondition> void toNetwork(PacketBuffer buffer, C condition) {
        ResourceLocation id = condition.getType().id;
        ConditionType<C> type = (ConditionType<C>) condition.getType();

        buffer.writeResourceLocation(id);
        type.serializer.toNetwork(buffer, condition);
    }

    public static <C extends ICraftingCondition> C fromNetwork(PacketBuffer buffer) {
        ResourceLocation id = buffer.readResourceLocation();
        ConditionType<C> type = Conditions.find(id);
        return type.serializer.fromNetwork(buffer);
    }
}
