package dev.toma.gunsrpg.integration.questing.reward.instance;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.toma.gunsrpg.api.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.api.common.data.ISkillProvider;
import dev.toma.gunsrpg.common.attribute.Attribs;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.skills.BartenderSkill;
import dev.toma.gunsrpg.integration.questing.reward.provider.GunsrpgChoiceRewardProvider;
import dev.toma.gunsrpg.util.SkillUtil;
import dev.toma.questing.common.component.reward.RewardType;
import dev.toma.questing.common.component.reward.instance.Reward;
import dev.toma.questing.common.component.reward.instance.RewardHolder;
import dev.toma.questing.common.component.reward.instance.SelectableReward;
import dev.toma.questing.common.quest.instance.Quest;
import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GunsrpgChoiceReward implements Reward, SelectableReward {

    public static final Codec<GunsrpgChoiceReward> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            GunsrpgChoiceRewardProvider.CODEC.fieldOf("provider").forGetter(t -> t.provider),
            Codec.INT.listOf().xmap(list -> (Set<Integer>) new IntArraySet(list), ArrayList::new).fieldOf("choices").forGetter(t -> t.choices),
            RewardType.REWARD_CODEC.listOf().fieldOf("rewardList").forGetter(t -> t.rewardList)
    ).apply(instance, GunsrpgChoiceReward::new));

    private final GunsrpgChoiceRewardProvider provider;
    private final IntSet choices;
    private final List<Reward> rewardList;

    public GunsrpgChoiceReward(GunsrpgChoiceRewardProvider provider, PlayerEntity player, Quest quest) {
        this.provider = provider;
        this.rewardList = this.getRewards(player, quest);
        this.choices = new IntArraySet();
    }

    public GunsrpgChoiceReward(GunsrpgChoiceRewardProvider provider, Set<Integer> selection, List<Reward> rewards) {
        this.provider = provider;
        this.choices = (IntSet) selection;
        this.rewardList = rewards;
    }

    @Override
    public void award(PlayerEntity player, Quest quest) {
        for (int i : choices) {
            this.rewardList.get(i).award(player, quest);
        }
    }

    @Override
    public List<Reward> getRewards() {
        return this.rewardList;
    }

    @Override
    public void select(int index, PlayerEntity player, Quest quest) {
        int limit = this.getRewardSelectionLimit(player, quest);
        if (limit < 0 || this.choices.size() < limit) {
            this.choices.add(index);
        }
    }

    @Override
    public void deselect(int index, PlayerEntity player, Quest quest) {
        this.choices.remove(index);
    }

    @Override
    public boolean isSelected(int index) {
        return this.choices.contains(index);
    }

    @Override
    public GunsrpgChoiceRewardProvider getProvider() {
        return this.provider;
    }

    protected List<Reward> getRewards(PlayerEntity player, Quest quest) {
        List<Reward> rewards = this.unwrap(this.provider.getRewardList().stream().map(p -> p.createReward(player, quest)).collect(Collectors.toList()));
        int choiceLimit = this.getChoiceGenerationLimit(player, quest);
        if (choiceLimit > 0 && rewards.size() > choiceLimit) {
            return rewards.subList(0, choiceLimit);
        }
        return rewards;
    }

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

    protected int getChoiceGenerationLimit(PlayerEntity player, Quest quest) {
        return PlayerData.get(player).map(data -> {
            IAttributeProvider provider = data.getAttributes();
            return provider.getAttribute(Attribs.QUEST_VISIBLE_REWARD).intValue();
        }).orElse((int) Attribs.QUEST_VISIBLE_REWARD.getBaseValue());
    }

    protected List<Reward> unwrap(List<Reward> in) {
        List<Reward> results = new ArrayList<>();
        for (Reward reward : in) {
            if (reward instanceof RewardHolder) {
                List<Reward> unwrapped = ((RewardHolder) reward).getRewards();
                results.addAll(this.unwrap(unwrapped));
            } else {
                results.add(reward);
            }
        }
        return results;
    }
}
