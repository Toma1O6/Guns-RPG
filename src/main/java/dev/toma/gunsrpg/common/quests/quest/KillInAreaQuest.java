package dev.toma.gunsrpg.common.quests.quest;

import dev.toma.gunsrpg.client.render.infobar.QuestDisplayDataModel;
import dev.toma.gunsrpg.common.quests.QuestProperties;
import dev.toma.gunsrpg.common.quests.trigger.Trigger;
import dev.toma.gunsrpg.common.quests.trigger.TriggerResponseStatus;
import dev.toma.gunsrpg.util.properties.IPropertyReader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.StringTextComponent;

import java.util.UUID;

public class KillInAreaQuest extends AbstractAreaBasedQuest<KillInAreaData> {

    public static final IQuestFactory<KillInAreaData, KillInAreaQuest> FACTORY = IQuestFactory.of(KillInAreaQuest::new, KillInAreaQuest::new);
    private int killCount;

    public KillInAreaQuest(QuestScheme<KillInAreaData> scheme, UUID traderId) {
        super(scheme, traderId);
    }

    public KillInAreaQuest(QuestDeserializationContext<KillInAreaData> context) {
        super(context);
    }

    @Override
    protected void registerAdditionalTriggers(ITriggerRegistration registration) {
        registration.addEntry(Trigger.ENTITY_KILLED, this::onEntityKilled, this::handleSuccessfulKill);
    }

    @Override
    protected void fillDataModel(QuestDisplayDataModel model) {
        model.addQuestHeader(this, false);
        model.addInformationRow(this.getScheme().getDisplayInfo().getInfo(), this, q -> new StringTextComponent(q.killCount + "/" + q.getActiveData().getKillTarget()));
        fillAreaDataModel(model);
    }

    @Override
    protected void writeAdditionalData(CompoundNBT nbt) {
        nbt.putInt("killCount", killCount);
    }

    @Override
    protected void readAddtionalData(CompoundNBT nbt) {
        killCount = nbt.getInt("killCount");
    }

    private TriggerResponseStatus onEntityKilled(Trigger trigger, IPropertyReader reader) {
        PlayerEntity player = reader.getProperty(QuestProperties.PLAYER);
        Entity entity = reader.getProperty(QuestProperties.ENTITY);
        if (area.isInArea(player)) {
            KillInAreaData data = this.getActiveData();
            if (data.getEntityFilter().test(entity)) {
                return TriggerResponseStatus.OK;
            }
        }
        return TriggerResponseStatus.PASS;
    }

    private void handleSuccessfulKill(Trigger trigger, IPropertyReader reader) {
        if (++killCount >= this.getActiveData().getKillTarget()) {
            setStatus(QuestStatus.COMPLETED);
        }
        trySyncClient();
    }

    @Override
    protected void handleSuccessfulTick(Trigger trigger, IPropertyReader reader) {
    }
}
