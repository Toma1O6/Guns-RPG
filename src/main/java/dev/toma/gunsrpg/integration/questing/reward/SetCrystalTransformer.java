package dev.toma.gunsrpg.integration.questing.reward;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.toma.gunsrpg.common.init.QuestRegistry;
import dev.toma.gunsrpg.common.item.perk.Crystal;
import dev.toma.gunsrpg.common.item.perk.CrystalItem;
import dev.toma.gunsrpg.resource.crate.CountFunctionType;
import dev.toma.gunsrpg.resource.crate.ICountFunction;
import dev.toma.questing.quest.Quest;
import dev.toma.questing.reward.AbstractItemReward;
import dev.toma.questing.reward.RewardTransformer;
import dev.toma.questing.reward.RewardTransformerType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class SetCrystalTransformer implements RewardTransformer<AbstractItemReward.ItemList> {

    public static final Codec<SetCrystalTransformer> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            CountFunctionType.CODEC.fieldOf("crystal").forGetter(t -> t.levelFunction),
            CountFunctionType.CODEC.fieldOf("buffs").forGetter(t -> t.buffFunction),
            CountFunctionType.CODEC.fieldOf("debuffs").forGetter(t -> t.debuffFunction)
    ).apply(instance, SetCrystalTransformer::new));
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

    @Override
    public RewardTransformerType<?, ?> getType() {
        return QuestRegistry.SET_CRYSTAL_TRANSFORMER;
    }
}
