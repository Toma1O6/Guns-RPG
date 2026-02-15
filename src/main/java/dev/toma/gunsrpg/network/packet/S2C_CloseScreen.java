package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.network.AbstractHandlePacket;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

public final class S2C_CloseScreen extends AbstractHandlePacket {

    public static final S2C_CloseScreen INSTANCE = new S2C_CloseScreen();

    private S2C_CloseScreen() {}

    @OnlyIn(Dist.CLIENT)
    @Override
    protected void handlePacket(NetworkEvent.Context context) {
        Minecraft client = Minecraft.getInstance();
        client.setScreen(null);
    }
}
