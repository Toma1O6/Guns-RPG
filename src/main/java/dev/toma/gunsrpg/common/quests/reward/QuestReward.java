package dev.toma.gunsrpg.common.quests.reward;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public final class QuestReward {

    private static final Random RANDOM = new Random();
    private final Choice[] choices;

    private QuestReward(Choice[] choices) {
        this.choices = choices;
    }

    public Choice[] getChoices() {
        return choices;
    }

    public static List<ItemStack> splitItemStack(ItemStack stack, int size) {
        List<ItemStack> list = new ArrayList<>();
        int left = stack.getCount();
        if (left <= size) {
            list.add(stack);
        } else {
            while (size > 0) {
                ItemStack copy = stack.copy();
                copy.setCount(Math.min(size, stack.getCount()));
                size -= copy.getCount();
                list.add(copy);
            }
        }
        return list;
    }

    public static QuestReward generate(QuestRewardList list, Options options) {
        return options.unique ? generateUnique(list, options) : generateRandomly(list, options);
    }

    private static QuestReward generateUnique(QuestRewardList list, Options options) {
        Supplier<List<IQuestItemProvider>> providerListSupplier = () -> {
            IQuestItemProvider[] providers = list.getItemProviders();
            List<IQuestItemProvider> providerList = new ArrayList<>(providers.length);
            providerList.addAll(Arrays.asList(providers));
            return providerList;
        };
        List<IQuestItemProvider> providers = providerListSupplier.get();
        Choice[] choiceArray = new Choice[options.choiceCount];
        for (int i = 0; i < options.choiceCount; i++) {
            IQuestItemProvider[] choiceProviders = new IQuestItemProvider[options.itemCount];
            for (int j = 0; j < options.itemCount; j++) {
                choiceProviders[j] = selectRandomUnique(providers, providerListSupplier);
            }
            choiceArray[i] = new Choice(choiceProviders);
        }
        return new QuestReward(choiceArray);
    }

    private static QuestReward generateRandomly(QuestRewardList list, Options options) {
        IQuestItemProvider[] listProviders = list.getItemProviders();
        Choice[] choices = new Choice[options.choiceCount];
        for (int i = 0; i < choices.length; i++) {
            IQuestItemProvider[] providers = new IQuestItemProvider[options.itemCount];
            for (int j = 0; j < options.itemCount; j++) {
                providers[j] = listProviders[RANDOM.nextInt(listProviders.length)];
            }
            choices[i] = new Choice(providers);
        }
        return new QuestReward(choices);
    }

    private static <T> T selectRandomUnique(List<T> list, Supplier<List<T>> supplier) {
        int index = RANDOM.nextInt(list.size());
        T t = list.get(index);
        list.remove(index);
        if (list.isEmpty()) {
            list.addAll(supplier.get());
        }
        return t;
    }

    public static class Choice {

        private final IQuestItemProvider[] providers;

        private Choice(IQuestItemProvider[] providers) {
            this.providers = providers;
        }

        public void distributeToInventory(PlayerEntity player) {
            List<ItemStack> stacks = new ArrayList<>();
            for (IQuestItemProvider provider : providers) {
                ItemStack raw = provider.assembleItem(player);
                stacks.addAll(splitItemStack(raw, 64));
            }
            stacks.forEach(player::addItem);
        }
    }

    public static class Options {

        private boolean unique;
        private int itemCount = 1;
        private int choiceCount = 1;

        public Options unique() {
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
