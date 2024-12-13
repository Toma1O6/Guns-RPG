package dev.toma.gunsrpg.common.quests.quest;

import dev.toma.gunsrpg.client.render.infobar.AreaDistanceElement;
import dev.toma.gunsrpg.client.render.infobar.QuestDisplayDataModel;
import dev.toma.gunsrpg.common.quests.QuestProperties;
import dev.toma.gunsrpg.common.quests.quest.area.IAreaQuest;
import dev.toma.gunsrpg.common.quests.quest.area.IQuestAreaProvider;
import dev.toma.gunsrpg.common.quests.quest.area.QuestArea;
import dev.toma.gunsrpg.common.quests.trigger.ITriggerHandler;
import dev.toma.gunsrpg.common.quests.trigger.Trigger;
import dev.toma.gunsrpg.common.quests.trigger.TriggerResponseStatus;
import dev.toma.gunsrpg.util.Interval;
import dev.toma.gunsrpg.util.properties.IPropertyReader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import java.util.UUID;

public abstract class AbstractAreaBasedQuest<D extends IQuestData & IQuestAreaProvider> extends Quest<D> implements IAreaQuest {

    protected int gracePeriod;
    protected boolean areaEntered;
    protected QuestArea area;

    public AbstractAreaBasedQuest(World level, QuestScheme<D> scheme, UUID traderId) {
        super(level, scheme, traderId);
        this.gracePeriod = this.getGracePeriodDuration();
    }

    public AbstractAreaBasedQuest(QuestDeserializationContext<D> context) {
        super(context);
    }

    @Override
    public QuestArea getQuestArea() {
        return area;
    }

    @Override
    public boolean isStarted() {
        return this.areaEntered;
    }

    // Ticking
    @Override
    public void tickQuest() {
        QuestStatus status = this.getStatus();
        if (status == QuestStatus.ACTIVE && area != null) {
            if (areaEntered) {
                area.tickArea(this.level, this.group);
            } else {
                super.tickQuest();
            }
            if (area.isInArea(this.group, this.level)) {
                gracePeriod = this.getGracePeriodDuration();
            } else if (areaEntered) {
                --gracePeriod;
                if (!this.level.isClientSide) {
                    String time = Interval.format(gracePeriod, f -> f.src(Interval.Unit.TICK).out(Interval.Unit.SECOND));
                    this.group.accept(this.level, member -> {
                        ServerPlayerEntity player = (ServerPlayerEntity) member;
                        player.sendMessage(new TranslationTextComponent("quest.return_to_area", time).withStyle(TextFormatting.RED), ChatType.GAME_INFO, Util.NIL_UUID);
                    });
                }
            }
        }
    }

    // Trigger registration
    @Override
    public final void registerTriggers(ITriggerRegistration registration) {
        registerDefaultTriggers(registration);
        registerAdditionalTriggers(registration);
    }

    protected void registerDefaultTriggers(ITriggerRegistration registration) {
        registration.addEntry(Trigger.TICK, this::onTick, this::handleSuccessfulTick);
        registration.addEntry(Trigger.PLAYER_DIED, this::onPlayerDeath, ITriggerHandler.NONE);
    }

    protected void registerAdditionalTriggers(ITriggerRegistration registration) {

    }

    protected void onAreaEntered() {

    }

    // Trigger handlers
    protected abstract void handleSuccessfulTick(Trigger trigger, IPropertyReader reader);

    protected TriggerResponseStatus onTick(Trigger trigger, IPropertyReader reader) {
        World level = reader.getProperty(QuestProperties.LEVEL);
        this.generateAreaIfMissing(level);
        if (area.isInArea(this.group, level)) {
            boolean wasEntered = areaEntered;
            areaEntered = true;
            if (!wasEntered) {
                this.onAreaEntered();
            }
            return TriggerResponseStatus.OK;
        }
        return areaEntered && !isGracePeriodActive() ? TriggerResponseStatus.FAIL : TriggerResponseStatus.PASS;
    }

    protected TriggerResponseStatus onPlayerDeath(Trigger trigger, IPropertyReader reader) {
        return areaEntered ? TriggerResponseStatus.FAIL : TriggerResponseStatus.PASS;
    }

    // Quest properties
    @Override
    protected boolean overrideFailureFromCondition() {
        return !areaEntered;
    }

    protected int getGracePeriodDuration() {
        return Interval.seconds(5).getTicks();
    }

    protected boolean isGracePeriodActive() {
        return gracePeriod > 0;
    }

    protected QuestArea generateArea(D activeData, World world) {
        PlayerEntity leader = world.getPlayerByUUID(this.group.getGroupId());
        return activeData.getAreaScheme().getArea(world, (int) leader.getX(), (int) leader.getZ());
    }

    protected void fillAreaDataModel(QuestDisplayDataModel dataModel) {
        if (area != null) {
            dataModel.addElement(new AreaDistanceElement<>(this, QuestArea.STAY_IN_AREA, quest -> fillAreaInfo(quest, dataModel.getClientId()), quest -> quest.area != null ? quest.area.getCenter() : BlockPos.ZERO));
        }
    }

    protected ITextComponent fillAreaInfo(AbstractAreaBasedQuest<D> quest, UUID clientId) {
        PlayerEntity player = this.level.getPlayerByUUID(clientId);
        QuestArea area = quest.area;
        double distance = area.getDistance(player);
        int intDist = (int) distance;
        String text = intDist + "m";
        int size = area.getScheme().getSize();
        boolean tooClose = size - intDist < 8;
        TextFormatting color = area.isInArea(player) ? tooClose ? TextFormatting.YELLOW : TextFormatting.GREEN : TextFormatting.RED;
        return new TranslationTextComponent(text).withStyle(color);
    }

    // Data saving
    @Override
    protected final void writeQuestData(CompoundNBT nbt) {
        nbt.putInt("gracePeriod", gracePeriod);
        nbt.putBoolean("areaEntered", areaEntered);
        if (area != null) {
            nbt.put("area", area.toNbt());
        }
        writeAdditionalData(nbt);
    }

    @Override
    protected final void readQuestData(CompoundNBT nbt) {
        gracePeriod = nbt.getInt("gracePeriod");
        areaEntered = nbt.getBoolean("areaEntered");
        if (nbt.contains("area", Constants.NBT.TAG_COMPOUND)) {
            D data = this.getActiveData();
            area = QuestArea.fromNbt(data.getAreaScheme(), nbt.getCompound("area"));
        }
        readAddtionalData(nbt);
    }

    protected void writeAdditionalData(CompoundNBT nbt) {
    }

    protected void readAddtionalData(CompoundNBT nbt) {
    }

    private void generateAreaIfMissing(World world) {
        if (area != null || world.isClientSide()) return;
        D activeData = this.getActiveData();
        this.area = this.generateArea(activeData, world);
        trySyncClient(world);
    }
}
