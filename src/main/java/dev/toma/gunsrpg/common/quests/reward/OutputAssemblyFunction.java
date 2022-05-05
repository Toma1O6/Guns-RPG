package dev.toma.gunsrpg.common.quests.reward;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import dev.toma.gunsrpg.api.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.resource.crafting.OutputModifier;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class OutputAssemblyFunction implements IAssemblyFunction {

    private final OutputModifier modifier;

    public OutputAssemblyFunction(OutputModifier modifier) {
        this.modifier = modifier;
    }

    @Override
    public ItemStack[] onAssembly(ItemStack stack, PlayerEntity player) {
        IAttributeProvider provider = PlayerData.getUnsafe(player).getAttributes();
        return modifier.applyAndSplit(stack, provider);
    }

    public static final class Serializer implements IAssemblyFunctionSerializer {

        @Override
        public IAssemblyFunction deserialize(JsonElement element, JsonDeserializationContext context) throws JsonParseException {
            return new OutputAssemblyFunction(OutputModifier.fromJson(JsonHelper.asJsonObject(element)));
        }
    }
}
