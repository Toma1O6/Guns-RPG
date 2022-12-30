package dev.toma.gunsrpg.integration.questing.reward;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.toma.gunsrpg.api.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.api.common.data.ISkillProvider;
import dev.toma.gunsrpg.common.attribute.Attribs;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.init.QuestRegistry;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.skills.BartenderSkill;
import dev.toma.gunsrpg.util.SkillUtil;
import dev.toma.questing.common.quest.Quest;
import dev.toma.questing.common.reward.ChoiceReward;
import dev.toma.questing.common.reward.Reward;
import dev.toma.questing.common.reward.RewardType;
import dev.toma.questing.utils.Utils;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Collections;
import java.util.List;

public class GunsrpgChoiceReward extends ChoiceReward {

    public static final Codec<GunsrpgChoiceReward> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            RewardType.CODEC.listOf().fieldOf("choices").forGetter(ChoiceReward::getRewards),
            RewardType.CODEC.listOf().optionalFieldOf("generated", Collections.emptyList()).forGetter(ChoiceReward::getRewards)
    ).apply(instance, GunsrpgChoiceReward::new));

    public GunsrpgChoiceReward(List<Reward> rewardList) {
        super(rewardList, 0);
    }

    public GunsrpgChoiceReward(List<Reward> rewardList, List<Reward> generatedChoices) {
        super(rewardList, 0, generatedChoices);
    }

    @Override
    public RewardType<?> getType() {
        return QuestRegistry.CHOICE_REWARD;
    }

    @Override
    public Reward copy() {
        return new GunsrpgChoiceReward(Utils.instantiate(this.rewardList, Reward::copy));
    }

    @Override
    protected int getChoiceGenerationLimit(PlayerEntity player, Quest quest) {
        return PlayerData.get(player).map(data -> {
            IAttributeProvider provider = data.getAttributes();
            return provider.getAttribute(Attribs.QUEST_VISIBLE_REWARD).intValue();
        }).orElse((int) Attribs.QUEST_VISIBLE_REWARD.getBaseValue());
    }

    @Override
    protected int getRewardSelectionLimit(PlayerEntity player, Quest quest) {
        return PlayerData.get(player).map(data -> {
            ISkillProvider provider = data.getSkillProvider();
            BartenderSkill skill = SkillUtil.getTopHierarchySkill(Skills.BARTENDER_I, provider);
            if (skill != null) {
                return skill.getRewardCount();
            }
            return 1;
        }).orElse(1);
    }
}
