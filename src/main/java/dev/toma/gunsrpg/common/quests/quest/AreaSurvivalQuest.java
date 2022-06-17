package dev.toma.gunsrpg.common.quests.quest;

import dev.toma.gunsrpg.client.render.infobar.QuestDisplayDataModel;
import dev.toma.gunsrpg.common.quests.trigger.Trigger;
import dev.toma.gunsrpg.util.Interval;
import dev.toma.gunsrpg.util.properties.IPropertyReader;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.StringTextComponent;

import java.util.UUID;

public class AreaSurvivalQuest extends AbstractAreaBasedQuest<AreaSurvivalData> {

    public static final IQuestFactory<AreaSurvivalData, AreaSurvivalQuest> FACTORY = IQuestFactory.of(AreaSurvivalQuest::new, AreaSurvivalQuest::new);
    private int timeLeft;

    public AreaSurvivalQuest(QuestScheme<AreaSurvivalData> scheme, UUID traderId) {
        super(scheme, traderId);
        this.timeLeft = this.getActiveData().getTicks();
    }

    public AreaSurvivalQuest(QuestDeserializationContext<AreaSurvivalData> context) {
        super(context);
    }

    @Override
    protected void fillDataModel(QuestDisplayDataModel model) {
        super.fillDataModel(model);
        Interval.IFormatFactory formatFactory = f -> f.src(Interval.Unit.TICK).out(Interval.Unit.MINUTE, Interval.Unit.SECOND).compact();
        model.addInformationRow(SurvivalQuest.TIME_REMAINING, this, q -> new StringTextComponent(Interval.format(q.timeLeft, formatFactory)));
        fillAreaDataModel(model);
        model.addConditionDisplay(this);
    }

    @Override
    protected void writeAdditionalData(CompoundNBT nbt) {
        nbt.putInt("timeLeft", timeLeft);
    }

    @Override
    protected void readAddtionalData(CompoundNBT nbt) {
        timeLeft = nbt.getInt("timeLeft");
    }

    @Override
    protected void handleSuccessfulTick(Trigger trigger, IPropertyReader reader) {
        if (--timeLeft < 0) {
            setStatus(QuestStatus.COMPLETED);
        }
    }
}
