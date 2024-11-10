package dev.toma.gunsrpg.common.quests.quest;

import dev.toma.gunsrpg.client.render.infobar.QuestDisplayDataModel;
import dev.toma.gunsrpg.common.quests.QuestProperties;
import dev.toma.gunsrpg.common.quests.trigger.ITriggerHandler;
import dev.toma.gunsrpg.common.quests.trigger.Trigger;
import dev.toma.gunsrpg.common.quests.trigger.TriggerResponseStatus;
import dev.toma.gunsrpg.util.Interval;
import dev.toma.gunsrpg.util.properties.IPropertyReader;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.UUID;

public class SurvivalQuest extends Quest<SurvivalData> {

    public static final IQuestFactory<SurvivalData, SurvivalQuest> FACTORY = IQuestFactory.of(SurvivalQuest::new, SurvivalQuest::new);
    public static final ITextComponent TIME_REMAINING = new TranslationTextComponent("quest.time_remaining");
    private int timeLeft;

    public SurvivalQuest(World world, QuestScheme<SurvivalData> scheme, UUID traderId) {
        super(world, scheme, traderId);
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
    public Object[] getDescriptionArguments() {
        SurvivalData data = this.getActiveData();
        return new Object[] { Interval.format(data.getTicks(), f -> f.src(Interval.Unit.TICK).out(Interval.Unit.HOUR, Interval.Unit.MINUTE, Interval.Unit.SECOND).skipAllEmptyValues()) };
    }

    @Override
    protected void fillDataModel(QuestDisplayDataModel model) {
        model.addQuestHeaderWithObjective(this, this.getDescriptionArguments());
        Interval.IFormatFactory formatFactory = f -> f.src(Interval.Unit.TICK).out(Interval.Unit.MINUTE, Interval.Unit.SECOND).compact();
        model.addInformationRow(
                this,
                q -> TIME_REMAINING,
                q -> new StringTextComponent(Interval.format(q.timeLeft, formatFactory))
        );
        model.addConditionDisplay(this);
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
        PlayerEntity player = reader.getProperty(QuestProperties.PLAYER);
        World level = reader.getProperty(QuestProperties.LEVEL);
        if (!level.isClientSide() && !this.group.isLeader(player.getUUID()))
            return;
        if (level.isClientSide())
            if (!shouldTick(player))
                return;
        if (timeLeft % 100 == 0)
            this.requestTemplateFactory.sendSyncRequest();
        if (--timeLeft < 0)
            setStatus(QuestStatus.COMPLETED);
    }

    @OnlyIn(Dist.CLIENT)
    private boolean shouldTick(PlayerEntity player) {
        Minecraft client = Minecraft.getInstance();
        return player == client.player;
    }
}
