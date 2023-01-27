package dev.toma.gunsrpg.integration.questing.task.provider;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.toma.gunsrpg.common.init.QuestRegistry;
import dev.toma.gunsrpg.integration.questing.task.instance.HandoverItemsTask;
import dev.toma.questing.common.component.condition.ConditionType;
import dev.toma.questing.common.component.condition.provider.ConditionProvider;
import dev.toma.questing.common.component.selector.Selector;
import dev.toma.questing.common.component.selector.SelectorType;
import dev.toma.questing.common.component.task.TaskType;
import dev.toma.questing.common.component.task.provider.AbstractTaskProvider;
import dev.toma.questing.common.component.trigger.ResponseType;
import dev.toma.questing.common.quest.instance.Quest;
import dev.toma.questing.utils.Codecs;
import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.List;

public class HandoverItemsTaskProvider extends AbstractTaskProvider<HandoverItemsTask> {

    public static final Codec<HandoverItemsTaskProvider> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codecs.enumCodecComap(ResponseType.class, ResponseType::fromString, Enum::name, String::toUpperCase).optionalFieldOf("onFail", ResponseType.PASS).forGetter(AbstractTaskProvider::getDefaultResponse),
            ConditionType.PROVIDER_CODEC.listOf().optionalFieldOf("conditions", Collections.emptyList()).forGetter(AbstractTaskProvider::getConditions),
            Codec.BOOL.optionalFieldOf("optional", false).forGetter(AbstractTaskProvider::isOptional),
            SelectorType.codec(Codecs.SIMPLIFIED_ITEMSTACK).fieldOf("items").forGetter(t -> t.itemSelector)
    ).apply(instance, HandoverItemsTaskProvider::new));
    private final Selector<ItemStack> itemSelector;

    public HandoverItemsTaskProvider(ResponseType defaultResponse, List<ConditionProvider<?>> conditions, boolean optional, Selector<ItemStack> selector) {
        super(defaultResponse, conditions, optional);
        this.itemSelector = selector;
    }

    @Override
    public HandoverItemsTask createTaskInstance(Quest quest) {
        return new HandoverItemsTask(this, quest);
    }

    @Override
    public TaskType<HandoverItemsTask, ?> getType() {
        return QuestRegistry.HANDOVER_ITEMS_TASK;
    }

    public List<ItemStack> getRequiredItems() {
        return this.itemSelector.getElements();
    }
}
