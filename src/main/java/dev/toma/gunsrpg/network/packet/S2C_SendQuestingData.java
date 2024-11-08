package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.api.client.ScreenDataEventListener;
import dev.toma.gunsrpg.network.AbstractNetworkPacket;
import dev.toma.gunsrpg.world.cap.QuestingDataProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

public class S2C_SendQuestingData extends AbstractNetworkPacket<S2C_SendQuestingData> {

    private final CompoundNBT nbt;

    public S2C_SendQuestingData() {
        this(null);
    }

    public S2C_SendQuestingData(CompoundNBT nbt) {
        this.nbt = nbt;
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeNbt(this.nbt);
    }

    @Override
    public S2C_SendQuestingData decode(PacketBuffer buffer) {
        return new S2C_SendQuestingData(buffer.readNbt());
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    protected void handlePacket(NetworkEvent.Context context) {
        Minecraft client = Minecraft.getInstance();
        ClientWorld world = client.level;
        QuestingDataProvider.getData(world).ifPresent(iquestingData -> {
            iquestingData.deserializeNBT(this.nbt);
            Screen screen = client.screen;
            if (screen instanceof ScreenDataEventListener) {
                ScreenDataEventListener listener = (ScreenDataEventListener) screen;
                listener.onQuestingDataReceived(iquestingData);
            }
        });
    }
}
