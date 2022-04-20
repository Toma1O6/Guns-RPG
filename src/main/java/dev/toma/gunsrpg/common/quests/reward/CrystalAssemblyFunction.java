package dev.toma.gunsrpg.common.quests.reward;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import dev.toma.gunsrpg.common.item.perk.Crystal;
import dev.toma.gunsrpg.common.item.perk.CrystalItem;
import dev.toma.gunsrpg.resource.crate.ICountFunction;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JSONUtils;

public class CrystalAssemblyFunction implements IAssemblyFunction {

    private final ICountFunction levelFunction;
    private final ICountFunction buffFunction;
    private final ICountFunction debuffFunction;

    public CrystalAssemblyFunction(ICountFunction levelFunction, ICountFunction buffFunction, ICountFunction debuffFunction) {
        this.levelFunction = levelFunction;
        this.buffFunction = buffFunction;
        this.debuffFunction = debuffFunction;
    }

    @Override
    public void onAssembly(ItemStack stack, PlayerEntity player) {
        int crystalLevel = levelFunction.getCount();
        int buffCount = buffFunction.getCount();
        int debuffCount = debuffFunction.getCount();
        Crystal crystal = Crystal.generate(crystalLevel, buffCount, debuffCount);
        CrystalItem.addCrystal(stack, crystal);
    }

    public static class Serializer implements IAssemblyFunctionSerializer {

        @Override
        public IAssemblyFunction deserialize(JsonElement element, JsonDeserializationContext context) throws JsonParseException {
            JsonObject object = JsonHelper.asJsonObject(element);
            JsonObject crystal = JSONUtils.getAsJsonObject(object, "crystal");
            ICountFunction level = context.deserialize(JSONUtils.getAsJsonObject(crystal, "level"), ICountFunction.class);
            ICountFunction buffs = context.deserialize(JSONUtils.getAsJsonObject(crystal, "buffs"), ICountFunction.class);
            ICountFunction debuffs = context.deserialize(JSONUtils.getAsJsonObject(crystal, "debuffs"), ICountFunction.class);
            return new CrystalAssemblyFunction(level, buffs, debuffs);
        }
    }
}
