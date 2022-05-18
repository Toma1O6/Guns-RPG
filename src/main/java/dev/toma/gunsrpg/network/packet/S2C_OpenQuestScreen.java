package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.client.screen.QuestScreen;
import dev.toma.gunsrpg.common.entity.MayorEntity;
import dev.toma.gunsrpg.common.quests.mayor.ReputationStatus;
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

public class S2C_OpenQuestScreen extends AbstractNetworkPacket<S2C_OpenQuestScreen> {

    private ReputationStatus status;
    private MayorEntity.ListedQuests quests;
    private int entityId;

    public S2C_OpenQuestScreen() {
    }

    public S2C_OpenQuestScreen(ReputationStatus status, MayorEntity.ListedQuests quests, int entityId) {
        this.status = status;
        this.quests = quests;
        this.entityId = entityId;
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeEnum(status);
        CompoundNBT wrap = new CompoundNBT();
        wrap.put("contents", quests.toNbt());
        buffer.writeNbt(wrap);
        buffer.writeInt(entityId);
    }

    @Override
    public S2C_OpenQuestScreen decode(PacketBuffer buffer) {
        ReputationStatus status = buffer.readEnum(ReputationStatus.class);
        CompoundNBT wrap = buffer.readNbt();
        ListNBT content = wrap.getList("contents", Constants.NBT.TAG_COMPOUND);
        MayorEntity.ListedQuests quests = MayorEntity.ListedQuests.loadNbt(content);
        return new S2C_OpenQuestScreen(status, quests, buffer.readInt());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    protected void handlePacket(NetworkEvent.Context context) {
        Minecraft mc = Minecraft.getInstance();
        World world = mc.level;
        Entity entity = world.getEntity(entityId);
        if (!(entity instanceof MayorEntity)) return;
        mc.setScreen(new QuestScreen(status, quests.getQuests(), (MayorEntity) entity));
    }
}
