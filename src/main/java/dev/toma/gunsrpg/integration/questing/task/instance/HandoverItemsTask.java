package dev.toma.gunsrpg.integration.questing.task.instance;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.toma.gunsrpg.integration.questing.task.provider.HandoverItemsTaskProvider;
import dev.toma.gunsrpg.integration.questing.trigger.GunsrpgTriggers;
import dev.toma.questing.common.component.condition.ConditionType;
import dev.toma.questing.common.component.condition.instance.Condition;
import dev.toma.questing.common.component.task.instance.AbstractTask;
import dev.toma.questing.common.component.trigger.ResponseType;
import dev.toma.questing.common.quest.ProgressStatus;
import dev.toma.questing.common.quest.TaskRegisterHandler;
import dev.toma.questing.common.quest.instance.Quest;
import dev.toma.questing.utils.Codecs;
import dev.toma.questing.utils.Utils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class HandoverItemsTask extends AbstractTask {

    public static final Codec<HandoverItemsTask> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            HandoverItemsTaskProvider.CODEC.fieldOf("provider").forGetter(t -> t.provider),
            Codecs.enumCodec(ProgressStatus.class, String::toUpperCase).fieldOf("status").forGetter(AbstractTask::getStatus),
            ConditionType.CONDITION_CODEC.listOf().fieldOf("conditions").forGetter(AbstractTask::getTaskConditions),
            Codecs.SIMPLIFIED_ITEMSTACK.listOf().fieldOf("items").forGetter(t -> t.remainingItems)
    ).apply(instance, HandoverItemsTask::new));
    private final HandoverItemsTaskProvider provider;
    private final List<ItemStack> remainingItems;

    private HandoverItemsTask(HandoverItemsTaskProvider provider, ProgressStatus status, List<Condition> conditions, List<ItemStack> items) {
        super(status, conditions);
        this.provider = provider;
        this.remainingItems = items;
    }

    public HandoverItemsTask(HandoverItemsTaskProvider provider, Quest quest) {
        this(provider, ProgressStatus.ACTIVE, provider.getConditions().stream().map(c -> c.createCondition(quest)).collect(Collectors.toList()), provider.getRequiredItems());
    }

    @Override
    public void registerTriggerHandlers(TaskRegisterHandler taskRegisterHandler) {
        taskRegisterHandler.registerTask(GunsrpgTriggers.HANDOVER_ITEMS, (data, level, quest) -> {
            PlayerEntity player = data.getPlayer();
            if (!Utils.checkIfEntityIsPartyMember(player, quest.getParty())) {
                return ResponseType.SKIP;
            }
            ItemStack stack = data.getStack();
            for (ItemStack itemStack : this.remainingItems) {
                if (ItemStack.isSameIgnoreDurability(stack, itemStack)) {
                    return ResponseType.OK;
                }
            }
            return this.getProvider().getDefaultResponse();
        }, (data, level, quest) -> {
            ItemStack stack = data.getStack();
            Iterator<ItemStack> iterator = this.remainingItems.iterator();
            while (iterator.hasNext()) {
                ItemStack itemStack = iterator.next();
                int stackSize = stack.getCount();
                if (ItemStack.isSameIgnoreDurability(stack, itemStack)) {
                    int itemSize = itemStack.getCount();
                    int take = Math.min(stackSize, itemSize);
                    itemStack.shrink(take);
                    stack.shrink(take);
                    if (itemStack.isEmpty()) {
                        iterator.remove();
                    }
                }
            }
        });
    }

    @Override
    public HandoverItemsTaskProvider getProvider() {
        return provider;
    }
}
