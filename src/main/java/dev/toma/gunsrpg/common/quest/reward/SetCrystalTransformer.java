package dev.toma.gunsrpg.common.quest.reward;

import com.google.gson.JsonObject;
import dev.toma.gunsrpg.common.item.perk.Crystal;
import dev.toma.gunsrpg.common.item.perk.CrystalItem;
import dev.toma.gunsrpg.resource.crate.CountFunctionRegistry;
import dev.toma.gunsrpg.resource.crate.ICountFunction;
import dev.toma.gunsrpg.resource.util.functions.IFunction;
import dev.toma.questing.quest.Quest;
import dev.toma.questing.reward.AbstractItemReward;
import dev.toma.questing.reward.RewardTransformer;
import dev.toma.questing.reward.RewardTransformerType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JSONUtils;

public class SetCrystalTransformer implements RewardTransformer<AbstractItemReward.ItemList> {

    private final ICountFunction levelFunction;
    private final ICountFunction buffFunction;
    private final ICountFunction debuffFunction;

    public SetCrystalTransformer(ICountFunction levelFunction, ICountFunction buffFunction, ICountFunction debuffFunction) {
        this.levelFunction = levelFunction;
        this.buffFunction = buffFunction;
        this.debuffFunction = debuffFunction;
    }

    @Override
    public AbstractItemReward.ItemList adjust(AbstractItemReward.ItemList originalValue, PlayerEntity player, Quest quest) {
        for (ItemStack stack : originalValue) {
            int crystalLevel = this.levelFunction.getCount();
            int buffCount = this.buffFunction.getCount();
            int debuffCount = this.debuffFunction.getCount();
            Crystal crystal = Crystal.generate(crystalLevel, buffCount, debuffCount);
            CrystalItem.addCrystal(stack, crystal);
        }
        return originalValue;
    }

    public static final class Serializer implements RewardTransformerType.Serializer<AbstractItemReward.ItemList, SetCrystalTransformer> {

        @Override
        public SetCrystalTransformer transformerFromJson(JsonObject data) {
            JsonObject crystal = JSONUtils.getAsJsonObject(data, "crystal");
            IFunction positive = i -> i >= 0;
            ICountFunction level = CountFunctionRegistry.fromJson(JSONUtils.getAsJsonObject(crystal, "level"), positive);
            ICountFunction buff = CountFunctionRegistry.fromJson(JSONUtils.getAsJsonObject(crystal, "buffs"), positive);
            ICountFunction debuff = CountFunctionRegistry.fromJson(JSONUtils.getAsJsonObject(crystal, "debuffs"), positive);
            return new SetCrystalTransformer(level, buff, debuff);
        }
    }
}
