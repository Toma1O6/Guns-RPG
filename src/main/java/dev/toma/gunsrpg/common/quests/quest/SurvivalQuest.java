package dev.toma.gunsrpg.common.quests.quest;

import dev.toma.gunsrpg.client.render.infobar.QuestDisplayDataModel;
import dev.toma.gunsrpg.common.quests.trigger.ITriggerHandler;
import dev.toma.gunsrpg.common.quests.trigger.Trigger;
import dev.toma.gunsrpg.common.quests.trigger.TriggerResponseStatus;
import dev.toma.gunsrpg.util.Interval;
import dev.toma.gunsrpg.util.properties.IPropertyReader;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.UUID;

public class SurvivalQuest extends Quest<SurvivalData> {

    public static final IQuestFactory<SurvivalData, SurvivalQuest> FACTORY = IQuestFactory.of(SurvivalQuest::new, SurvivalQuest::new);
    public static final ITextComponent TIME_REMAINING = new TranslationTextComponent("quest.time_remaining");
    private int timeLeft;

    public SurvivalQuest(QuestScheme<SurvivalData> scheme, UUID traderId) {
        super(scheme, traderId);
        this.timeLeft = this.getActiveData().getTicks();
    }

    public SurvivalQuest(QuestDeserializationContext<SurvivalData> context) {
        super(context);
    }

    @Override
    public void registerTriggers(ITriggerRegistration registration) {
        registration.addEntry(Trigger.TICK, this::onTick, this::handleSuccessfulTick);
        registration.addEntry(Trigger.PLAYER_DIED, this::onPlayerDied, ITriggerHandler.NONE);
    }

    @Override
    protected void fillDataModel(QuestDisplayDataModel model) {
        super.fillDataModel(model);

        Interval.IFormatFactory formatFactory = f -> f.src(Interval.Unit.TICK).out(Interval.Unit.MINUTE, Interval.Unit.SECOND).compact();
        model.addInformationRow(TIME_REMAINING, this, q -> new StringTextComponent(Interval.format(q.timeLeft, formatFactory)));
    }

    @Override
    protected void writeQuestData(CompoundNBT nbt) {
        nbt.putInt("timeLeft", timeLeft);
    }

    @Override
    protected void readQuestData(CompoundNBT nbt) {
        timeLeft = nbt.getInt("timeLeft");
    }

    private TriggerResponseStatus onTick(Trigger trigger, IPropertyReader reader) {
        return TriggerResponseStatus.OK;
    }

    private TriggerResponseStatus onPlayerDied(Trigger trigger, IPropertyReader reader) {
        return TriggerResponseStatus.FAIL;
    }

    private void handleSuccessfulTick(Trigger trigger, IPropertyReader reader) {
        if (--timeLeft < 0) {
            setStatus(QuestStatus.COMPLETED);
        }
    }
}
