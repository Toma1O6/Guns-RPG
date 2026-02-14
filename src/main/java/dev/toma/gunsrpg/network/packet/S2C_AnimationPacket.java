package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.network.AbstractNetworkPacket;
import lib.toma.animations.AnimationEngine;
import lib.toma.animations.api.AnimationType;
import lib.toma.animations.api.IAnimation;
import lib.toma.animations.api.IAnimationPipeline;
import lib.toma.animations.api.lifecycle.Registries;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

public class S2C_AnimationPacket extends AbstractNetworkPacket<S2C_AnimationPacket> {

    private final Action action;
    private final ResourceLocation typeId;

    public S2C_AnimationPacket(PacketBuffer buffer) {
        this(buffer.readEnum(Action.class), buffer.readResourceLocation());
    }

    public S2C_AnimationPacket(Action action, ResourceLocation typeId) {
        this.action = action;
        this.typeId = typeId;
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeEnum(action);
        buffer.writeResourceLocation(typeId);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    protected void handlePacket(NetworkEvent.Context context) {
        IAnimationPipeline pipeline = AnimationEngine.get().pipeline();
        handleGeneric(pipeline);
    }

    @SuppressWarnings("unchecked")
    @OnlyIn(Dist.CLIENT)
    private <A extends IAnimation> void handleGeneric(IAnimationPipeline pipeline) {
        AnimationType<A> type = (AnimationType<A>) Registries.ANIMATION_TYPES.getElement(typeId);
        switch (action) {
            case PLAY:
                pipeline.insert(type);
                break;
            case STOP:
                pipeline.remove(type);
                break;
        }
    }

    public enum Action {

        PLAY,
        STOP
    }
}
