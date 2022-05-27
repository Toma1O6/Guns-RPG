package dev.toma.gunsrpg.common.quests.quest;

import dev.toma.gunsrpg.client.render.infobar.QuestDisplayDataModel;
import dev.toma.gunsrpg.common.quests.QuestProperties;
import dev.toma.gunsrpg.common.quests.trigger.Trigger;
import dev.toma.gunsrpg.common.quests.trigger.TriggerResponseStatus;
import dev.toma.gunsrpg.util.properties.IPropertyReader;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.UUID;

public class KillEntitiesQuest extends Quest<KillEntityData> {

    public static final IQuestFactory<KillEntityData, KillEntitiesQuest> FACTORY = IQuestFactory.of(KillEntitiesQuest::new, KillEntitiesQuest::new);
    public static final ITextComponent KILLED_ENTITIES = new TranslationTextComponent("quest.killed_entities");
    private int killCount;

    public KillEntitiesQuest(QuestScheme<KillEntityData> scheme, UUID traderId) {
        super(scheme, traderId);
    }

    public KillEntitiesQuest(QuestDeserializationContext<KillEntityData> context) {
        super(context);
    }

    @Override
    protected void fillDataModel(QuestDisplayDataModel model) {
        int required = this.getActiveData().getKillTarget();
        model.addQuestHeader(this, false);
        model.addInformationRow(KILLED_ENTITIES, this, quest -> new StringTextComponent(quest.killCount + "/" + required));
    }

    @Override
    public void registerTriggers(ITriggerRegistration registration) {
        registration.addEntry(Trigger.ENTITY_KILLED, this::handleEntityKilled, this::onSuccessfulKill);
    }

    @Override
    protected void writeQuestData(CompoundNBT nbt) {
        nbt.putInt("kills", killCount);
    }

    @Override
    protected void readQuestData(CompoundNBT nbt) {
        killCount = nbt.getInt("kills");
    }

    protected TriggerResponseStatus handleEntityKilled(Trigger trigger, IPropertyReader reader) {
        Entity entity = reader.getProperty(QuestProperties.ENTITY);
        if (this.getActiveData().getEntityFilter().test(entity)) {
            return TriggerResponseStatus.OK;
        }
        return TriggerResponseStatus.PASS;
    }

    protected void onSuccessfulKill(Trigger trigger, IPropertyReader reader) {
        ++killCount;
        trySyncClient();
    }
}
