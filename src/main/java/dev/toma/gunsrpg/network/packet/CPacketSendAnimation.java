package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.network.AbstractNetworkPacket;
import lib.toma.animations.AnimationEngine;
import lib.toma.animations.AnimationUtils;
import lib.toma.animations.api.AnimationType;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

public class CPacketSendAnimation extends AbstractNetworkPacket<CPacketSendAnimation> {

    private AnimationType<?> type;

    public CPacketSendAnimation() {
    }

    public CPacketSendAnimation(AnimationType<?> type) {
        this.type = type;
        if (!type.hasCreator())
            throw new IllegalArgumentException(String.format("Animation type (%s) doesn't support raw animation creation!", type.getKey()));
    }

    @Override
    public void encode(PacketBuffer buffer) {
        AnimationUtils.encodeAnimationType(type, buffer);
    }

    @Override
    public CPacketSendAnimation decode(PacketBuffer buffer) {
        return new CPacketSendAnimation(AnimationUtils.decodeAnimationType(buffer));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    protected void handlePacket(NetworkEvent.Context context) {
        AnimationEngine.get().pipeline().insert(type);
    }
}
