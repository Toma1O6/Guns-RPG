package dev.toma.gunsrpg.resource.crafting;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.toma.gunsrpg.api.common.attribute.IAttribute;
import dev.toma.gunsrpg.api.common.attribute.IAttributeId;
import dev.toma.gunsrpg.api.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.api.common.attribute.IModifierOp;
import dev.toma.gunsrpg.common.attribute.Attribs;
import dev.toma.gunsrpg.common.attribute.AttributeOps;
import dev.toma.questing.utils.Codecs;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

public final class OutputModifier {

    public static final Codec<OutputModifier> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.flatXmap(location -> {
                IAttributeId id = Attribs.find(location);
                return id == null ? DataResult.error("Unknown attribute " + location) : DataResult.success(id);
            }, attributeId -> attributeId == null ? DataResult.error("Attribute is null") : DataResult.success(attributeId.getId()))
                    .fieldOf("attribute").forGetter(t -> t.attributeId),
            ResourceLocation.CODEC.flatXmap(location -> {
                IModifierOp op = AttributeOps.find(location);
                return op == null ? DataResult.error("Unknown operation " + location) : DataResult.success(op);
            }, operation -> operation == null ? DataResult.error("Operation is null") : DataResult.success(operation.getId()))
                    .fieldOf("operation").forGetter(t -> t.operator)
    ).apply(instance, OutputModifier::new));

    private final IAttributeId attributeId;
    private final IModifierOp operator;

    public OutputModifier(IAttributeId attributeId, IModifierOp operator) {
        this.attributeId = attributeId;
        this.operator = operator;
    }

    public double getModifiedValue(IAttributeProvider provider, int in) {
        double f = provider.getAttributeValue(this.attributeId);
        return this.operator.combine(in, f);
    }

    public void applyRaw(ItemStack stack, IAttributeProvider provider) {
        IAttribute attribute = provider.getAttribute(attributeId);
        int count = stack.getCount();
        double attributeValue = attribute.value();
        int result = (int) Math.round(operator.combine(count, attributeValue));
        stack.setCount(result);
    }

    public void encode(PacketBuffer buffer) {
        buffer.writeResourceLocation(attributeId.getId());
        buffer.writeResourceLocation(operator.getId());
    }

    public static OutputModifier fromJson(JsonObject object) throws JsonParseException {
        ResourceLocation id = new ResourceLocation(JSONUtils.getAsString(object, "attribute"));
        ResourceLocation op = new ResourceLocation(JSONUtils.getAsString(object, "operation"));
        IAttributeId attributeId = Attribs.find(id);
        IModifierOp iModifierOp = AttributeOps.find(op);
        if (attributeId == null)
            throw new JsonSyntaxException("Unknown attribute: " + id);
        if (iModifierOp == null)
            throw new JsonSyntaxException("Unknown operation: " + op);
        return new OutputModifier(attributeId, iModifierOp);
    }

    public static OutputModifier decode(PacketBuffer buffer) {
        ResourceLocation id = buffer.readResourceLocation();
        ResourceLocation op = buffer.readResourceLocation();
        return new OutputModifier(Attribs.find(id), AttributeOps.find(op));
    }
}
