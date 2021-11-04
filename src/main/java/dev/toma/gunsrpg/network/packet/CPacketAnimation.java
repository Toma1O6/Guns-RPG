package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.network.AbstractNetworkPacket;
import lib.toma.animations.AnimationEngine;
import lib.toma.animations.AnimationUtils;
import lib.toma.animations.api.AnimationType;
import lib.toma.animations.api.IAnimationPipeline;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.BiConsumer;

public class CPacketAnimation extends AbstractNetworkPacket<CPacketAnimation> {

    private Action action;
    private AnimationType<?> type;

    public CPacketAnimation() {
    }

    public CPacketAnimation(Action action, AnimationType<?> type) {
        this.action = action;
        this.type = type;
        if (action.requiresInstanceFactory() && !type.hasCreator()) {
            throw new IllegalArgumentException(String.format("Animation type (%s) doesn't support raw animation creation!", type.getKey()));
        }
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeEnum(action);
        AnimationUtils.encodeAnimationType(type, buffer);
    }

    @Override
    public CPacketAnimation decode(PacketBuffer buffer) {
        return new CPacketAnimation(buffer.readEnum(Action.class), AnimationUtils.decodeAnimationType(buffer));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    protected void handlePacket(NetworkEvent.Context context) {
        action.handle(AnimationEngine.get().pipeline(), type);
    }

    public enum Action {

        PLAY(IAnimationPipeline::insert),
        STOP(IAnimationPipeline::remove);

        private final BiConsumer<IAnimationPipeline, AnimationType<?>> handler;

        Action(BiConsumer<IAnimationPipeline, AnimationType<?>> handler) {
            this.handler = handler;
        }

        public boolean requiresInstanceFactory() {
            return this == PLAY;
        }

        public void handle(IAnimationPipeline pipeline, AnimationType<?> type) {
            handler.accept(pipeline, type);
        }
    }
}
