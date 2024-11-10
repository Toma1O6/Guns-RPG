package dev.toma.gunsrpg.common.quests.reward;

import com.google.common.collect.Queues;
import dev.toma.gunsrpg.util.ModUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.Constants;

import java.util.*;

public final class QuestReward {

    private final Choice[] choices;

    private QuestReward(Choice[] choices) {
        this.choices = choices;
    }

    public QuestReward(CompoundNBT nbt) {
        ListNBT choices = nbt.getList("choices", Constants.NBT.TAG_LIST);
        this.choices = choices.stream().map(inbt -> {
            ListNBT listNBT = (ListNBT) inbt;
            ItemStack[] items = listNBT.stream().map(itemStackNbt -> {
                CompoundNBT compoundNBT = (CompoundNBT) itemStackNbt;
                return ItemStack.of(compoundNBT);
            }).toArray(ItemStack[]::new);
            return new Choice(items);
        }).toArray(Choice[]::new);
    }

    public Choice[] getChoices() {
        return choices;
    }

    public static QuestReward generate(QuestRewardList list, Options options, PlayerEntity player) {
        int totalProviderCount = list.size();
        Collection<IQuestItemProvider> generatedCollection = options.unique ? new HashSet<>() : new ArrayList<>();
        int requiredCount = Math.min(totalProviderCount, options.choiceCount * options.itemCount);
        int attemptIndex = 0;
        while (generatedCollection.size() < requiredCount && attemptIndex < totalProviderCount * 2) {
            IQuestItemProvider provider = list.getRandomProvider();
            generatedCollection.add(provider);
            ++attemptIndex;
        }
        int providerCount = generatedCollection.size();
        int countPerChoice = options.itemCount;
        int remainder = providerCount % countPerChoice;
        int actualPossibleChoiceCount = providerCount / countPerChoice;
        if (remainder > 0) {
            ++actualPossibleChoiceCount;
        }
        Deque<IQuestItemProvider> providerPool = Queues.newArrayDeque(generatedCollection);
        Choice[] choices = new Choice[actualPossibleChoiceCount];
        for (int i = 0; i < actualPossibleChoiceCount; i++) {
            IQuestItemProvider[] providers = new IQuestItemProvider[Math.min(countPerChoice, providerPool.size())];
            for (int j = 0; j < providers.length; j++) {
                providers[j] = providerPool.poll();
            }
            Choice choice = Choice.generateItems(providers, player);
            choices[i] = choice;
        }
        return new QuestReward(choices);
    }

    public CompoundNBT toNbt() {
        CompoundNBT nbt = new CompoundNBT();
        ListNBT list = new ListNBT();
        for (Choice choice : choices) {
            list.add(choice.toNbt());
        }
        nbt.put("choices", list);
        return nbt;
    }

    public static class Choice {

        private final ItemStack[] items;

        private Choice(ItemStack[] items) {
            this.items = items;
        }

        public static Choice generateItems(IQuestItemProvider[] providers, PlayerEntity player) {
            List<ItemStack> list = new ArrayList<>();
            for (IQuestItemProvider provider : providers) {
                ItemStack[] processedItems = provider.assembleItem(player);
                list.addAll(Arrays.asList(processedItems));
            }
            return new Choice(list.toArray(new ItemStack[0]));
        }

        public ItemStack[] getContents() {
            return items;
        }

        public void distributeToInventory(PlayerEntity player) {
            for (ItemStack stack : items) {
                ModUtils.addItem(player, stack);
            }
        }

        public ListNBT toNbt() {
            ListNBT list = new ListNBT();
            for (ItemStack stack : items) {
                list.add(stack.serializeNBT());
            }
            return list;
        }
    }

    public static class Options {

        private boolean unique;
        private int itemCount = 1;
        private int choiceCount = 1;

        public Options setUnique() {
            this.unique = true;
            return this;
        }

        public Options items(int items) {
            this.itemCount = items;
            return this;
        }

        public Options choiceCount(int choiceCount) {
            this.choiceCount = choiceCount;
            return this;
        }
    }
}
