package dev.toma.gunsrpg.common.quests.quest;

import dev.toma.gunsrpg.client.render.infobar.QuestDisplayDataModel;
import dev.toma.gunsrpg.common.quests.QuestProperties;
import dev.toma.gunsrpg.common.quests.trigger.Trigger;
import dev.toma.gunsrpg.util.Interval;
import dev.toma.gunsrpg.util.properties.IPropertyReader;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.UUID;

public class AreaSurvivalQuest extends AbstractAreaBasedQuest<AreaSurvivalData> {

    public static final IQuestFactory<AreaSurvivalData, AreaSurvivalQuest> FACTORY = IQuestFactory.of(AreaSurvivalQuest::new, AreaSurvivalQuest::new);
    private int timeLeft;

    public AreaSurvivalQuest(World world, QuestScheme<AreaSurvivalData> scheme, UUID traderId) {
        super(world, scheme, traderId);
        this.timeLeft = this.getActiveData().getTicks();
    }

    public AreaSurvivalQuest(QuestDeserializationContext<AreaSurvivalData> context) {
        super(context);
    }

    @Override
    public Object[] getDescriptionArguments() {
        AreaSurvivalData data = this.getActiveData();
        return new Object[] { Interval.format(data.getTicks(), f -> f.src(Interval.Unit.TICK).out(Interval.Unit.HOUR, Interval.Unit.MINUTE, Interval.Unit.SECOND).skipAllEmptyValues()) };
    }

    @Override
    protected void fillDataModel(QuestDisplayDataModel model) {
        model.addQuestHeader(this, true, this.getDescriptionArguments());
        Interval.IFormatFactory formatFactory = f -> f.src(Interval.Unit.TICK).out(Interval.Unit.MINUTE, Interval.Unit.SECOND).compact();
        model.addInformationRow(
                this,
                q -> SurvivalQuest.TIME_REMAINING,
                q -> new StringTextComponent(Interval.format(q.timeLeft, formatFactory))
        );
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
