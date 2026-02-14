package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.client.screen.quest.QuestScreen;
import dev.toma.gunsrpg.common.entity.MayorEntity;
import dev.toma.gunsrpg.common.quests.mayor.ReputationStatus;
import dev.toma.gunsrpg.common.quests.quest.Quest;
import dev.toma.gunsrpg.common.quests.reward.QuestReward;
import dev.toma.gunsrpg.network.AbstractNetworkPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Arrays;
import java.util.List;

public class S2C_OpenQuestScreen extends AbstractNetworkPacket<S2C_OpenQuestScreen> {

    private final ReputationStatus status;
    private final ListNBT questsNbt;
    private final int entityId;
    private final long timer;
    private final QuestReward pendingReward;

    public S2C_OpenQuestScreen(ReputationStatus status, ListNBT questsNbt, int entityId, long timer, QuestReward pendingReward) {
        this.status = status;
        this.questsNbt = questsNbt;
        this.entityId = entityId;
        this.timer = timer;
        this.pendingReward = pendingReward;
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeEnum(status);
        CompoundNBT wrap = new CompoundNBT();
        wrap.put("contents", questsNbt);
        buffer.writeNbt(wrap);
        buffer.writeInt(entityId);
        buffer.writeLong(timer);
        buffer.writeBoolean(this.pendingReward != null);
        if (this.pendingReward != null) {
            buffer.writeNbt(this.pendingReward.toNbt());
        }
    }

    public static S2C_OpenQuestScreen decode(PacketBuffer buffer) {
        ReputationStatus status = buffer.readEnum(ReputationStatus.class);
        CompoundNBT wrap = buffer.readNbt();
        ListNBT content = wrap.getList("contents", Constants.NBT.TAG_COMPOUND);
        int entityId = buffer.readInt();
        long timer = buffer.readLong();
        boolean hasReward = buffer.readBoolean();
        QuestReward reward = null;
        if (hasReward) {
            reward = new QuestReward(buffer.readNbt());
        }
        return new S2C_OpenQuestScreen(status, content, entityId, timer, reward);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    protected void handlePacket(NetworkEvent.Context context) {
        Minecraft mc = Minecraft.getInstance();
        World world = mc.level;
        Entity entity = world.getEntity(entityId);
        if (!(entity instanceof MayorEntity))
            return;
        MayorEntity mayor = (MayorEntity) entity;
        mayor.setRefreshTimer(this.timer);
        MayorEntity.ListedQuests mayorQuestList = MayorEntity.ListedQuests.loadNbt(world, this.questsNbt);
        List<Quest<?>> quests = Arrays.asList(mayorQuestList.getQuests());

        mc.setScreen(new QuestScreen(mayor, this.status, quests, this.pendingReward));
    }
}
