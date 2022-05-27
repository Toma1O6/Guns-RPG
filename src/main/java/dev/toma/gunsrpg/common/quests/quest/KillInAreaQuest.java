package dev.toma.gunsrpg.common.quests.quest;

import dev.toma.gunsrpg.client.render.infobar.QuestDisplayDataModel;
import dev.toma.gunsrpg.common.quests.QuestProperties;
import dev.toma.gunsrpg.common.quests.trigger.ITriggerHandler;
import dev.toma.gunsrpg.common.quests.trigger.Trigger;
import dev.toma.gunsrpg.common.quests.trigger.TriggerResponseStatus;
import dev.toma.gunsrpg.util.properties.IPropertyReader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.StringTextComponent;

import java.util.UUID;

public class KillInAreaQuest extends Quest<KillInAreaData> {

    public static final IQuestFactory<KillInAreaData, KillInAreaQuest> FACTORY = IQuestFactory.of(KillInAreaQuest::new, KillInAreaQuest::new);
    private int killCount;
    // TODO area

    public KillInAreaQuest(QuestScheme<KillInAreaData> scheme, UUID traderId) {
        super(scheme, traderId);
    }

    public KillInAreaQuest(QuestDeserializationContext<KillInAreaData> context) {
        super(context);
    }

    @Override
    public void registerTriggers(ITriggerRegistration registration) {
        registration.addEntry(Trigger.ENTITY_KILLED, this::onEntityKilled, this::handleSuccessfulKill);
        registration.addEntry(Trigger.TICK, this::onAreaTick, ITriggerHandler.PASS);
    }

    @Override
    protected void fillDataModel(QuestDisplayDataModel model) {
        model.addQuestHeader(this, false);
        model.addInformationRow(KillEntitiesQuest.KILLED_ENTITIES, this, q -> new StringTextComponent(q.killCount + "/" + q.getActiveData().getKillTarget()));
        // TODO add area data
    }

    @Override
    protected void writeQuestData(CompoundNBT nbt) {
        nbt.putInt("killCount", killCount);
        // TODO area
    }

    @Override
    protected void readQuestData(CompoundNBT nbt) {
        killCount = nbt.getInt("killCount");
        // TODO area
    }

    private TriggerResponseStatus onEntityKilled(Trigger trigger, IPropertyReader reader) {
        // TODO check filters and in area
        return TriggerResponseStatus.PASS;
    }

    private TriggerResponseStatus onAreaTick(Trigger trigger, IPropertyReader reader) {
        this.generateAreaIfNeeded();
        return TriggerResponseStatus.PASS;
    }

    private void handleSuccessfulKill(Trigger trigger, IPropertyReader reader) {
        ++killCount;
        trySyncClient();
    }

    private void generateAreaIfNeeded() {
        // TODO
    }
}
