package dev.toma.gunsrpg.common.quests.quest;

import dev.toma.gunsrpg.client.render.infobar.QuestDisplayDataModel;
import dev.toma.gunsrpg.common.quests.QuestProperties;
import dev.toma.gunsrpg.common.quests.sharing.QuestingGroup;
import dev.toma.gunsrpg.common.quests.trigger.ITriggerHandler;
import dev.toma.gunsrpg.common.quests.trigger.Trigger;
import dev.toma.gunsrpg.common.quests.trigger.TriggerResponseStatus;
import dev.toma.gunsrpg.util.properties.IPropertyReader;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import java.util.UUID;

public class KillEntitiesQuest extends Quest<KillEntityData> {

    public static final IQuestFactory<KillEntityData, KillEntitiesQuest> FACTORY = IQuestFactory.of(KillEntitiesQuest::new, KillEntitiesQuest::new);
    private int requiredKillCount;
    private int killCount;

    public KillEntitiesQuest(World world, QuestScheme<KillEntityData> scheme, UUID traderId) {
        super(world, scheme, traderId);
    }

    public KillEntitiesQuest(QuestDeserializationContext<KillEntityData> context) {
        super(context);
    }

    @Override
    public Object[] getDescriptionArguments() {
        return new Object[] { KillEntityData.formatKillRequirement(this.requiredKillCount, this) };
    }

    @Override
    protected void onAssigned(QuestingGroup group) {
        this.requiredKillCount = this.getScheme().getData().getKillTarget(group, this.allowTargetMultipliers());
    }

    @Override
    protected void fillDataModel(QuestDisplayDataModel model) {
        model.addQuestHeader(this, false);
        model.addInformationRow(
                this,
                quest -> quest.getScheme().getDisplayInfo().getInfo(quest.getDescriptionArguments()),
                quest -> new StringTextComponent(quest.killCount + "/" + KillEntityData.formatKillRequirement(quest.requiredKillCount, quest))
        );
        model.addConditionDisplay(this);
    }

    @Override
    public void registerTriggers(ITriggerRegistration registration) {
        registration.addEntry(Trigger.ENTITY_KILLED, this::handleEntityKilled, this::onSuccessfulKill);
        registration.addEntry(Trigger.PLAYER_DIED, this::onPlayerDied, ITriggerHandler.NONE);
    }

    @Override
    protected void writeQuestData(CompoundNBT nbt) {
        nbt.putInt("kills", killCount);
        nbt.putInt("required", requiredKillCount);
    }

    @Override
    protected void readQuestData(CompoundNBT nbt) {
        killCount = nbt.getInt("kills");
        requiredKillCount = nbt.getInt("required");
    }

    private TriggerResponseStatus handleEntityKilled(Trigger trigger, IPropertyReader reader) {
        Entity entity = reader.getProperty(QuestProperties.ENTITY);
        if (this.getActiveData().getEntityFilter().test(entity)) {
            return TriggerResponseStatus.OK;
        }
        return TriggerResponseStatus.PASS;
    }

    private void onSuccessfulKill(Trigger trigger, IPropertyReader reader) {
        if (++killCount >= this.requiredKillCount) {
            setStatus(QuestStatus.COMPLETED);
        }
        trySyncClient(this.level);
    }

    private TriggerResponseStatus onPlayerDied(Trigger trigger, IPropertyReader reader) {
        return TriggerResponseStatus.FAIL;
    }
}
