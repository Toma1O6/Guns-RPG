package dev.toma.gunsrpg.common.quests.quest;

import dev.toma.gunsrpg.client.render.infobar.QuestDisplayDataModel;
import dev.toma.gunsrpg.common.quests.QuestProperties;
import dev.toma.gunsrpg.common.quests.quest.area.QuestArea;
import dev.toma.gunsrpg.common.quests.quest.area.QuestAreaScheme;
import dev.toma.gunsrpg.common.quests.trigger.ITriggerHandler;
import dev.toma.gunsrpg.common.quests.trigger.Trigger;
import dev.toma.gunsrpg.common.quests.trigger.TriggerResponseStatus;
import dev.toma.gunsrpg.util.Interval;
import dev.toma.gunsrpg.util.properties.IPropertyReader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.Constants;

import java.util.UUID;

public class AreaSurvivalQuest extends Quest<AreaSurvivalData> {

    public static final IQuestFactory<AreaSurvivalData, AreaSurvivalQuest> FACTORY = IQuestFactory.of(AreaSurvivalQuest::new, AreaSurvivalQuest::new);
    private int timeLeft;
    private boolean hasEnteredArea;
    private QuestArea area;

    public AreaSurvivalQuest(QuestScheme<AreaSurvivalData> scheme, UUID traderId) {
        super(scheme, traderId);
        this.timeLeft = this.getActiveData().getTicks();
    }

    public AreaSurvivalQuest(QuestDeserializationContext<AreaSurvivalData> context) {
        super(context);
    }

    @Override
    public void registerTriggers(ITriggerRegistration registration) {
        registration.addEntry(Trigger.TICK, this::onTick, this::handleSuccessfulTick);
        registration.addEntry(Trigger.PLAYER_DIED, this::onPlayerDied, ITriggerHandler.NONE);
    }

    @Override
    public void tickQuest(PlayerEntity player) {
        if (getStatus() == QuestStatus.ACTIVE && area != null && area.isInArea(player)) {
            area.tickArea(player.level, player);
        }
    }

    @Override
    protected void fillDataModel(QuestDisplayDataModel model) {
        super.fillDataModel(model);

        Interval.IFormatFactory formatFactory = f -> f.src(Interval.Unit.TICK).out(Interval.Unit.MINUTE, Interval.Unit.SECOND).compact();
        model.addInformationRow(SurvivalQuest.TIME_REMAINING, this, q -> new StringTextComponent(Interval.format(q.timeLeft, formatFactory)));
        if (area != null) model.addInformationRow(QuestArea.STAY_IN_AREA, this, this::fillAreaInfo);
    }

    @Override
    protected void writeQuestData(CompoundNBT nbt) {
        nbt.putInt("timeLeft", timeLeft);
        nbt.putBoolean("wasInArea", hasEnteredArea);
        if (area != null) nbt.put("area", area.toNbt());
    }

    @Override
    protected void readQuestData(CompoundNBT nbt) {
        timeLeft = nbt.getInt("timeLeft");
        hasEnteredArea = nbt.getBoolean("wasInArea");
        if (nbt.contains("area", Constants.NBT.TAG_COMPOUND)) {
            QuestAreaScheme areaScheme = this.getActiveData().getAreaScheme();
            area = QuestArea.fromNbt(areaScheme, nbt.getCompound("area"));
        }
    }

    private TriggerResponseStatus onTick(Trigger trigger, IPropertyReader reader) {
        this.generateAreaIfNeeded();
        if (area.isInArea(reader.getProperty(QuestProperties.PLAYER))) {
            hasEnteredArea = true;
            return TriggerResponseStatus.OK;
        }
        return hasEnteredArea ? TriggerResponseStatus.FAIL : TriggerResponseStatus.PASS;
    }

    private TriggerResponseStatus onPlayerDied(Trigger trigger, IPropertyReader reader) {
        return TriggerResponseStatus.FAIL;
    }

    private void handleSuccessfulTick(Trigger trigger, IPropertyReader reader) {
        if (--timeLeft < 0) {
            setStatus(QuestStatus.COMPLETED);
        }
    }

    private void generateAreaIfNeeded() {
        if (area == null) {
            this.area = this.getActiveData().getAreaScheme().getArea(player.level, (int) player.getX(), (int) player.getZ());
            trySyncClient();
        }
    }

    private ITextComponent fillAreaInfo(AreaSurvivalQuest quest) {
        PlayerEntity player = quest.player;
        QuestArea area = quest.area;
        double distance = area.getDistance(player);
        int intDist = (int) distance;
        String text = intDist + "m";
        int size = area.getScheme().getSize();
        boolean tooClose = size - intDist < 8;
        TextFormatting color = area.isInArea(player) ? tooClose ? TextFormatting.YELLOW : TextFormatting.GREEN : TextFormatting.RED;
        return new TranslationTextComponent(text).withStyle(color);
    }
}
