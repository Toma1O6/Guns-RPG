package dev.toma.gunsrpg.common.quests.quest;

import dev.toma.gunsrpg.client.render.infobar.QuestDisplayDataModel;
import dev.toma.gunsrpg.common.quests.trigger.Trigger;
import dev.toma.gunsrpg.common.quests.trigger.TriggerResponseStatus;
import dev.toma.gunsrpg.util.properties.IPropertyReader;
import net.minecraft.nbt.CompoundNBT;

import java.util.UUID;

public class ItemHandoverQuest extends Quest<ItemHandoverData> {

    public static final IQuestFactory<ItemHandoverData, ItemHandoverQuest> FACTORY = IQuestFactory.of(ItemHandoverQuest::new, ItemHandoverQuest::new);

    public ItemHandoverQuest(QuestScheme<ItemHandoverData> scheme, UUID traderId) {
        super(scheme, traderId);
    }

    public ItemHandoverQuest(QuestDeserializationContext<ItemHandoverData> context) {
        super(context);
    }

    @Override
    public void registerTriggers(ITriggerRegistration registration) {
        registration.addEntry(Trigger.ITEM_HANDOVER, this::tryItemHandover, this::handleSuccessfulHandover);
    }

    @Override
    protected void fillDataModel(QuestDisplayDataModel model) {
        model.addQuestHeader(this, false);
        // TODO information
    }

    @Override
    protected void writeQuestData(CompoundNBT nbt) {

    }

    @Override
    protected void readQuestData(CompoundNBT nbt) {

    }

    private TriggerResponseStatus tryItemHandover(Trigger trigger, IPropertyReader reader) {
        return TriggerResponseStatus.PASS;
    }

    private void handleSuccessfulHandover(Trigger trigger, IPropertyReader reader) {

    }
}
