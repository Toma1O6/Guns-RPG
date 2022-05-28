package dev.toma.gunsrpg.common.quests.quest;

import dev.toma.gunsrpg.client.render.infobar.QuestDisplayDataModel;
import dev.toma.gunsrpg.common.quests.QuestProperties;
import dev.toma.gunsrpg.common.quests.quest.area.IAreaQuest;
import dev.toma.gunsrpg.common.quests.quest.area.QuestArea;
import dev.toma.gunsrpg.common.quests.quest.area.QuestAreaScheme;
import dev.toma.gunsrpg.common.quests.trigger.ITriggerHandler;
import dev.toma.gunsrpg.common.quests.trigger.Trigger;
import dev.toma.gunsrpg.common.quests.trigger.TriggerResponseStatus;
import dev.toma.gunsrpg.util.properties.IPropertyReader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.Constants;

import java.util.UUID;

public class KillInAreaQuest extends Quest<KillInAreaData> implements IAreaQuest {

    public static final IQuestFactory<KillInAreaData, KillInAreaQuest> FACTORY = IQuestFactory.of(KillInAreaQuest::new, KillInAreaQuest::new);
    private int killCount;
    private boolean hasEnteredArea;
    private QuestArea area;

    public KillInAreaQuest(QuestScheme<KillInAreaData> scheme, UUID traderId) {
        super(scheme, traderId);
    }

    public KillInAreaQuest(QuestDeserializationContext<KillInAreaData> context) {
        super(context);
    }

    @Override
    public void registerTriggers(ITriggerRegistration registration) {
        registration.addEntry(Trigger.ENTITY_KILLED, this::onEntityKilled, this::handleSuccessfulKill);
        registration.addEntry(Trigger.TICK, this::onAreaTick, ITriggerHandler.NONE);
        registration.addEntry(Trigger.PLAYER_DIED, this::onPlayerDied, ITriggerHandler.NONE);
    }

    @Override
    public void tickQuest(PlayerEntity player) {
        if (getStatus() == QuestStatus.ACTIVE && area != null && area.isInArea(player)) {
            area.tickArea(player.level, player);
        }
    }

    @Override
    public QuestArea getQuestArea() {
        return area;
    }

    @Override
    protected void fillDataModel(QuestDisplayDataModel model) {
        model.addQuestHeader(this, false);
        model.addInformationRow(this.getScheme().getDisplayInfo().getInfo(), this, q -> new StringTextComponent(q.killCount + "/" + q.getActiveData().getKillTarget()));
        if (area != null) model.addInformationRow(QuestArea.STAY_IN_AREA, this, this::fillAreaInfo);
    }

    @Override
    protected void writeQuestData(CompoundNBT nbt) {
        nbt.putInt("killCount", killCount);
        nbt.putBoolean("wasInArea", hasEnteredArea);
        if (area != null) {
            nbt.put("area", area.toNbt());
        }
    }

    @Override
    protected void readQuestData(CompoundNBT nbt) {
        killCount = nbt.getInt("killCount");
        hasEnteredArea = nbt.getBoolean("wasInArea");
        if (nbt.contains("area", Constants.NBT.TAG_COMPOUND)) {
            QuestAreaScheme areaScheme = this.getActiveData().getAreaScheme();
            area = QuestArea.fromNbt(areaScheme, nbt.getCompound("area"));
        }
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

    private TriggerResponseStatus onAreaTick(Trigger trigger, IPropertyReader reader) {
        this.generateAreaIfNeeded();
        boolean isInArea = area.isInArea(reader.getProperty(QuestProperties.PLAYER));
        if (isInArea) {
            hasEnteredArea = true;
        }
        return isInArea ? TriggerResponseStatus.OK : hasEnteredArea ? TriggerResponseStatus.FAIL : TriggerResponseStatus.PASS;
    }

    private TriggerResponseStatus onPlayerDied(Trigger trigger, IPropertyReader reader) {
        return TriggerResponseStatus.FAIL;
    }

    private void handleSuccessfulKill(Trigger trigger, IPropertyReader reader) {
        if (++killCount >= this.getActiveData().getKillTarget()) {
            setStatus(QuestStatus.COMPLETED);
        }
        trySyncClient();
    }

    private void generateAreaIfNeeded() {
        if (area == null) {
            area = this.getActiveData().getAreaScheme().getArea(player.level, (int) player.getX(), (int) player.getZ());
            trySyncClient();
        }
    }

    private ITextComponent fillAreaInfo(KillInAreaQuest quest) {
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
