package dev.toma.gunsrpg.resource.crafting;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import dev.toma.gunsrpg.api.common.attribute.IAttribute;
import dev.toma.gunsrpg.api.common.attribute.IAttributeId;
import dev.toma.gunsrpg.api.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.api.common.attribute.IModifierOp;
import dev.toma.gunsrpg.common.attribute.Attribs;
import dev.toma.gunsrpg.common.attribute.AttributeOps;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

public final class OutputModifier {

    private final IAttributeId attributeId;
    private final IModifierOp operator;

    public OutputModifier(IAttributeId attributeId, IModifierOp operator) {
        this.attributeId = attributeId;
        this.operator = operator;
    }

    public ItemStack[] applyAndSplit(ItemStack stack, IAttributeProvider provider) {
        IAttribute attribute = provider.getAttribute(attributeId);
        int count = stack.getCount();
        double attributeValue = attribute.value();
        int result = (int) Math.round(operator.combine(count, attributeValue));
        int remain = result % 64 > 0 ? 1 : 0;
        ItemStack[] results = new ItemStack[result / 64 + remain];
        int index = 0;
        while (result > 0) {
            ItemStack item = stack.copy();
            int amount = Math.min(64, result);
            item.setCount(amount);
            result -= amount;
            results[index++] = item;
        }
        return results;
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
