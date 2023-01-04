package dev.toma.gunsrpg.integration.questing.reward.transformer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.toma.gunsrpg.common.init.QuestRegistry;
import dev.toma.gunsrpg.common.item.perk.Crystal;
import dev.toma.gunsrpg.common.item.perk.CrystalItem;
import dev.toma.gunsrpg.resource.crate.CountFunctionType;
import dev.toma.gunsrpg.resource.crate.ICountFunction;
import dev.toma.questing.common.component.reward.transformer.RewardTransformer;
import dev.toma.questing.common.component.reward.transformer.RewardTransformerType;
import dev.toma.questing.common.quest.instance.Quest;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class SetCrystalTransformer implements RewardTransformer<ItemStack> {

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
    public ItemStack adjust(ItemStack stack, PlayerEntity player, Quest quest) {
        int crystalLevel = this.levelFunction.getCount();
        int buffCount = this.buffFunction.getCount();
        int debuffCount = this.debuffFunction.getCount();
        Crystal crystal = Crystal.generate(crystalLevel, buffCount, debuffCount);
        CrystalItem.addCrystal(stack, crystal);
        return stack;
    }

    @Override
    public RewardTransformerType<?, ?> getType() {
        return QuestRegistry.SET_CRYSTAL_TRANSFORMER;
    }
}
